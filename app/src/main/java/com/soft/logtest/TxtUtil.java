package com.soft.logtest;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class TxtUtil {
    public static String readInternal(int pageNum, String filename) throws IOException {
        int num=pageNum*3;
        String line="";
        StringBuilder result = new StringBuilder("");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filename = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "log" + File.separator + filename;
            try {
                File file=new File(filename);
                LineNumberReader lnr = new LineNumberReader(new FileReader(file));
                FileInputStream fileInputStream=new FileInputStream(file);
                BufferedReader br=new BufferedReader(new InputStreamReader(fileInputStream));
                Log.e("mpk","pageNum==="+pageNum);
                while ((line=br.readLine()) != null && num< pageNum*3+3 && num>=pageNum*3) {

                    result.append(line+"\n");
                    num++;
                    Log.e("mpk","num==="+num);
                    Log.e("mpk","lnr==="+lnr.getLineNumber());
                }


                fileInputStream.close();
                br.close();
            }catch (Exception e){

            }
        }
        return result.toString();
    }


    public static String getStr(String filename) {
        filename = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "log" + File.separator + filename;
        LineNumberReader reader = null;
        StringBuilder result = new StringBuilder("");
        String[] lines = new String[3];
        try {
            File file=new File(filename);
            reader = new LineNumberReader(new FileReader(file));
            for (int len = 0; len < 3; len++) {
                if ((lines[len] = reader.readLine()) != null) //EOF
                {
                    result.append(lines[len]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
