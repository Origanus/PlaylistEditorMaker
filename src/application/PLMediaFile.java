/**
 * @author	: Origanus Ramfate 
 * @date	: February 2019
 * Parpose	: This class contains is an object used as a media file, it contains all the 
 * 			  necessary information needed to create a playlist file.
 * 
 * 			  It also contains static methods for retrieving media files from a directory
 * 			  and a playlist file.
 * 
 *  		  To instantiate this class a File or String(absolute path) is passed.
 * 
 */

package application;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PLMediaFile {
	
	//Media file variables
	private final File mediaFile;
	private final StringProperty fileName = new SimpleStringProperty(this, "fileName");
	private final StringProperty title = new SimpleStringProperty(this, "title");
	private final StringProperty artist = new SimpleStringProperty(this, "artist");
	private final StringProperty album = new SimpleStringProperty(this, "album");
	private final StringProperty location = new SimpleStringProperty(this, "location");
	
	public PLMediaFile(String string) {
		this(new File(string));
	}
	
	private PLMediaFile(File file) {
		
		mediaFile = file;
		
		//Set the variables if the file exists else the variable  values are null
		if(exists()) {
			this.fileName.set(mediaFile.getName());
			
			//To be implemented
			//Get the metatags using ID3
//			this.title.set();
//			this.artist.set();
//			this.album.set();
			
			this.location.set(mediaFile.getAbsolutePath());
		}
		
	}
	
	/**
	 * Checks if the passed file exists and returns a boolean value
	 * 
	 * @return
	 */
	public boolean exists() {
		return mediaFile.exists();
	}
	
	/**
	 * Returns the media title as a StringProperty
	 * 
	 * @return
	 */
	public StringProperty getTitleProperty() {
		return title;
	}
	
	/**
	 * Returns the media title as a String
	 * 
	 * @return
	 */
	public String getTitle() {
		return title.get();
	}
	
	/**
	 * Returns the media artist name(songs) as a StringProperty
	 * 
	 * @return
	 */
	public StringProperty getArtistProperty() {
		return artist;
	}
	
	/**
	 * Returns the media artist name(songs) as a String
	 * 
	 * @return
	 */
	public String getArtist() {
		return artist.get();
	}
	
	/**
	 * Returns the media album name(songs) as a StringProperty
	 * 
	 * @return
	 */
	public StringProperty getAlbumProperty() {
		return album;
	}
	
	/**
	 * Returns the media album name(songs) as a String
	 * 
	 * @return
	 */
	public String getAlbum() {
		return album.get();
	}
	

	/**
	 * Returns the media absolute path as a StringProperty
	 * 
	 * @return
	 */
	public StringProperty getFileLocationProperty() {
		return location;
	}
	
	/**
	 * Returns the media absolute path as a String
	 * 
	 * @return
	 */
	public String getFileLocation() {
		return location.get();
	}
	
	/**
	 * Returns the media name as a StringProperty
	 * 
	 * @return
	 */
	public StringProperty getFileNameProperty() {
		return fileName;
	}
	
	/**
	 * Returns the media name as a String
	 * 
	 * @return
	 */
	public String getFileName() {
		return fileName.get();
	}
	
	
	/**
	 * Goes through the passed list of files adds the files to the temporary List
	 * if it is supported and is not a playlist. If the file is a playlist it is 
	 * passed to the readPlaylist(File) method and the returned list is added to
	 * the temporary List.
	 * 
	 * If none of the files is supported or is a playlist an empty List is returned.
	 * 
	 * @param files
	 * @return
	 */
	private static List<PLMediaFile> getMediaFiles(List<File> files) {
		
		List<PLMediaFile> temp = new ArrayList<>();
		
		for(File file : files) {
			
			//Checks if it is a file
			if(file.isFile()/* && file.exists()*/) {
				
				if(PlaylistFileCreator.fileSupport(file) && !PlaylistFileCreator.isPlaylist(file))
					temp.add(new PLMediaFile(file));
				else if(PlaylistFileCreator.isPlaylist(file))
					temp.addAll(readPLaylist(file));
				
			}	
		}
		
		return temp;
	}
	
	/**
	 * If the passed file is not a directory null is returned.
	 * 
	 * A temporary List is created and assigned the result of getMediaFiles,
	 * the directories list of files are passed to getMediaFile.
	 * 
	 * The directory is then searched for other directories if a directory is
	 * found it is passed to getFiles. If the results in not empty/null it is 
	 * added to the temporary List.
	 * 
	 * @param directory
	 * @return
	 * @throws NullPointerException
	 */
	public static List<PLMediaFile> getFiles(File directory) {
		
		if(directory.isDirectory()/* && directory.length() > 0*/) {
			
			List<PLMediaFile> temp = PLMediaFile.getMediaFiles(
					Arrays.asList(directory.listFiles()));

			//Iterates the directory String[] list for directories
			for(String string: directory.list()) {
				
				//Creates a file using the file name String
				File tpFile = new File(directory.getPath() + "\\" + string);
				
				if(tpFile.isDirectory()) {
					
					List<PLMediaFile> rec = getFiles(tpFile);
					
					if(!rec.equals(null)) {
						temp.addAll(rec);
					}
					
				}
				
			}				
			return temp;
		}
		return null;
	}
	
	/**
	 * Reads a playlist file and returns a List of media files.
	 * 
	 * Passes the playlist file to the PlaylistFileCreator.readPlaylistFile(File)
	 * and returns the results of PlaylistFileCreator.readPlaylistFile(File) as is.
	 * 
	 * @param playlist
	 * @return
	 */
	public static List<PLMediaFile> readPLaylist(File playlist) {
		return PlaylistFileCreator.readPlaylistFile(playlist);
	}
	
	@Override
	public String toString() {
		return "File: " + this.getFileName() + " Directory: " + this.mediaFile.getPath();
	}
	
}
