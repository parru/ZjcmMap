package com.cucmap.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;
  
/*
 * 
 */
public class HttpDownLoader {  
    private static URL url = null;
    
    /** 
     * 根据URL得到输入流       
     *                       
     * @param urlStr         网络文件路径
     * @return inputStream   将网络问价转化成输入流
     * @throws java.net.MalformedURLException
     * @throws java.io.IOException
     */  
    public static InputStream getInputStreamFromUrl(String urlStr)  
            throws MalformedURLException, IOException {
        url = new URL(urlStr);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();  
        urlConn.setReadTimeout(5*1000);
        urlConn.setRequestMethod("GET");
        InputStream inputStream = urlConn.getInputStream();
        return inputStream;
    }
    
    /** 
     * 将路径为urlStr的文件下载到path目录中，命名为filaName
     * @param urlStr      网络文件的路径
     * @param path        网络文件保存到本地的路径
     * @return flag       网络文件的下载结果，-1：代表下载文件出错， 0：代表下载文件成功 
     */  
    public static int downloadFileToRom(String urlStr, String path, String fileName) {  
        InputStream inputStream = null;  
        try {  
            FileUtils fileUtils = new FileUtils();
            inputStream = getInputStreamFromUrl(urlStr);
            
            File resultFile = fileUtils.writeToRom(path, fileName, inputStream);  
            if (resultFile == null) {  
                return -1;  
            }    
        } catch (Exception e) {  
            if(inputStream == null){
            	Log.i("NULLInputStream-->", urlStr);
            }
            e.printStackTrace();  
            return -1;  
        }   
        return 0;  
    }
}  