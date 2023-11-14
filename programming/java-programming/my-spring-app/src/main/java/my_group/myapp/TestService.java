package my_group.myapp;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class TestService {
    PgDataSource dataSource;

    public TestService(PgDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void testQuery() throws SQLException {
        Connection conn = dataSource.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("select * from test");
        while (rs.next()) {
            System.out.print("Column 1 returned ");
            System.out.println(rs.getString(1));
        }
        rs.close();
        st.close();
    }
}
