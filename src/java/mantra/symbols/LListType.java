package mantra.symbols;

import org.antlr.v4.runtime.tree.ParseTree;

public class LListType extends ListType {
	public LListType(ParseTree tree, Type elemType) {
		super(tree, elemType);
	}
}
