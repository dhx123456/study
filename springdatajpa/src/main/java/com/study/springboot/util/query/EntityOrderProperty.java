package com.study.springboot.util.query;

public class EntityOrderProperty {
	private String entityProp;
	private String columnName;
	private SortMethod sortMethod;

	public EntityOrderProperty(String entityProp, String columnName, SortMethod sortMethod) {
		super();
		this.entityProp = entityProp;
		this.columnName = columnName;
		this.sortMethod = sortMethod;
	}

	public String getEntityProp() {
		return entityProp;
	}

	public void setEntityProp(String entityProp) {
		this.entityProp = entityProp;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public SortMethod getSortMethod() {
		return sortMethod;
	}

	public void setSortMethod(SortMethod sortMethod) {
		this.sortMethod = sortMethod;
	}
}
