package mantra.symbols;

import mantra.MantraParser;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/** Represents all possible type trees like simple int, int[10],
 *  list<string>, and func<(int):float>[10].
 */
public class Type {
	public static final Type INVALID = new Type(null);
	public static final int BOOLEAN = MantraParser.BOOLEAN;
	public static final int BYTE = MantraParser.BYTE;
	public static final int CHAR = MantraParser.CHAR;
	public static final int INT = MantraParser.INT;
	public static final int SHORT = MantraParser.SHORT;
	public static final int LONG = MantraParser.LONG;
	public static final int FLOAT = MantraParser.FLOAT;
	public static final int DOUBLE = MantraParser.DOUBLE;
	public static final int STRING = MantraParser.STRING;

	public static final Type _boolean = new Type(new TerminalNodeImpl(new CommonToken(MantraParser.BOOLEAN, "boolean")));
	public static final Type _byte = new Type(new TerminalNodeImpl(new CommonToken(MantraParser.BYTE, "byte")));
	public static final Type _char = new Type(new TerminalNodeImpl(new CommonToken(MantraParser.CHAR, "char")));
	public static final Type _int = new Type(new TerminalNodeImpl(new CommonToken(MantraParser.INT, "int")));
	public static final Type _short = new Type(new TerminalNodeImpl(new CommonToken(MantraParser.SHORT, "short")));
	public static final Type _long = new Type(new TerminalNodeImpl(new CommonToken(MantraParser.LONG, "long")));
	public static final Type _float = new Type(new TerminalNodeImpl(new CommonToken(MantraParser.FLOAT, "float")));
	public static final Type _double = new Type(new TerminalNodeImpl(new CommonToken(MantraParser.DOUBLE, "double")));

	public static final Type _string = new Type(new TerminalNodeImpl(new CommonToken(MantraParser.STRING, "string")));

	public final ParseTree tree;

	/** Result types for most binary operations (arithmetic and relational) on built in types */
	public static final Map<Long, Integer> resultTypes = new HashMap<Long, Integer>();

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
	static {
		// make a table to handle lots of kinds of expressions, but not all
		Set<Integer> arithmeticOps = new HashSet<Integer>() {{
			add(MantraParser.ADD);
			add(MantraParser.SUB);
			add(MantraParser.MUL);
			add(MantraParser.DIV);
			add(MantraParser.MOD);
			add(MantraParser.BITAND);
			add(MantraParser.BITOR);
			add(MantraParser.CARET);
		}};
		Set<Integer> relationalOps = new HashSet<Integer>() {{
			add(MantraParser.LE);
			add(MantraParser.GE);
			add(MantraParser.LT);
			add(MantraParser.GT);
			add(MantraParser.EQUAL);
			add(MantraParser.NOTEQUAL);
			add(MantraParser.AND);
			add(MantraParser.OR);
			add(MantraParser.IN);
		}};
		Set<Integer> arithmeticTypes = new HashSet<Integer>() {{
			add(CHAR);
			add(BYTE);
			add(SHORT);
			add(INT);
			add(LONG);
			add(FLOAT);
			add(DOUBLE);
		}};
		for (int aop : arithmeticOps) {
			for (int type : arithmeticTypes) {
				resultTypes.put(opkey(type, aop, type), type);
			}
		}
		for (int rop : relationalOps) {
			for (int type : arithmeticTypes) {
				resultTypes.put(opkey(type, rop, type), BOOLEAN);
			}
			resultTypes.put(opkey(STRING, rop, STRING), BOOLEAN);
		}
	}

	public Type(ParseTree tree) { this.tree = tree; }

	@Override
	public String toString() {
		return tree.getText();
	}

	public static long opkey(int left, int op, int right) {
		return ((long)left) << 32 | ((long)op) << 16 | right;
	}

	public static Type resultType(Type left, int opTokenType, Type right) {
		return INVALID;
	}

	public static Type promoteFromTo(Type from, int opTokenType, Type to) {
		return INVALID;
	}

	public static Type canAssignTo(Type from, Type to) {
		return INVALID;
	}
}
