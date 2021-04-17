package sample;

import com.chilkatsoft.CkHttp;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;

public class MainController {
    static {
        try {
            System.loadLibrary("chilkat");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
    }
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
   /* public void addSQL(String str) throws SQLException{
        Connection dbConnection = getDBConnection();
        Statement statement = dbConnection.createStatement();
        System.out.println(str);
        statement.executeUpdate(str);
    }*/
    @FXML
    private Button app_Login_button;
    @FXML
    private Button app_Registration_button;
    //App.fxml page
    @FXML
    private Button app_Shop_button;
    @FXML
    private Button app_Library_button;
    @FXML
    private Button app_Info_button;
    @FXML
    private Button app_Logout_button;
    @FXML
    private Button app_Download_button;
    @FXML
    private Button app_Balance_button;
    @FXML
    private Label Registred_email;
    @FXML
    HBox hBox;
    ImageView imageView;
    Button lbutton, rButton;

    ArrayList<String> list = new ArrayList<>();
    double orgCliskSceneX, orgReleaseSceneX;
    int j = 0;

    boolean subscription;

    public void initialize(){
    }

    public void initSessionID(final LoginManager loginManager, String sessionID) throws FileNotFoundException {
        list.add("C:\\Users\\erdau\\IdeaProjects\\Ironware_launcher\\src\\sample\\1.png");
        list.add("C:\\Users\\erdau\\IdeaProjects\\Ironware_launcher\\src\\sample\\ICON.png");

        Connection dbConnection = getDBConnection();
        Statement statement = null;
        try {
            statement = dbConnection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        ResultSet rs = null;
        try {
            rs = statement.executeQuery("select subscription from customers " +
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
                subscription = rs.getBoolean("subscription");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        lbutton = new Button("<");
        rButton = new Button(">");
        Image images[] = new Image[list.size()];
        for (int i = 0; i < list.size(); i++) {
            images[i] = new Image(new FileInputStream(list.get(i)));
        }

        imageView = new ImageView(images[j]);
        imageView.setCursor(Cursor.CLOSED_HAND);

        imageView.setOnMousePressed(circleOnMousePressedEventHandler);

        imageView.setOnMouseReleased(e -> {
            orgReleaseSceneX = e.getSceneX();
            if (orgCliskSceneX > orgReleaseSceneX) {
                lbutton.fire();
            } else {
                rButton.fire();
            }
        });

        rButton.setOnAction(e -> {
            j = j + 1;
            if (j == list.size()) {
                j = 0;
            }
            imageView.setImage(images[j]);

        });
        lbutton.setOnAction(e -> {
            j = j - 1;
            if (j == 0 || j > list.size() + 1 || j == -1) {
                j = list.size() - 1;
            }
            imageView.setImage(images[j]);

        });

        imageView.setFitWidth(504);
        imageView.setFitHeight(359);
        //hBox.setSpacing(500);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(imageView);


        if (sessionID!="") {
            Registred_email.setText(sessionID);
            app_Login_button.setVisible(false);
            app_Registration_button.setVisible(false);
            Registred_email.setVisible(true);
        }
        else {
            app_Logout_button.setVisible(false);
        }
        if (subscription)
        {
            app_Download_button.setVisible(true);
        }
        else
        {
            app_Download_button.setVisible(false);
        }
        app_Logout_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                loginManager.logout();
            }
        });
        app_Registration_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                loginManager.showRegistrationScreen();
            }
        });
        app_Login_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                loginManager.showLoginScreen();
            }
        });
        app_Shop_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                loginManager.showShopScreen(sessionID);
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
/*
    public void openHomePage() {
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
        //Registred_email.setText("registred_user");
        stage.showAndWait();
        //app_Registration_button.setVisible(buttonsVisible);
        //app_Login_button.setVisible(buttonsVisible);

    }
*/
EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

    @Override
    public void handle(MouseEvent t) {
        orgCliskSceneX = t.getSceneX();
    }
};

}
