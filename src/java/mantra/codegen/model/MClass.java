package mantra.codegen.model;

import mantra.symbols.ClassSymbol;

import java.util.ArrayList;
import java.util.List;

/** A model of a mantra class */
public class MClass extends MAggregate {
	@NestedModel public List<MMember> members = new ArrayList<MMember>();

	public MClass(ClassSymbol sym) {
		super(sym);
	}

	public void addMember(MMember m) {
		if ( m!=null ) {
			members.add(m);
		}
	}
}
