package sample;

import com.chilkatsoft.CkHttp;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ShopController {
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
    private Button Home_button;
    @FXML
    private Button Library_button;
    @FXML
    private Button Info_button;
    @FXML
    private Button Offer_button;
    @FXML
    private Label Registred_email;
    @FXML
    private Button month1;
    @FXML
    private Button month3;
    @FXML
    private Button month12;
    @FXML
    private Button app_Download_button;
    @FXML
    private Button app_Balance_button;
    int price;


    public void initialize(){}

    public void initSessionID(final LoginManager loginManager, String sessionID) {
        if (sessionID!="") {
            Registred_email.setText(sessionID);
            Registred_email.setVisible(true);
        }
        else {
            System.out.println("NOPE");
        }
        Home_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                loginManager.showMainView(sessionID);
            }
        });
        app_Balance_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                loginManager.showBalanceScreen(sessionID);
            }
        });
        app_Download_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Download();
            }
        });
        Offer_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                loginManager.showOfferScreen(sessionID);
            }
        });


        //create xml buy page
        month1.setOnAction(new EventHandler<ActionEvent>(){
            @Override public void handle(ActionEvent event) {
                loginManager.showBuyScreen(sessionID, 1.99);
            }
        });
        month3.setOnAction(new EventHandler<ActionEvent>(){
            @Override public void handle(ActionEvent event) {
                loginManager.showBuyScreen(sessionID, 4.99);
            }
        });
        month12.setOnAction(new EventHandler<ActionEvent>(){
            @Override public void handle(ActionEvent event) {
                loginManager.showBuyScreen(sessionID, 9.99);
            }
        });
    }


    public void Download()
    {
        //  This requires the Chilkat API to have been previously unlocked.
        //  See Global Unlock Sample for sample code.

        CkHttp http = new CkHttp();

        http.put_AwsAccessKey("AKIAI6HIVEUDDMJCYWIQ");
        http.put_AwsSecretKey("deL7R+t9r1fnvfuhWAK8QokRT5amm5Pl7Jg6BaiR");
        http.put_S3Ssl(false);
        http.put_AwsRegion("us-east-2");
        http.put_AwsEndpoint("s3-us-east-2.amazonaws.com");

        String bucketName = "yerdaulet-test-bucket";
        String objectName = "yerdaulet-test_conf_backup_20200914_100108.zip";
        String localFilePath = "yerdaulet-test_conf_backup_20200914_100108.zip";

        boolean success = http.S3_DownloadFile(bucketName,objectName,localFilePath);

        if (success != true) {
            System.out.println(http.lastErrorText());
        }
        else {
            System.out.println("File downloaded.");
        }
    }

    }
