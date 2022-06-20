package com.suicuntong.sct.entity;

public class File {

    private String name; // 存储在HDFS中的文件名
    private String originalName; // 原文件名
    private String fileExtension;
    private boolean isDir = false; // 是否是目录
    private String filePath; // 文件路径
    private String originalPath;
    private String type;
    private long size;
    private String modificationTime;
    private String content;

    public final static String TYPE_FOLDER = "folder";
    public final static String TYPE_FILE = "file";
    public final static String TYPE_TXT = "txt";
    public final static String TYPE_CSS = "css";
    public final static String TYPE_DOC = "doc";
    public final static String TYPE_HTML = "html";
    public final static String TYPE_JS = "js";
    public final static String TYPE_JPG = "jpg";
    public final static String TYPE_PNG = "png";
    public final static String TYPE_PPT = "ppt";
    public final static String TYPE_ZIP = "zip";
    public final static String TYPE_EXE = "exe";
    public final static String TYPE_MP3 = "mp3";
    public final static String TYPE_PDF = "pdf";
    public final static String TYPE_PHP = "php";
    public final static String TYPE_AVI = "avi";

    public void setFileInfo(){
        String content = this.name;
        int lastDotpos = content.lastIndexOf( "." );
        if(lastDotpos < 0){
            this.type = TYPE_FOLDER;
        } else {
            String ext = content.substring(lastDotpos + 1);
            this.fileExtension = ext;
            this.type = ext;
            switch (type.toLowerCase()){
                case "txt":
                case "md":
                case "lrc":
                case "log":
                    this.type = TYPE_TXT;
                    break;
                case "css":
                    this.type = TYPE_CSS;
                    break;
                case "docx":
                case "doc":
                    this.type = TYPE_DOC;
                    break;
                case "html":
                    this.type = TYPE_HTML;
                    break;
                case "js":
                    this.type = TYPE_JS;
                    break;
                case "jpeg":
                case "jpg":
                    this.type = TYPE_JPG;
                    break;
                case "png":
                    this.type = TYPE_PNG;
                    break;
                case "pptx":
                case "ppt":
                    this.type = TYPE_PPT;
                    break;
                case "7z":
                case "zip":
                    this.type = TYPE_ZIP;
                    break;
                case "exe":
                    this.type = TYPE_EXE;
                    break;
                case "mp3":
                    this.type = TYPE_MP3;
                    break;
                case "pdf":
                    this.type = TYPE_PDF;
                    break;
                case "php":
                    this.type = TYPE_PHP;
                    break;
                case "avi":
                case "mp4":
                case "rmvb":
                case "mov":
                    this.type = TYPE_AVI;
                    break;
                default:
                    this.type = TYPE_FILE;
                    break;
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(String modificationTime) {
        this.modificationTime = modificationTime;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public  String getTypeFolder() {
        return TYPE_FOLDER;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
