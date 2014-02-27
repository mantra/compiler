package mantra.codegen;

import mantra.MantraParser;
import mantra.Tool;
import mantra.codegen.model.MClass;
import mantra.codegen.model.MFile;
import mantra.codegen.model.OutputModelObject;
import mantra.semantics.SetScopeListener;
import mantra.symbols.ClassSymbol;
import org.antlr.v4.runtime.misc.NotNull;

public class ModelBuilder extends SetScopeListener {
	public Tool tool;

	public MFile mfile;
	public MClass mclass;
	public String packageName;
	public String mantraFileName;

	public ModelBuilder(Tool tool, String mantraFileName) {
		this.tool = tool;
		this.mantraFileName = mantraFileName;
	}

	// NOTE: make sure call super.exitXXX and enterXXX so we get scopes set

	@Override
	public void exitPackageDef(@NotNull MantraParser.PackageDefContext ctx) {
		super.exitPackageDef(ctx);
		packageName = ctx.packageName().getText();
	}

	@Override
	public void exitClazz(@NotNull MantraParser.ClazzContext ctx) {
		super.exitClazz(ctx);
		mclass = new MClass((ClassSymbol)currentScope.resolve(ctx.ID().getText()));
		mfile = new MFile(mantraFileName, packageName, mclass);
	}

	public OutputModelObject getModel() {
		return mfile;
	}
}
