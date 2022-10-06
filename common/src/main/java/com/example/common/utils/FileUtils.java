package com.example.common.utils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.example.common.config.LearnConfig;
import com.example.common.constants.FileConstants;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author 16537
 * @Classname FileUtils 文件操作工具
 * @Description
 * @Version 1.0.0
 * @Date 2022/9/26 11:06
 */
public class FileUtils {
    private static final AtomicInteger ID = new AtomicInteger();
    private static final LearnConfig learnConfig = SpringUtil.getBean("learnConfig");
    /**
     * 上传文件
     * @param file 文件对象
     * @return 访问文件的url
     */
    public static String fileUpload(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String uploadPath = learnConfig.getUploadPath();
        assertFile(file);
        File absolutFile = getAbsolutFile(filename, uploadPath);
        try {
            file.transferTo(absolutFile);
        } catch (IOException e) {
            throw new RuntimeException("上传失败");
        }
        String absolutePath = absolutFile.getAbsolutePath().replace("\\", "/");

        return getURL(absolutePath,learnConfig.getResource());
    }

    /**
     * 获取文件访问url
     * @param absolutePath 文件绝对路径
     * @param resource 系统资源路径
     * @return 文件访问url
     */
    private static String getURL(String absolutePath, String resource) {
      String url =  ServletUtils.getURL();
        String uri = ServletUtils.getURI();
        String hostAndPort = url.substring(0, url.length() - uri.length());
        String newUri = absolutePath.substring(resource.length());
        return hostAndPort+newUri;
    }

    /**
     * 获取绝对路径所指文件对象
     * @param filename 文件名
     * @param uploadPath 文件上传路径
     * @return 文件对象
     */
    private static File getAbsolutFile(String filename, String uploadPath) {
        String absolutPath=getAbsolutPath(filename,uploadPath);
        File absolutFile = new File(absolutPath);
        if(!absolutFile.getParentFile().exists()){
            absolutFile.getParentFile().mkdirs();
        }

        return absolutFile;
    }

    /**
     * 分日期存储文件
     * @param filename 文件名
     * @param uploadPath 上传路径
     * @return 绝对路径名
     */
    private static String getAbsolutPath(String filename, String uploadPath) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date = simpleDateFormat.format(new Date());
        String extension = filename.substring(filename.lastIndexOf("."));
        String filenamePrefix = filename.substring(0,filename.lastIndexOf("."));
        String id = getID();
        return StrUtil.format("{}/{}/{}_{}",uploadPath,date,filenamePrefix,id+extension);
    }

    /**
     * 获取防重名id
     * @return 返回id
     */
    private synchronized static String getID() {
        int i = ID.incrementAndGet();
        if(i>=Math.pow(10,3)){
            ID.compareAndSet(i,0);
            i=ID.get();
        }

        return getIDStr(i);

    }

    /**
     * id转字符，不足3位前面补0
     * @param i id
     * @return id字符串
     */
    private static String getIDStr(int i) {
        StringBuilder s = new StringBuilder().append(i);
        int append=0;
        if (s.length()<3) {
            append = 3 - s.length();
        }
        for(int j=0;j<append;j++){
            s.insert(0,"0");
        }
        return s.toString();
    }

    /**
     * 文件合法判断
     * @param file 文件对象
     */
    private static void assertFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if(filename==null){
            throw new RuntimeException("上传文件名为空");
        }
        if(filename.length()> FileConstants.MAX_FILENAME_LENGTH){
            throw new RuntimeException("文件名长度过长");
        }
        long size = file.getSize();
        if(size>FileConstants.MAX_FILE_SIZE){
            throw new RuntimeException("文件超过"+FileConstants.MAX_FILE_SIZE/1024/1024+"MB,不可上传");
        }
        String  extension = filename.substring(filename.lastIndexOf(".")+1);
        String[] defaultAllowedExtension = FileConstants.DEFAULT_ALLOWED_EXTENSION;
        List<String> collect = Arrays.stream(defaultAllowedExtension).filter(str -> str.equals(extension)).collect(Collectors.toList());
        if (collect.isEmpty()){
            throw new RuntimeException("文件后缀无许可");
        }

    }
}
