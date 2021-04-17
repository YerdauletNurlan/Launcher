package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.*;

public class RegistrationController {
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
    public ResultSet getSQL(String str) throws SQLException {
        Connection dbConnection = getDBConnection();
        Statement statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery(str);
        return rs;
    }
    @FXML
    private Button Login_button;
    @FXML
    private Button Register_button;
    @FXML
    private TextField Email;
    @FXML
    private TextField Name;
    @FXML
    private TextField Surname;
    @FXML
    private TextField Password;
    @FXML
    private TextField Age;
    @FXML
    private Label error;

    public void initialize(){}

    public void initManager(final LoginManager loginManager) {
        Register_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String sessionID = null;
                try {
                    sessionID = Registration();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                if (sessionID != "error") {
                    loginManager.authenticated(sessionID);
                }
                else {
                }
            }
        });
        Login_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                loginManager.showLoginScreen();
            }
        });
    }

    public String Registration() throws SQLException {
        String email = Email.getText();
        String name = Name.getText();
        String surname = Surname.getText();
        String password = Password.getText();
        String  age = Age.getText();
        int id=0;

        Boolean checkE = false;
        Boolean checkN = false;
        Boolean checkS = false;
        Boolean checkP = false;
        Boolean checkA = false;
        Boolean empty = true;

        Connection dbConnection = getDBConnection();
        Statement statement = dbConnection.createStatement();
        String selectQ="select id from customers order by id desc limit 1;";
        ResultSet rs = getSQL(selectQ);
        while (rs.next()) {
            id = rs.getInt("id") + 1;
        }
        if (email.isEmpty() || name.isEmpty() || surname.isEmpty() || password.isEmpty() || age.isEmpty())
        {
            empty=false;
            error.setVisible(true);
            error.setText("one of the fields is empty");
        }
        if (checkEmail(email))
        {
            checkE=true;
        }
        else {
            checkE=false;
            error.setText("this email already registred");
            error.setVisible(true);
        }
        if ( name.matches("[a-zA-Z]*"))
        {
            checkN=true;
        }
        else {
            checkN=false;
            error.setVisible(true);
            error.setText("name must contain only letters");
        }
        if ( surname.matches("[a-zA-Z]*"))
        {
            checkS=true;
        }
        else {
            checkS=false;
            error.setVisible(true);
            error.setText("surname must contain only letters");
        }
        if ( password.matches("[a-zA-Z0-9_-]*"))
        {
            checkP=true;
        }
        else {
            checkP=false;
            error.setVisible(true);
            error.setText("password can contain only letters, numbers and '_-'");
        }
        if ( age.matches("[0-9]*"))
        {
            checkA=true;
        }
        else {
            checkA=false;
            error.setVisible(true);
            error.setText("age must contain only numbers");
        }

        if (checkE && checkN && checkS && checkP && checkA && empty){
            error.setVisible(false);
            String insertQ="INSERT INTO Customers (id, email, name, surname, age, password) " +
                    "VALUES(" + id + ", \'" + email + "\', \'" + name + "\', \'" + surname + "\', " + Integer.parseInt(age) + ", \'" + password + "\');";
            System.out.println(insertQ);
            addSQL(insertQ);
            return email;
        }
        else
        {
            return "error";
        }
    }

    public boolean checkEmail(String email)throws SQLException {
        boolean check = true;
        String dbEmail;
        Connection dbConnection = getDBConnection();
        Statement statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery("select email from customers");
        System.out.println("WORKING");
        while (rs.next()) {
            dbEmail = rs.getString("email");
            System.out.println(dbEmail + "/ /"+ email);
            if (dbEmail.equals(email)) {
                System.out.println("IFWROKS");
                check = false;
            }
        }
        return check;
    }
}
