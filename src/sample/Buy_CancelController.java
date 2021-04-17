package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.sql.*;

public class Buy_CancelController {
    private static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("OK");
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR");
        }
        try {
            dbConnection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/postgres", "postgres", "123");
            System.out.println("OK2");
            return dbConnection;
        } catch (SQLException e) {
            System.out.println("ERROR");
        }
        return dbConnection;
    }
    public void addSQL(String str) throws SQLException{
        Connection dbConnection = getDBConnection();
        Statement statement = dbConnection.createStatement();
        System.out.println(str);
        statement.executeUpdate(str);
    }

    @FXML
    private Button buy;
    @FXML
    private Button cancel;
    @FXML
    private Label error;

    double balance = 0;
    boolean subscription = false;

    public void initialize(){}

    public void initSessionID(final LoginManager loginManager, String sessionID, double cost){
        if (sessionID!="") {

        }
        else {
            System.out.println("NOPE");
        }
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                loginManager.showShopScreen(sessionID);
            }
        });

        //create xml buy page
        buy.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                Connection dbConnection = getDBConnection();
                Statement statement = null;
                try {
                    statement = dbConnection.createStatement();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                ResultSet rs = null;
                try {
                    rs = statement.executeQuery("select balance, subscription from customers " +
                            "where email = \'"+ sessionID  + "\';");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                while (true)
                {
                    try {
                        if (!rs.next()) break;
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    try {
                        balance = rs.getDouble("balance");
                        subscription = rs.getBoolean("subscription");
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                if (subscription != true) {
                    if (balance >= cost) {

                        balance = balance - cost;
                        subscription = true;

                        String SQL = "UPDATE customers "
                                + "SET balance = ? "
                                + ", subscription = ? "
                                + "WHERE email = ?";
                        PreparedStatement pstmt = null;
                        try {
                            pstmt = dbConnection.prepareStatement(SQL);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        try {
                            pstmt.setDouble(1, balance);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        try {
                            pstmt.setBoolean(2, subscription);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        try {
                            pstmt.setString(3, sessionID);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        try {
                            pstmt.executeUpdate();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        loginManager.showShopScreen(sessionID);
                    } else {
                        error.setText("not enough money");
                        error.setVisible(true);
                    }
                }
                else {
                    error.setText("You already have sub");
                    error.setVisible(true);

                }
            }
        });
    }
    }
