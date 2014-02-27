package mantra.codegen;

import mantra.MantraParser;
import mantra.Tool;
import mantra.codegen.model.MClass;
import mantra.codegen.model.MField;
import mantra.codegen.model.MFile;
import mantra.codegen.model.MMember;
import mantra.codegen.model.MMethod;
import mantra.semantics.SetScopeListener;
import mantra.symbols.ClassSymbol;
import mantra.symbols.FunctionSymbol;
import mantra.symbols.VariableSymbol;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.xpath.XPath;

import java.util.Collection;

import static mantra.MantraParser.*;

/** Construct an output model for a single Mantra file. Only a single
 *  aggregate (class, interface, enum) can be defined within a single file.
 *  Must play well in Java universe.
 */
public class ModelBuilder extends SetScopeListener {
	public Tool tool;
	public ParseTree tree;
	public Parser parser;

	public MFile fileModel;
	public String packageName;
	public String mantraFileName;

	public MClass classInFile;

	public ModelBuilder(Tool tool, ParseTree tree, Parser parser, String mantraFileName) {
		this.tool = tool;
		this.tree = tree;
		this.parser = parser;
		this.mantraFileName = mantraFileName;
	}

	public MFile getModel() {
		return fileModel;
	}

	// NOTE: make sure to call super.exitXXX and enterXXX so we get scopes set

	@Override
	public void exitCompilationUnit(@NotNull CompilationUnitContext ctx) {
		super.exitCompilationUnit(ctx);
		fileModel = new MFile(mantraFileName, packageName, classInFile);
	}

	@Override
	public void exitPackageDef(@NotNull PackageDefContext ctx) {
		super.exitPackageDef(ctx);
		packageName = ctx.packageName().getText();
	}

	@Override
	public void enterClazz(@NotNull ClazzContext ctx) {
		super.enterClazz(ctx);
		ClassSymbol s = (ClassSymbol)currentScope.resolve(ctx.ID().getText());
		classInFile = new MClass(s);
	}

	@Override
	public void exitVarField(@NotNull VarFieldContext ctx) {
		DeclContext decl = ctx.getRuleContext(DeclContext.class, 0);
		String fieldName = decl.ID().getText();
		VariableSymbol s = (VariableSymbol)currentScope.resolve(fieldName);
		MField f = new MField(s);
		classInFile.addMember(f);
	}

	@Override
	public void exitMemberFunction(@NotNull MemberFunctionContext ctx) {
		super.exitMemberFunction(ctx);
		Collection<ParseTree> fheads = XPath.findAll(ctx, "//functionHead", parser);
		FunctionHeadContext fhead = (FunctionHeadContext)fheads.iterator().next();
		FunctionSymbol s = (FunctionSymbol)currentScope.resolve(fhead.ID().getText());
		MMethod m = new MMethod(s);
		classInFile.addMember(m);
	}

	@Override
	public void exitValField(@NotNull ValFieldContext ctx) {
	}

	@Override
	public void exitVarDeclWithType(@NotNull VarDeclWithTypeContext ctx) {
	}

	@Override
	public void exitVarDeclNoType(@NotNull VarDeclNoTypeContext ctx) {
	}

	@Override
	public void exitMultiVarDeclNoType(@NotNull MultiVarDeclNoTypeContext ctx) {
	}

}
