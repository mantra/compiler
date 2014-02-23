package mantra.symbols;

public class EnumSymbol extends ScopedSymbol implements TypeName {
	public EnumSymbol(Scope enclosingScope, String name) {
     super(name, enclosingScope);
	}
}
