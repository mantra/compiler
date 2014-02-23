package mantra.symbols;

import org.antlr.v4.runtime.tree.ParseTree;

/** Represents all possible type trees like simple int, int[10],
 *  list<string>, and </string>func<(int):float>[10].
 */
public class Type {
	public ParseTree tree;
	public Type(ParseTree tree) { this.tree = tree; }

	@Override
	public String toString() {
		return tree.getText();
	}
}
