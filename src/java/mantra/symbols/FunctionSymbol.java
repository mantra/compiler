package mantra.symbols;

import mantra.misc.Utils;

public class FunctionSymbol extends ScopedSymbol {
	Type retType;

	public FunctionSymbol(Scope enclosingScope, String name, Type retType) {
        super(name, enclosingScope);
		this.retType = retType;
    }

    public String getName() {
        return name+"("+ Utils.stripBrackets(symbols.keySet().toString())+")";
    }
}
