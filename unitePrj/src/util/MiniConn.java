package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MiniConn {

   public static Connection getConnection() {
      Connection conn = null;
      
      try {

         // 커넥션 객체 얻어서 리턴
         Class.forName("oracle.jdbc.driver.OracleDriver");

         // 접속 정보
         String url = "jdbc:oracle:thin:@localhost:1521:xe";
         String id = "C##KH";
         String pwd = "KH";

         conn = DriverManager.getConnection(url, id, pwd);
         conn.setAutoCommit(false);
      
         return conn;
      } catch (Exception e) {
         System.out.println("커넥션 가져오기 실패!");
         e.printStackTrace();
      }
      return conn;
   }

   public static void close(Connection conn) {
      try {if(conn != null) conn.close();} catch (SQLException e) {e.printStackTrace();}
   }
   
   public static void close(Statement stmt) {
      try {if(stmt != null) stmt.close();} catch (Exception e) {}
   }
   
   public static void close(ResultSet rs) {
      try {if(rs != null) rs.close();} catch (Exception e) {}
   }
   
   public static void close(Statement stmt, ResultSet rs, Connection conn) {
      try {if(rs != null) rs.close();} catch (Exception e) {}
      try {if(stmt != null) stmt.close();} catch (Exception e) {}
      try {if(conn != null) conn.close();} catch (SQLException e) {e.printStackTrace();}
   }
   
   public static void close(Statement stmt, Connection conn) {
      try {if(stmt != null) stmt.close();} catch (Exception e) {}
      try {if(conn != null) conn.close();} catch (SQLException e) {e.printStackTrace();}
   }
   
   public static void commit(Connection conn) {
      if(conn != null ) {try {conn.commit();} catch (SQLException e) {e.printStackTrace();}}
   }
   
   public static void rollback(Connection conn) {
      if(conn != null ) {
         try {conn.rollback();} catch (SQLException e) {e.printStackTrace();}}
   }
   
}// class