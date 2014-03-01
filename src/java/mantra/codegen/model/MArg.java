package mantra.codegen.model;

import mantra.codegen.JavaGenerator;
import mantra.symbols.Arg;

public class MArg extends OutputModelObject {
	public Arg arg;
	public String name;
	public String javaType;
	@NestedModel MExpr defaultValue;

	public MArg(Arg arg) {
		this.arg = arg;
		this.name = arg.name;
		javaType = JavaGenerator.asJavaType(arg.type);
	}
}
