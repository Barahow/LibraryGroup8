package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SearchCatalogMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/search.fxml"));
        //root.getStylesheets().add(getClass().getResource("Library.css").toExternalForm());
        Scene scene = new Scene(root);
        scene.getStylesheets().add("Search/LMS.css");
        primaryStage.setTitle("Library");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
