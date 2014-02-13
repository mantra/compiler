package mantra.symbols;

import java.util.LinkedHashMap;
import java.util.Map;

public class FunctionSymbol extends ScopedSymbol {
	Map<String, Symbol> orderedArgs = new LinkedHashMap<String, Symbol>();

	public FunctionSymbol(String name, Type retType, Scope parent) {
        super(name, retType, parent);
    }

    public Map<String, Symbol> getMembers() { return orderedArgs; }

    public String getName() {
        return name+"("+stripBrackets(orderedArgs.keySet().toString())+")";
    }
}
