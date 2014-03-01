package mantra.symbols;

import org.antlr.v4.runtime.tree.ParseTree;

public class Arg {
	public String name;
	public Type type;
	public ParseTree defaultValue;

	public Arg(String name, Type type, ParseTree defaultValue) {
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
	}
}
