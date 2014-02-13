package mantra.symbols;

import static mantra.symbols.GlobalScope.GLOBALS;

public class SymbolTable {
	public PackageSymbol defaultPackage;
	public ClassSymbol objectRoot;

    public SymbolTable() { initTypeSystem(); }

    protected void initTypeSystem() {
		defaultPackage = new PackageSymbol("main", GLOBALS);
		GLOBALS.define(defaultPackage);

		// define mantra::lang
		PackageSymbol mantraPackage = new PackageSymbol("mantra", GLOBALS);
		PackageSymbol mantraLangPackage = new PackageSymbol("lang", mantraPackage);
		objectRoot = new ClassSymbol("Object", mantraLangPackage, null);
		GLOBALS.define(objectRoot);

        /*
        FunctionSymbol hashCode =
           new FunctionSymbol("hashCode",new BuiltInTypeSymbol("int"),objectRoot);
        objectRoot.define(hashCode);
         */

		GLOBALS.define(new BuiltInTypeSymbol("boolean"));
		GLOBALS.define(new BuiltInTypeSymbol("char"));
		GLOBALS.define(new BuiltInTypeSymbol("byte"));
		GLOBALS.define(new BuiltInTypeSymbol("short"));
		GLOBALS.define(new BuiltInTypeSymbol("int"));
		GLOBALS.define(new BuiltInTypeSymbol("long"));
		GLOBALS.define(new BuiltInTypeSymbol("float"));
		GLOBALS.define(new BuiltInTypeSymbol("double"));

		GLOBALS.define(new BuiltInTypeSymbol("string"));
		GLOBALS.define(new BuiltInTypeSymbol("map"));
		GLOBALS.define(new BuiltInTypeSymbol("list"));
		GLOBALS.define(new BuiltInTypeSymbol("llist"));
		GLOBALS.define(new BuiltInTypeSymbol("set"));
	}

    /** 'this' and 'super' need to know about enclosing class */
    public static ClassSymbol getEnclosingClass(Scope s) {
        while ( s!=null ) { // walk upwards from s looking for a class
            if ( s instanceof ClassSymbol ) return (ClassSymbol)s;
            s = s.getParentScope();
        }
        return null;
    }

    public String toString() { return GLOBALS.toString(); }
}
