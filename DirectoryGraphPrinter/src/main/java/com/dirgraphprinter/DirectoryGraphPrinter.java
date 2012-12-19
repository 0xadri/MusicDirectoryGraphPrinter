package com.dirgraphprinter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DirectoryGraphPrinter {

	private List<String> fileExtensionsToSkip = new ArrayList<String>();
	private Comparator<File> customComparator = new FileComparator<File>();
	private String currentWorkingDirPath = null;
	private String currentWorkingDirName = null;
	private StringBuilder indentation = new StringBuilder();
	
	private void initFileExtensionsToSkip() {
		fileExtensionsToSkip.add(".exe");

		fileExtensionsToSkip.add(".ini");
		fileExtensionsToSkip.add(".db");

		fileExtensionsToSkip.add(".jpg");
		fileExtensionsToSkip.add(".jpeg");
		fileExtensionsToSkip.add(".gif");
		
		fileExtensionsToSkip.add(".mov");
		fileExtensionsToSkip.add(".avi");
		fileExtensionsToSkip.add(".mkv");

		fileExtensionsToSkip.add(".txt");
		fileExtensionsToSkip.add(".nfo");
		fileExtensionsToSkip.add(".doc");
		fileExtensionsToSkip.add(".docx");
	}
	
	private void initCurrentWorkingDirFields(){
		URL mainMethodPath = DirectoryGraphPrinter.class.getProtectionDomain().getCodeSource().getLocation();
		//String currentWorkingDirPath = "C:\\dir\\anotherDir\\AndSoOn\\AnSoOn";
		this.currentWorkingDirPath = mainMethodPath.getFile().substring(1, mainMethodPath.getFile().lastIndexOf("/") );
		this.currentWorkingDirName = currentWorkingDirPath.substring(currentWorkingDirPath.lastIndexOf("/") + 1 );
		
        System.out.println( "\n" + "current directory is : " + currentWorkingDirPath );
        System.out.println( "\n" + "root directory name is : " + currentWorkingDirName );
	}
	
	public void start() throws IOException {
		
		this.initFileExtensionsToSkip();
		
		this.initCurrentWorkingDirFields();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		File outputFile = new File(currentWorkingDirPath + "\\directorygraph-" + dateFormat.format(new Date()) + ".txt");
		
		BufferedWriter outBw = null;
		
		outputFile.createNewFile();
		
		try {
			outBw = new BufferedWriter(new FileWriter(outputFile), 32768);
			getPrivateDirectoryContent(currentWorkingDirPath, outBw);
			outBw.flush();
		} 
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		finally{
			if (outBw!=null){
				try {
					outBw.close();
				} catch (IOException e) {
					// just ignore it
				}
			}
		}
		
		System.out.println("\n" + "Music directory exctraction completed, see " + outputFile.getPath() );
	}
	
	
	private void getPrivateDirectoryContent(String filePath, BufferedWriter outBw ) throws IOException {
		
		File currentDirOrFile = new File(filePath);
		
		if ( !currentDirOrFile.exists() ){
			System.out.println(indentation + "Does not exist");
			return;
		}
		else if ( currentDirOrFile.isFile() ){
			
			if ( isFileTypeAccepted(currentDirOrFile) ){
				// System.out.println(indentation + formatFileName(currentDirOrFile.getName()) );
				outBw.write(indentation + removeExtensionFromFileName(currentDirOrFile.getName()) );
				outBw.newLine();
			}
			return;
		}
		else{
			//System.out.println("\n" + indentation + currentDirOrFile.getPath().substring(currentDirOrFile.getPath().indexOf(currentWorkingDirName)).replaceAll(currentWorkingDirName, "ROOT-DIR").toUpperCase() + "\n");
			outBw.newLine();
			outBw.write(indentation + currentDirOrFile.getPath().substring(currentDirOrFile.getPath().indexOf(currentWorkingDirName)).replaceAll(currentWorkingDirName, "ROOT-DIR").toUpperCase());
			outBw.newLine();
			
			indentation.append(".  ");
			
			List<File> fileList = Arrays.asList(currentDirOrFile.listFiles());
			Collections.sort(fileList, customComparator);
			
			for ( File currentFileOrDir : fileList ){
				//System.out.println(currentDirOrFile + "\\" + currentFileOrDirName);
				getPrivateDirectoryContent(currentDirOrFile + "\\" + currentFileOrDir.getName(), outBw);
			}
			
			if (indentation.length() - 3 > 3 ){
				indentation.delete(indentation.length() - 3, indentation.length());
			}
		}		
	}

	
	private String removeExtensionFromFileName(String fileName) {
		return fileName.substring(0, fileName.length() - 4);
	}

	private boolean isFileTypeAccepted(File currentDirOrFile) {
		for (String extension : fileExtensionsToSkip){
			if ( currentDirOrFile.getName().toLowerCase().endsWith(extension) )
				return false;
		}
		return true;
	}

}
