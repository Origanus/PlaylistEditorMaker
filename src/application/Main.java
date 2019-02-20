/**
 * @author	: Origanus Ramfate 
 * @date	: February 2019
 * Parpose	: This project explores playlists and converting between playlists
 * 			  1 playlist format is used for saving "m3u" but other playlists 
 * 			  can be converted or opened on this application and they are
 * 			  xspf, wpl, zpl and m3u8.
 * 
 *  		  Drag and drop can be used to add files into the list of media files
 *   		  for creating a playlist.
 *   
 *   		  To be implemeted ideas:
 *   			+ saving playlists in xspf, wpl, zpl and m3u8.
 *   			+ copy, cut and past
 *   			+ adding supported files
 *   			+ saving setings
 *   			+ detect if file is change and requer file save
 *   			+ ID3 Tag edit
 *   			+ display album, artist and song title
 *   			+ Encoding playlist files
 * 
 */

package application;
	
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;


public class Main extends Application {
	
	@Override
	public void start(Stage stage) {
		try {
//			BorderPane root = new BorderPane();
			
			Parent root = FXMLLoader.load(getClass()
					.getResource("MainWindow.fxml"), new ResourceBundle() {//ResourceBundles
				
						@Override
						protected Object handleGetObject(String objName) {
							if(objName == "main")
								return (Object)(stage);
							return null;
						}
						
						@Override
						public Enumeration<String> getKeys() {
							return null;
						}
					});
			Scene scene = new Scene(root,585, 710);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.setTitle("PLST Editor");
//			stage.getIcons().add(new Image("application/images/icon.jpg"));
			stage.setResizable(false);
			stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
