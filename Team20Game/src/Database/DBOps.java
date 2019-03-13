package Database;

import java.sql.*;

public class DBOps{
    private String url = "jdbc:mysql://mysql.stud.idi.ntnu.no:3306/martijni?user=martijni&password=wrq71s2w";
    private Connection con;
    private Statement stmt;
    private ResultSet res;

    public DBOps() {
        this.con = null;
        this.stmt = null;
        this.res = null;
    }
        /*try {
            this.con = DriverManager.getConnection("jdbc:mysql://mysql.stud.idi.ntnu.no:3306/martijni?user=martijni&password=wrq71s2w");
            this.stmt = con.createStatement();
            this.res = null;
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
    }*/

    /*public Connection getCon(){ return con;}
    public Statement getStmt(){ return stmt;}*/

    public ResultSet exQuery(String sqlString){
        try{
            con = DriverManager.getConnection(url);
            stmt = con.createStatement();
            res = stmt.executeQuery(sqlString);
        } catch(SQLException sql){
            sql.printStackTrace();
        } finally{
            close();
        }
        return res;
    }

    public int exUpdate(String sqlString) {
        int affectedRows = 0;
        try {
            con = DriverManager.getConnection(url);
            stmt = con.createStatement();
            affectedRows = stmt.executeUpdate(sqlString);
            if(affectedRows == 0){
                throw new SQLException("Something went wrong while writing to the database!");
            }
        } catch (SQLException sql) {
            sql.printStackTrace();
        } finally {
            close();
        }
        return affectedRows;
    }

    public void close(){
        try {
            if(con != null){
                if(stmt != null){
                    if(res != null){
                        res.close();
                    }
                    stmt.close();
                }
                con.close();
            }
        } catch(SQLException sql){
            sql.printStackTrace();
        }
    }
}
