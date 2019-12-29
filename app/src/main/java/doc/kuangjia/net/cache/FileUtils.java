package com.zkys.pad.launcher.net.cache;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;

/**
 * @author any
 * @date 2017/11/30
 */
public class FileUtils {

    /**
     * 获取文件内容
     *
     * @param filePath
     * @return
     */
    public static String getResultByFilePath(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) return null;
            FileInputStream input = new FileInputStream(file);
            return convertStreamToString(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 写入文件，覆盖写入
     *
     * @param filePath
     * @param result
     */
    public static synchronized int writeResultToFile(String filePath, String result) {
        createFile(filePath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            fos.write(result.getBytes("UTF-8"));
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 1;
    }


    public static void createFile(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return;
        File file = new File(filePath);
        if (file.exists()) {
            return;
        }
        File parenFile = file.getParentFile();
        if (parenFile != null) {
            boolean bool = parenFile.mkdirs();
            Log.e("msg", "boolean: " + bool);
        }

    }

    public static String convertStreamToString(InputStream is) {
        try {
            if (is != null) {
                StringBuilder sb = new StringBuilder();
                String line;
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 处理文件名 key
     * @param key
     * @return
     */
    public static String getMD5(String key) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            char[] charArray = key.toCharArray();
            byte[] byteArray = new byte[charArray.length];
            for (int i = 0; i < charArray.length; i++) {
                byteArray[i] = (byte) charArray[i];
            }
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }

}
