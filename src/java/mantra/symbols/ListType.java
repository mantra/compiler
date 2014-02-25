package mantra.symbols;

import org.antlr.v4.runtime.tree.ParseTree;

public class ListType extends Type {
	public Type elemType;
	public ListType(ParseTree tree, Type elemType) {
		super(tree);
		this.elemType = elemType;
	}

	@Override
	public String toString() {
		return "list<"+elemType+">";
	}
}
