package com.study.springboot.util.query;

/**
 * 排序方式
 * 
 * @author dhx
 *
 */
public enum SortMethod {

	ASC("ASC"), DESC("DESC");

	private String value;

	private SortMethod(String value) {
		this.value = value;
	}

	public String toString() {
		return this.value;
	}

	public static SortMethod getSortMethod(String type) {
		if (type != null && !"".endsWith(type)) {
			type = type.toUpperCase();
			switch (type) {
			case "ASC":
				return SortMethod.ASC;
			case "DESC":
				return SortMethod.DESC;
			default:
				return null;
			}
		}
		return null;
	}
}

