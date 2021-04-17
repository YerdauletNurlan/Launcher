package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) { launch(args); }
    @Override
    public void start(Stage primaryStage) throws IOException {
        Scene scene = new Scene(new StackPane(), 620, 400);
        LoginManager controller = new LoginManager(scene);
        controller.showLoginScreen();
        /*Parent root = FXMLLoader.load(getClass().getResource("app.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 700, 450));
        primaryStage.show();*/
        primaryStage.setResizable(false);
        primaryStage.setTitle("IRONWARE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
