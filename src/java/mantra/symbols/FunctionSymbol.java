package mantra.symbols;

import mantra.misc.Utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class FunctionSymbol extends ScopedSymbol {
	Type retType;
	Map<String, Symbol> orderedArgs = new LinkedHashMap<String, Symbol>();

	public FunctionSymbol(String name, Type retType, Scope enclosingScope) {
        super(name, enclosingScope);
		this.retType = retType;
    }

    public Map<String, Symbol> getMembers() { return orderedArgs; }

    public String getName() {
        return name+"("+ Utils.stripBrackets(orderedArgs.keySet().toString())+")";
    }
}
