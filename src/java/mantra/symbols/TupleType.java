package mantra.symbols;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public class TupleType extends Type {
	List<Type> elemTypes;
	public TupleType(ParseTree tree, List<Type> elemTypes) {
		super(tree);
		this.elemTypes = elemTypes;
	}

	@Override
	public String toString() {
		return "("+elemTypes+")";
	}
}
