package mantra.codegen.model;

public class MFile extends OutputModelObject {
	public String mantraFileName;
	public String packageName;

	@NestedModel public MAggregate aggregate;

	public MFile(String mantraFileName, String packageName, MAggregate aggregate) {
		this.mantraFileName = mantraFileName;
		this.packageName = packageName;
		this.aggregate = aggregate;
	}
}
