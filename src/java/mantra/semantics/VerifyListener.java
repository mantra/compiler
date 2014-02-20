package mantra.semantics;

import mantra.MantraParser;
import mantra.symbols.Scope;
import org.antlr.v4.runtime.misc.NotNull;

public class VerifyListener extends SetScopeListener {
	public Scope currentScope; // pick up scope from tree as we descend

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
	public void enterFunctionHead(@NotNull MantraParser.FunctionHeadContext ctx) {
		super.enterFunctionHead(ctx);
	}

	@Override
	public void enterBlock(@NotNull MantraParser.BlockContext ctx) {
		super.enterBlock(ctx);
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
	public void exitArrayLvalue(@NotNull MantraParser.ArrayLvalueContext ctx) {
		System.out.println("array index");
	}

	// expression stuff

	/** Has to be exit not enter so op occurs after children */
	@Override
	public void exitFieldAccessExpr(@NotNull MantraParser.FieldAccessExprContext ctx) {
		System.out.println("field "+ctx.ID());
	}

	@Override
	public void exitArrayIndexExpr(@NotNull MantraParser.ArrayIndexExprContext ctx) {
		System.out.println("array index on right");
	}

	@Override
	public void enterPrimary(@NotNull MantraParser.PrimaryContext ctx) {
		if ( ctx.ID()!=null ) {
			System.out.println("ID ref "+ctx.ID().getText());
		}
	}

	public void exitScope() {
		if ( currentScope!=null ) {
			currentScope = currentScope.getEnclosingScope();
		}
	}
}
