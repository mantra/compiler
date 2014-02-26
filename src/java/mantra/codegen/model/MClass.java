package mantra.codegen.model;

import mantra.symbols.ClassSymbol;

/** A model of a mantra class */
public class MClass extends OutputModelObject {
	public ClassSymbol sym;
	public String name;

	public MClass(ClassSymbol sym) {
		this.sym = sym;
		this.name = sym.getName();
	}
}
