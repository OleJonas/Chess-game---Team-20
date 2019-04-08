package Database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariCP {

    private static HikariDataSource ds;

    /**
     * Setting up the HikariCP data source. Setting limits for prepared statement size and timeouts.
     */
    static{
        HikariConfig config1 = new HikariConfig();
        config1.setJdbcUrl("jdbc:mysql://mysql.stud.idi.ntnu.no:3306/martijni");
        config1.setUsername("martijni");
        config1.setPassword("wrq71s2w");
        config1.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config1.addDataSourceProperty("cachePrepStmts", "true");
        config1.addDataSourceProperty("prepStmtCacheSize", "250");
        config1.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config1.setPoolName("Chess Pool");
        config1.setMaxLifetime(30000);

        ds = new HikariDataSource(config1);
    }

    /**
     * Method that returns a HikariDataSource object.
     * @return returns the HikariDataSource object after calling getConnection() using the parameters set in the above code-block.
     * @throws SQLException
     */
    public static Connection getCon() throws SQLException {
        return ds.getConnection();
    }
}