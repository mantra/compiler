package mantra.semantics;

import mantra.MantraParser;
import mantra.symbols.ClassSymbol;
import mantra.symbols.EnumSymbol;
import mantra.symbols.InterfaceSymbol;
import mantra.symbols.ListType;
import mantra.symbols.Symbol;
import mantra.symbols.TupleType;
import mantra.symbols.Type;
import mantra.symbols.VariableSymbol;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;
import java.util.List;

/*
expression
locals [Type exprType]
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
public class ComputeExprTypes extends SetScopeListener {
	@Override public void exitPrimaryExpr(@NotNull MantraParser.PrimaryExprContext ctx) {
		ctx.exprType = ctx.primary().exprType;
	}
	@Override public void exitFieldAccessExpr(@NotNull MantraParser.FieldAccessExprContext ctx) { }
	@Override public void exitClassPtrExpr(@NotNull MantraParser.ClassPtrExprContext ctx) { }
	@Override public void exitArrayIndexExpr(@NotNull MantraParser.ArrayIndexExprContext ctx) { }
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
	locals [Type exprType]
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
			ctx.exprType = ctx.expression().exprType;
		}
		else if ( ctx.tuple()!=null ) {
			ctx.exprType = ctx.tuple().exprType;
		}
		else if ( ctx.THIS()!=null ) {
		}
		else if ( ctx.SUPER()!=null ) {
		}
		else if ( ctx.literal()!=null ) {
			ctx.exprType = ctx.literal().exprType;
		}
		else if ( ctx.list()!=null ) {
			ctx.exprType = ctx.list().exprType;
		}
		else if ( ctx.map()!=null ) {
			ctx.exprType = ctx.map().exprType;
		}
		else if ( ctx.set()!=null ) {
			ctx.exprType = ctx.set().exprType;
		}
		else if ( ctx.ctor()!=null ) {
			ctx.exprType = ctx.ctor().exprType;
		}
		else if ( ctx.lambda()!=null ) {
			ctx.exprType = ctx.lambda().exprType;
		}
		else if ( ctx.ID()!=null ) {
			Symbol s = currentScope.resolve(ctx.ID().getText());
			if ( s==null ) return;
			if ( s instanceof VariableSymbol ) {
				ctx.exprType = ((VariableSymbol)s).getType();
			}
			else if ( s instanceof ClassSymbol ) {
			}
			else if ( s instanceof InterfaceSymbol) {
			}
			else if ( s instanceof EnumSymbol ) {
			}
		}
		System.out.println("primary '"+ctx.getText()+"' type is "+ctx.exprType);
	}

	/*
	tuple
	locals [Type exprType]
		:	'(' expression (',' expression)+ ')' ; // can also be a tuple of pipelines, yielding pipeline graph
	 */
	@Override
	public void exitTuple(@NotNull MantraParser.TupleContext ctx) {
		List<Type> elemTypes = new ArrayList<Type>();
		for (MantraParser.ExpressionContext ectx : ctx.expression()) {
			elemTypes.add(ectx.exprType);
		}
		//var elemTypes = ctx.expression() => { ectx | ectx.exprType }
		//var elemTypes = apply(ctx.expression(), ectx -> ectx.exprType)

		ctx.exprType = new TupleType(ctx, elemTypes);
	}

	/*
	literal
	locals [Type exprType]
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
				ctx.exprType = Type._int;
				break;
			case MantraParser.FloatingPointLiteral :
				ctx.exprType = Type._float;
				break;
			case MantraParser.CharacterLiteral :
				ctx.exprType = Type._char;
				break;
			case MantraParser.StringLiteral :
				ctx.exprType = Type._string;
				break;
			case MantraParser.BooleanLiteral :
				ctx.exprType = Type._boolean;
				break;
			case MantraParser.NIL :
				ctx.exprType = Type._nil;
				break;
			default :
				System.err.println("unknown literal: "+ctx.getText());
		}
	}

	@Override
	public void exitList(@NotNull MantraParser.ListContext ctx) {
		if ( ctx.expression()==null ) {
			ctx.exprType = Type._object;
		}
		else {
			List<Type> elemTypes = new ArrayList<Type>();
			for (MantraParser.ExpressionContext ectx : ctx.expression()) {
				elemTypes.add(ectx.exprType);
			}
			// check they are same
			// var elemType = count(operator==(ctx.expression())) = len(ctx.expression())
			// var elemType = reduce(ctx.expression(), (x,y) -> x==y)
			Type uniqType = elemTypes.get(0);
			for (Type t : elemTypes) {
				if ( !t.equals(uniqType) ) {
					System.out.println("hetero list");
					ctx.exprType = new ListType(ctx, Type._object);
					return;
				}
			}
			ctx.exprType = new ListType(ctx, uniqType);
		}
	}

	@Override
	public void exitMap(@NotNull MantraParser.MapContext ctx) {
		ctx.exprType = new Type(ctx);
	}

	@Override
	public void exitSet(@NotNull MantraParser.SetContext ctx) {
		ctx.exprType = new Type(ctx);
	}

	/*
	ctor
	locals [Type exprType]
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
		ctx.exprType = new Type(ctx);
	}
}
