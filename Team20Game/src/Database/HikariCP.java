package Database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

//Jonas database         jdbc:mysql://mysql.stud.idi.ntnu.no:3306/olejlia?user=olejlia&password=YvJwUsj8
//Håvard database:       jdbc:mysql://mysql.stud.idi.ntnu.no:3306/haavasma?user=haavasma&password=eSVol6ey
//Martin database:       jdbc:mysql://mysql.stud.idi.ntnu.no:3306/martijni?user=martijni&password=wrq71s2w

public class HikariCP {

    private static HikariDataSource ds;

    static{

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://mysql.stud.idi.ntnu.no:3306/olejlia");
        config.setUsername("olejlia");
        config.setPassword("YvJwUsj8");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setPoolName("Chess Pool");
        config.setMaxLifetime(30000);

        ds = new HikariDataSource(config);
    }

    public static Connection getCon() throws SQLException {
        return ds.getConnection();
    }
}

