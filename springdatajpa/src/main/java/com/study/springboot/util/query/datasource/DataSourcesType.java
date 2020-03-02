package com.study.springboot.util.query.datasource;

public enum DataSourcesType {

	UN_KNOWN(-1, "UN_KNOWN", "未知类型"), //
	OTHER(0, "OTHER", "其他类型"), //
	MYSQL(1, "MYSQL", "Mysql数据库"), //
	ORACLE(2, "ORACLE", "Oracle数据库"), //
	POSTGRE_SQL(3, "POSTGRE_SQL", "Postgresql数据库"), //
	HDFS(4, "HDFS", "Hadoop文件"), //
	FILE(5, "FILE", "普通文件"), //
	ES_INDEX(6, "ES_INDEX", "Elastic Search 索引文件"),//
	;

	private int index;
	private String desc;
	private String name;

	private DataSourcesType(int index, String name, String desc) {
		this.index = index;
		this.name = name;
		this.desc = desc;
	}

	public String getConn(String host, String port, String db) {
		return getConn(index, host, port, db);
	}

	public static String getConn(int index, String host, String port, String db) {
		String jdbc = "";
		if (index == MYSQL.getIndex()) {
			jdbc = String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&characterEncoding=UTF-8&useSSL=false", host,
					port, db);
		} else if (index == POSTGRE_SQL.getIndex()) {
			jdbc = String.format("jdbc:postgresql://%s:%s/%s?useUnicode=true&characterEncoding=UTF-8", host, port, db);
		} else if (index == ORACLE.getIndex()) {
			jdbc = String.format(
					"jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS_LIST =(ADDRESS = (PROTOCOL = TCP)(HOST = %s)(PORT = %s)))(CONNECT_DATA =(SERVICE_NAME = %s)))",
					host, port, db);
		}
		return jdbc;
	}

	public static String getDriveName(int index) {
		String driveName = "";
		if (index == MYSQL.getIndex()) {
			driveName = "com.mysql.jdbc.Driver";
		} else if (index == POSTGRE_SQL.getIndex()) {
			driveName = "org.postgresql.Driver";
		} else if (index == ORACLE.getIndex()) {
			driveName = "oracle.jdbc.driver.OracleDriver";
		}
		return driveName;
	}

	public static String getDriveUrl(int index, String ip, String dbName, String port) {
		String driveUrl = "";
		if (index == MYSQL.getIndex()) {
			driveUrl = "jdbc:mysql://" + ip + ":" + port + "/" + dbName + "?useUnicode=true&characterEncoding=UTF8";
		} else if (index == POSTGRE_SQL.getIndex()) {
			driveUrl = "jdbc:postgresql://" + ip + ":" + port + "/" + dbName
					+ "?useUnicode=true&amp;characterEncoding=utf8";
		} else if (index == ORACLE.getIndex()) {
			driveUrl = "jdbc:oracle:thin:@(DESCRIPTION =(ADDRESS_LIST =(ADDRESS = (PROTOCOL = TCP)(HOST = " + ip
					+ ")(PORT = " + port + ")))(CONNECT_DATA =(SERVICE_NAME = " + dbName + ")))";
		}
		return driveUrl;
	}

	public static DataSourcesType valueBy(int index) {
		DataSourcesType type = UN_KNOWN;
		if (index == OTHER.getIndex()) {
			type = OTHER;
		} else if (index == MYSQL.getIndex()) {
			type = MYSQL;
		} else if (index == ORACLE.getIndex()) {
			type = ORACLE;
		} else if (index == POSTGRE_SQL.getIndex()) {
			type = POSTGRE_SQL;
		} else if (index == HDFS.getIndex()) {
			type = HDFS;
		} else if (index == FILE.getIndex()) {
			type = FILE;
		} else if (index == ES_INDEX.getIndex()) {
			type = ES_INDEX;
		}
		return type;
	}

	public int getIndex() {
		return index;
	}

	public String getDesc() {
		return desc;
	}

	public String getName() {
		return name;
	}
}
