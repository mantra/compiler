package mantra.semantics;

import mantra.MantraBaseListener;
import mantra.MantraParser;
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
public class ComputeExprTypes extends MantraBaseListener {
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
}
