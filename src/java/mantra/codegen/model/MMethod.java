package mantra.codegen.model;

import mantra.codegen.JavaGenerator;
import mantra.symbols.Arg;
import mantra.symbols.FunctionSymbol;

import java.util.ArrayList;
import java.util.List;

public class MMethod extends MMember {
	public FunctionSymbol sym;
	public String name;
	@NestedModel public List<MArg> args = new ArrayList<MArg>();
	public String javaRetType;

	public MMethod(FunctionSymbol sym) {
		this.sym = sym;
		this.name = sym.name;
		for (Arg a : sym.args) {
			args.add( new MArg(a) );
		}
		javaRetType = JavaGenerator.asJavaType(sym.retType);
	}
}
