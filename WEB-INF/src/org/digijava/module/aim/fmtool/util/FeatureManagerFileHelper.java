package org.digijava.module.aim.fmtool.util;

import java.io.*;
import java.util.*;

public class FeatureManagerFileHelper {

	public List<File> getJspFilesFromDirectory(File dir){
		List<File> retValue = new ArrayList<File>();
		for (File file : getFilesFromDirectory(dir,false)) {
			if (file.getName().toLowerCase().trim().endsWith(".jsp")){
				retValue.add(file);
			}
		}
		return retValue;
	}

	public List<File> getDirectoriesFromDirectory(File dir){
		return getFilesFromDirectory(dir,true);
	}
	
	
	public List<File> getFilesFromDirectory(File dir, boolean directory){
		List<File> retValue = new ArrayList<File>();
		if (dir != null && dir.isDirectory()){
			for (File file : dir.listFiles()) {
				if (directory)
					if (file.isDirectory())
						retValue.add(file);
					else
						retValue.add(file);

			}
		}
		return retValue;
	}
}
