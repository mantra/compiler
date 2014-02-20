package mantra.semantics;

import mantra.MantraBaseListener;
import mantra.MantraParser;
import org.antlr.v4.runtime.misc.NotNull;

public class VerifySymbols extends MantraBaseListener {

	@Override
	public void enterPackageDef(@NotNull MantraParser.PackageDefContext ctx) {

	}

	@Override
	public void enterClazz(@NotNull MantraParser.ClazzContext ctx) {
		System.out.println("sup="+ctx.supType);
		System.out.println("impl="+ctx.iTypes);
	}

	@Override
	public void enterEnumDef(@NotNull MantraParser.EnumDefContext ctx) {
		System.out.println("elem "+ctx.elements);
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
}
