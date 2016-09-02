package com.example.progressbar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;
import android.os.Message;

public class FileUtils {  
    private String SDCARD = null;  
  
    public FileUtils() {  
        SDCARD = Environment.getExternalStorageDirectory() + "/";  
    }  
  
    // 创建一个目录   
    public File createSDDir(String dirName) {  
        File fileDir = new File(SDCARD + dirName);  
        fileDir.mkdir();  
        return fileDir;  
    }  
  
    // 创建一个文件；   
    public File createSDFile(String fileName) throws IOException {  
        File file = new File(SDCARD+fileName);   //注意在这里一定要加上主目录 SDCARD中，才可以，不然会找不到目录 。   
        file.createNewFile();  
        return file;  
    }  
  
    // 判断SD卡上的文件是不是存在；   
    public boolean existSDFile(String fileName) {  
        File file = new File(SDCARD + fileName);  
        return file.exists();  
    }  
  
    // 将一个流对象写入SDCARD   
    public File write2SDCARDFromInputSteam(String path, String fileName,  
            InputStream is) {  
        File file = null;  
        OutputStream os = null;  
        try {  
            createSDDir(path);  
            file = createSDFile(path + fileName);  
            os = new FileOutputStream(file);  
            byte[] buffer = new byte[4 * 1024];
            
            int length=0;
            int temp=0;
            
            
           int count=  Const.fileLength/100;
           int currentIndex=0; 
           
           
            while ((temp=is.read(buffer)) != -1) {  
                os.write(buffer); 
                length+=temp;
                int thisIndex=length/count;
                if(thisIndex!=currentIndex)
                {
                
                Message message=Message.obtain();
                message.arg1= (int)((length+0.0f)/Const.fileLength*100);
              
                MainActivity.instance.handler.sendMessage(message);
                currentIndex=thisIndex;
                }
                
            }  
            os.flush();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                os.close();  
                is.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
  
        return file;  
    }  
}  