package Database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class HikariCP {

    private static HikariDataSource ds;

    static{
        HikariConfig config1 = new HikariConfig();
        config1.setJdbcUrl("jdbc:mysql://mysql.stud.idi.ntnu.no:3306/martijni?serverTimezone=GMT");
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

    public static Connection getCon() throws SQLException {
        return ds.getConnection();
    }
}