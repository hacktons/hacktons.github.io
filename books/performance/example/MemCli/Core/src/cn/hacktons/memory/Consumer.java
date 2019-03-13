package cn.hacktons.memory;

import java.sql.*;

/**
 * Created by aven on 16/04/2018.
 */
public class Consumer {

  final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS mem_sample (\n"
          + " id integer PRIMARY KEY AUTOINCREMENT,\n"
          + " java_heap integer,\n"
          + " native_heap integer,\n"
          + " total integer,\n"
          + " time integer\n" +
          ");";
  final String SQL_INSERT_DATA = "INSERT INTO mem_sample(java_heap, native_heap, total, time) VALUES(?,?,?,?)";
  Connection connection;
  String name;

  public void create(String dbname) {
    name = dbname;
    String url = "jdbc:sqlite:" + dbname;
    try {
      connection = DriverManager.getConnection(url);
      DatabaseMetaData metaData = connection.getMetaData();
      System.out.println("connect to: " + metaData.getURL() + ",\nDriver: " + metaData.getDriverName());
      Statement stmt = connection.createStatement();
      stmt.execute(SQL_CREATE_TABLE);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean insert(MemBean bean) {
    try {
      if (connection == null || connection.isClosed()) {
        create(name);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    if (connection != null) {
      try {
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_DATA);
        preparedStatement.setInt(1, bean.javaHeapSize);
        preparedStatement.setInt(2, bean.nativeHeapSize);
        preparedStatement.setInt(3, bean.totalSize);
        preparedStatement.setLong(4, bean.time);
        preparedStatement.executeUpdate();
        return true;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return false;
  }


}
