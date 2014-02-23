package mantra.symbols;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BaseScope implements Scope {
	public Scope enclosingScope; // null if global (outermost) scope

	public Map<String, Symbol> symbols = new LinkedHashMap<String, Symbol>();

	/** All scopes enclosed/nested within this one; down ptrs in scope tree. */
	List<Scope> nestedScopes = new ArrayList<Scope>();

	public BaseScope(Scope enclosingScope) { setEnclosingScope(enclosingScope);	}

	/** Indicate how subclasses store scope members. Allows us to
	 *  factor out common code in this class.
	 */
	public Map<String, Symbol> getMembers() {
		return symbols;
	}

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

	@Override
	public Symbol resolve(String name) {
		Symbol s = symbols.get(name);
		if ( s!=null ) {
			System.out.println("found "+name+" in "+this.asScopeStackString());
			return s;
		}
		// if not here, check any enclosing scope
		if ( getParentScope() != null ) return getParentScope().resolve(name);
		return null; // not found
	}

	public void define(Symbol sym) {
		symbols.put(sym.getName(), sym);
	}

	public Scope getParentScope() { return getEnclosingScope(); }
	public List<Scope> getParentScopes() {
		return new ArrayList<Scope>() {{add(getParentScope());}};
	}
	public Scope getEnclosingScope() { return enclosingScope; }

	@Override
	public Collection<Symbol> getSymbols() {
		return symbols.values();
	}

	@Override
	public Set<String> getSymbolNames() {
		return symbols.keySet();
	}

	public String toString() { return symbols.keySet().toString(); }

	public String asScopeStackString() {
		List<String> names = new ArrayList<String>();
		Scope p = this;
		while ( p!=null ) {
			names.add(p.getScopeName());
			p = p.getEnclosingScope();
		}
		Collections.reverse(names);
		return names.toString();
	}

	public static void dump(Scope s) {
		dump(s, 0);
	}

	public static void dump(Scope s, int level) {
		tab(level);	System.out.println(s.getScopeName()+" {");
		level++;
		for (Symbol sym : s.getSymbols()) {
			if ( !(sym instanceof Scope) ) {
				tab(level);	System.out.println(sym);
			}
		}
		for (Scope nested : s.getNestedScopes()) {
			dump(nested, level);
		}
		level--;
		tab(level);	System.out.println("}");
	}

	public static void tab(int n) {
		for (int i=1; i<=n; i++) System.out.print("    ");
	}
}
