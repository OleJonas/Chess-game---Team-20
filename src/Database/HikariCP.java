package Database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * <h1>HikariCP</h1>
 * This class is used for connection pooling.
 * @since 10.04.2019
 * @author Team 20
 */

public class HikariCP {

    private static HikariDataSource ds;

    /**
     * Setting up the HikariCP data source. Setting limits for url, username, password, driver-class, prepared statement cache-size, --
     * -- max prepared stmt size that will be cached, connection-pool name and finally max lifetime for --
     * -- each connection before they are returned to the pool by timeout.
     */

    // For reading username and password from config-file https://www.javatpoint.com/properties-class-in-java
    static{

        Properties p = new Properties();
        String path = "./config.properties";
        try{
            FileInputStream in = new FileInputStream(path);
            p.load(in);
            in.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }

        HikariConfig config1 = new HikariConfig();
        config1.setJdbcUrl("jdbc:mysql://mysql.stud.idi.ntnu.no:3306/"+p.getProperty("username"));
        config1.setUsername(p.getProperty("username"));
        config1.setPassword(p.getProperty("password"));
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
     * @return returns the HikariDataSource object already initiated in the above code-block.
     * @throws SQLException
     */
    public static Connection getCon() throws SQLException {
        return ds.getConnection();
    }
}