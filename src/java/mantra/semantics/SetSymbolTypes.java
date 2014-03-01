package mantra.semantics;

import mantra.MantraParser;
import mantra.symbols.BaseScope;
import mantra.symbols.BlockScope;
import mantra.symbols.ClassSymbol;
import mantra.symbols.EnumSymbol;
import mantra.symbols.FunctionSymbol;
import mantra.symbols.InterfaceSymbol;
import mantra.symbols.PackageSymbol;
import mantra.symbols.Scope;
import mantra.symbols.Type;
import mantra.symbols.VariableSymbol;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;

import static mantra.symbols.GlobalScope.GLOBALS;

/** After defining symbols like classes, interfaces, fields, methods, and locals
 *  we have to go back and set the type of variables
 */
public class SetSymbolTypes extends SetScopeListener {
	@Override
	public void enterPackageDef(@NotNull MantraParser.PackageDefContext ctx) {
	}

	@Override
	public void exitCompilationUnit(@NotNull MantraParser.CompilationUnitContext ctx) {
	}

	@Override
	public void enterClazz(@NotNull MantraParser.ClazzContext ctx) {
		MantraParser.TypespecContext extendsType = ctx.getRuleContext(MantraParser.TypespecContext.class, 0);
//		ClassSymbol superS = extendsType!=null ? extendsType.getText() : null;
	}

	@Override
	public void exitClazz(@NotNull MantraParser.ClazzContext ctx) {
	}

	@Override
	public void enterInterfaze(@NotNull MantraParser.InterfazeContext ctx) {
	}

	@Override
	public void exitInterfaze(@NotNull MantraParser.InterfazeContext ctx) {
	}

	@Override
	public void enterEnumDef(@NotNull MantraParser.EnumDefContext ctx) {
	}

	@Override
	public void exitEnumDef(@NotNull MantraParser.EnumDefContext ctx) {
	}

	@Override
	public void enterFunctionHead(@NotNull MantraParser.FunctionHeadContext ctx) {
	}

	@Override
	public void exitFunction(@NotNull MantraParser.FunctionContext ctx) {
	}

	@Override
	public void enterBlock(@NotNull MantraParser.BlockContext ctx) {
	}

	@Override
	public void exitBlock(@NotNull MantraParser.BlockContext ctx) {
	}

	// DEFINE SYMBOLS

	@Override
	public void enterVarField(@NotNull MantraParser.VarFieldContext ctx) {
	}

	@Override
	public void enterValField(@NotNull MantraParser.ValFieldContext ctx) {
	}

	@Override
	public void enterArgDef(@NotNull MantraParser.ArgDefContext ctx) {
	}

	@Override
	public void enterVarDeclWithType(@NotNull MantraParser.VarDeclWithTypeContext ctx) {
	}

	@Override
	public void enterVarDeclNoType(@NotNull MantraParser.VarDeclNoTypeContext ctx) {
	}

	@Override
	public void enterMultiVarDeclNoType(@NotNull MantraParser.MultiVarDeclNoTypeContext ctx) {
	}

	@Override
	public void enterValDeclNoType(@NotNull MantraParser.ValDeclNoTypeContext ctx) {
	}

	@Override
	public void enterValDeclWithType(@NotNull MantraParser.ValDeclWithTypeContext ctx) {
	}
}
