package mantra.symbols;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.Map;

public abstract class ScopedSymbol extends BaseScope implements Symbol, Scope {
	public String name;      // All symbols at least have a name
	public Type type;
	public Scope scope;      // All symbols know what scope contains them.
	public ParseTree def;    // points at ID node in tree

    public ScopedSymbol(String name, Scope enclosingScope) {
		super(enclosingScope);
        this.name = name;
	}

	public Symbol resolve(String name) {
        Symbol s = getMembers().get(name);
        if ( s!=null ) return s;
        // if not here, check any parent scope
        if ( getParentScope() != null ) {
            return getParentScope().resolve(name);
        }
        return null; // not found
    }

    public void define(Symbol sym) {
//        getMembers().put(sym.name, sym);
//        sym.scope = this; // track the scope in each symbol
    }

	@Override
	public String getName() { return name; }

	public Scope getParentScope() { return getEnclosingScope(); }
    public Scope getEnclosingScope() { return enclosingScope; }

    public String getScopeName() { return name; }

    /** Indicate how subclasses store scope members. Allows us to
     *  factor out common code in this class.
     */
    public abstract Map<String, Symbol> getMembers();
}
