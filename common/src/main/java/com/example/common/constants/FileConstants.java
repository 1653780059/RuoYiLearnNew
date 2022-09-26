package com.example.common.constants;

/**
 * @author 16537
 * @Classname FileConstants
 * @Description 文件相关常量
 * @Version 1.0.0
 * @Date 2022/9/26 10:55
 */
public class FileConstants {
    public static final Integer MAX_FILENAME_LENGTH=32;
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    public static final String[] DEFAULT_ALLOWED_EXTENSION = {
            // 图片
            "bmp", "gif", "jpg", "jpeg", "png",
            // word excel powerpoint
            "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt",
            // 压缩文件
            "rar", "zip", "gz", "bz2",
            // 视频格式
            "mp4", "avi", "rmvb",
            // pdf
            "pdf" };
}
