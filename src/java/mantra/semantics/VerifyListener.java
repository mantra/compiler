package mantra.semantics;

import mantra.MantraParser;
import mantra.symbols.Symbol;
import org.antlr.v4.runtime.misc.NotNull;

public class VerifyListener extends SetScopeListener {
	@Override
	public void enterPackageDef(@NotNull MantraParser.PackageDefContext ctx) {
		super.enterPackageDef(ctx);
	}

	@Override
	public void enterClazz(@NotNull MantraParser.ClazzContext ctx) {
		super.enterClazz(ctx);
		System.out.println("sup="+ctx.supType);
		System.out.println("impl=" + ctx.iTypes);
	}

	@Override
	public void enterInterfaze(@NotNull MantraParser.InterfazeContext ctx) {
		super.enterInterfaze(ctx);
	}

	@Override
	public void enterEnumDef(@NotNull MantraParser.EnumDefContext ctx) {
		super.enterEnumDef(ctx);
		System.out.println("elem " + ctx.elements);
	}

	@Override
	public void enterCatchType(@NotNull MantraParser.CatchTypeContext ctx) {
	}

	// left side of assignment

	@Override
	public void enterIdLvalue(@NotNull MantraParser.IdLvalueContext ctx) {
	}

	@Override
	public void exitFieldLvalue(@NotNull MantraParser.FieldLvalueContext ctx) {
		System.out.println("field "+ctx.ID());
	}

	@Override
	public void exitIndexedLvalue(@NotNull MantraParser.IndexedLvalueContext ctx) {
		System.out.println("array index");
	}

	// expression stuff

	/** Has to be exit not enter so op occurs after children */
	@Override
	public void exitFieldAccessExpr(@NotNull MantraParser.FieldAccessExprContext ctx) {
		System.out.println("field "+ctx.ID());
	}

	@Override
	public void exitIndexedExpr(@NotNull MantraParser.IndexedExprContext ctx) {
		System.out.println("array index on right");
	}

	@Override
	public void enterPrimary(@NotNull MantraParser.PrimaryContext ctx) {
		if ( ctx.ID()!=null ) {
			System.out.println("ID ref "+ctx.ID().getText());
			Symbol v = currentScope.resolve(ctx.ID().getText());
			System.out.println("resolved to "+v);
		}
	}

	public void exitScope() {
		if ( currentScope!=null ) {
			currentScope = currentScope.getEnclosingScope();
		}
	}
}
