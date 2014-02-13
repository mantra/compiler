package mantra.symbols;

import java.util.Map;

public class PackageSymbol extends ScopedSymbol {
	public PackageSymbol(String name, Scope enclosingScope) {
		super(name, enclosingScope);
	}

	@Override
	public Map<String, Symbol> getMembers() {
		return null;
	}
}
