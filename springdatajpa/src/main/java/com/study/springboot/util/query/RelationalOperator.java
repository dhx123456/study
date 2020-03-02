package com.study.springboot.util.query;
/**
 * 关系运算符
 * 
 * EQ("="), NE("<>"), GT(">"), GE(">="), LT("<"), LE("<="), LIKE("LIKE")
 * 
 * @author dhx
 *
 */
public enum RelationalOperator {
	EQ("="), NE("<>"), GT(">"), GE(">="), LT("<"), LE("<="), LIKE("LIKE"),LLIKE("LLIKE"), IN("IN"),BETWEEN("BETWEEN"),ISNULL("ISNULL"),ISNOTNULL("ISNOTNULL");

	private String value;

	private RelationalOperator(String value) {
		this.value = value;
	}

	public String toString() {
		return this.value;
	}

	public static RelationalOperator getRelOpt(String optValue) {
		if (optValue != null && !"".equals(optValue)) {
			optValue=optValue.toUpperCase();
			switch (optValue) {
			case "=":
				return RelationalOperator.EQ;
			case "<>":
				return RelationalOperator.NE;
			case ">":
				return RelationalOperator.GT;
			case ">=":
				return RelationalOperator.GE;
			case "<":
				return RelationalOperator.LT;
			case "<=":
				return RelationalOperator.LE;
			case "LIKE":
				return RelationalOperator.LIKE;
			case "LLIKE":
				return RelationalOperator.LLIKE;
			case "IN":
				return RelationalOperator.IN;
			case "BETWEEN":
				return RelationalOperator.BETWEEN;
			case "ISNULL":
				return RelationalOperator.ISNULL;
			case "ISNOTNULL":
				return RelationalOperator.ISNOTNULL;
			default:
				return null;
			}
		}
		return null;
	}
}

