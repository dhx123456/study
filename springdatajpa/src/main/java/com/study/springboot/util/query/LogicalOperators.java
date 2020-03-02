package com.study.springboot.util.query;

public enum LogicalOperators {
	AND("AND"), OR("OR");
	private String value;

	private LogicalOperators(String value) {
		this.value = value;
	}

	public String toString() {
		return new String(this.value);
	}

	public static LogicalOperators getLogicalOpt(String value) {
		if (value != null && !"".equals(value)) {
			value = value.toUpperCase();
			switch (value) {
			case "AND":
				return LogicalOperators.AND;
			case "OR":
				return LogicalOperators.OR;
			default:
				return null;
			}
		}
		return null;
	}
}
