package mantra.symbols;

/** A symbol to represent built in types such int, float primitive types and
 *  complex ones like list, set, map, ...
 */
public class BuiltInTypeSymbol extends BaseSymbol implements Type {
    public BuiltInTypeSymbol(String name) { super(name); }
}
