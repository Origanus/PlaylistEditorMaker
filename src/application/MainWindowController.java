/**
 * @author	: Origanus Ramfate 
 * @date	: February 2019
 * Parpose	: This class is a Controller for the MainWindow.fxml it contain
 * 			  methods for handling events, shortcuts and initialize the view.
 * 
 */

package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainWindowController implements Initializable{
	
	@FXML
	private BorderPane root;
	@FXML
	private TableView<PLMediaFile> playlist;
	@FXML
	private TableColumn<PLMediaFile, String> tcName;
	@FXML
	private TableColumn<PLMediaFile, String> tcTitle;
	@FXML
	private TableColumn<PLMediaFile, String> tcArtist;
	@FXML
	private TableColumn<PLMediaFile, String> tcAlbum;
	@FXML
	private TableColumn<PLMediaFile, String> tcLocation;
	@FXML
	private RadioMenuItem pathRelative;
	@FXML
	private RadioMenuItem btRelative;
	@FXML
	private RadioMenuItem pathAbsolute;
	@FXML
	private RadioMenuItem btAbsolute;
	
	private File currentFile = null;
	
	private Stage mainStage;
	
	/**
	 * Opens AboutWindow as a dialog with information about the application
	 * 
	 * @throws IOException
	 */
	@FXML
	private void getAbout() throws IOException {
		Parent aboutWindow = FXMLLoader.load(getClass().getResource("AboutWindow.fxml"));

		Dialog<String> d = new Dialog<>();
		d.setTitle("About");
		d.setHeaderText("PLST Editor");
		d.setContentText(((TextArea)aboutWindow).getText());
		Stage stage = (Stage)d.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("application/images/icon.jpg"));
		stage.setOnCloseRequest(e -> stage.close());
		d.showAndWait();
		
	}
	
	//To be implemented
	/**
	 * Sets the default playlist file format for saving
	 * when the user sets it.
	 */
	@FXML
	private void setFomart() {
		//if file doesn't exists
			//create
		//get format from program and save to settings
	}
	
	//To be implemented
	/**
	 * Gets the default playlist file format for saving
	 */
	@FXML
	private void getFomart() {
		//if file exists
			//get patformath from file and assign to program
	}
		
	/**
	 * Sets currentFile(File) object for updating opened existing files
	 * 
	 * @param file
	 */
	private void setCurrentFile(File file) {
		currentFile = file;
		
		if(currentFile != null)
			mainStage.setTitle(mainStage.getTitle() + " - " + file.getAbsolutePath());
		else 
			mainStage.setTitle("PLST Editor - Untiled");
	}
	
	/**
	 * Unsaved file warnings:
	 * Gets the message and the warning parameters and creates/pops a warning
	 * for the user. If the user selects save the method returns true else false
	 * 
	 * @param message
	 * @param warning
	 * @return
	 */
	private boolean warningDialog(String message, String warning) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning Dialog");
		alert.setHeaderText(warning);
		alert.setContentText(message);		
		alert.getButtonTypes().clear();
		
		ButtonType cancel = new ButtonType("Close", ButtonData.CANCEL_CLOSE);
		ButtonType save = new ButtonType("Save", ButtonData.OK_DONE);
		
		alert.getButtonTypes().addAll(save, cancel);
		
		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.get().equals(save))
			return true;
		
		return false;
	}
	
	/**
	 * Creates a new file. If the playist(TableView) is not empty a warning to save the
	 * file is shown if save is selected saveFile() is selected.
	 * 
	 * The playlist items are clears whether save or close is selected
	 */
	@FXML
	private void createNew() {
		
		if(!playlist.getItems().isEmpty()) {
			
			boolean result = warningDialog("Would you like to save before you continue?", "Current file not saved");
			
			if(result) 				
				saveFile();
			
			playlist.getItems().clear();
		}
		setCurrentFile(null);
	}
	
	/**
	 * Opens a file. If the playist(TableView) is not empty a warning to save the
	 * file is shown if save is selected saveFile() is selected. If the file is not
	 * saved the playlist items are not cleared else it is cleared.
	 * 
	 * The file chooser appears for opening the supported playlist file
	 */
	@FXML
	private void openFile() {

		if(!playlist.getItems().isEmpty()) {
			
			boolean result = warningDialog("Would you like to save before you continue?", "Current file not saved");
			
			if(result) {			
				if(saveFile())
					playlist.getItems().clear();
				else
					return;
			}
			
		}
		
		FileChooser openFile = new FileChooser();
		openFile.setTitle("Open File");
		openFile.getExtensionFilters().addAll(
				new ExtensionFilter("All Files", "*.m3u", "*.m3u8", "*.wpl", "*.zpl", "*.xspf"),
				new ExtensionFilter("M3U Files", "*.m3u"),
				new ExtensionFilter("M3U8 Files", "*.m3u8"),
				new ExtensionFilter("WPL Files", "*.wpl", "*.zpl"),
				new ExtensionFilter("XSPF Files", "*.xspf")
		);
		File selectedFile = openFile.showOpenDialog(mainStage);
		if(selectedFile != null) {
			if(PlaylistFileCreator.isPlaylist(selectedFile)) {
				
				playlist.getItems().clear();
				
				if(currentFile == null) 
					setCurrentFile(selectedFile);
				
				playlist.getItems().addAll(PLMediaFile.readPLaylist(selectedFile));
			}
		}
	}
	
	/**
	 * Shuffles the playlist(TableView) items using the FXCollections shuffle method
	 */
	@FXML
	private void shufflePlaylist() {
		
		FXCollections.shuffle(playlist.getItems());
		
	}
	
	/**
	 * Updates the current file if exists or save the playlist items into
	 * a new supported playlist file
	 */
	@FXML
	private boolean saveFile() {
		//To be implemented
		//save file if exists
		//else
			//call save as()
		return saveFileAs();
	}
	
	/**
	 * Creates a new playlist file from the playlist(TableView) items
	 * using the file chooser created file
	 */
	@FXML
	private boolean saveFileAs() {
		
		if(playlist.getItems().isEmpty()) return false;
		
		//save dialog box
		FileChooser saveFile = new FileChooser();
		saveFile.setTitle("Save File");		
		saveFile.getExtensionFilters().addAll(
				new ExtensionFilter("M3U Files", "*.m3u")
				
				//To be implemented
//				,
//				new ExtensionFilter("M3U8 Files", "*.m3u8"),
//				new ExtensionFilter("WPL Files", "*.wpl"),
//				new ExtensionFilter("XSPF Files", "*.xspf"),
//				new ExtensionFilter("All Files", "*.*")
		);
		
		File savedFile = saveFile.showSaveDialog(mainStage);
		if(savedFile != null && !playlist.getItems().isEmpty()) {
			PlaylistFileCreator.createPlaylistFile(savedFile, playlist.getItems());
			return true;
		}	
		else
			return false;
	}
	
	/**
	 * Opens ConvertWindow using the convertFile
	 * passes null for default convertion format
	 * 
	 * @throws IOException
	 */
	@FXML
	private void getConvert() throws IOException {
		convertFile(null);
	}
	
	/**
	 * Takes an event for setting the convertion value and 
	 * opens a window with the option to drag a playlist file/use a file chooser
	 * to get the file.
	 * 
	 * The save window with the selected convertion format set and the name of the 
	 * passed file.
	 *  
	 * @param event
	 * @throws IOException
	 */
	@FXML
	private void convertFile(ActionEvent event) throws IOException {
		
		Stage convertStage = new Stage();
		Parent convertWindow = FXMLLoader.load(getClass()
				.getResource("ConvertWindow.fxml"), new ResourceBundle() {
			
					@Override
					protected Object handleGetObject(String format) {						
						
						if(event != null && format.equals("format")) {
							String name = ((MenuItem)event.getTarget()).getId();
							
							if(name.equals(Formats.formatM3U8.toString()))
								return Formats.formatM3U8.getFormatName();
							else if(name.equals(Formats.formatWPL.toString()))
								return Formats.formatWPL.getFormatName();
							else if(name.equals(Formats.formatXSPF.toString()))
								return Formats.formatXSPF.getFormatName();
								
						}
						else if(format.equals("stage")) {
							return convertStage;
						}
						
						return Formats.formatM3U.getFormatName(); //Default covert file;						
					}
					
					@Override
					public Enumeration<String> getKeys() {
						// TODO Auto-generated method stub
						return null;
					}
				});
		
		Scene convertScene = new Scene(convertWindow);		
				
		convertStage.setScene(convertScene);
		convertStage.setTitle("Convert playlist");
		convertStage.getIcons().add(new Image("bin/application/images/icon.jpg"));
		convertStage.setResizable(false);
		convertStage.initModality(Modality.APPLICATION_MODAL);
		convertStage.show();
	}
	
	/**
	 * Clears the TableView items if it is not empty
	 */
	@FXML
	private void clear() {
		if(!playlist.getItems().isEmpty())	{
			playlist.getItems().clear();
		}
	}
	
	/**
	 * Closes the  application
	 */
	@FXML
	private void close() {
		if(!mainStage.equals(null))	{
			mainStage.close();
		}
	}

	
	//To be implemented
	/**
	 * Get the user settings from a file if the file exist
	 * if the file doesn't exist the default settings are used
	 */
	@FXML
	private void getSettings() {
		
		//get data file
		//set format
		//set path
	}
	
	//To be implemented
	/**
	 * If the user changes the settings save the settings to settings file
	 * if the settings file doesn't exit create the file and write the settings
	 * to it for later retrieval
	 */
	@FXML
	private void setSettings() {
		
		//check if file exists
			//clear file
		//else
			//create file
		
		//write to file and save
		
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle rb) {
		
		mainStage = (Stage)rb.getObject("main");

		//To be implemented
		//If settings file exists get the settings
			//getSettings
		
		//TableView default open screen
//		ImageView iv = new ImageView("bin/application/images/background.jpg");
		Label placeholder = new Label("Drag and Drop Audio/Video/Playlist");
//		placeholder.setGraphic(iv);
//		placeholder.setContentDisplay(ContentDisplay.TOP);
		playlist.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		playlist.setPlaceholder(placeholder);
		
		//Paths event
		pathAbsolute.setSelected(true);
		btAbsolute.selectedProperty().bindBidirectional(pathAbsolute.selectedProperty());
		btRelative.selectedProperty().bindBidirectional(pathRelative.selectedProperty());
				
		//setCellValue for TableColums
		setCellValue(tcName, PLMediaFile::getFileNameProperty);		
		setCellValue(tcTitle, PLMediaFile::getTitleProperty);
		setCellValue(tcArtist, PLMediaFile::getArtistProperty);
		setCellValue(tcAlbum, PLMediaFile::getAlbumProperty);
		setCellValue(tcLocation, PLMediaFile::getFileLocationProperty);
		
		//To be implemented
		tcTitle.setVisible(false);
		tcArtist.setVisible(false);
		tcAlbum.setVisible(false);
		
		//Set application shortcuts and events for the TableView and TableRow
		setTableRowEvents();
		setTableViewEvents();
		setShortCuts();
		
	}
	
	/**
	 *  Keyboard short cuts for the application
	 */
	private void setShortCuts() {
		
		playlist.setOnKeyPressed(e -> {
			if(e.getCode().equals(KeyCode.DELETE) && 
					!playlist.getSelectionModel().getSelectedItems().isEmpty()) { //Remove selected item using the DELETE key
				
				playlist.getItems().removeAll(playlist.getSelectionModel().getSelectedItems());
			
			}
			else if(e.isControlDown()) {
				
				if(e.isShiftDown() && e.getCode().equals(KeyCode.S)) //Save as
					saveFileAs();
				else if(e.getCode().equals(KeyCode.S)) //Save
					saveFile();
				else if(e.getCode().equals(KeyCode.N)) //New file
					createNew();
				else if(e.getCode().equals(KeyCode.O)) //Open
					openFile();
				
				//To be implemented
//				else if(e.getCode().equals(KeyCode.V)) //Paste
//					System.out.println("Ctrl + " + e.getCode());
//				else if(e.getCode().equals(KeyCode.C)) //Copy
//					System.out.println("Ctrl + " + e.getCode());
//				else if(e.getCode().equals(KeyCode.X)) //Cut
//					System.out.println("Ctrl + " + e.getCode());
				
			}
			e.consume();
		});
		
		
	}
	
	/**
	 * Set the setCellValueFactory the passed TableColumn using the PLMediaFile
	 * method passed with the TableColumn
	 * 
	 * @param tc
	 * @param plMediaMethod
	 */
	private void setCellValue(TableColumn<PLMediaFile, String> tc, Function<PLMediaFile, ObservableValue<String>> plMediaMethod){
		
		TableColumn<PLMediaFile, String> col = tc;
		col.setCellValueFactory(cellData -> plMediaMethod.apply(cellData.getValue()));
		
	}
	
	/**
	 * Set the playlist's DragOver and DragDropped events to retrieve the media/playlist file(s)
	 * that are/is dropped on the TableView
	 */
	private void setTableViewEvents() {
		
		//Deselect item
		playlist.setOnMouseClicked(e -> {
			if(!playlist.getSelectionModel().isEmpty() && e.getClickCount() == 2) 
				playlist.getSelectionModel().clearSelection();
			e.consume();
		});
		
		//Set onDragOver for the playlist TableView
		playlist.setOnDragOver(draggedFile -> {
			
			if(draggedFile.getDragboard().hasFiles())
				draggedFile.acceptTransferModes(TransferMode.ANY);
			
			draggedFile.consume();
			
		});
		
		//Set onDragDropped for the playlist TableView
		playlist.setOnDragDropped(droppedFile -> {

			//List of files dropped on the tableView
			List<File> files = droppedFile.getDragboard().getFiles();
			
			//Add the media files to the table view playlist
			files.forEach(file -> {
						
				if(file.isDirectory()) {//get files inside a directory
					playlist.getItems().addAll(PLMediaFile.getFiles(file));
				}
				else {//get a file
					if(PlaylistFileCreator.fileSupport(file)) {
						if(!PlaylistFileCreator.isPlaylist(file))
							playlist.getItems().add(new PLMediaFile(file.getPath()));
						else {
							
							if(currentFile == null && files.size() == 1) 
								setCurrentFile(file);
							
							playlist.getItems().addAll(PLMediaFile.readPLaylist(file));
						}
					}
				}	
				
			});

			droppedFile.consume();
			
		});
	}
	
	/**
	 * Set the playlist's (TableView) TableRow's DragDetected, DragOver and DragDropped 
	 * for ordering the files inside the TableView
	 * 
	 * Contains	: Stack Overflow solution:
	 * URL		: https://stackoverflow.com/questions/28603224/sort-tableview-with-drag-and-drop-rows
	 */
	private void setTableRowEvents() {
		
		ArrayList<PLMediaFile> selected = new ArrayList<>(); //Selected TableRows
		DataFormat df = new DataFormat("application/x-java-serialized-object"); //Identifies drag and drop objects for sorting 
		
		playlist.setRowFactory(event -> {
			
			TableRow<PLMediaFile> row = new TableRow<>(); //creates new row

			//Context menu for the playlist files
			ContextMenu menu = new ContextMenu();	
			MenuItem remove = new MenuItem("Remove");
			menu.getItems().add(remove);
			
			//To be implemented
//			MenuItem copy = new MenuItem("Copy");
//			MenuItem cut = new MenuItem("Cut");
//			MenuItem paste = new MenuItem("Paste");
//			menu.getItems().addAll(cut, copy, paste, remove);
			
			//Remove currently selected object/playlist file
			remove.setOnAction(removeClicked -> {
				playlist.getItems().remove(row.getItem());
			});
			
			row.setContextMenu(menu); //Assign context menu to the row
			
			//Get context menu when the right mouse button is clicked
			row.setOnMouseClicked(e -> {
				
				//Show dialog box if the right mouse button is clicked
				if(e.getButton() == MouseButton.SECONDARY) {
					
					row.getContextMenu();
				}
				
				e.consume();
			});
			
			//Set OnDragDetected for TableRow
			row.setOnDragDetected(detectedDrag -> {
				
				if(!row.isEmpty()) {
					
					Integer index = row.getIndex();
					
					selected.clear(); //Selected items Clear Collection List	
					
					ObservableList<PLMediaFile> items = playlist.getSelectionModel().getSelectedItems();
					
					 //Add Selected items on the TableView into the Collection List	
					items.forEach(mediaFile -> selected.add(mediaFile));
					
					Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
					db.setDragView(row.snapshot(null, null));					
					ClipboardContent cc = new ClipboardContent();
					cc.put(df, index);
					db.setContent(cc);
					
					detectedDrag.consume();
				}
				
			});
			
			//Set OnDragOver for TableRow
			row.setOnDragOver(dragOver -> {
				
				Dragboard db = dragOver.getDragboard();
				
				/**
				 * Checks if the dragged and dropped item if from the application or directory
				 * if it is from the application the event is consumed else it is not from the 
				 * application it is ignored and the event is left to go up the hierarchy and 
				 * is handled by the TableView setOnDragDropped event.
				 */
				if(db.hasContent(df)) {
					
					if(row.getIndex() != ((Integer)db.getContent(df)).intValue()) {
						dragOver.acceptTransferModes(TransferMode.COPY_OR_MOVE);
					}
					
					dragOver.consume(); //IMPORTANT: Consume if contains df(DataFile) else leave to travel up the hierarchy 
				}
					
			});
			
			//Set OnDragDropped for TableRow
			row.setOnDragDropped(droppedDrag -> {
									
				Dragboard db =  droppedDrag.getDragboard();
				
				/**
				 * Checks if the dragged and dropped item if from the application or directory
				 * if it is from the application the event is consumed else it is not from the 
				 * application it is ignored and the event is left to go up the hierarchy and 
				 * is handled by the TableView setOnDragDropped event.
				 */
				if(db.hasContent(df)) {
					
					int dropIndex;
					int count = 0;
					PLMediaFile plmf = null;
					
					if(row.isEmpty()) {
						dropIndex = playlist.getItems().size();
					}
					else {
						dropIndex = row.getIndex();
						plmf = playlist.getItems().get(dropIndex);
					}
											
					if(!plmf.equals(null)) {
						
						while (selected.contains(plmf)) {							
							count = 1;
							--dropIndex;
							
							//Base case: end loop if index is less than 0s
							if (dropIndex < 0) {
								plmf = null;
								dropIndex = 0;
								break;
							}
							
							plmf = playlist.getItems().get(dropIndex);
						}
						
					}
					
					//Remove selected items from the TableView
					selected.forEach(mediaFile -> playlist.getItems().remove(mediaFile));
					
					
					if(!plmf.equals(null))
						dropIndex = playlist.getItems().indexOf(plmf) + count;
					else if(dropIndex != 0)
						dropIndex = playlist.getItems().size();
					
					//Clear selected items on the TableView
					playlist.getSelectionModel().clearSelection();
					
					//Re-add items in the Collection List into the TableView using new index locations
					for(PLMediaFile mediaFile : selected) {
						playlist.getItems().add(dropIndex, mediaFile);
						playlist.getSelectionModel().select(dropIndex);
						dropIndex++;
					}
					
					droppedDrag.setDropCompleted(true);
					selected.clear(); //Selected items Clear Collection List
					
					droppedDrag.consume(); //IMPORTANT: Consume if contains df(DataFile) else leave to travel up the hierarchy 
				}			
			
			});
			
			return row;
			
		});
		
	}
}
