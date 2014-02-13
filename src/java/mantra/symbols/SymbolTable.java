package mantra.symbols;

public class SymbolTable {
	public GlobalScope globals;
	public ClassSymbol objectRoot;

    public SymbolTable() { initTypeSystem(); }

    protected void initTypeSystem() {
		globals = new GlobalScope();
		objectRoot = new ClassSymbol("Object", globals, null);
		globals.define(objectRoot);
        /*
        FunctionSymbol hashCode =
           new FunctionSymbol("hashCode",new BuiltInTypeSymbol("int"),objectRoot);
        objectRoot.define(hashCode);
         */

		globals.define(new BuiltInTypeSymbol("boolean"));
		globals.define(new BuiltInTypeSymbol("char"));
		globals.define(new BuiltInTypeSymbol("byte"));
		globals.define(new BuiltInTypeSymbol("short"));
		globals.define(new BuiltInTypeSymbol("int"));
		globals.define(new BuiltInTypeSymbol("long"));
		globals.define(new BuiltInTypeSymbol("float"));
		globals.define(new BuiltInTypeSymbol("double"));

		globals.define(new BuiltInTypeSymbol("string"));
		globals.define(new BuiltInTypeSymbol("map"));
		globals.define(new BuiltInTypeSymbol("list"));
		globals.define(new BuiltInTypeSymbol("llist"));
		globals.define(new BuiltInTypeSymbol("set"));
	}

    /** 'this' and 'super' need to know about enclosing class */
    public static ClassSymbol getEnclosingClass(Scope s) {
        while ( s!=null ) { // walk upwards from s looking for a class
            if ( s instanceof ClassSymbol ) return (ClassSymbol)s;
            s = s.getParentScope();
        }
        return null;
    }

    public String toString() { return globals.toString(); }
}
