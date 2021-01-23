package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Controller {
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
    //Used in many places
    @FXML
    private Button app_Login_button;
    @FXML
    private Button app_Registration_button;
    @FXML
    private Button app_home_button;
    //App.fxml page
    @FXML
    private Button app_Shop_button;
    @FXML
    private Button app_Library_button;
    @FXML
    private Button app_Info_button;
    //Registration.fxml page
    @FXML
    private TextField Reg_Email;
    @FXML
    private TextField Reg_Name;
    @FXML
    private TextField Reg_Surname;
    @FXML
    private TextField Reg_Password;
    @FXML
    private TextField Reg_Age;
    @FXML
    private Button Reg_Register_button;
    @FXML
    private Label reg_error;
    //Login.fxml
    @FXML
    private TextField Login_Email;
    @FXML
    private TextField Login_Password;
    @FXML
    private Button Login_Login_button;
    @FXML
    private Label log_error;

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

    @FXML
    public void openLoginPage()
    {
        //app_Login_button.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/Login.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root, 700, 450));
        stage.showAndWait();
    }

    @FXML
    public void openRegistrationPage()
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/Registration.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root, 700, 450));
        stage.showAndWait();
    }

    @FXML
    public void openHomePage()
    {
        //app_home_button.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/sample/app.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root, 700, 450));
        stage.showAndWait();
    }


    @FXML
    public void Registration() throws SQLException {
        String email = Reg_Email.getText();
        String name = Reg_Name.getText();
        String surname = Reg_Surname.getText();
        String password = Reg_Password.getText();
        int age = Integer.parseInt(Reg_Age.getText());
        int id=0;
        Connection dbConnection = getDBConnection();
        Statement statement = dbConnection.createStatement();
        ResultSet rs = statement.executeQuery("select id from customers order by id desc limit 1;");
        while (rs.next()) {
            id = rs.getInt("id") + 1;
        }

        if (checkEmail(email)){
            reg_error.setVisible(false);
            String str="INSERT INTO Customers (id, email, name, surname, age, password) " +
                    "VALUES(" + id + ", \'" + email + "\', \'" + name + "\', \'" + surname + "\', " + age + ", \'" + password + "\');";
            System.out.println(str);
            addSQL(str);
            openHomePage();
        }
        else
        {
            reg_error.setText("Please change email");
            reg_error.setVisible(true);
        }
    }

    @FXML
    public void Login() throws SQLException{
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
            log_error.setVisible(false);
            System.out.println("WORKS");
            openHomePage();
            app_Login_button.setVisible(false);
            app_Registration_button.setVisible(false);

        }
        else {
            log_error.setText("email or password incorrect");
            log_error.setVisible(true);
            System.out.println("ERROR");
        }

    }
}