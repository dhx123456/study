package com.study.springboot.util.query.datasource;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;

public class DynamicDataSource {

  /**
   * 创建指定数据源
   *
   * @param url
   * @param username
   * @param password
   * @return
   */
  public static final HikariDataSource createDataSource(
      String url, String username, String password, int poolSize) {

    HikariDataSource hikariDataSource =
        DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .url(url)
            .username(username)
            .password(password)
            .build();
    hikariDataSource.setMaximumPoolSize(poolSize);
    return hikariDataSource;
  }

  /**
   * 创建指定名字的数据源头便于监控如果
   *
   * @param poolName
   * @param url
   * @param username
   * @param password
   * @param poolSize
   * @return
   */
  public static final HikariDataSource createDataSource(
      String poolName, String url, String username, String password, int poolSize) {

    HikariDataSource hikariDataSource =
        DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .url(url)
            .username(username)
            .password(password)
            .build();
    hikariDataSource.setPoolName(poolName);
    hikariDataSource.setMaximumPoolSize(poolSize);
    return hikariDataSource;
  }
}
