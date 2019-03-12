package sample;

import Connection.ConnectionPool;
import Connection.ConnectionPool;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.*;

import Connection.Cleaner;
import javafx.scene.control.Button;

import static sample.ControllerHome.getUserName;

public class ChooseOpponent{

    private Connection connection;
    private String username = getUserName();
    private String opponentUsername = null;
    private static int gameId;


    @FXML
    public TextField opponent;
    public Button challenge;
    public Label usernameWrong;

    public void findOpponent(ActionEvent event) {
        ResultSet rs = null;
        PreparedStatement insertSentence = null;

        try{
            connection = ConnectionPool.getConnection();
            usernameWrong.setVisible(false);
            String insertSql = "SELECT username FROM Player WHERE username =?;";
            insertSentence = connection.prepareStatement(insertSql);
            insertSentence.setString(1, opponent.getText());
            rs = insertSentence.executeQuery();
            if(rs.next()){
                opponentUsername = rs.getString("username");
                    makeGame(username, opponentUsername);
            }
            else {
                usernameWrong.setVisible(true);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Cleaner.close(insertSentence, rs, connection);
        }
    }

    private void makeGame(String player1, String player2) {
        Statement statement = null;
        ResultSet rsGameId = null;

        try{
            connection = ConnectionPool.getConnection();
            statement = connection.createStatement();
            String sqlInsert = "INSERT INTO Game(player1, player2, p1Points, p2Points) VALUES('"+ player1 + "', '" + player2 + "', 0, 0);";
            statement.executeUpdate(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            rsGameId = statement.getGeneratedKeys();
            rsGameId.next();
            gameId = rsGameId.getInt(1);

            sqlInsert = "UPDATE `Player` SET `gameId` = " + gameId + " WHERE `Player`.`username` = '" + player1 + "'";
            statement.executeUpdate(sqlInsert);

            sqlInsert = "UPDATE `Player` SET `gameId` = " + gameId + " WHERE `Player`.`username` = '" + player2 + "'";
            statement.executeUpdate(sqlInsert);

            System.out.println(gameId);
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            Cleaner.close(statement, rsGameId, connection);
        }
    }

    public static int getGameId() {
        return gameId;
    }
}
