package Database;


import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.ArrayList;

public class DBOps{
    private Connection con;
    private PreparedStatement stmt;
    private ResultSet res;

    public DBOps() {
        this.con = null;
        this.stmt = null;
        this.res = null;
    }

    private int getMaxUserID(){
        int max = 0;
        try{
            con = HikariCP.getCon();
            stmt = con.prepareStatement("SELECT MAX(user_id) FROM User");
            res = stmt.executeQuery();
            res.next();
            max = res.getInt(1) + 1;
        } catch(SQLException sql){
            sql.printStackTrace();
        } finally{
            close();
        }
        return max;
    }

    public boolean register(String username, String saltHash, String salt){
        boolean regOK = true;
        int maxID = getMaxUserID();
        try{
            con = HikariCP.getCon();
            con.setAutoCommit(false);
            stmt = con.prepareStatement("INSERT INTO User(username, password, SALT, avatar, gamesPlayed, gamesWon, gamesLost, gamesRemis, ELOrating) values(?, ?, ?, 'avatar1.jpg', 0, 0, 0, 0, 1000);");

            stmt.setString(1, username);
            stmt.setString(2, saltHash);
            stmt.setString(3, salt);
            int reg = stmt.executeUpdate();

            if(reg == 0){
                System.out.println("Something went wrong when inserting username, password etc.");
                regOK = false;
            }
            con.commit();

            // Inserting data into usersettings
            stmt = con.prepareStatement("INSERT INTO UserSettings(user_id , username, darkTileColor, lightTileColor, skinName) values(?, ?, '#8B4513', '#FFEBCD', 'Standard');");
            stmt.setInt(1, maxID);
            stmt.setString(2, username);
            int userSettingsReg = stmt.executeUpdate();

            if(userSettingsReg == 0) regOK = false;
            con.commit();
            regOK = true;
            con.setAutoCommit(true);
        } catch(SQLException sql){
            sql.printStackTrace();
        } finally{
            if(con != null){
                try {
                    if (!regOK) {
                        con.rollback();
                        con.setAutoCommit(true);
                    }
                }catch(SQLException hehe){
                    hehe.printStackTrace();
                } finally{
                    close();
                }
            }
            close();
        }
        return regOK;
    }

    public ArrayList<String> checkPW(String pw, String username){
        ArrayList<String> out = new ArrayList<>();
        try{
            con = HikariCP.getCon();
            stmt = con.prepareStatement("SELECT password, SALT FROM User WHERE username = ?;");
            stmt.setString(1, username);
            res = stmt.executeQuery();

            while(res.next()){
                for(int i = 0; i < 2; i++)
                out.add(res.getString(i+1));
            }
        } catch(SQLException sql){
            sql.printStackTrace();
        } finally{
            close();
        }
        return out;
    }

    public String checkUsername(String username){
        String out = "";
        try{
            con = HikariCP.getCon();
            stmt = con.prepareStatement("SELECT username FROM User WHERE username = ?;");
            stmt.setString(1,username);
            res = stmt.executeQuery();
            while(res.next()) {
                out = res.getString(1);
            }
        } catch(SQLException sql){
            sql.printStackTrace();
        } finally{
            close();
        }
        return out;
    }

    public ArrayList<String> exQuery(String sqlString, int amountOfColumns){
        ArrayList<String> out = new ArrayList<>();
        try{
            con = HikariCP.getCon();
            stmt = con.prepareStatement(sqlString);
            res = stmt.executeQuery();

            while(res.next()){
                for(int i = 0; i < amountOfColumns; i++){
                    out.add(res.getString(i+1));
                }
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
            con = HikariCP.getCon();
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

    public ArrayList<String> findUser(String username, int columns){
        ArrayList<String> out = new ArrayList<>();
        try{
            con = HikariCP.getCon();
            stmt = con.prepareStatement("SELECT avatar, gamesPlayed, gamesWon, gamesLost, gamesRemis, ELOrating FROM User WHERE username = ?;");
            stmt.setString(1, username);
            res = stmt.executeQuery();

            while(res.next()){
                for(int i = 0; i < columns; i++){
                    out.add(res.getString(i+1));
                }
            }
        } catch(SQLException sql){
            sql.printStackTrace();
        } finally {
            close();
        }
        return out;
    }

    public void createGame(int userid, int mode, int time, int increment, boolean color, int rated, int friendid){
        try{
            con = HikariCP.getCon();
            stmt = con.prepareStatement("INSERT INTO Game VALUES(DEFAULT, ?, ?, DEFAULT, ?, ?, ?, ?, 1, ?);");
            stmt.setInt(3, time);
            stmt.setInt(4, increment);
            stmt.setInt(5, rated);
            stmt.setInt(6, friendid);
            stmt.setInt(7, mode);

            if(color){
                stmt.setInt(1, userid);
                stmt.setNull(2, Types.INTEGER);
            } else{
                stmt.setInt(2, userid);
                stmt.setNull(1, Types.INTEGER);
            }
            stmt.executeUpdate();
        } catch(SQLException sql){
            sql.printStackTrace();
        } finally {
            close();
        }
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

    public void writeFirst(int gameid, int userid, String msg){
        try{
            con = HikariCP.getCon();
            stmt = con.prepareStatement("INSERT INTO Chat VALUES(?, 1, ?, ?);");
            stmt.setInt(1, gameid);
            stmt.setInt(2, userid);
            stmt.setString(3, msg);
            stmt.executeUpdate();
        } catch(SQLException sql){
            sql.printStackTrace();
        } finally{
            close();
        }
    }

    public void writeChat(int gameid, int lastChat, int userid, String msg){
        try{
            con = HikariCP.getCon();
            stmt = con.prepareStatement("INSERT INTO Chat VALUES(?, ?, ?, ?);");
            stmt.setInt(1, gameid);
            stmt.setInt(2, lastChat);
            stmt.setInt(3, userid);
            stmt.setString(4, msg);
            stmt.executeUpdate();
        } catch(SQLException sql){
            sql.printStackTrace();
        } finally{
            close();
        }
    }
}