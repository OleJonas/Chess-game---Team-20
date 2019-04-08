package Database;

import java.sql.*;
import java.util.ArrayList;

/**
 * <h1>DBOps</h1>
 * This class is used to make queries and updates to the database.
 * @since 08.04.2019
 * @author Team 20
 */

public class DBOps{
    private Connection con;
    private PreparedStatement stmt;
    private ResultSet res;

    /**
     * Constructor that initiates Connection, PreparedStatement and ResultSet.
     */
    public DBOps() {
        this.con = null;
        this.stmt = null;
        this.res = null;
    }

    /**
     * Method that finds the largest user id (most recently added).
     * @return Returns the largest user id.
     */
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

    /**
     * Method that registers a new user.
     * @param username The username of the new account.
     * @param passwordHash The hashed version of the password with the salt.
     * @param salt The unhashed salt.
     * @return Returns true if register is successful.
     */
    public boolean register(String username, String passwordHash, String salt){
        boolean regOK = true;
        int maxID = getMaxUserID();
        try{
            con = HikariCP.getCon();
            con.setAutoCommit(false);
            stmt = con.prepareStatement("INSERT INTO User(username, password, SALT, avatar, gamesPlayed, gamesWon, gamesLost, gamesRemis, ELOrating) values(?, ?, ?, 'avatar1.jpg', 0, 0, 0, 0, 1000);");

            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
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

    /**
     * Method that checks if password is correct.
     * @param username Name of user.
     * @return Returns password and salt.
     */
    public ArrayList<String> checkPW(String username){
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

    /**
     * Checks if username exists.
     * @param username Username that is to be checked.
     * @return Returns username.
     */
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

    /**
     * Method for making a query to the database.
     * @param sqlString The SQL query.
     * @param amountOfColumns The amount of columns that are returned.
     * @return Returns an arraylist of all the findings.
     */
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

    /**
     * Makes an update to the database.
     * @param sqlString The update that is to be made.
     * @return Returns the amount of affected rows.
     */
    public int exUpdate(String sqlString) {
        int affectedRows = 0;
        try {
            con = HikariCP.getCon();
            stmt = con.prepareStatement(sqlString);
            affectedRows = stmt.executeUpdate();
        } catch (SQLException sql) {
            sql.printStackTrace();
        } finally {
            close();
        }
        return affectedRows;
    }

    /**
     * Returns info about user.
     * @param username Username of user.
     * @param columns Amount of columns that is to be returned.
     * @return Returns info.
     */
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

    /**
     * Method for creating a game.
     * @param userid User id of person creating the game.
     * @param mode The mode of the chess game.
     * @param time The time for the game.
     * @param increment The increment for the game.
     * @param color The color you will play.
     * @param rated If the game is rated or not.
     * @param friendid Id of friend you want to invite.
     */

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

    /**
     * Method that returns the database-connection to the HikariCP connection-pool.
     * The connection-pool then takes care of further closing and administration (eg. closing the connection entirely or do some cleanup).
     */
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

    /**
     * Method for the first message you write to chat.
     * @param gameid Id of the game you are writing in.
     * @param userid Your user id.
     * @param msg The message you want to send.
     */
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

    /**
     * Method for writing to chat.
     * @param gameid Id of the game you are writing in.
     * @param lastChat Variable for just getting the last message.
     * @param userid Your user id.
     * @param msg The message you want to send.
     */
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