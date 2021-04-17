package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.*;

public class LoginController {
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

    @FXML
    private Button app_Registration_button;
    @FXML
    private Button app_home_button;
    //Login.fxml
    @FXML
    private TextField Login_Email;
    @FXML
    private TextField Login_Password;
    @FXML
    private Button Login_Login_button;
    @FXML
    private Label log_error;

    public void initialize(){}

    public void initManager(final LoginManager loginManager) {
        Login_Login_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String sessionID = null;
                try {
                    sessionID = authorize();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                if (sessionID != "error") {
                    loginManager.authenticated(sessionID);
                }
                else {
                    log_error.setText("wrong username or password");
                }
            }
        });

        app_Registration_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                loginManager.showRegistrationScreen();
            }
        });
        app_home_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                loginManager.showMainView("");
            }
        });
    }

    private String authorize() throws SQLException {
        String email = Login_Email.getText();
        String password = Login_Password.getText();
        String checkEM = "";
        String checkPWD = "";
        int check = 0;
        Connection dbConnection = getDBConnection();
        Statement statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery("select email, password from customers " +
                "where email = \'"+ email +"\' and password = \'" + password + "\';");
        System.out.println(email + password);
        while (rs.next())
        {
            checkEM=rs.getString("email");
            checkPWD=rs.getString("password");
            check++;
        }
        if(check!=0)
        {
            //log_error.setVisible(false);
            System.out.println("WORKS");
            System.out.println("login" + email + password);
            return email;
        }
        else {
            log_error.setText("email or password incorrect");
            log_error.setVisible(true);
            System.out.println("ERROR");
            return "error";
        }

    }

/*
    private String generateSessionID() {
        sessionID++;
        return "xyzzy - session " + sessionID;
    }
*/



    /*
    @FXML
    public void Login() throws SQLException {
        String email = Login_Email.getText();
        String password = Login_Password.getText();
        String checkEM = "";
        String checkPWD = "";
        int check = 0;
        Connection dbConnection = getDBConnection();
        Statement statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery("select email, password from customers " +
                "where email = \'"+ email +"\' and password = \'" + password + "\';");
        System.out.println(email + password);
        while (rs.next())
        {
            checkEM=rs.getString("email");
            checkPWD=rs.getString("password");
            check++;
        }
        if(check!=0)
        {
            //log_error.setVisible(false);
            System.out.println("WORKS");
            System.out.println("login" + email + password);
        }
        else {
            log_error.setText("email or password incorrect");
            log_error.setVisible(true);
            System.out.println("ERROR");
        }
    }*/
}
