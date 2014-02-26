package mantra.symbols;

import org.antlr.v4.runtime.tree.ParseTree;

public class InstanceType extends Type implements TypeName {
	public ScopedSymbol symbol;
	public InstanceType(ParseTree tree, ScopedSymbol symbol) {
		super(tree);
		this.symbol = symbol;
	}

	@Override
	public String getName() {
		return symbol.getName();
	}

	@Override
	public String toString() {
		return symbol.getQualifiedName();
	}
}
