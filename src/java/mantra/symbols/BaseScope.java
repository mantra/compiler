package mantra.symbols;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseScope implements Scope {
	public Scope enclosingScope; // null if global (outermost) scope

	public Map<String, Symbol> symbols = new LinkedHashMap<String, Symbol>();

	/** All scopes enclosed/nested within this one; down ptrs in scope tree. */
	List<Scope> nestedScopes = new ArrayList<Scope>();

    public BaseScope(Scope enclosingScope) { setEnclosingScope(enclosingScope);	}

	@Override
	public void addNestedScope(Scope s) {
		if ( !nestedScopes.contains(this) ) {
			nestedScopes.add(s);
		}
	}

	@Override
	public void setEnclosingScope(Scope enclosingScope) {
		this.enclosingScope = enclosingScope;
		if ( enclosingScope!=null ) {
			this.enclosingScope.addNestedScope(this);
		}
	}

	@Override
	public List<Scope> getNestedScopes() {
		return nestedScopes;
	}

    public Symbol resolve(String name) {
		Symbol s = symbols.get(name);
        if ( s!=null ) return s;
		// if not here, check any enclosing scope
		if ( getParentScope() != null ) return getParentScope().resolve(name);
		return null; // not found
	}

	public void define(Symbol sym) {
//		symbols.put(sym.name, sym);
//		sym.scope = this; // track the scope in each symbol
	}

    public Scope getParentScope() { return getEnclosingScope(); }
    public Scope getEnclosingScope() { return enclosingScope; }

	public String toString() { return symbols.keySet().toString(); }
}
