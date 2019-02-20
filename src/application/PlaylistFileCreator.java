/**
 * @author	: Origanus Ramfate 
 * @date	: February 2019
 * Parpose	: This class contains static methods for dealing with the playlist file
 * 			  like reading and writing playlist files. It also contain the supported 
 * 			  playlist files and playlist formats. It can be used to verifty if objects
 * 			  are supported and if a file is a playlist.
 * 
 * 			  This class can not be instantiated.
 * 
 */

package application;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javafx.collections.ObservableList;

public class PlaylistFileCreator {
	
	private static Set<String> supportedFormats = new HashSet<>();
	
	private PlaylistFileCreator() {};
	
	/**
	 * Creates a new file from the passed string and pass it to the fileSupport
	 * and returns the results of fileSupport
	 * 
	 * @param file
	 * @return
	 */
	public static boolean fileSupport(final String file) {
		
		if(file.isEmpty()) 
			return false;
		return fileSupport(new File(file));
		
	}
	
	/**
	 * Checks the if the passed file is in the supportedFormats Set
	 * if the Set contains the file format true is returned else false.
	 * 
	 * Default values are added to the Set(supportedFormats) before the
	 * value can be compared with the default supported formats.
	 * 
	 * fileExtension(File file) is used to get the file format of the passed 
	 * file to compare with the items in the supportedFormats Set. 
	 * 
	 * @param file
	 * @return
	 */
	public static boolean fileSupport(final File file) {
		
		//Adds values to the supportedFormats set
		//Videos
		supportedFormats.add("webm");
		supportedFormats.add("mp4");
		supportedFormats.add("mp4v");
		supportedFormats.add("mov");
		supportedFormats.add("mkv");
		supportedFormats.add("avi");
		
		//Audios
		supportedFormats.add("flac");
		supportedFormats.add("mp3");
		supportedFormats.add("m4a");
		supportedFormats.add("wav");
		
		//Playlists
		supportedFormats.add("xspf");
		supportedFormats.add("wpl");
		supportedFormats.add("zpl");
		supportedFormats.add("m3u");
		supportedFormats.add("m3u8");
		
		return supportedFormats.contains(fileExtension(file));
	}
	
	/**
	 * Extracts the file extension from the passed file and
	 * returns the extension as a String
	 * 
	 * @param file
	 * @return
	 */
	private static String fileExtension(final File file) {
		
		return (file.getName().substring(
				file.getName().lastIndexOf('.') + 1
			)).toLowerCase();
		
	}

	/**
	 * Uses fileExtension(File) to retrieve the passed file's extension.
	 * If the extension matches any of the playlist type cases true is
	 * returned else false
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isPlaylist(final File file) {
		
		switch (fileExtension(file)) {
			case "xspf":
			case "wpl":
			case "zpl":
			case "m3u":
			case "m3u8":
				return true;	
			default:
				return false;
		}
		
	}
	
	/**
	 * Create a M3U8 content using createM3u(ObservableList).
	 * 
	 * A string object with the playlist text is returned that is 
	 * returned to be written to a playlist file. 
	 * 
	 * @param list
	 * @return
	 */
	private static String createM3u8(ObservableList<PLMediaFile> list) {
		//Create M3U8 file
		return createM3u(list);	
	}
	
	/**
	 * Create a M3U content using a String that is returned to be wrote 
	 * to a playlist file.
	 * 
	 * @param list
	 * @return
	 */
	private static String createM3u(ObservableList<PLMediaFile> list) {
		
		String space = "\r\n"; //new line break
		
		if(!list.isEmpty()) {
			//Create M3U file
			String playlist = "#EXTM3U"; //Header
			
			//Creates three lines for every song/media file
			for(PLMediaFile file : list) {
				
				if(!file.equals(null)) {
					playlist += space + "#EXTINF:0," + file.getFileName(); //Song information - To add artist and song title
					playlist += space + file.getFileLocation();	//File location
					playlist += space + space; //Create new line
				}
				
			}
			
			return playlist; //returns M3U content
		}
		return null;
	}
	
	
	//To be implemented
	/**
	 * Create a Wpl content using a String that is returned to be wrote 
	 * to a playlist file.
	 * 
	 * @param list
	 * @return
	 */
	private static String createWpl(ObservableList<PLMediaFile> list) {
		
		String space = "\r\n"; //new line break
		
		//<?zpl version="2.0"?>/<?wpl version="1.0"?>
		//<smil>
			//create header			
			//<head>
		    	//<guid>{}</guid>
		    	//<meta name="totalDuration" content="" />
		    	//<meta name="itemCount" content="" />
		    	//<meta name="generator" content="" />
		    	//<title></title>
			//</head>
			//open body
			//<body>
				//<seq>
					//<media src="" albumTitle="" albumArtist="" trackTitle="" trackArtist="" duration="" />
					//Version 1 - requires tid="{}"  for every media
				//</seq>
			//close body
		  	//</body>
		//</smil>

		return null;
	}

