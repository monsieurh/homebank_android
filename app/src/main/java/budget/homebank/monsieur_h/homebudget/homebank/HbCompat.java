package budget.homebank.monsieur_h.homebudget.homebank;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HbCompat {

    public static long dateToJulian(Date date) {

        GregorianCalendar calendarDate = new GregorianCalendar();
        calendarDate.setTime(date);

        Calendar calendarBegin = new GregorianCalendar(1, GregorianCalendar.JANUARY, 1);

        long millisecondsDate = calendarDate.getTimeInMillis();
        long millisecondsBegin = calendarBegin.getTimeInMillis();
        long diff = millisecondsDate - millisecondsBegin;
        long diffDays = diff / (24 * 60 * 60 * 1000) - 1;

        return diffDays;
    }

    public static Date julianToDate(long date) {
        GregorianCalendar calendarBegin = new GregorianCalendar(1, GregorianCalendar.JANUARY, 1);
        long millisecondsBegin = calendarBegin.getTimeInMillis();

        long diff = date * (24 * 60 * 60 * 1000);
        long millisecondsDate = diff + millisecondsBegin;
        int days = (int) (millisecondsDate / (24 * 60 * 60 * 1000));

        GregorianCalendar calendarDate = new GregorianCalendar();
        calendarDate.setTimeInMillis(0);
        calendarDate.add(GregorianCalendar.DAY_OF_MONTH, days + 1);//todo : why +1 ?

        return calendarDate.getTime();
    }

}
