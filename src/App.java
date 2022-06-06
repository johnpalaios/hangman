import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.*;
import javafx.scene.Parent;




class InvalidCountExeception extends Exception {}
class UndersizeException extends Exception {}

public class App extends Application
{	
	public static void main(String [] args) 
	{
        launch(args);
    }
    @Override
	public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml-files/main.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}






