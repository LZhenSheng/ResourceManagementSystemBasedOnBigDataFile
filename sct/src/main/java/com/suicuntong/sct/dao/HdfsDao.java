package com.suicuntong.sct.dao;


import com.suicuntong.sct.utils.FileUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HdfsDao {

    private static String hdfsPath;
    private static String user;

    @Value("${hdfs.path}")
    public void setHdfsPath(String hdfsPath){
        this.hdfsPath = hdfsPath;
    }
    @Value("${hdfs.user}")
    public void setUser(String user){
        this.user = user;
    }


    /**
     * 获取hdfs配置信息
     * @return
     */
    private Configuration getConfiguration(){
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", hdfsPath);
        return configuration;
    }

    /**
     * 获取文件系统对象
     * @return
     */
    public FileSystem getFileSystem() throws Exception {
        FileSystem fileSystem = FileSystem.get(new URI(hdfsPath), getConfiguration(), user);
        return fileSystem;
    }

    /**
     * 判断HDFS的文件是否存在
     * @param path
     * @return
     * @throws Exception
     */
    public boolean exist(String path) throws Exception {
        if(StringUtils.isBlank(path)){
            return false;
        }
        FileSystem fileSystem = getFileSystem();
        return fileSystem.exists(new Path(path));
    }
    /**
     * 获取目录下的文件列表
     * @param path
     * @return
     * @throws Exception
     */
    public List<Map<String,Object>> listFiles(String path) throws Exception {
        if(!exist(path)){
            return null;
        }
        FileSystem fileSystem  = getFileSystem();
        FileStatus[] statuses;
        FileUtil fileUtil = new FileUtil();
        List<Map<String,Object>> result = new ArrayList<>();
        System.out.println(path);
        statuses = fileSystem.listStatus(new Path(path));
        if(statuses != null){
            for(FileStatus status : statuses){
                Map<String,Object> fileMap = new HashMap<>();
                fileMap.put("fileName", status.getPath().getName());
                fileMap.put("filePath", fileUtil.getPath(status.getPath().toString()));
                fileMap.put("size", status.getLen());
                fileMap.put("modificationTime", fileUtil.getTime(String.valueOf(status.getModificationTime())));
                result.add(fileMap);
            }
        }
        return  result;
    }

    /**
     * 获取指定路径的文件
     * @param path
     * @return
     * @throws Exception
     */
    public Map<String,Object> getFileInfo(String path) throws Exception {
        if(!exist(path)){
            return null;
        }
        FileSystem fileSystem = getFileSystem();
        FileStatus statuse;
        FileUtil fileUtil = new FileUtil();
        Map<String,Object> result = new HashMap<>();
        System.out.println(path);
        statuse = fileSystem.getFileStatus(new Path(path));
        if(statuse != null){
            result.put("fileName", statuse.getPath().getName());
            result.put("filePath", fileUtil.getPath(statuse.getPath().toString()));
            result.put("size", statuse.getLen());
            result.put("modificationTime", fileUtil.getTime(String.valueOf(statuse.getModificationTime())));
        }
        return  result;
    }



    /**
     * 创建HDFS文件夹
     * @param dir
     * @return
     * @throws Exception
     */
    public boolean mkdir(String dir) throws Exception{
        if(StringUtils.isBlank(dir)){
            return false;
        }
        if(exist(dir)){
            return true;
        }
        FileSystem fileSystem = getFileSystem();
        boolean isOk = fileSystem.mkdirs(new Path(dir));
        fileSystem.close();
        return isOk;
    }

    /**
     * HDFS创建文件
     * @param filePath
     * @param context
     * @throws Exception
     */
    public boolean createFile(String filePath, String context) throws Exception {
        boolean isOk = false;
        if (StringUtils.isBlank(filePath)) {
            return false;
        }
        Path path = new Path(filePath);
        try {
            FileSystem fs = getFileSystem();
            if(!fs.exists(path)){
                FSDataOutputStream fsDataOutputStream = fs.create(path,new Short((short)1));
                fsDataOutputStream.write(context.getBytes());
                fsDataOutputStream.flush();
                fsDataOutputStream.close();
                isOk = true;
            }else {
                isOk = false;
            }
        } catch (Exception e) {
            isOk = false;
            e.printStackTrace();
        }
        return isOk;
    }

    /**
     * 重命名HDFS文件
     * @param oldName
     * @param newName
     * @return
     * @throws Exception
     */
    public boolean renameFile(String oldName, String newName)throws Exception{
        if(!exist(oldName) || StringUtils.isBlank(newName)){
            return false;
        }
        FileSystem fs = getFileSystem();
        boolean isOk = fs.rename(new Path(oldName), new Path(newName));
        fs.close();
        return isOk;
    }

    /**
     * 删除HDFS文件
     * @param path
     * @return
     * @throws Exception
     */
    public boolean deleteFile(String path)throws Exception {
        if(!exist(path)){
            return false;
        }
        FileSystem fs = getFileSystem();
        boolean isOk = fs.deleteOnExit(new Path(path));
        fs.close();
        return isOk;
    }

    /**
     * 上传文件到HDFS
     * @param path
     * @param uploadPath
     * @throws Exception
     */
    public boolean uploadFile(String path,String uploadPath){
        boolean isOk = false;
        if(StringUtils.isBlank(path) || StringUtils.isBlank(uploadPath)){
            return isOk;
        }
        FileSystem fs = null;
        try {
            fs = getFileSystem();
            fs.copyFromLocalFile(new Path(path), new Path(uploadPath));
            fs.close();
            isOk = true;
        } catch (Exception e) {
            e.printStackTrace();
            isOk = false;
        }
        return isOk;
    }

    /**
     * 从HDFS下载文件
     * @param path
     * @param downloadPath
     * @throws Exception
     */
    public boolean downloadFile(String path, String downloadPath){
        boolean isOk = false;
        if(StringUtils.isBlank(path) || StringUtils.isBlank(downloadPath)){
            return isOk;
        }
        FileSystem fs = null;
        try {
            fs = getFileSystem();
            fs.copyToLocalFile(new Path(path), new Path(downloadPath) );
            fs.close();
            isOk = true;
        } catch (Exception e) {
            e.printStackTrace();
            isOk = false;
        }
        return isOk;
    }


    /**
     * 获取文件块在集群的位置
     * @param path
     * @return
     * @throws Exception
     */
    public BlockLocation[] getFileBlockLocations(String path)throws Exception{
        if(exist(path)){
            return null;
        }
        FileSystem fs = getFileSystem();
        FileStatus fileStatus = fs.getFileStatus(new Path(path));
        return fs.getFileBlockLocations(fileStatus, 0, fileStatus.getLen());
    }

    public boolean moveFile(String sourcePath, String targetPath) {
        boolean isOk = false;
        try {
            if(copyFile(sourcePath,targetPath)){
                FileSystem fs = getFileSystem();
                fs.deleteOnExit(new Path(sourcePath));
                fs.close();
                isOk = true;
            } else {
                isOk = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOk;
    }

    /***
     * 移动文件
     * @param sourcePath
     * @param targetPath
     */
    public boolean copyFile(String sourcePath, String targetPath) {
        boolean isOk = false;
        if(StringUtils.isBlank(sourcePath) || StringUtils.isBlank(targetPath)){
            return isOk;
        } else {
            FSDataInputStream inputStream = null;
            FSDataOutputStream outputStream = null;
            FileSystem fs = null;
            try {
                fs = getFileSystem();
                inputStream = fs.open(new Path(sourcePath));
                outputStream = fs.create(new Path(targetPath));
                IOUtils.copyBytes(inputStream, outputStream, 1024);
                isOk = true;
            } catch (Exception ex) {
                isOk = false;
                ex.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    fs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return isOk;
    }


    /**
     * 读取HDFS文件内容
     * @param path
     * @return
     * @throws Exception
     */
    public String readFileToString(String path) throws Exception{
        if(!exist(path)){
            return null;
        }
        FileSystem fs = getFileSystem();
        FSDataInputStream inputStream = null;
        try {
            inputStream = fs.open(new Path(path));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null){
                sb.append(line);
            }
            return sb.toString();
        }finally {
            if(inputStream != null){
                inputStream.close();
            }
            fs.close();
        }
    }


//    /**
//     *
//     * @Title: getPdfFileText
//     * @Description: 获取指定位置pdf的文件内容
//     * @param @param fileName
//     * @param @return
//     * @param @throws IOException
//     * @return String 返回类型
//     * @throws
//     */
//    public static String getPdfFileText(String fileName) throws IOException {
//        PdfReader reader = new PdfReader(fileName);
//        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
//        StringBuffer buff = new StringBuffer();
//        TextExtractionStrategy strategy;
//        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
//            strategy = parser.processContent(i,
//                    new SimpleTextExtractionStrategy());
//            buff.append(strategy.getResultantText());
//        }
//        return buff.toString();
//    }
//
//
//    /**
//     * 获取doc文档
//     *
//     * @Title: getTextFromWord
//     * @param @param filePath
//     * @param @return
//     * @return String 返回类型
//     * @throws
//     */
//    public static String getTextFromWord(String filePath) {
//        String result = null;
//        File file = new File(filePath);
//        try {
//            FileInputStream fis = new FileInputStream(file);
//            WordExtractor wordExtractor = new WordExtractor(fis);
//            result = wordExtractor.getText();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//
//    /**
//     * 读取excel内容
//     *
//     * @Title: getTextFromExcel
//     * @param @param filePath
//     * @param @return
//     * @return String 返回类型
//     * @throws
//     */
//    public static String getTextFromExcel(String filePath) {
//        StringBuffer buff = new StringBuffer();
//        try {
//            // 创建对Excel工作簿文件的引用
//            HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(filePath));
//            // 创建对工作表的引用。
//            for (int numSheets = 0; numSheets < wb.getNumberOfSheets(); numSheets++) {
//                if (null != wb.getSheetAt(numSheets)) {
//                    HSSFSheet aSheet = wb.getSheetAt(numSheets);// 获得一个sheet
//                    for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet
//                            .getLastRowNum(); rowNumOfSheet++) {
//                        if (null != aSheet.getRow(rowNumOfSheet)) {
//                            HSSFRow aRow = aSheet.getRow(rowNumOfSheet); // 获得一个行
//                            for (int cellNumOfRow = 0; cellNumOfRow <= aRow
//                                    .getLastCellNum(); cellNumOfRow++) {
//                                if (null != aRow.getCell(cellNumOfRow)) {
//                                    HSSFCell aCell = aRow.getCell(cellNumOfRow);// 获得列值
//                                    switch (aCell.getCellType()) {
//                                        case HSSFCell.CELL_TYPE_FORMULA:
//                                            break;
//                                        case HSSFCell.CELL_TYPE_NUMERIC:
//                                            buff
//                                                    .append(
//                                                            aCell
//                                                                    .getNumericCellValue())
//                                                    .append('\t');
//                                            break;
//                                        case HSSFCell.CELL_TYPE_STRING:
//                                            buff.append(aCell.getStringCellValue())
//                                                    .append('\t');
//                                            break;
//                                    }
//                                }
//                            }
//                            buff.append('\n');
//                        }
//                    }
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return buff.toString();
//    }


}



