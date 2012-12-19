package com.dirgraphprinter;

import java.io.File;
import java.util.Comparator;

public class FileComparator<T> implements Comparator<File> {
	
	public int compare(File fileOne, File fileTwo) {
		if (fileOne.isFile() && fileTwo.isFile())
			return fileOne.getName().compareTo(fileTwo.getName() );
		else if (!fileOne.isFile() && !fileTwo.isFile())
			return fileOne.getName().compareTo(fileTwo.getName() );
		else if ( fileOne.isFile() )
			return -1;
		else
			return 1;
	}

}
