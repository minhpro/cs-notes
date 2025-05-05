/*
 * Transaction examples
 */
package jdbc.example;

import java.sql.*;
import java.util.Properties;

public class TransactionExamples {

    public static void main(String[] args) {
        Properties connectionProps = new Properties();
        connectionProps.put("user", "postgres");
        connectionProps.put("password", "Abc@1234");
        final String dbms = "postgresql";
        final String serverName = "localhost";
        final int portNumber = 5432;
        final String database = "forumdb";
        try (Connection conn = DriverManager.getConnection("jdbc:" + dbms + "://" +
                serverName +
                ":" + portNumber + "/" +
                database,
                connectionProps)) {
            conn.setAutoCommit(false);
            executeSelect(conn);
            executeInsert(conn, "C++");
            executeSelect(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void executeSelect(Connection conn) throws SQLException {
        String query = "SELECT * FROM tags";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            String tag = rs.getString("tag");
            System.out.println(tag);
        }
    }

    private static void executeInsert(Connection conn, String tag) throws SQLException {
        String insert = "INSERT INTO tags (tag) VALUES (?)";
        PreparedStatement pStmt = conn.prepareStatement(insert);
        pStmt.setString(1, tag);
        pStmt.executeUpdate();
    }
}
