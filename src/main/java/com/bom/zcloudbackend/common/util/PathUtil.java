package com.bom.zcloudbackend.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.util.ResourceUtils;

@Slf4j
public class PathUtil {

    public static String getFilePath() {
        String path = "upload";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        path = File.separator + path + File.separator + formatter.format(new Date());

        String staticPath = PathUtil.getStaticPath();

        File directory = new File(staticPath + path);
        if (!directory.exists()) {
            try {
                boolean isMakeDir = directory.mkdirs();
                if (!isMakeDir) {
                    log.error("目录创建失败:" + staticPath + path);
                }
            } catch (Exception e) {
                log.error("目录创建失败:" + staticPath + path);
                return "";
            }
        }
        return path;
    }

    public static String getStaticPath() {
        String localStoragePath = PropertiesUtil.getProperty("file.lacal-storage-path");
        if (StringUtils.isNoneEmpty(localStoragePath)) {
            return localStoragePath;
        } else {
            String projectRootAbsolutePath = getProjectRootPath();
            int index = projectRootAbsolutePath.indexOf("file:");
            if (index != -1) {
                projectRootAbsolutePath = projectRootAbsolutePath.substring(0, index);
            }

            return projectRootAbsolutePath + "static" + File.separator;
        }
    }

    /**
     * 获取项目所在根路径
     *
     * @return
     */
    public static String getProjectRootPath() {
        String absolutePath = null;
        try {
            String url = ResourceUtils.getURL("classpath:").getPath();
            absolutePath = urlDecode(new File(url).getAbsolutePath() + File.separator);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return absolutePath;
    }


    /**
     * 路径解码
     *
     * @param url
     * @return
     */
    public static String urlDecode(String url) {
        String decodeUrl = null;
        try {
            decodeUrl = URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodeUrl;
    }
}
