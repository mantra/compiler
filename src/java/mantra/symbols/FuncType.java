package mantra.symbols;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.List;

public class FuncType extends Type {
	public static class Arg {
		public String name;
		public Type type;
		public ParseTree defaultValue;
	}
	public Type retType;
	public List<Arg> argTypes = new ArrayList<Arg>();
	public FuncType(ParseTree tree, List<Arg> argTypes, Type retType) {
		super(tree);
		this.retType = retType;
	}

	@Override
	public String toString() {
		return "func<"+ retType +">";
	}
}
