package mantra.semantics;

import mantra.MantraBaseListener;
import mantra.MantraParser;
import mantra.symbols.Scope;
import org.antlr.v4.runtime.misc.NotNull;

import static mantra.symbols.GlobalScope.GLOBALS;

public class SetScopeListener extends MantraBaseListener {
	public Scope currentScope; // pick up scope from tree as we descend and ascend

	@Override
	public void enterPackageDef(@NotNull MantraParser.PackageDefContext ctx) {
		currentScope = ctx.scope; // pick up scope
	}

	@Override
	public void exitCompilationUnit(@NotNull MantraParser.CompilationUnitContext ctx) {
		if ( currentScope!=GLOBALS ) {
			exitScope(); // pop package scope (if any)
		}
	}

	@Override
	public void enterClazz(@NotNull MantraParser.ClazzContext ctx) {
		currentScope = ctx.scope; // pick up scope
	}

	@Override
	public void exitClazz(@NotNull MantraParser.ClazzContext ctx) {
		exitScope();
	}

	@Override
	public void enterInterfaze(@NotNull MantraParser.InterfazeContext ctx) {
		currentScope = ctx.scope; // pick up scope
	}

	@Override
	public void exitInterfaze(@NotNull MantraParser.InterfazeContext ctx) {
		exitScope();
	}

	@Override
	public void enterEnumDef(@NotNull MantraParser.EnumDefContext ctx) {
		currentScope = ctx.scope; // pick up scope
	}

	@Override
	public void exitEnumDef(@NotNull MantraParser.EnumDefContext ctx) {
		exitScope();
	}

	@Override
	public void enterFunction(@NotNull MantraParser.FunctionContext ctx) {
		currentScope = ctx.scope; // pick up scope
	}

	@Override
	public void exitFunction(@NotNull MantraParser.FunctionContext ctx) {
		exitScope();
	}

	@Override
	public void enterBlock(@NotNull MantraParser.BlockContext ctx) {
		currentScope = ctx.scope; // pick up scope
	}

	@Override
	public void exitBlock(@NotNull MantraParser.BlockContext ctx) {
		exitScope();
	}

	public void exitScope() {
		if ( currentScope!=null ) {
			currentScope = currentScope.getEnclosingScope();
		}
	}
}
