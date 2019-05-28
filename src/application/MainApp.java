package application;
	
import java.io.IOException;

import application.view.MainWindowController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class MainApp extends Application {
	
	public Stage primaryStage;
	
	private AnchorPane rootLayout;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Gitaxian Compare");
		try {
			AnchorPane root = new AnchorPane();
			Scene scene = new Scene(root,400,600);
			//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		initMainLayout();
		
	}
	
	public void initMainLayout() {
		FXMLLoader loader = new FXMLLoader();
		try {
			loader.setLocation(MainApp.class.getResource("view/MainWindow.fxml"));
			
			rootLayout = (AnchorPane) loader.load();
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
			MainWindowController controller = loader.getController();
			controller.setMainApp(this);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setUpController() {
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
