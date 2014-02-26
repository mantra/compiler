package mantra.codegen;

import mantra.MantraParser;
import mantra.codegen.model.MClass;
import mantra.codegen.model.OutputModelObject;
import mantra.semantics.SetScopeListener;
import mantra.symbols.ClassSymbol;
import org.antlr.v4.runtime.misc.NotNull;

public class ModelBuilder extends SetScopeListener {
	public MClass mclass;

	@Override
	public void exitClazz(@NotNull MantraParser.ClazzContext ctx) {
		mclass = new MClass((ClassSymbol)currentScope.resolve(ctx.ID().getText()));
	}

	public OutputModelObject getModel() {
		return mclass;
	}
}
