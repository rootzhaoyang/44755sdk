package com.example.progressbar;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public  class DownloadHelper {  
    private URL url = null;  
      
    public String download(String newUrl){  
        StringBuffer sb = new StringBuffer();  
        String line = null;  
        BufferedReader br = null;  
          
        //����һ��url����   
        try {  
            url = new URL(newUrl);  
            //����һ��http����   
            HttpURLConnection urlConn  = (HttpURLConnection)url.openConnection(); 
           
            br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));  
            while((line = br.readLine())!=null){  
                sb.append(line);  
                System.out.println(sb.toString());  
            }  
              
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally{  
            try{  
                br.close();  
            }catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return sb.toString();  
    }  
      
    //����-1�����ļ���������0���سɹ�������1�ļ��Ѿ�����   
    public int downFile(String urlStr,String path,String fileName){  
        try {  
            InputStream is = null;  
            FileUtils fileUtils = new FileUtils();  
            if(fileUtils.existSDFile(path+fileName)){  
                return 1;  
            }else{  
                //inputStream = �ϸ��������ϻ�õ�������   
                is = getInputStreamFromUrl(urlStr);  
                File resultFile = fileUtils.write2SDCARDFromInputSteam(path, fileName, is);  
                if(resultFile==null)return -1;  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return 0;  
    }  
      
    public InputStream getInputStreamFromUrl(String newUrl){  
        URL url = null;  
        HttpURLConnection httpURLConnection = null;  
        InputStream is = null;  
        try {  
            url = new URL(newUrl);  
            httpURLConnection = (HttpURLConnection)url.openConnection();  
            is = httpURLConnection.getInputStream();  
             Const.fileLength=httpURLConnection.getContentLength(); 
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return is;  
    }  
} 