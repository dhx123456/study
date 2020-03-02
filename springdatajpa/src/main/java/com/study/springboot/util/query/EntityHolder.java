package com.study.springboot.util.query;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.common.util.StringHelper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 查询条件转换支撑方法
 * 
 * 要求的JSON条件格式：{"cond":[{"FEILDNAME":"name","OPERATION":"LIKE","VALUE":"name",
 * "LOGICOPT":""}],"order":[{"FEILDNAME":"name","SORTMETHOD":"DESC"}]}
 * 
 * @author dhx
 *
 */
public class EntityHolder {

	/**
	 * 基类数据库字段名称与实体成员变量名称对照，成员变量名为key
	 */
	private static Map<String, Object> SUPER_CLASS_FIELDS = new HashMap<String, Object>();
	/**
	 * 基类的数据库字段名称值，基类全名称为key，数据库库字段逗号分割字符串为value
	 */
	private static Map<String, Object> SUPER_CLASS_FIELD_STR = new HashMap<String, Object>();
	/**
	 * 实体的数据库字段名称值，实体全名称为key，数据库字段逗号分割字符串为value
	 */
	private static Map<String, Object> ENTITY_FIELD_STR = new HashMap<String, Object>();
	
	/**
	 * 数据库表字段转实体熟悉字符串
	 */
	private static Map<String, Object> COLUMN_ENTITY_STR = new HashMap<String, Object>();

	public static String buildCountSQL(Class<?> clazz, String json) {
		String ret = "";
		String sql = buildSQL(clazz, json);
		if (sql.length() > 0) {
			if (sql.indexOf("FROM") > -1) {
				String removeFrom = sql.substring(sql.lastIndexOf("FROM"));
				if (removeFrom.indexOf("ORDER") > -1) {
					String removeOrder = removeFrom.substring(0, removeFrom.indexOf("ORDER"));
					ret = "SELECT COUNT(1) AS COUNT " + removeOrder;
				} else {
					ret = "SELECT COUNT(1) AS COUNT " + removeFrom;
				}
			}
		}
		return ret;
	}

	// public static void main(String[] args) {
	// String sql=" SELECT COUNT(1) FROM T_DS_DATASOURCES AS TEMP_TABLE_ALIAS_0
	// WHERE DS_NAME LIKE '%齐文辉%' ORDER BY DS_NAME DESC";
	// System.out.println(sql.indexOf("FROM"));
	// }

