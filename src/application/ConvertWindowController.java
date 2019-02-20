/**
 * @author	: Origanus Ramfate 
 * @date	: February 2019
 * Parpose	: This class is a Controller for the ConvertWindow.fxml it contain
 * 			  methods for handling events and initialize the view.
 * 
 */

package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ConvertWindowController implements Initializable {
	@FXML
	private ToggleButton plM3U;
	@FXML
	private ToggleButton plM3U8;
	@FXML
	private ToggleButton plWPL;
	@FXML
	private ToggleButton plXSPF;
	@FXML
	private StackPane convertPane;
	@FXML
	private Stage convertWindowStage;
	@FXML
	private Button openButton;
	@FXML
	private TextField filenameTF;
	
	private ObservableList<PLMediaFile> mediaFileList;
	
	/**
	 * Sets the ToggleButtons according to the passed parameter
	 * 
	 * @param name
	 */
	private void setFormat(String name) {
		switch (name.toLowerCase()) {
			case "m3u8":
				plM3U8.selectedProperty().set(true);
				break;
			case "wpl":
				plWPL.selectedProperty().set(true);
				break;
			case "xspf":
				plXSPF.selectedProperty().set(true);
				break;
			default:
				plM3U.selectedProperty().set(true);
				break;
		}
	}
	
	/**
	 * Opens supported playlist files using the file chooser
	 * if a file is selected createObservableList(File file, String name) is called
	 */
	@FXML
	private void openFile() {

		FileChooser openFile = new FileChooser();
		openFile.setTitle("Open File");
		openFile.getExtensionFilters().addAll(
				new ExtensionFilter("All Files", "*.m3u", "*.m3u8", "*.wpl", "*.zpl", "*.xspf"),
				new ExtensionFilter("M3U Files", "*.m3u"),
				new ExtensionFilter("M3U8 Files", "*.m3u8"),
				new ExtensionFilter("WPL Files", "*.wpl", "*.zpl"),
				new ExtensionFilter("XSPF Files", "*.xspf")
		);
		File selectedFile = openFile.showOpenDialog(convertWindowStage);
		if(selectedFile != null) {
			createObservableList(selectedFile, selectedFile.getName());
		}
		
	}
	
	/**
	 * Displays a save dialog with the passed String as file name.
	 *  if the user saves the file a file is created using the file chooser file.
	 * 
	 * PlaylistFileCreator.createPlaylistFile(File, OservableList) is
	 * used to create a playlist using the mediaFileList(OservableList)
	 *  
	 * @param name
	 */
	private void saveFile(String name) {
		//save dialog box
		FileChooser saveFile = new FileChooser();
		saveFile.setTitle("Save File");	
		saveFile.setInitialFileName(name);
		saveFile.getExtensionFilters().addAll(
				new ExtensionFilter("M3U Files", "*.m3u")
				
				//To be implemented
//				,
//				new ExtensionFilter("M3U8 Files", "*.m3u8"),
//				new ExtensionFilter("WPL Files", "*.wpl"),
//				new ExtensionFilter("XSPF Files", "*.xspf"),
//				new ExtensionFilter("All Files", "*.*")
		);
		
		File savedFile = saveFile.showSaveDialog(convertWindowStage);
		if(savedFile != null && !mediaFileList.isEmpty() && !mediaFileList.equals(null)) {
			
			PlaylistFileCreator.createPlaylistFile(savedFile, mediaFileList); //Create a playlist file
	
		}	
	}
	
	/**
	 * If the passed file is a playlist the textfield displays the file location
	 * and the content of the playlist is read int the mediaFileList(ObservableList)
	 * the name of the file is plassed to the saveFile and a saceFile dialog is displayed
	 * 
	 * @throws InterruptedException 
	 * 
	 */
	private void createObservableList(File file, String name) {

		if(PlaylistFileCreator.isPlaylist(file)) {
			
			filenameTF.setText(file.getAbsolutePath());
			name = name.substring(0, name.lastIndexOf("."));
			
			mediaFileList = FXCollections.observableArrayList();
			mediaFileList.addAll(PLMediaFile.readPLaylist(file));
			
			saveFile(name);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle rb) {
		
		setFormat(rb.getObject("format").toString()); //set ToggleButtons
		if(rb.getObject("stage") instanceof Stage)
			convertWindowStage = (Stage)rb.getObject("stage");
		else
			convertWindowStage = null;
			
		//Default window content
		Text placeholder = new Text("Drag and Drop Playlist file");
		convertPane.getChildren().add(placeholder);
		
		//Set drag event for retrieving dragged playlists
		convertPane.setOnDragOver(e -> {
			
			if(e.getDragboard().hasFiles())
				e.acceptTransferModes(TransferMode.ANY);
			
			e.consume();	
		});
		
		convertPane.setOnDragDropped(e -> {
			
			//Converts one playlist at a time, if the user dropped more than one they are ignored
			if(e.getDragboard().getFiles().size() == 1) {
				File plstFile = e.getDragboard().getFiles().get(0);
				createObservableList(plstFile, plstFile.getName());
				
			}
			
			e.consume();
		});		
	}
}
