package mantra.symbols;

/** hmm...readonly scope with len() and such?  Free functions then have to live
 *  in a package like math::sin()
 *
 *  wait: what about packages at top level?  Maybe that is the default package
 *  so define one and add.
 */
public class GlobalScope extends BaseScope {
	public static final GlobalScope GLOBALS = new GlobalScope();

    private GlobalScope() { super(null); }
    public String getScopeName() { return "global"; }

	@Override
	public void setEnclosingScope(Scope enclosingScope) {  // readonly
	}
}
