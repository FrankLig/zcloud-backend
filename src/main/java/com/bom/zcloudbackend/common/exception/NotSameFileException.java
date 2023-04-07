package com.bom.zcloudbackend.common.exception;


/**
 * @author Frank Liang
 */
public class NotSameFileException extends Exception{
    public NotSameFileException(){
        super("File MD5 Different");
    }

}