	//To be implemented : contains test code
	/**
	 * Create a xspf content using a String that is returned to be wrote 
	 * to a playlist file.
	 * 
	 * @param list
	 * @return
	 */
	private static String createXspf(ObservableList<PLMediaFile> list) {
		
		String space = "\r\n"; //new line break
		
		if(!list.isEmpty()) {
		//Create xspf file
		//Xspf header
		String playlist = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + space +
				"<playlist xmlns=\"http://xspf.org/ns/0/\" version=\"1\">" + space +
				"<title>PLST Editor<title>" + space +
				"<trackList>" + space;
		
		int id = 0;
		for(PLMediaFile file : list) {
			
			if(!file.equals(null)) {
				playlist += "<track>" + space +
						"<location>file:///" + file.getFileLocation() + "</location>" + space +
						"<title>" + 
							file.getFileName().substring(0, file.getFileName().lastIndexOf(".") - 1) + 
						"</title>" + space +
						"<extension application=\"http://www.videolan.org/vlc/playlist/0\">" + space +
							"<vlc:id>" + id++ + "</vlc:id>" + space +
						"</extension>" + space +
						"</track>" + space ;
			}
			
		}
		
		playlist += "<extension application=\"http://www.videolan.org/vlc/playlist/0\">" + space;
		
		for (int i = 0; i <= id; i++) {
			playlist += "<vlc:item tid=\"" + i + "\"/>";
		}
		
		playlist += "</extension>" + space +
				"</trackList>" + space + 
				"</playlist>";
		
		return playlist;
	}
	return null;
	}
	
	/**
	 * Use fileExtension(File) to get the passed file extension if it matches one of
	 * the supported file formats a playlist content of that format is create and
	 * is passed into the write writeFile(String, String) to write to the passed
	 * file.
	 * 
	 * The passed file is used to verify the format and retrieve the file's absolute path
	 * and the ObservableList(list) is passed/used to create playlist content.
	 * 
	 * If the file create successfully true is returned else false is returned, false
	 * is returned if the file passed is not a supported playlist.
	 * 
	 * @param file
	 * @param list
	 * @return
	 */
	public static boolean createPlaylistFile(File file, ObservableList<PLMediaFile> list) {
		
		//writes the playlist content into the passed file
		switch (fileExtension(file)) {
			case "m3u":
				return writeFile(createM3u(list), file.getAbsolutePath());
			case "m3u8":
				return writeFile(createM3u8(list), file.getAbsolutePath());
			case "wpl":
				return writeFile(createWpl(list), file.getAbsolutePath());
			case "xspf":
				return writeFile(createXspf(list), file.getAbsolutePath());
		}
		
		return false;	
	}
	
	/**
	 * Uses PrintWriter to write playlist content(fileData) to the playlist file(absolutePath)
	 * a success(boolean) is false and if there is an exception it will be returned else true is returned
	 * 
	 * @param fileData
	 * @param absolutePath
	 * @return
	 */
	private static boolean writeFile(String fileData, String absolutePath) {

		boolean success = false;
		try(
				java.io.PrintWriter newPlayList = 
				new java.io.PrintWriter(absolutePath);
		) {
			
			//Writes playlist content to the playlist file
			newPlayList.write(fileData);
			success = true;
			
		} catch (IOException e) {}
		
		return success;
	}

