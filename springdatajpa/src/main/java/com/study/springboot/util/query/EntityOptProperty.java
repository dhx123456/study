package com.study.springboot.util.query;

/**
 * 查询条件转换对象
 * 
 * @author dhx
 *
 */
public class EntityOptProperty {
	private String entityProp;
	private String columnName;
	private RelationalOperator relOpt;
	private ValueType valueType;
	private String value;
	private LogicalOperators logicalOperators;

	public EntityOptProperty(String entityProp, String columnName, RelationalOperator relOpt, ValueType valueType,
			LogicalOperators logicalOperators,String value) {
		this.entityProp = entityProp;
		this.columnName = columnName;
		this.relOpt = relOpt;
		this.valueType = valueType;
		this.logicalOperators=logicalOperators;
		this.value = value;
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

	public RelationalOperator getRelOpt() {
		return relOpt;
	}

	public void setRelOpt(RelationalOperator relOpt) {
		this.relOpt = relOpt;
	}

	public ValueType getValueType() {
		return valueType;
	}

	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public LogicalOperators getLogicalOperators() {
		return logicalOperators;
	}

	public void setLogicalOperators(LogicalOperators logicalOperators) {
		this.logicalOperators = logicalOperators;
	}

}
