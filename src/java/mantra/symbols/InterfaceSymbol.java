package mantra.symbols;

import mantra.misc.Utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class InterfaceSymbol extends ScopedSymbol implements Type {
    /** This is the super interface not enclosingScope field. We still record
     *  the enclosing scope so we can push in and pop out of interface defs.
     */
    InterfaceSymbol superInterface;

    /** List of all methods */
    public Map<String,Symbol> members=new LinkedHashMap<String,Symbol>();

    public InterfaceSymbol(String name, Scope enclosingScope, InterfaceSymbol superInterface) {
        super(name, enclosingScope);
        this.superInterface = superInterface;
    }

    public Scope getParentScope() {
        if ( superInterface==null ) return enclosingScope; // globals
        return superInterface; // if not root interface, return super
    }

    /** For a.b, only look in a's class hierarchy to resolve b, not globals */
    public Symbol resolveMember(String name) {
        return null; // not found
    }

    public Map<String, Symbol> getMembers() { return members; }
    public String toString() {
        return "interface "+name+":{"+
               Utils.stripBrackets(members.keySet().toString())+"}";
    }
}
