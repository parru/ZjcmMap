package com.map.core.sdcard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import android.os.Environment;
import android.util.Log;

public class FileManager {
	public String RAM_PATH = "/data";
	public String SD_PATH = "mnt/sdcard";
	public String PACKAGE = "com.cucmap";
	public String XML_DIR = "xml";
	public String IMAGE_DIR = "images";
	
	public FileManager(){
		Environment.getDataDirectory().getAbsolutePath();
		RAM_PATH += "/data"+ File.separator + PACKAGE + File.separator;
	}
	
	public InputStream readFileFromRAM(String path, String filename){
		InputStream input = null;
		String uri = RAM_PATH + path + File.separator + filename;
		Log.i("URI-->", uri);
		try {
			input = new FileInputStream(uri);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return input;
	}
}
