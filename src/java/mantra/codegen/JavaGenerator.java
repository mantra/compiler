package mantra.codegen;

import mantra.Tool;
import mantra.codegen.model.MFile;
import mantra.errors.ErrorType;
import mantra.symbols.Type;
import org.stringtemplate.v4.NumberRenderer;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;
import org.stringtemplate.v4.misc.STMessage;

public class JavaGenerator {
	public static final String TEMPLATES = "mantra/templates/codegen/java/Java.stg";
	public Tool tool;
	public STGroup templates;

	public JavaGenerator(Tool tool) { this.tool = tool; templates = loadTemplates(); }

	public ST translate(MFile file) {
		OutputModelWalker walker = new OutputModelWalker(tool, templates);
		return walker.walk(file);
	}

	public STGroup loadTemplates() {
		STGroup result = new STGroupFile(TEMPLATES);
		result.registerRenderer(Integer.class, new NumberRenderer());
		result.registerRenderer(String.class, new StringRenderer());
		result.setListener(new STErrorListener() {
			@Override
			public void compileTimeError(STMessage msg) {
				reportError(msg);
			}

			@Override
			public void runTimeError(STMessage msg) {
				reportError(msg);
			}

			@Override
			public void IOError(STMessage msg) {
				reportError(msg);
			}

			@Override
			public void internalError(STMessage msg) {
				reportError(msg);
			}

			private void reportError(STMessage msg) {
				tool.errMgr.toolError(ErrorType.STRING_TEMPLATE_WARNING, msg.cause, msg.toString());
			}
		});

		return result;
	}

	public static String asJavaType(Type mtype) {
		return mtype.toString();
	}
}
