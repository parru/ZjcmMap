package com.cucmap.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;
import android.util.Log;

class FileUtils {
    private String PACKAGE_NAME = "com.cucmap";
    private String ROM_PATH = "/data";
    
    public FileUtils(){
    	ROM_PATH += Environment.getDataDirectory().getAbsolutePath();
    	ROM_PATH += File.separator + PACKAGE_NAME;
    }
    
    /** 
     * 创建文件 
     * @throws java.io.IOException
     */  
    public File createFile(String path, String fileName) throws IOException {  
        File file = new File(path + File.separator + fileName);  
        file.createNewFile();
        return file;  
    }  
      
    /** 
     * 创建目录   
     * @param dirName 
     */  
    public File createDir(String dirName) {  
        File dir = new File(dirName);  
        dir.mkdir();  
        return dir;  
    }  
  
    /** 
     * 判断文件是否存在
     * @param path     文件所在文件夹的路径
     * @param fileName 文件名
     * @return flag    文件是否存在的标志
     */  
    public boolean isFileExist(String path, String fileName){  
        boolean flag = false;
    	File file = new File(path + File.separator + fileName);  
        flag = file.exists();
        return flag;
    }  
      
    /** 
     * 将一个InputStream流存储到手机内部存储卡
     * @param path     存储文件的路径
     * @param fileName 存储的文件名
     * @param input    文件输入流
     * @return file    新生成的file
     */  
    public File writeToRom(String path, String fileName, InputStream input){
    	String system_path = ROM_PATH + File.separator + path;
    	
    	if(! new File(system_path).exists()){
    		new File(system_path).mkdir();
    	}
          
        FileOutputStream fout = null;
        try{
        	
        	fout = new FileOutputStream(system_path + File.separator + fileName);
        	byte buffer [] = new byte[1024];
        	int length = 0;
            while((length = input.read(buffer)) != -1){  
                fout.write(buffer, 0, length);  
            }
            fout.flush();  
        } catch(Exception e){  
            e.printStackTrace();  
        } finally{  
            try{  
                fout.close();  
            }  
            catch(Exception e){  
                e.printStackTrace();  
            }  
        }  
        if(!new File(system_path + File.separator + fileName).exists()){
            return null;
        }
        return new File(system_path + File.separator + fileName);
    } 
}  
