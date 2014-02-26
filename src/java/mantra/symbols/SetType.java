package mantra.symbols;

import org.antlr.v4.runtime.tree.ParseTree;

public class SetType extends Type {
	public Type elemType;
	public SetType(ParseTree tree, Type elemType) {
		super(tree);
		this.elemType = elemType;
	}

	@Override
	public String toString() {
		return "set<"+elemType+">";
	}
}
