package mantra.symbols;

import java.util.List;

public interface Scope {
    public String getScopeName();

    /** Where to look next for symbols; superclass or enclosing scope. */
    public Scope getParentScope();

    /** Scope in which this scope defined. For global scope, it's null */
    public Scope getEnclosingScope();

	/** What scope encloses this scope. E.g., if this scope is a function,
	 *  the enclosing scope could be a class.
	 */
	public void setEnclosingScope(Scope s);

	/** We want a scope tree with up/down ptrs so support adding enclosed scopes. */
	public void addNestedScope(Scope s);

	/** Return all immediately enclosed scopes. E.g., a class would return
	 *  all nested classes and any methods.
	 */
	public List<Scope> getNestedScopes();

    /** Define a symbol in the current scope */
    public void define(Symbol sym);

    /** Look up name in this scope or in parent scope if not here */
    public Symbol resolve(String name);
}
