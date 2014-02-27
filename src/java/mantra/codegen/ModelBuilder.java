package mantra.codegen;

import mantra.MantraParser;
import mantra.Tool;
import mantra.codegen.model.MClass;
import mantra.codegen.model.MFile;
import mantra.semantics.SetScopeListener;
import mantra.symbols.ClassSymbol;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;

/** Construct an output model for a single Mantra file. Only a single
 *  aggregate (class, interface, enum) can be defined within a single file.
 *  Must play well in Java universe.
 */
public class ModelBuilder extends SetScopeListener {
	public Tool tool;
	public ParseTree tree;

	public MFile fileModel;
	public String packageName;
	public String mantraFileName;

	public ModelBuilder(Tool tool, ParseTree tree, String mantraFileName) {
		this.tool = tool;
		this.tree = tree;
		this.mantraFileName = mantraFileName;
	}

	// NOTE: make sure to call super.exitXXX and enterXXX so we get scopes set

	@Override
	public void exitPackageDef(@NotNull MantraParser.PackageDefContext ctx) {
		super.exitPackageDef(ctx);
		packageName = ctx.packageName().getText();
	}

	@Override
	public void exitClazz(@NotNull MantraParser.ClazzContext ctx) {
		super.exitClazz(ctx);
		MClass mclass = new MClass((ClassSymbol)currentScope.resolve(ctx.ID().getText()));
		fileModel = new MFile(mantraFileName, packageName, mclass);
	}

	public MFile getModel() {
		return fileModel;
	}
}
