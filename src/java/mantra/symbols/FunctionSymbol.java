package mantra.symbols;

import mantra.misc.Utils;

public class FunctionSymbol extends ScopedSymbol {
	TypeName retType;

	public FunctionSymbol(Scope enclosingScope, String name, TypeName retType) {
        super(name, enclosingScope);
		this.retType = retType;
    }

    public String toString() {
        return name+"("+ Utils.stripBrackets(symbols.keySet().toString())+")";
    }
}
