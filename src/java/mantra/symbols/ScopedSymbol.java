package mantra.symbols;

import org.antlr.v4.runtime.tree.ParseTree;

public abstract class ScopedSymbol extends BaseScope implements Symbol, Scope {
	public String name;      // All symbols at least have a name
//	public Type type;
	public Scope scope;      // All symbols know what scope contains them.
	public ParseTree def;    // points at ID node in tree

    public ScopedSymbol(String name, Scope enclosingScope) {
		super(enclosingScope);
        this.name = name;
	}

	@Override public String getName() { return name; }
	@Override public Scope getScope() { return scope; }

	public Scope getParentScope() { return getEnclosingScope(); }
    public Scope getEnclosingScope() { return enclosingScope; }

    public String getScopeName() { return name; }

	public String getQualifiedName() {
		if ( scope instanceof PackageSymbol ) {
			return scope+"::"+name;
		}
		return scope+"."+name;
	}
}
