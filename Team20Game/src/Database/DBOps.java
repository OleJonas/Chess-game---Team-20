package Database;

import java.sql.*;
import java.util.ArrayList;

public class DBOps{
    private String url = "jdbc:mysql://mysql.stud.idi.ntnu.no:3306/olejlia?user=olejlia&password=YvJwUsj8";
    private Connection con;
    private PreparedStatement stmt;
    private ResultSet res;

    public DBOps() {
        this.con = null;
        this.stmt = null;
        this.res = null;
    }

    public void createTable(String table){
        String in1 = "DROP TABLE IF EXISTS " + table + ";";
        String in2 = "CREATE TABLE " + table + "(def INTEGER);";
        exUpdate(in1);
        exUpdate(in2);
    }

    public ArrayList<String> exQuery(String sqlString){
        ArrayList<String> out = new ArrayList<>();
        int colNr = 1;
        try{
            con = DriverManager.getConnection(url);
            stmt = con.prepareStatement(sqlString);
            res = stmt.executeQuery();

            while(res.next()){
                out.add(res.getString(colNr));
                colNr++;
            }
        } catch(SQLException sql){
            sql.printStackTrace();
        } finally{
            close();
        }
        return out;
    }

    public int exUpdate(String sqlString) {
        int affectedRows = 0;
        try {
            con = DriverManager.getConnection(url);
            stmt = con.prepareStatement(sqlString);
            affectedRows = stmt.executeUpdate();
            /*if(affectedRows == 0){
                throw new SQLException("Something went wrong while writing to the database!");
            }*/
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
