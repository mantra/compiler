package mantra.semantics;

import mantra.MantraBaseListener;
import mantra.MantraParser;
import mantra.symbols.BaseScope;
import mantra.symbols.BlockScope;
import mantra.symbols.ClassSymbol;
import mantra.symbols.EnumSymbol;
import mantra.symbols.FunctionSymbol;
import mantra.symbols.InterfaceSymbol;
import mantra.symbols.PackageSymbol;
import mantra.symbols.Scope;
import mantra.symbols.VariableSymbol;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;

import static mantra.MantraParser.BlockContext;
import static mantra.MantraParser.ClazzContext;
import static mantra.MantraParser.CompilationUnitContext;
import static mantra.MantraParser.EnumDefContext;
import static mantra.MantraParser.FunctionHeadContext;
import static mantra.MantraParser.InterfazeContext;
import static mantra.MantraParser.PackageDefContext;
import static mantra.MantraParser.TypeContext;
import static mantra.symbols.GlobalScope.GLOBALS;

/** Create scope tree and def symbols in Scopes
 *
 *  Scopes are: package, class, interface, enum, function, block
 *  Symbol defs are: package, class, interface, enum, function, arg, field, var, val
 */
public class DefScopesAndSymbols extends MantraBaseListener {
	public Scope currentScope = GLOBALS;  // start out in global scope

	// DEFINE SCOPES

	@Override
	public void enterPackageDef(@NotNull PackageDefContext ctx) {
		ParseTree pname = ctx.getChild(1);
		PackageSymbol s = new PackageSymbol(currentScope, pname.getText()); // push
		currentScope.define(s);
		enterScope(s);
	}

	@Override
	public void exitCompilationUnit(@NotNull CompilationUnitContext ctx) {
		if ( currentScope!=GLOBALS ) {
			exitScope(); // pop package scope (if any)
		}
		BaseScope.dump(currentScope);
	}

	@Override
	public void enterClazz(@NotNull ClazzContext ctx) {
		TypeContext extendsType = ctx.getRuleContext(TypeContext.class, 0);
//		ClassSymbol superS = extendsType!=null ? extendsType.getText() : null;
		ClassSymbol s = new ClassSymbol(currentScope, ctx.name.getText(), null);
		currentScope.define(s);
		enterScope(s);
	}

	@Override
	public void exitClazz(@NotNull ClazzContext ctx) {
		exitScope();
	}

	@Override
	public void enterInterfaze(@NotNull InterfazeContext ctx) {
		InterfaceSymbol s = new InterfaceSymbol(currentScope, ctx.name.getText(), null);
		currentScope.define(s);
		enterScope(s);
	}

	@Override
	public void exitInterfaze(@NotNull InterfazeContext ctx) {
		exitScope();
	}

	@Override
	public void enterEnumDef(@NotNull EnumDefContext ctx) {
		EnumSymbol s = new EnumSymbol(currentScope, ctx.name.getText());
		currentScope.define(s);
		enterScope(s);
	}

	@Override
	public void exitEnumDef(@NotNull EnumDefContext ctx) {
		exitScope();
	}

	@Override
	public void enterFunctionHead(@NotNull FunctionHeadContext ctx) {
		FunctionSymbol s = new FunctionSymbol(currentScope, ctx.name.getText(), null);
		currentScope.define(s);
		enterScope(s);
	}

	@Override
	public void exitFunction(@NotNull MantraParser.FunctionContext ctx) {
		exitScope();
	}

	@Override
	public void enterBlock(@NotNull BlockContext ctx) {
		BlockScope s = new BlockScope(currentScope);
		enterScope(s);
	}

	@Override
	public void exitBlock(@NotNull BlockContext ctx) {
		exitScope();
	}

	public void enterScope(Scope s) {
		currentScope = s;
	}
	public void exitScope() {
		if ( currentScope!=null ) {
			currentScope = currentScope.getEnclosingScope();
		}
	}

	// DEFINE SYMBOLS


	@Override
	public void enterVarDeclWithType(@NotNull MantraParser.VarDeclWithTypeContext ctx) {
		MantraParser.DeclContext d = (MantraParser.DeclContext)ctx.getChild(1);
		VariableSymbol s = new VariableSymbol(currentScope, d.name.getText(), null);
		s.scope = currentScope;
		currentScope.define(s);
	}

	@Override
	public void enterVarDeclNoType(@NotNull MantraParser.VarDeclNoTypeContext ctx) {
		VariableSymbol s = new VariableSymbol(currentScope, ctx.name.getText(), null);
		s.scope = currentScope;
		currentScope.define(s);
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
