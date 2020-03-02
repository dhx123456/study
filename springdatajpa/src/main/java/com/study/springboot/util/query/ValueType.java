package com.study.springboot.util.query;

/**
 * 值类型
 * @author dhx
 *
 */
public enum ValueType {
	NUMBER, DATE, STRING, TIME, DATETIME;
	
	public static ValueType getValueType(String type){
		switch (type) {
		case "java.lang.String":
			return ValueType.STRING;
		case "java.lang.Date":
			return ValueType.DATE;
		case "java.lang.Integer":
			return ValueType.NUMBER;
		default:
			return ValueType.STRING;
		}
	}
	
}
