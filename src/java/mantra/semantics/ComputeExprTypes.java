package mantra.semantics;

import mantra.MantraParser;
import mantra.symbols.ClassSymbol;
import mantra.symbols.EnumSymbol;
import mantra.symbols.InterfaceSymbol;
import mantra.symbols.Symbol;
import mantra.symbols.VariableSymbol;
import org.antlr.v4.runtime.misc.NotNull;

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
	public void enterPrimary(@NotNull MantraParser.PrimaryContext ctx) {
		// not broken down by # labels
		if ( ctx.expression()!=null ) {
			ctx.exprType = ctx.expression().exprType;
		}
		else if ( ctx.tuple()!=null ) {
		}
		else if ( ctx.THIS()!=null ) {
		}
		else if ( ctx.SUPER()!=null ) {
		}
		else if ( ctx.literal()!=null ) {
		}
		else if ( ctx.list()!=null ) {
		}
		else if ( ctx.map()!=null ) {
		}
		else if ( ctx.set()!=null ) {
		}
		else if ( ctx.ctor()!=null ) {
		}
		else if ( ctx.lambda()!=null ) {
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
	}
}
