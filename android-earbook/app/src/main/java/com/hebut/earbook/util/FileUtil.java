package com.hebut.earbook.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.RandomAccessFile;

public class FileUtil {
    public static String LOG_Info = "FILE_UTIL";
    public static String LOG_Error = "FILE_UTIL_ERROR";

    /**
     * 会了这常见的三种方式足够了
     * 一种是跟 app 绑定在一起的 数据库SQLite操作的时候需要使用  跟随着APP的生命周期而变换 apk私有目录 不会导致数据残留  卸载就没了  但是重新烧录不会改变数据库   数据库操作（亲测）
     * 一种是存在内存当中的，会一直存在  图片保存 数据库操作 （亲测）Environment 方式获取路径 公共目录
     * 还有一种就是挂载的SD卡 这类就比较烦  而且是特别烦，除非必须使用，一般不推荐使用 容易出错  现在的手机不支持外部SD卡了， 保存图片 亲测可以用
     */
    /**
     * @param FileDirName：你想创建的文件夹的名字
     * @Function: 测试内部存储
     * @attention: 数据跟APP绑定 app卸载后就没有了
     * 生成的文件 存储在 NANDFlash --> Android --> data   里面是的 app的包名 com.XXX.......---> files 这种格式 找到对应的就可以了
     * @Return: 返回文件夹的路径，可以在文件夹下继续创建文件
     */
    public static String TestFilePathApkPrivate(Context context, String FileDirName) {
        //不需要挂载测试，因为 app 都可以装 为什么 会没有数据
        String filedirpath = context.getExternalFilesDir(FileDirName).getPath();  //文件夹
        File fileDir = new File(filedirpath);                   //创建文件夹
        if (fileDir.exists()) {    //判断文件是否存在  很重要  别每次都会去覆盖数据
            fileDir.setWritable(true);
            Log.i(LOG_Info, "文件夹已经存在    TestFilePathInternalData（）");
        } else {
            try {
                fileDir.mkdir();
                Log.i(LOG_Info, "文件夹创建成功    TestFilePathExternalData（）");
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(LOG_Error, "文件夹创建错误   TestFilePathExternalData()" + e.getMessage());
            }
        }
        return filedirpath;
    }

    /**
     * @param content:    要写的内容
     * @param filedirname 文件夹的名字
     * @param filename:   文件的名字
     * @param mode:       以什么方式往里面去写 0 1 2 3
     * @param ways:       两种方式 Buffer RandomAccessFile  Print  0 1 2
     * @Function: 将content写到指定的文件的指定的目录下去
     * @Return:
     */
    public static String WriteDataToStorage(String content, String filedirname, String filename, int mode, int ways) {
        String FileName = filedirname + File.separator + filename;   //拼接字符串  文件的存储路径
        File subfile = new File(FileName);  //文件夹路径和文件路径   判断文件是否存在
        if (subfile.exists()) {
            subfile.setWritable(true);
            boolean readable = subfile.canRead();
            boolean writeable = subfile.canWrite();
            Log.i(LOG_Info, "文件创建成功" + "readable:" + readable + " writeable:" + writeable);
        } else {
            try {
                subfile.createNewFile();
            } catch (IOException e) {
                Log.i(LOG_Error, "文件创建出错  " + e.getMessage());
                e.printStackTrace();
            }
        }
        int Context_Mode = mode;
        int Ways = ways;
        if (Context_Mode == 0) {
            Context_Mode = Context.MODE_PRIVATE;  //该文件只能被当前程序读写。
        } else if (Context_Mode == 1) {
            Context_Mode = Context.MODE_APPEND;   //以追加方式打开该文件，应用程序可以向该文件中追加内容。
        } else if (Context_Mode == 2) {
            Context_Mode = Context.MODE_WORLD_READABLE;  //该文件的内容可以被其他应用程序读取。
        } else if (Context_Mode == 3) {
            Context_Mode = Context.MODE_WORLD_WRITEABLE;  //该文件的内容可由其他程序读、写。
        } else {
            Context_Mode = Context.MODE_WORLD_WRITEABLE;  //省的烦   反正都可以读
        }
        if (Ways == 0) {
            Log.i(LOG_Info, "BufferWriter");
            FileOutputStream fileOutputStream = null;
            BufferedWriter bufferedWriter = null;
            OutputStreamWriter outputStreamWriter = null;
            try {
                fileOutputStream = new FileOutputStream(subfile);
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream, "utf-8"));  //解决输入中文的问题
                bufferedWriter.write(content + "\t");
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(LOG_Error, "写入数据出错 " + e.getMessage());
            } finally {
                if (bufferedWriter != null) {
                    try {
                        bufferedWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (Ways == 1) {
            Log.i(LOG_Info, "RandomAccessFile");
            try {
                RandomAccessFile raf = new RandomAccessFile(subfile, "rw");
                raf.seek(subfile.length());
                raf.write(content.getBytes());
                raf.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(LOG_Error, "写入数据出错 " + e.getMessage());
            }
        } else if (Ways == 2) {
            Log.i(LOG_Info, "Printer");
            try {
                FileOutputStream fileoutputStream = new FileOutputStream(subfile);
                //openFileOutput("text2", Context.MODE_PRIVATE);
                PrintStream ps = new PrintStream(fileoutputStream);
                ps.print(content + "\t");
                ps.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return FileName;
    }

    /**
     * @param fileDirName: 文件夹的路径
     * @Function: 列出文件夹下所有文件的名字
     * @Return:
     */
    public static File[] ListFileDirName(String fileDirName) {
        File fileDir = new File(fileDirName);
        File[] files = new File[0];
        if (fileDir.isDirectory()) {
            files = fileDir.listFiles();
        }
        for (File a : files) {   //可以利用适配器做成界面  完成为了玩没意思
            Log.i(LOG_Info, a.toString());
        }
        return files;
    }

    /**
     * @param fileDirName:文件夹目录
     * @param fileName:文件名字
     * @param ways:读取文件的方式
     * @Function: 从存储路径中读出数据
     * @Return:
     */
    public static void ReadDataFromStorage(String fileDirName, String fileName, int ways) throws IOException {
        File file = new File(fileDirName, fileName);
        int Ways = ways;
        if (Ways == 0) {
            Log.i(LOG_Info, "FileInputStream");
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] bytes = new byte[fileInputStream.available()];
                fileInputStream.read(bytes);
                String result = new String(bytes);
                Log.i(LOG_Info, "读取的内容是：" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Ways == 1) {   //最好使用 Buffer 缓冲流，安全机制 大量的文件
            Log.i(LOG_Info, "Bufferreader");
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String readline = "";
                StringBuffer stringBuffer = new StringBuffer();
                while ((readline = bufferedReader.readLine()) != null) {
                    stringBuffer.append(readline);
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                    Log.i(LOG_Info, "读取的内容是：" + stringBuffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (Ways == 2) {
            Log.i(LOG_Info, "Input+Buffer");
            try {
                String fileContent = null;
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader reader = new BufferedReader(read);
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContent += line;
                }
                reader.close();
                read.close();
                Log.i(LOG_Info, fileContent);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(LOG_Error, e.getMessage());
            }
        } else
            Ways = 2;
    }

    /**
     * @param file: 文件/文件夹的路径
     * @Function: 文件夹  文件的删除
     * @Return:
     */
    private void DeleteFileDirORFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    return;
                }
                if (childFile.length > 1) {
                    for (File f : childFile) {
                        DeleteFileDirORFile(f);
                    }
                }
            }
        }
    }
}
