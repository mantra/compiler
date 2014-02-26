package mantra.semantics;

import mantra.MantraParser;
import mantra.symbols.ClassSymbol;
import mantra.symbols.EnumSymbol;
import mantra.symbols.InterfaceSymbol;
import mantra.symbols.ListType;
import mantra.symbols.MapType;
import mantra.symbols.SetType;
import mantra.symbols.Symbol;
import mantra.symbols.TupleType;
import mantra.symbols.Type;
import mantra.symbols.VariableSymbol;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.xpath.XPath;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ComputeTypes extends SetScopeListener {
	public Parser parser;
	public ComputeTypes(Parser parser) {
		this.parser = parser;
	}


	/*
	expression
	returns [Type type]
		:   primary												# PrimaryExpr
		|   expression '.' ID									# FieldAccessExpr
		|   type '.' 'class'									# ClassPtrExpr
		|   expression '[' expression ']'						# ArrayIndexExpr
		|	'len' '(' expression ')'							# LenExpr
		|	'xor' '(' expression ')'							# BitXorExpr
		|   expression '(' argExprList? ')' lambda?				# CallExpr
		|   '-' expression										# NegateExpr
		|   ('~'|'!') expression								# NotExpr
		|   expression ('*'|'/'|'%') expression					# MultExpr
		|   expression ('+'|'-') expression						# AddExpr
		|   expression ('<' '<' | '>' '>' '>' | '>' '>') expression	# ShiftExpr
		|   expression ('<=' | '>=' | '>' | '<') expression		# CmpExpr
		|   expression 'instanceof' type						# InstanceOfExpr
		|   expression ('==' | '!=' | 'is') expression			# EqExpr
		|   expression '&' expression							# BitAndExpr
		|   expression '^' expression							# ExpExpr
		|   expression '|' expression							# BitOrExpr
		|	expression ':' expression 							# RangeExpr
		|   expression 'and' expression							# AndExpr
		|   expression 'or' expression							# OrExpr
		|   expression 'in' expression							# InExpr
		|   expression '?' expression ':' expression			# TernaryIfExpr
		|	expression pipeOperator	expression					# PipeExpr
		;
	 */
	@Override public void exitPrimaryExpr(@NotNull MantraParser.PrimaryExprContext ctx) {
		ctx.type = ctx.primary().type;
	}
	@Override public void exitFieldAccessExpr(@NotNull MantraParser.FieldAccessExprContext ctx) { }
	@Override public void exitClassPtrExpr(@NotNull MantraParser.ClassPtrExprContext ctx) { }
	@Override public void exitIndexedExpr(@NotNull MantraParser.IndexedExprContext ctx) { }
	@Override public void exitLenExpr(@NotNull MantraParser.LenExprContext ctx) { }
	@Override public void exitBitXorExpr(@NotNull MantraParser.BitXorExprContext ctx) { }
	@Override public void exitCallExpr(@NotNull MantraParser.CallExprContext ctx) { }
	@Override public void exitNegateExpr(@NotNull MantraParser.NegateExprContext ctx) { }
	@Override public void exitNotExpr(@NotNull MantraParser.NotExprContext ctx) { }
	@Override public void exitMultExpr(@NotNull MantraParser.MultExprContext ctx) { }
	@Override public void exitAddExpr(@NotNull MantraParser.AddExprContext ctx) { }
	@Override public void exitShiftExpr(@NotNull MantraParser.ShiftExprContext ctx) { }
	@Override public void exitCmpExpr(@NotNull MantraParser.CmpExprContext ctx) { }
	@Override public void exitInstanceOfExpr(@NotNull MantraParser.InstanceOfExprContext ctx) { }
	@Override public void exitEqExpr(@NotNull MantraParser.EqExprContext ctx) { }
	@Override public void exitBitAndExpr(@NotNull MantraParser.BitAndExprContext ctx) { }
	@Override public void exitExpExpr(@NotNull MantraParser.ExpExprContext ctx) { }
	@Override public void exitBitOrExpr(@NotNull MantraParser.BitOrExprContext ctx) { }
	@Override public void exitRangeExpr(@NotNull MantraParser.RangeExprContext ctx) { }
	@Override public void exitAndExpr(@NotNull MantraParser.AndExprContext ctx) { }
	@Override public void exitOrExpr(@NotNull MantraParser.OrExprContext ctx) { }
	@Override public void exitInExpr(@NotNull MantraParser.InExprContext ctx) { }
	@Override public void exitTernaryIfExpr(@NotNull MantraParser.TernaryIfExprContext ctx) { }
	@Override public void exitPipeExpr(@NotNull MantraParser.PipeExprContext ctx) { }

	/*
	primary
	locals [Type type]
		:	'(' expression ')'
	    |	tuple
	    |   THIS
	    |   SUPER
	    |   literal
	    |   list
	    |   map
	    |   set
	    |   ctor
	    |   lambda
	    |   ID // string[] could match string here then [] as next statement; keep this as last alt
	    ;
	 */
	@Override
	public void exitPrimary(@NotNull MantraParser.PrimaryContext ctx) {
		// not broken down by # labels as I figured 10x2 new enter/exit methods wasn't worth it
		if ( ctx.expression()!=null ) {
			ctx.type = ctx.expression().type;
		}
		else if ( ctx.tuple()!=null ) {
			ctx.type = ctx.tuple().type;
		}
		else if ( ctx.THIS()!=null ) {
		}
		else if ( ctx.SUPER()!=null ) {
		}
		else if ( ctx.literal()!=null ) {
			ctx.type = ctx.literal().type;
		}
		else if ( ctx.list()!=null ) {
			ctx.type = ctx.list().type;
		}
		else if ( ctx.map()!=null ) {
			ctx.type = ctx.map().type;
		}
		else if ( ctx.set()!=null ) {
			ctx.type = ctx.set().type;
		}
		else if ( ctx.ctor()!=null ) {
			ctx.type = ctx.ctor().type;
		}
		else if ( ctx.lambda()!=null ) {
			ctx.type = ctx.lambda().type;
		}
		else if ( ctx.ID()!=null ) {
			Symbol s = currentScope.resolve(ctx.ID().getText());
			if ( s==null ) return;
			if ( s instanceof VariableSymbol ) {
				ctx.type = ((VariableSymbol)s).getType();
			}
			else if ( s instanceof ClassSymbol ) {
			}
			else if ( s instanceof InterfaceSymbol) {
			}
			else if ( s instanceof EnumSymbol ) {
			}
		}
		System.out.println("primary '"+ctx.getText()+"' type is "+ctx.type);
	}

	/*
	tuple
	returns [Type type]
		:	'(' expression (',' expression)+ ')' ; // can also be a tuple of pipelines, yielding pipeline graph
	 */
	@Override
	public void exitTuple(@NotNull MantraParser.TupleContext ctx) {
		List<Type> elemTypes = new ArrayList<Type>();
		for (MantraParser.ExpressionContext ectx : ctx.expression()) {
			elemTypes.add(ectx.type);
		}
		//var elemTypes = ctx.expression() => { ectx | ectx.type }
		//var elemTypes = apply(ctx.expression(), ectx -> ectx.type)
		// actually ctx.expression().type would work in Mantra

		ctx.type = new TupleType(ctx, elemTypes);
	}

	/*
	literal
	locals [Type type]
	    :   IntegerLiteral
	    |   FloatingPointLiteral
	    |   CharacterLiteral
	    |   StringLiteral
	    |   BooleanLiteral
	    |   'nil'
	    ;
	 */
	@Override
	public void exitLiteral(@NotNull MantraParser.LiteralContext ctx) {
		ParseTree lit = ctx.getChild(0);
		if ( !(lit instanceof TerminalNode) ) {
			return;
		}
		switch ( ((TerminalNode)lit).getSymbol().getType() ) {
			case MantraParser.IntegerLiteral :
				ctx.type = Type._int;
				break;
			case MantraParser.FloatingPointLiteral :
				ctx.type = Type._float;
				break;
			case MantraParser.CharacterLiteral :
				ctx.type = Type._char;
				break;
			case MantraParser.StringLiteral :
				ctx.type = Type._string;
				break;
			case MantraParser.BooleanLiteral :
				ctx.type = Type._boolean;
				break;
			case MantraParser.NIL :
				ctx.type = Type._nil;
				break;
			default :
				System.err.println("unknown literal: "+ctx.getText());
		}
	}

	@Override
	public void exitList(@NotNull MantraParser.ListContext ctx) {
		if ( ctx.expression()==null ) {
			ctx.type = Type._object;
		}
		else {
			List<Type> elemTypes = new ArrayList<Type>();
			for (MantraParser.ExpressionContext ectx : ctx.expression()) {
				elemTypes.add(ectx.type);
			}
			// check they are same
			// var elemType = count(operator==(ctx.expression())) = len(ctx.expression())
			// var elemType = reduce(ctx.expression(), (x,y) -> x==y)
			Type uniqType = elemTypes.get(0);
			for (Type t : elemTypes) {
				if ( !t.equals(uniqType) ) {
					System.out.println("hetero list");
					ctx.type = new ListType(ctx, Type._object);
					return;
				}
			}
			ctx.type = new ListType(ctx, uniqType);
		}
	}

	/*
	map
	locals [Type type]
		:   '[' mapElem (',' mapElem)* ']'
	    ;

	mapElem
	locals [Type type]
	    :   expression '=' expression
	    ;
	 */
	@Override
	public void exitMap(@NotNull MantraParser.MapContext ctx) {
		Collection<ParseTree> mapElems = XPath.findAll(ctx, "//mapElem", parser);
		Type key = null;
		Type value = null;
		for (ParseTree t : mapElems) {
			MantraParser.MapElemContext m = (MantraParser.MapElemContext)t;
			MantraParser.ExpressionContext k = m.expression(0);
			MantraParser.ExpressionContext v = m.expression(1);
			if ( key==null ) {
				key = k.type;
				value = v.type;
			}
			else if ( !k.type.equals(key) ) {
				// hetero map
				key = Type._object;
			}
			else if ( !v.type.equals(value) ) {
				// hetero map
				value = Type._object;
			}
		}
		ctx.type = new MapType(ctx, key, value);
	}

	/*
	set
	locals [Type type]
		:   'set' typeArguments? '(' (expression (',' expression)*)? ')'
		;
	 */
	@Override
	public void exitSet(@NotNull MantraParser.SetContext ctx) {
		if ( ctx.expression()==null ) {
			ctx.type = Type._object;
			return;
		}
		if ( ctx.typeArguments()!=null ) {

		}
		List<Type> elemTypes = new ArrayList<Type>();
		for (MantraParser.ExpressionContext ectx : ctx.expression()) {
			elemTypes.add(ectx.type);
		}
		// check they are same
		Type uniqType = elemTypes.get(0);
		for (Type t : elemTypes) {
			if ( !t.equals(uniqType) ) {
				ctx.type = new SetType(ctx, Type._object);
				return;
			}
		}
		ctx.type = new SetType(ctx, uniqType);
	}

	/*
	ctor
	locals [Type type]
		:	classOrInterfaceType '(' argExprList? ')'  	// Button(title="foo")
		|	builtInType          '(' argExprList? ')'  	// int(10), string()
		|	classOrInterfaceType ('[' expression? ']')+	// User[10][] list of 10 User lists of unspecified initial size
		|	builtInType          ('[' expression? ']')+ // int[] list of ints with unspecified initial size, int[10] 10 initial ints
		;
	 */
	@Override
	public void exitCtor(@NotNull MantraParser.CtorContext ctx) {
	}

	@Override
	public void exitLambda(@NotNull MantraParser.LambdaContext ctx) {
		ctx.type = new Type(ctx);
	}
}
