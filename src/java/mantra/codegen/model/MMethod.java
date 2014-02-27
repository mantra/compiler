package mantra.codegen.model;

import mantra.symbols.FunctionSymbol;

public class MMethod extends MMember {
	public FunctionSymbol sym;
	public String name;

	public MMethod(FunctionSymbol sym) {
		this.sym = sym;
		this.name = sym.name;
	}
}
