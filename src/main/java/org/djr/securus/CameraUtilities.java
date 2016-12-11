package org.djr.securus;

import org.joda.time.DateTime;

/**
 * Created by djr4488 on 12/8/16.
 */
public class CameraUtilities {
    public static DateTime getDateTime(CameraPostEvent cameraPostEvent) {
        DateTime now = DateTime.now();
        String dateTime = cameraPostEvent.getxTimeStampedFile();
        int year = Integer.parseInt(dateTime.substring(0, 2));
        int month = Integer.parseInt(dateTime.substring(2, 4));
        int day = Integer.parseInt(dateTime.substring(4, 6));
        int hour = Integer.parseInt(dateTime.substring(6, 8));
        int minute = Integer.parseInt(dateTime.substring(8, 10));
        int second = Integer.parseInt(dateTime.substring(10, 12));
        now.withYear(year);
        now.withMonthOfYear(month);
        now.withDayOfMonth(day);
        now.withHourOfDay(hour);
        now.withMinuteOfHour(minute);
        now.withSecondOfMinute(second);
        return now;
    }
}
