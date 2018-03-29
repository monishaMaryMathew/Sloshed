package com.monisha.samples.sloshed.util;

import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by Divya on 27-03-2018.
 */

public class TimeConverter
{
    static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @TypeConverter
    public static Date fromTimestamp(String value)
    {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

    @TypeConverter
    public static String dateToTimestamp(Date value)
    {
        return value == null ? null : df.format(value);
    }
}
