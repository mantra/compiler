package mantra.symbols;

public class BlockScope extends BaseScope {
    public BlockScope(Scope parent) { super(parent); }
    public String getScopeName() { return "local"; }
}
