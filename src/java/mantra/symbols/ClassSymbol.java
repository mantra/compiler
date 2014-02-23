package mantra.symbols;

import mantra.misc.Utils;

public class ClassSymbol extends ScopedSymbol implements TypeName {
    /** This is the superclass not enclosingScope field. We still record
     *  the enclosing scope so we can push in and pop out of class defs.
     */
    ClassSymbol superClass;

    public ClassSymbol(Scope enclosingScope, String name, ClassSymbol superClass) {
        super(name, enclosingScope);
        this.superClass = superClass;
    }

    public Scope getParentScope() {
        if ( superClass==null ) return enclosingScope; // globals
        return superClass; // if not root object, return super
    }

    /** For a.b, only look in a's class hierarchy to resolve b, not globals */
    public Symbol resolveMember(String name) {
        Symbol s = symbols.get(name);
        if ( s!=null ) return s;
        // if not here, check just the superclass chain
        if ( superClass != null ) {
            return superClass.resolveMember(name);
        }
        return null; // not found
    }

    public String toString() {
        return "class "+name+":{"+
               Utils.stripBrackets(symbols.keySet().toString())+"}";
    }
}
