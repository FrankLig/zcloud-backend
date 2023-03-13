package com.bom.zcloudbackend.common.util;

import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class DateUtil {

    public static String getCurrentTime() {
        Date date = new Date();
        String stringDate = String.format("%tF %<tT", date);
        return stringDate;
    }
}
