package mantra.codegen.model;

import mantra.symbols.ScopedSymbol;

/** A class, interface, or enum */
public class MAggregate extends OutputModelObject {
	public ScopedSymbol sym;
	public String name;

	public MAggregate(ScopedSymbol sym) {
		this.sym = sym;
		this.name = sym.getName();
	}
}
