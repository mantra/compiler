package mantra.codegen.model;

import mantra.symbols.InterfaceSymbol;

/** Interface is just a class w/o fields and method bodies */
public class MInterface extends MAggregate {
	public MInterface(InterfaceSymbol sym) {
		super(sym);
	}
}
