package mantra.symbols;

import org.antlr.v4.runtime.tree.ParseTree;

public class MapType extends Type {
	public Type keyType;
	public Type valueType;
	public MapType(ParseTree tree, Type keyType, Type valueType) {
		super(tree);
		this.keyType = keyType;
		this.valueType = valueType;
	}

	@Override
	public String toString() {
		return "map<"+keyType+","+valueType+">";
	}
}
