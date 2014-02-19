package mantra.symbols;

import java.util.Map;

public class PackageSymbol extends ScopedSymbol {
	public PackageSymbol(Scope enclosingScope, String name) {
		super(name, enclosingScope);
	}

	@Override
	public Map<String, Symbol> getMembers() {
		return null;
	}
}
