/**
 * Significant portions of this file and error message infrastructure copied
 * from ANTLR v4 which has BSD license, copyright by Terence Parr and Sam
 * Harwell. https://github.com/antlr/antlr4
 */
package mantra;

import mantra.errors.DefaultToolListener;
import mantra.errors.ErrorManager;
import mantra.errors.ErrorType;
import mantra.errors.MantraMessage;
import mantra.errors.MantraToolListener;
import mantra.semantics.ComputeTypes;
import mantra.semantics.DefScopesAndSymbols;
import mantra.semantics.VerifyListener;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Tool {
	public static final String VERSION = "0.1";
	public static final String MANTRA_EXTENSION = ".om";

	public static enum OptionArgType { NONE, STRING } // NONE implies boolean
	public static class Option {
		String fieldName;
		String name;
		OptionArgType argType;
		String description;

		public Option(String fieldName, String name, String description) {
			this(fieldName, name, OptionArgType.NONE, description);
		}

		public Option(String fieldName, String name, OptionArgType argType, String description) {
			this.fieldName = fieldName;
			this.name = name;
			this.argType = argType;
			this.description = description;
		}
	}

	public static Option[] optionDefs = {
    	new Option("compileOnly",		"-c", OptionArgType.STRING, "compile only"),
		new Option("warnings_are_errors", "-Werror", "treat warnings as errors"),
		new Option("msgFormat",			"-message-format", OptionArgType.STRING, "specify output style for messages in mantra, gnu, vs2005"),
	};

	// option fields
	protected boolean compileOnly = false;
	protected boolean warnings_are_errors = false;
	protected String msgFormat = "mantra";

	public ErrorManager errMgr;

	List<MantraToolListener> listeners = new CopyOnWriteArrayList<MantraToolListener>();
	/** Track separately so if someone adds a listener, it's the only one
	 *  instead of it and the default stderr listener.
	 */
	DefaultToolListener defaultListener = new DefaultToolListener(this);

	public final String[] args;

	protected List<String> mantraFiles = new ArrayList<String>();

	public Tool(String[] args) {
		this.args = args;
		errMgr = new ErrorManager(this);
		errMgr.setFormat(msgFormat);
		handleArgs();
	}

	public static void main(String[] args) {
		Tool antlr = new Tool(args);
		if ( args.length == 0 ) { antlr.help(); antlr.exit(0); }
		antlr.handleArgs();
		antlr.processMantraFiles();
	}

	public void processMantraFiles() {
		for (String f : mantraFiles) {
			try {
				processMantraFile(f);
			}
			catch (IOException ioe) {
				//errMgr.toolError(ErrorType.CANNOT_WRITE_FILE);
				ioe.printStackTrace(System.err);
			}
		}
	}

	public void processMantraFile(String fileName) throws IOException {
		Pair<ParseTree,Parser> r = parseMantraFile(fileName);
		ParseTree tree = r.a;
		Parser parser = r.b;

		// def scopes and symbols
		DefScopesAndSymbols def = new DefScopesAndSymbols();
		ParseTreeWalker.DEFAULT.walk(def, tree);

		VerifyListener ref = new VerifyListener();
		ParseTreeWalker.DEFAULT.walk(ref, tree);

		ComputeTypes types = new ComputeTypes(parser);
		ParseTreeWalker.DEFAULT.walk(types, tree);
	}

	public Pair<ParseTree,Parser> parseMantraFile(String fileName) throws IOException {
		ANTLRInputStream input = new ANTLRFileStream(fileName);
		MantraLexer lexer = new MantraLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		MantraParser parser = new MantraParser(tokens);
		ParserRuleContext tree = parser.compilationUnit();
		tree.inspect(parser);
		return new Pair<ParseTree,Parser>(tree, parser);
	}

	// listener / error support

	public void addListener(MantraToolListener tl) {
		if ( tl!=null ) listeners.add(tl);
	}
	public void removeListener(MantraToolListener tl) { listeners.remove(tl); }
	public void removeListeners() { listeners.clear(); }
	public List<MantraToolListener> getListeners() { return listeners; }

	public void info(String msg) {
		if ( listeners.isEmpty() ) {
			defaultListener.info(msg);
			return;
		}
		for (MantraToolListener l : listeners) l.info(msg);
	}
	public void error(MantraMessage msg) {
		if ( listeners.isEmpty() ) {
			defaultListener.error(msg);
			return;
		}
		for (MantraToolListener l : listeners) l.error(msg);
	}
	public void warning(MantraMessage msg) {
		if ( listeners.isEmpty() ) {
			defaultListener.warning(msg);
		}
		else {
			for (MantraToolListener l : listeners) l.warning(msg);
		}

		if (warnings_are_errors) {
			errMgr.emit(ErrorType.WARNING_TREATED_AS_ERROR, new MantraMessage(ErrorType.WARNING_TREATED_AS_ERROR));
		}
	}

	public void help() {
		info("Mantra Compiler  Version " + Tool.VERSION);
		for (Option o : optionDefs) {
			String name = o.name + (o.argType!=OptionArgType.NONE? " ___" : "");
			String s = String.format(" %-19s %s", name, o.description);
			info(s);
		}
	}

	public void version() {
		info("Mantra Compiler  Version " + VERSION);
	}

	public void exit(int e) { System.exit(e); }

	public void panic() { throw new Error("Mantra panic"); }

	protected void handleArgs() {
		int i=0;
		while ( args!=null && i<args.length ) {
			String arg = args[i];
			i++;
			if ( arg.charAt(0)!='-' ) { // file name
				if ( !mantraFiles.contains(arg) ) mantraFiles.add(arg);
				continue;
			}
			boolean found = false;
			for (Option o : optionDefs) {
				if ( arg.equals(o.name) ) {
					found = true;
					String argValue = null;
					if ( o.argType==OptionArgType.STRING ) {
						argValue = args[i];
						i++;
					}
					// use reflection to set field
					Class<? extends Tool> c = this.getClass();
					try {
						Field f = c.getField(o.fieldName);
						if ( argValue==null ) {
							if ( arg.startsWith("-no-") ) f.setBoolean(this, false);
							else f.setBoolean(this, true);
						}
						else f.set(this, argValue);
					}
					catch (Exception e) {
						errMgr.toolError(ErrorType.INTERNAL_ERROR, "can't access field "+o.fieldName);
					}
				}
			}
			if ( !found ) {
				errMgr.toolError(ErrorType.INVALID_CMDLINE_ARG, arg);
			}
		}
	}
}