	/**
	 * Temporary List is created to store media file locations as a PLMediaFile
	 * a Scanner is used to read the passed file according to the passed extension
	 * 
	 * If the extension is supported a List of read file content is returned else
	 * an empty List is returned. If the file is empty and empty List is returned.
	 * 
	 * @param file
	 * @param ext
	 * @return
	 */
	private static List<PLMediaFile> playlistFileReader(File file, String ext) {
		
		List<PLMediaFile> temp = new ArrayList<>();
		
		try (
			Scanner input = new Scanner(file)
		){
			//Checks if the passed extension is supported
			//If it is supported a while loop is used to read the file
			if(Formats.formatM3U.getFormatName().toLowerCase().equals(ext) || Formats.formatM3U8.getFormatName().toLowerCase().equals(ext)) {
				
				//Read M3U or M3U8
				while(input.hasNext()) {
					String line = input.nextLine().trim();
					
					if(!line.equals("") && !line.toUpperCase().contains("#")) {
						
						PLMediaFile plmTemp = new PLMediaFile(line);
						
						//Saves the media file if it is a path
						if(!plmTemp.equals(null) && plmTemp.exists())
							temp.add(new PLMediaFile(line));
						
					}
				}
				
			}
			else if(Formats.formatWPL.getFormatName().toLowerCase().equals(ext)) {
				
				//Read WPL
				while(input.hasNext()) {					
					String line = input.nextLine().trim();
					
					if(!line.equals("") && line.toUpperCase().contains("MEDIA")) {
						
						int start = line.indexOf("\"");
						line = line.substring(start + 1);
						int end = line.indexOf("\"");
						line = line.substring(0, end);

						PLMediaFile plmTemp = new PLMediaFile(line);
						
						//Saves the media file if it is a path
						if(!plmTemp.equals(null) && plmTemp.exists())
							temp.add(new PLMediaFile(line));
						
					}
				}
				
			}
			else if(Formats.formatXSPF.getFormatName().toLowerCase().equals(ext)) {
				
				//Read XSPF
				while(input.hasNext()) {
					String line = input.nextLine().trim();
					
					if(!line.equals("") && line.toUpperCase().contains("<LOCATION>")) {
						
						int start = line.indexOf("file:///");
						line = line.substring(start + "file:///".length());
						int end = line.indexOf("</location>");
						line = line.substring(0, end);	
						
						PLMediaFile plmTemp = new PLMediaFile(line);
						
						//Saves the media file if it is a path
						if(!plmTemp.equals(null) && plmTemp.exists())
							temp.add(new PLMediaFile(line));
						
					}
				}				
			}				
		} 
		catch (Exception e) {}
		
		return temp;
	}
	
	/**
	 * Temporary List is used to store and return the result for reading the passed file.
	 * fileExtension(File) is used to retrieve the file's extension that is passed to the
	 * playlistFileReader(File, String). The results are stored to the temporary List.
	 * 
	 * null/empty List is returned if the file is not supported
	 * 
	 * @param file
	 * @return
	 */
	public static List<PLMediaFile> readPlaylistFile(File file) {
	
		List<PLMediaFile> temp = null;
		
		String ext = fileExtension(file);
		
		if(Formats.formatM3U.getFormatName().toLowerCase().equals(ext) || Formats.formatM3U8.getFormatName().toLowerCase().equals(ext)) {
			temp = playlistFileReader(file, ext);
		}
		else if(Formats.formatWPL.getFormatName().toLowerCase().equals(ext) || ext.equals("zpl")) {
			ext = "wpl";
			temp = playlistFileReader(file, ext);
		}
		else if(Formats.formatXSPF.getFormatName().toLowerCase().equals(ext)) {
			temp = playlistFileReader(file, ext);
		}	
				
		return temp;
	}
}

/**
 * Playlist formats for easy access.
 * 
 * getFormatName() returns a String format name
 *
 */
enum Formats {
	formatM3U("M3U"), 	formatM3U8("M3U8"),
	formatWPL("WPL"), 	formatXSPF("XSPF");
	
	private String formatName;
	
	private Formats(String formatName) {
		this.formatName = formatName;
	}
	
	public String getFormatName() {
		return formatName;
	}
}
