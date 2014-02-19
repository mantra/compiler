package mantra.symbols;

import java.util.LinkedHashMap;
import java.util.Map;

public class EnumSymbol extends ScopedSymbol implements Type {
	/** List of all fields and methods */
	public Map<String,Symbol> elements=new LinkedHashMap<String,Symbol>();

	public EnumSymbol(Scope enclosingScope, String name) {
     super(name, enclosingScope);
	}

	@Override
	public Map<String, Symbol> getMembers() {
		return elements;
	}
}
