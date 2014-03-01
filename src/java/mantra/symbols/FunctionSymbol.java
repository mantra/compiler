package mantra.symbols;

import mantra.misc.Utils;

import java.util.ArrayList;
import java.util.List;

public class FunctionSymbol extends ScopedSymbol {
	public Type retType;
	public List<Arg> args;

	public FunctionSymbol(Scope enclosingScope, String name,
						  List<Arg> args, Type retType)
	{
        super(name, enclosingScope);
		this.retType = retType;
    }

    public String toString() {
        return name+"("+ Utils.stripBrackets(symbols.keySet().toString())+")";
    }
}
