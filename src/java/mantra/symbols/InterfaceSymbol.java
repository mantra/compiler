package mantra.symbols;

import mantra.misc.Utils;

import java.util.Collections;
import java.util.List;

public class InterfaceSymbol extends ScopedSymbol implements TypeName {
    /** This is the super interface not enclosingScope field. We still record
     *  the enclosing scope so we can push in and pop out of interface defs.
	 *  The elements are actually InterfaceSymbols.
     */
	List<Scope> superInterfaces;

    public InterfaceSymbol(Scope enclosingScope, String name, List<Scope> superInterfaces) {
        super(name, enclosingScope);
        this.superInterfaces = superInterfaces;
    }

	@Override
    public Scope getParentScope() {
		throw new UnsupportedOperationException("interfaces can have multiple superinterfaces");
    }

	@Override
	public List<Scope> getParentScopes() {
		if ( superInterfaces!=null ) {
			return superInterfaces;
		}
		return Collections.emptyList();
	}

	/** For a.b, only look in a's class hierarchy to resolve b, not globals */
    public Symbol resolveMember(String name) {
        return null; // not found
    }

	@Override
	public String toString() {
        return "interface "+name+":{"+
               Utils.stripBrackets(symbols.keySet().toString())+"}";
    }
}
