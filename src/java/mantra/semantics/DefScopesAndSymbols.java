package mantra.semantics;

import mantra.MantraBaseListener;
import static mantra.MantraParser.*;

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
		ctx.scope = s; // record scope in parse tree
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
		TypespecContext extendsType = ctx.getRuleContext(TypespecContext.class, 0);
//		ClassSymbol superS = extendsType!=null ? extendsType.getText() : null;
		ClassSymbol s = new ClassSymbol(currentScope, ctx.name.getText(), null);
		currentScope.define(s);
		ctx.scope = s; // record scope in parse tree
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
		ctx.scope = s; // record scope in parse tree
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
		ctx.scope = s; // record scope in parse tree
		enterScope(s);
	}

	@Override
	public void exitEnumDef(@NotNull EnumDefContext ctx) {
		exitScope();
	}

	@Override
	public void enterFunctionHead(@NotNull FunctionHeadContext ctx) {
		FunctionSymbol s = new FunctionSymbol(currentScope, ctx.ID().getText(), null);
		currentScope.define(s);
		ctx.scope = s; // record scope in parse tree
		enterScope(s);
	}

	@Override
	public void exitFunction(@NotNull FunctionContext ctx) {
		exitScope();
	}

	@Override
	public void enterBlock(@NotNull BlockContext ctx) {
		BlockScope s = new BlockScope(currentScope);
		ctx.scope = s; // record scope in parse tree
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
	public void enterVarField(@NotNull VarFieldContext ctx) {
		defineVarWithType(ctx);
	}

	@Override
	public void enterValField(@NotNull ValFieldContext ctx) {
		DeclContext d = ctx.getRuleContext(DeclContext.class, 0);
		VariableSymbol s = new VariableSymbol(currentScope, d.name.getText(), new Type(d.typespec()));
		s.isConstant = true;
		currentScope.define(s);
	}

	@Override
	public void enterArgDef(@NotNull ArgDefContext ctx) {
		defineVarWithType(ctx);
	}

	@Override
	public void enterVarDeclWithType(@NotNull VarDeclWithTypeContext ctx) {
		defineVarWithType(ctx);
	}

	@Override
	public void enterVarDeclNoType(@NotNull VarDeclNoTypeContext ctx) {
		VariableSymbol s = new VariableSymbol(currentScope, ctx.name.getText(), null);
		currentScope.define(s);
	}

	@Override
	public void enterMultiVarDeclNoType(@NotNull MultiVarDeclNoTypeContext ctx) {
		for (Token t : ctx.names) {
			VariableSymbol s = new VariableSymbol(currentScope, t.getText(), null);
			currentScope.define(s);
		}
	}

	// TODO go back and compute types for these fields

	@Override
	public void enterValDeclNoType(@NotNull ValDeclNoTypeContext ctx) {
		VariableSymbol s = new VariableSymbol(currentScope, ctx.name.getText(), null);
		currentScope.define(s);
	}

	@Override
	public void enterValDeclWithType(@NotNull ValDeclWithTypeContext ctx) {
		defineVarWithType(ctx);
	}

	// SUPPORT

	public void defineVarWithType(ParserRuleContext ctx) {
		DeclContext d = ctx.getRuleContext(DeclContext.class, 0);
		VariableSymbol s = new VariableSymbol(currentScope, d.name.getText(),
											  new Type(d.typespec()));
		currentScope.define(s);
	}
}