	/**
	 * 构造SQL语句
	 * 
	 * @param clazz
	 * @param json
	 * @return
	 */
	public static String buildSQL(Class<?> clazz, String json) {
		String sql = "";
		try {
			if (null != clazz) {
				getColumnEntityClassField(clazz);
				getBaseClassField(clazz);
				getEntityFieldStr(clazz);
			} else {
				throw new Exception("实体对象不能为空");
			}

			String tableName = EntityHolder.getTableNameByEntity(clazz);
			String superKey = clazz.getSuperclass().getName();
			String classKey = clazz.getName();

			SQLQueryUtil util = new SQLQueryUtil();
			util.addTable(tableName);
			/*System.out.println("=======" + SUPER_CLASS_FIELD_STR.get(superKey) + "," + ENTITY_FIELD_STR.get(classKey));
			util.addColumn(String.valueOf(SUPER_CLASS_FIELD_STR.get(superKey)) + ","
					+ String.valueOf(ENTITY_FIELD_STR.get(classKey)));*/
			if(!(String.valueOf(SUPER_CLASS_FIELD_STR.get(superKey)).equalsIgnoreCase("null"))){
				/*util.addColumn(String.valueOf(SUPER_CLASS_FIELD_STR.get(superKey)) + ","
						+ String.valueOf(ENTITY_FIELD_STR.get(classKey)));*/
				util.addColumn(String.valueOf(SUPER_CLASS_FIELD_STR.get(superKey)) + ","
						+ String.valueOf(COLUMN_ENTITY_STR.get(classKey)));
			}else{
				//util.addColumn(String.valueOf(ENTITY_FIELD_STR.get(classKey)));
				util.addColumn(String.valueOf(COLUMN_ENTITY_STR.get(classKey)));
			}
			if (null == json || "".equals(json)) {
				return sql = util.toSQL();
			}

			// 解析JSON
			JSONObject obj = JSONObject.parseObject(json.toString());
			String condStr = obj.getString("cond");
			String orderStr = obj.getString("order");

			Map<String, EntityOptProperty> mapCond = convertCond(condStr, clazz);
			Map<String, EntityOrderProperty> mapOrder = convertOrder(orderStr, clazz);

			buildWhere(util, mapCond, true);
			buildOrder(util, mapOrder, true);

			sql = util.toSQL();
			System.out.println("=======" + sql);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sql;
	}

	/**
	 * 构造HQL语句
	 * 
	 * @param clazz
	 * @param json
	 * @return
	 */
	public static String buildHQL(Class<?> clazz, String json) {
		String sql = "";
		try {
			String tableName = clazz.getSimpleName();
			SQLQueryUtil util = new SQLQueryUtil();
			util.addTable(tableName);

			if (null == json || "".equals(json)) {
				sql = util.toSQL();
				return sql.substring(sql.indexOf("FROM"));
			}

			JSONObject obj = JSONObject.parseObject(json.toString());
			String condStr = obj.getString("cond");
			String orderStr = obj.getString("order");

			Map<String, EntityOptProperty> mapCond = convertCond(condStr, clazz);
			// 构造排序字段
			Map<String, EntityOrderProperty> mapOrder = convertOrder(orderStr, clazz);

			buildWhere(util, mapCond, false);
			buildOrder(util, mapOrder, false);

			sql = util.toSQL();
			sql = sql.substring(sql.indexOf("FROM"));
			System.out.println("=======" + sql);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sql;
	}

	/**
	 * 将JSON转换为条件关系实体集合
	 * 
	 * @param requestJson
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public static Map<String, EntityOptProperty> convertCond(String requestJson, Class<?> clazz) throws Exception {
		Map<String, EntityOptProperty> map = new HashMap<String, EntityOptProperty>();
		String superKey = clazz.getSuperclass().getName();

		JSONArray array = JSONArray.parseArray(requestJson);
		for (Object object : array) {
			if (null != object) {
				JSONObject obj = JSONObject.parseObject(object.toString());
				String entityProp = obj.getString("FEILDNAME");
				String columnName = "";
				Class<?> columnType = null;
				if (StringHelper.isNotEmpty(entityProp)) {
					try {
						Object superFeild = SUPER_CLASS_FIELDS.get(entityProp);

						if ("java.lang.Object".equals(superKey)) {
							Field field = (Field) clazz.getDeclaredField(entityProp);
							Column col = (Column) field.getAnnotation(Column.class);
							columnName = col.name();
							columnType = field.getType();
						} else if (null != superFeild && !"".equals(superFeild)) {
							Field field = (Field) superFeild;
							Column col = (Column) field.getAnnotation(Column.class);
							columnName = col.name();
							columnType = field.getType();
						} else {
							Field field = (Field) clazz.getDeclaredField(entityProp);
							Column col = (Column) field.getAnnotation(Column.class);
							columnName = col.name();
							columnType = field.getType();
						}
					} catch (Exception e) {
						throw new Exception("字段" + entityProp + "未找到");
					}
					RelationalOperator operation = RelationalOperator.getRelOpt(obj.getString("OPERATION"));
					ValueType valueType = ValueType.getValueType(columnType.getName());
					String value = obj.getString("VALUE");
					LogicalOperators logicalOperators = LogicalOperators.getLogicalOpt(obj.getString("LOGICOPT"));

					EntityOptProperty optProp = new EntityOptProperty(entityProp, columnName, operation, valueType,
							logicalOperators, value);
					map.put(entityProp, optProp);
				}
			}
		}

		return map;
	}

	/**
	 * 将JSON转换为排序关系实体集合
	 * 
	 * @param requestJson
	 * @param clazz
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static Map<String, EntityOrderProperty> convertOrder(String requestJson, Class<?> clazz)
			throws NoSuchFieldException, SecurityException {
		Map<String, EntityOrderProperty> map = new HashMap<String, EntityOrderProperty>();

		JSONArray array = JSONArray.parseArray(requestJson);

		for (Object object : array) {
			if (null != object) {
				JSONObject obj = JSONObject.parseObject(object.toString());
				String entityProp = obj.getString("FEILDNAME");
				String sortMethod = obj.getString("SORTMETHOD");
				String columnName = "";
				Class<?> columnType = null;
				String superKey = clazz.getSuperclass().getName();
				if (StringHelper.isNotEmpty(entityProp)) {
					try {
						Object superFeild = SUPER_CLASS_FIELDS.get(entityProp);

						if ("java.lang.Object".equals(superKey)) {
							Field field = (Field) clazz.getDeclaredField(entityProp);
							Column col = (Column) field.getAnnotation(Column.class);
							columnName = col.name();
							columnType = field.getType();
						} else if (null != superFeild && !"".equals(superFeild)) {
							Field field = (Field) superFeild;
							Column col = (Column) field.getAnnotation(Column.class);
							columnName = col.name();
							columnType = field.getType();
						} else {
							Field field = (Field) clazz.getDeclaredField(entityProp);
							Column col = (Column) field.getAnnotation(Column.class);
							columnName = col.name();
							columnType = field.getType();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					EntityOrderProperty orderProp = new EntityOrderProperty(entityProp, columnName,
							SortMethod.getSortMethod(sortMethod));
					map.put(entityProp, orderProp);
				}
			}
		}
		return map;
	}

	public static String getEntityFieldStr(Class<?> clazz) throws NoSuchFieldException, SecurityException {
		String ret = "";
		String key = clazz.getName();
		if (null == ENTITY_FIELD_STR.get(key) || "".equals(ENTITY_FIELD_STR.get(key))) {
			if (null != clazz) {
				Field[] fields = clazz.getDeclaredFields();
				if (fields.length > 0) {
					for (Field field : fields) {
						Annotation[] annotations = field.getAnnotations();
						boolean b = true;
						//忽略transient注解的字段
						for (Annotation annotation:field.getAnnotations()
							 ) {
							if(annotation.annotationType().getName().contains("Transient")){
								b =false;
							}
						}

						if (b&&!"serialVersionUID".equals(field.getName()) && !"api".equals(field.getName())) {
							Column col = field.getAnnotation(Column.class);
							ret += col.name() + ",";
						}
					}
					ENTITY_FIELD_STR.put(key, ret.length() > 0 ? ret.substring(0, ret.length() - 1) : "");
					System.out.println("=======" + ENTITY_FIELD_STR.get(key));
				} else {
					ret = (String) ENTITY_FIELD_STR.get(key);
				}
			}
		} else {
			ret = (String) ENTITY_FIELD_STR.get(key);
		}
		return ret;
	}

	public static String getBaseClassField(Class<?> clazz) throws NoSuchFieldException, SecurityException {
		String ret = "";
		String key = clazz.getSuperclass().getName();
		if (null == SUPER_CLASS_FIELD_STR.get(key) || "".equals(SUPER_CLASS_FIELD_STR.get(key))) {
			if (null != clazz) {
				Field[] fields = clazz.getSuperclass().getDeclaredFields();
				if (fields.length > 0) {
					for (Field field : fields) {
						if (!"serialVersionUID".equals(field.getName())) {
							Column col = field.getAnnotation(Column.class);
							SUPER_CLASS_FIELDS.put(field.getName(), field);
							ret += col.name() + ",";
						}
					}
					SUPER_CLASS_FIELD_STR.put(key, ret.length() > 0 ? ret.substring(0, ret.length() - 1) : "");
					System.out.println("=======" + SUPER_CLASS_FIELD_STR.get(key));
				} else {
					ret = (String) SUPER_CLASS_FIELD_STR.get(key);
				}
			}
		} else {
			ret = (String) SUPER_CLASS_FIELD_STR.get(key);
		}
		return ret;
	}

	public static String getTableNameByEntity(Class<?> entity) {
		try {
			Table table = entity.getAnnotation(Table.class);
			String tableName = "";
			if (null != table && !"".equals(table.name())) {
				tableName = table.name();
				return tableName;
			} else {
				throw new Exception("实体的表名不能为空！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 构造WHERE的SQL和HQL语句
	 * 
	 * @param util
	 * @param mapCond
	 * @param isSQL
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private static void buildWhere(SQLQueryUtil util, Map<String, EntityOptProperty> mapCond, boolean isSQL)
			throws Exception {
		if (mapCond.size() > 0) {
			Iterator it = mapCond.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				// String key = String.valueOf(entry.getKey());
				EntityOptProperty value = (EntityOptProperty) entry.getValue();
				if (null != value.getLogicalOperators()) {
					if (value.getLogicalOperators().equals(LogicalOperators.OR)) {
						buildOrWhere(util, value, isSQL);
					} else {
						buildAndWhere(util, value, isSQL);
					}
				} else {
					buildAndWhere(util, value, isSQL);
				}
			}
		}
	}

	/**
	 * 构造逻辑关系为OR的where条件语句
	 * 
	 * @param util
	 * @param value
	 * @param isSQL
	 * @throws Exception
	 */
	private static void buildOrWhere(SQLQueryUtil util, EntityOptProperty value, boolean isSQL) throws Exception {
		if (RelationalOperator.LIKE.equals(value.getRelOpt())) {
			if (isSQL) {
				util.addOrLikeCondition(value.getColumnName(), value.getValue());
			} else {
				util.addOrLikeCondition(value.getEntityProp(), value.getValue());
			}
		}else if(RelationalOperator.LLIKE.equals(value.getRelOpt())){
			if (isSQL)
				util.addAndLLikeCondition(value.getColumnName(), value.getValue());
			else
				util.addAndLLikeCondition(value.getEntityProp(), value.getValue());
		} else if (RelationalOperator.IN.equals(value.getRelOpt())) {
			if (isSQL)
				util.addOrInCondition(value.getColumnName(), value.getValue());
			else
				util.addOrInCondition(value.getEntityProp(), value.getValue());

		} else if(RelationalOperator.BETWEEN.equals(value.getRelOpt())){
			if (isSQL)
				util.addBetweenCondition(value.getColumnName(), value.getValue());
			else
				util.addBetweenCondition(value.getEntityProp(), value.getValue());
		} else if (RelationalOperator.ISNULL.equals(value.getRelOpt())){
			if (isSQL)
				util.addIsNullCondition(value.getColumnName());
			else
				util.addIsNullCondition(value.getEntityProp());
		} else if (RelationalOperator.ISNOTNULL.equals(value.getRelOpt())){
			if (isSQL)
				util.addIsNotNullCondition(value.getColumnName());
			else
				util.addIsNotNullCondition(value.getEntityProp());
		} else {
			if (isSQL)
				util.addOrCondition(value.getColumnName(), value.getRelOpt(), value.getValue(), value.getValueType());
			else
				util.addOrCondition(value.getEntityProp(), value.getRelOpt(), value.getValue(), value.getValueType());
		}
	}

	/**
	 * 构造逻辑关系为AND的WHERE条件语句
	 * 
	 * @param util
	 * @param value
	 * @param isSQL
	 * @throws Exception
	 */
	private static void buildAndWhere(SQLQueryUtil util, EntityOptProperty value, boolean isSQL) throws Exception {
		if (RelationalOperator.LIKE.equals(value.getRelOpt())) {
			if (isSQL)
				util.addAndLikeCondition(value.getColumnName(), value.getValue());
			else
				util.addAndLikeCondition(value.getEntityProp(), value.getValue());
		}else if(RelationalOperator.LLIKE.equals(value.getRelOpt())){
			if (isSQL)
				util.addAndLLikeCondition(value.getColumnName(), value.getValue());
			else
				util.addAndLLikeCondition(value.getEntityProp(), value.getValue());
		} else if (RelationalOperator.IN.equals(value.getRelOpt())) {
			if (isSQL)
				util.addAndInCondition(value.getColumnName(), value.getValue());
			else
				util.addAndInCondition(value.getEntityProp(), value.getValue());
		} else if(RelationalOperator.BETWEEN.equals(value.getRelOpt())){
			if (isSQL)
				util.addBetweenCondition(value.getColumnName(), value.getValue());
			else
				util.addBetweenCondition(value.getEntityProp(), value.getValue());
		} else if (RelationalOperator.ISNULL.equals(value.getRelOpt())){
			if (isSQL)
				util.addIsNullCondition(value.getColumnName());
			else
				util.addIsNullCondition(value.getEntityProp());
		} else if(RelationalOperator.ISNOTNULL.equals(value.getRelOpt())){
			if (isSQL)
				util.addIsNotNullCondition(value.getColumnName());
			else
				util.addIsNotNullCondition(value.getEntityProp());
		} else {
			if (isSQL)
				util.addAndCondition(value.getColumnName(), value.getRelOpt(), value.getValue(), value.getValueType());
			else
				util.addAndCondition(value.getEntityProp(), value.getRelOpt(), value.getValue(), value.getValueType());
		}
	}

	/**
	 * 构造Order By语句(SQL and HQL)
	 * 
	 * @param util
	 * @param mapOrder
	 * @param isSQL
	 */
	@SuppressWarnings("rawtypes")
	private static void buildOrder(SQLQueryUtil util, Map<String, EntityOrderProperty> mapOrder, boolean isSQL) {
		if (mapOrder.size() > 0) {
			Iterator it = mapOrder.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				// String key = String.valueOf(entry.getKey());
				EntityOrderProperty value = (EntityOrderProperty) entry.getValue();
				if (value.getSortMethod().equals(SortMethod.DESC)) {
					if (isSQL)
						util.addDescOrderBy(value.getColumnName());
					else
						util.addDescOrderBy(value.getEntityProp());
				} else {
					if (isSQL)
						util.addAscOrderBy(value.getColumnName());
					else
						util.addAscOrderBy(value.getEntityProp());
				}
			}
		}
	}

	/**
	 * 将JSON转换为条件关系实体集合
	 * 
	 * @param
	 * @param clazz
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static Map<String, Object> getSearchByCond(String condStr, String orderStr, Class<?> clazz)
			throws NoSuchFieldException, SecurityException {

		Map<String, Object> condMap = new HashMap<String, Object>();
		Map<String, Object> orderMap = new HashMap<String, Object>();

		JSONArray array = JSONArray.parseArray(condStr);
		for (Object object : array) {
			if (null != object) {
				JSONObject obj = JSONObject.parseObject(object.toString());
				String entityProp = obj.getString("FEILDNAME");
				RelationalOperator operation = RelationalOperator.getRelOpt(obj.getString("OPERATION"));

				String value = obj.getString("VALUE");
				if (operation != null && StringUtils.isNotBlank(value)) {
					entityProp = operation.name() + "_" + entityProp;
					condMap.put(entityProp, value);
				}
			}
		}

		JSONArray orderarray = JSONArray.parseArray(orderStr);
		if (orderarray != null && orderarray.size() > 0) {
			for (Object object : orderarray) {
				if (null != object) {
					JSONObject obj = JSONObject.parseObject(object.toString());
					String entityProp = obj.getString("FEILDNAME");
					String sortMethod = obj.getString("SORTMETHOD");
					orderMap.put("orderField", entityProp);
					orderMap.put("orderSort", sortMethod);
				}
			}
		} else {
			orderMap.put("orderField", "");
			orderMap.put("orderSort", "");
		}

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("orderMap", orderMap);
		resultMap.put("condMap", condMap);
		return resultMap;
	}
	
	/**
	 * 根据实体获取 字段及属性对比集合
	 * @param clazz
	 * @return
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	public static String getColumnEntityClassField(Class<?> clazz) throws NoSuchFieldException, SecurityException {
		String ret = "";
		String key = clazz.getName();
		if (null != clazz) {
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				Column col = field.getAnnotation(Column.class);
				String columnName = field.getName().toLowerCase();
				ret += col.name()+ " as \"" + columnName+"\",";
			}
			COLUMN_ENTITY_STR.put(key, ret.length() > 0 ? ret.substring(0, ret.length() - 1) : "");
		}
		return ret;
	}

}
