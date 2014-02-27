package mantra.codegen.model;

import mantra.codegen.JavaGenerator;
import mantra.symbols.VariableSymbol;

public class MField extends MMember {
	public VariableSymbol sym;
	public String name;
	public String javaType;

	public MField(VariableSymbol sym) {
		this.sym = sym;
		this.name = sym.name;
		javaType = JavaGenerator.asJavaType(sym.type);
	}
}
