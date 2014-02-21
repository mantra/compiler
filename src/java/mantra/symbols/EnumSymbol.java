package mantra.symbols;

public class EnumSymbol extends ScopedSymbol implements Type {
	public EnumSymbol(Scope enclosingScope, String name) {
     super(name, enclosingScope);
	}
}
