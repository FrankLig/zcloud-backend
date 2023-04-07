package com.bom.zcloudbackend.common.util;

/**
 * <p>
 * 文件工具类
 * </p>
 * @author Frank Liang
 */
public class FileUtil {

    public static final String[] IMG_FILE = {"bmp", "jpg", "png", "tif", "gif", "jpeg"};
    public static final String[] DOC_FILE = {"doc", "docx", "ppt", "pptx", "xls", "xlsx", "txt", "hlp", "wps", "rtf",
        "html", "pdf"};
    public static final String[] VIDEO_FILE = {"avi", "mp4", "mpg", "mov", "swf"};
    public static final String[] MUSIC_FILE = {"wav", "aif", "au", "mp3", "ram", "wma", "mmf", "amr", "aac", "flac"};
    public static final int IMAGE_TYPE = 1;
    public static final int DOC_TYPE = 2;
    public static final int VIDEO_TYPE = 3;
    public static final int MUSIC_TYPE = 4;
    public static final int OTHER_TYPE = 5;
    public static final int SHARE_FILE = 6;
    public static final int RECYCLE_FILE = 7;
    public static final String DOT=".";

    /**
     * 文件是否为图片
     *
     * @param extendName
     * @return
     */
    public static boolean isImageFile(String extendName) {
        for (int i = 0; i < IMG_FILE.length; i++) {
            if (extendName.equalsIgnoreCase(IMG_FILE[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件拓展名
     *
     * @param fileName
     * @return
     */
    public static String getFileExtendName(String fileName) {
        if (fileName.lastIndexOf(DOT) == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(DOT) + 1);
    }

    /**
     * 获取文件名(不带拓展名)
     *
     * @param fileName
     * @return
     */
    public static String getFileNameNotExtend(String fileName) {
        String fileType = getFileExtendName(fileName);
        return fileName.replace(DOT + fileType, "");
    }


}
