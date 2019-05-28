package application.view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import application.MainApp;
import application.model.DeckCompare;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainWindowController {

	@FXML
	private Button oldDeckButton;
	@FXML
	private TextField oldDeckTextField;
	@FXML
	private Button newDeckButton;
	@FXML
	private TextField newDeckTextField;
	@FXML
	private Button calculateDifference;
	@FXML
	private TextArea differenceArea;
	
	private MainApp mainApp;
	
    FileChooser fileChooser;

    DeckCompare deckCompare;
    
    Stage stage;
    
	@FXML
	private void start() {

	}
	
	public MainWindowController() {
    	fileChooser = new FileChooser();
    	deckCompare = new DeckCompare();
	}
	
	/**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
    	
    	
    	oldDeckButton.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override public void handle(ActionEvent e) {
    	    	handleDeck(oldDeckTextField);
    	    }
    	});
    	newDeckButton.setOnAction(new EventHandler<ActionEvent>() {
    	    @Override public void handle(ActionEvent e) {
    	    	handleDeck(newDeckTextField);
    	    }
    	});    
    	
    	//System.out.println(calculateDifference.getText());
    	//calculateDifference.setText("huh");
    	
    	calculateDifference.setOnAction(new EventHandler<ActionEvent>() {
    		@Override public void handle(ActionEvent e) {
    			handleCalculate();
    		}
    	});
    	
    	
    	
    }
    
    @FXML
    public void handleDeck(TextField tf) {
    	System.out.println("deck button working");
    	File file = fileChooser.showOpenDialog(mainApp.primaryStage);
    	
    	//System.out.println();
    	
        if (file != null) {
        	if (tf.equals(this.oldDeckTextField)) {
        		openFile(file, deckCompare.old);
        	} else {
        		openFile(file, !deckCompare.old);
        	}
        	tf.setText(file.getAbsolutePath());
        }
    }
    
    @FXML
    public void handleCalculate() {
    	//System.out.println("got calculate without crashing");

    	// null checks
    	differenceArea.setText("");
    	boolean oldDeckLoaded = !oldDeckTextField.getText().equals(""), 
    			newDeckLoaded =  !newDeckTextField.getText().equals("");
    	if (!oldDeckLoaded || !newDeckLoaded) {
    		if (!oldDeckLoaded) {// newDeckTextField.getText().equals("")) {
        		differenceArea.appendText("Old deck not properly loaded or specified\n");
        	}
        	if (!newDeckLoaded) {
        		differenceArea.appendText("New deck not properly loaded or specified");
        	}
        	return;
    	}
    	if (oldDeckTextField.getText().indexOf("http") != -1) {
    		//System.out.println("old deck website detected");
    		deckCompare.loadDeck(
    				deckCompare.downloadDeck(oldDeckTextField.getText(), deckCompare.old), deckCompare.old
    				);
    		//deckCompare.switchFile();
    	}
    	if (newDeckTextField.getText().indexOf("http") != -1) {
    		//System.out.println("new deck website detected");
    		deckCompare.loadDeck(
    				deckCompare.downloadDeck(newDeckTextField.getText(), !deckCompare.old), !deckCompare.old
    				);
    		//deckCompare.switchFile();
    	}
    	
    	
    	//System.out.println(deckCompare.compare());
    	differenceArea.setText(deckCompare.compare());
    	
    	
    }
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

    }
    
    public void openFile(File file, boolean old) {
    	//System.out.println("got here without crashing");
    	deckCompare.loadDeck(file, old);
    	//deckCompare.switchFile();
    }
    
    
    
}
