package com.willconnelly.dateformatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by William Connelly on 9/07/2018.
 * <p>
 * connelly.william@gmail.com
 */
public class DateFormatter {

    public enum Type {

        FULL_MONTH("dd MMMM yyyy"),
        FULL_MONTH_TIME_12("dd MMMM yyyy' - 'h:mm aa"),
        FULL_MONTH_TIME_24("dd MMMM yyyy' - 'HH:mm"),
        SHORT_MONTH("dd MMM yyyy"),
        SHORT_MONTH_TIME_12("dd MMM yyyy' - 'h:mm aa"),
        SHORT_MONTH_TIME_24("dd MMM yyyy' - 'HH:mm"),
        TIME_12("h:mm aa"),
        TIME_24("HH:mm"),
        NO_YEAR_SHORT("dd MMM"),
        NO_YEAR_FULL("dd MMMM"),
        NO_DAY("MMMM yyyy"),
        NO_DAY_SHORT("MMM yy"),
        MONTH_ONLY_SHORT("MMM"),
        MONTH_ONLY_FULL("MMMM"),
        DAY_ONLY("d"),
        NO_DAY_SHORT_MONTH("MMM yyyy"),
        NO_DAY_SHORT_YEAR("MMMM yy"),

        SERVER("yyyy-MM-dd'T'HH:mm:ss"),
        SERVER_TIMEZONE("yyyy-MM-dd'T'HH:mm:ssZZZZZ"),
        SERVER_NO_TIME("yyyy-MM-dd");

        String value;
        Type(String value){this.value = value;}

        public String get() { return value; }
    }


    public DateFormatter(){
    }

    public static DateFormatter from(Date date){


        return new DateFormatter().withDate(date);
    }
    public static DateFormatter from(long date){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);

        return new DateFormatter().withDate(cal.getTime());
    }
    public static DateFormatter from(String date){

        return new DateFormatter().withDate(date);
    }


    private SimpleDateFormat mSDF;
    private String mSDFPattern;
    private CharSequence mResult;


    private DateFormatter withDate(Date date){
        init(date);
        return this;
    }
    private DateFormatter withDate(String string){
        init(string);
        return this;
    }

    private void init(String string){
        if (string == null) return;
        mResult = string;
        if (!string.contains("-")) {
            if (string.length() == 11 || string.length() == 12)  mSDFPattern = "dd MMM yyyy";
            else  mSDFPattern = "dd MMMM yyyy";
        }
        else if(string.contains(" - ")){
            String[] split = string.split(" - ");
            String date;
            String time;
            if (split[0].length() == 11 || split[0].length() == 12) date = "dd MMM yyyy";
            else date = "dd MMMM yyyy";

            if (split[1].length() == 5) time = "HH:mm";
            else time = "hh:mm aa";

            mSDFPattern = date + "' - '" + time;
        }
        else if (string.contains(" ")){
            if (string.length() == 19)  mSDFPattern = "yyyy-MM-dd HH:mm:ss";
        }
        else if (string.length() == 5)  mSDFPattern = "MM-dd";
        else if (string.length() == 10)  mSDFPattern = "yyyy-MM-dd";
        else if (string.length() == 16)  mSDFPattern = "yyyy-MM-dd'T'HH:mm";
        else if (string.length() == 19)  mSDFPattern = "yyyy-MM-dd'T'HH:mm:ss";
        else if (string.length() > 19 )  mSDFPattern = "yyyy-MM-dd'T'HH:mm:ssZZZZZ";

        mSDF = new SimpleDateFormat( mSDFPattern, Locale.getDefault());
    }
    private void init(Date date){

        mSDFPattern = "yyyy-MM-dd'T'HH:mm:ssZZZZZ";
        mResult = new SimpleDateFormat(mSDFPattern, Locale.getDefault()).format(date);
        mSDF = new SimpleDateFormat(mSDFPattern, Locale.getDefault());
    }
    public static DateFormatter from(int year, int month, int dayOfMonth){
        DateFormatter df= new DateFormatter();
//        mToUTC = true;
        String sYear = String.valueOf(year);
        String sMonth = String.valueOf(month + 1);
        String sDay = String.valueOf(dayOfMonth);

        if(sYear.length() == 1) sYear = "0" + sYear;
        if(sMonth.length() == 1) sMonth = "0" + sMonth;
        if(sDay.length() == 1) sDay = "0" + sDay;

        df.mResult = new StringBuilder()
                .append(sYear)
                .append(sMonth)
                .append(sDay);

        StringBuilder iYear = new StringBuilder();
        StringBuilder iMonth = new StringBuilder();
        StringBuilder iDay = new StringBuilder();

        int yearLength = sYear.length();
        int monthLength = sMonth.length();
        int dayLength = sDay.length();

        while (yearLength-- > 0){ iYear.append("y"); }
        while (monthLength-- > 0){ iMonth.append("M"); }
        while (dayLength-- > 0){ iDay.append("d"); }

        df.mSDF = new SimpleDateFormat(iYear.toString() + iMonth.toString() + iDay.toString(), Locale.getDefault());

        return df;
    }
    public static DateFormatter from(int hourOfDay, int minute){
        DateFormatter df = new DateFormatter();

//        mToUTC = true;
        String sHour = String.valueOf(hourOfDay);
        String sMinute = String.valueOf(minute);

        if(sHour.length() == 1) sHour = "0" + sHour;
        if(sMinute.length() == 1) sMinute = "0" + sMinute;

        df.mResult = new StringBuilder()
                .append(sHour)
                .append(sMinute);

        StringBuilder iHour = new StringBuilder();
        StringBuilder iMinute = new StringBuilder();

        int hourLength = sHour.length();
        int minuteLength = sMinute.length();

        while (hourLength-- > 0){ iHour.append("H"); }
        while (minuteLength-- > 0){ iMinute.append("m"); }

        df.mSDF = new SimpleDateFormat(iHour.toString() + iMinute.toString(), Locale.getDefault());

        return df;
    }

    private Boolean mFromUTC;
    public DateFormatter fromUTC(){
        this.mFromUTC = true;
        return this;
    }
    private Boolean mToUTC;
    public DateFormatter toUTC(){
        this.mToUTC = true;
        return this;
    }

    public String to(Type type){

        SimpleDateFormat sdf = new SimpleDateFormat(type == null ? Type.SERVER_TIMEZONE.get() : type.get(), Locale.getDefault());

        if (mFromUTC != null) mSDF.setTimeZone(TimeZone.getTimeZone("UTC"));
        if (mToUTC != null) sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        String result = "";

        try {
            if (mResult != null) {
                result = sdf.format(mSDF.parse(mResult.toString()));
            }
            else {
                result = sdf.format(Calendar.getInstance().getTime());
            }
        } catch (ParseException e) { e.printStackTrace(); }

        return result;
    }


    private Date toDate(){

        Date result = new Date();
        try {
            result = mSDF.parse(mResult.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean isDateSame(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        cal1.setTime(date1);
        cal2.setTime(date2);

        return cal1.get(java.util.Calendar.DAY_OF_MONTH) == cal2.get(java.util.Calendar.DAY_OF_MONTH) &&
                cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH) &&
                cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR);
    }

    public static boolean isDateSame(String dateString1, String dateString2) {
        Date date1 = DateFormatter.from(dateString1).toDate();
        Date date2 = DateFormatter.from(dateString2).toDate();

        return isDateSame(date1, date2);
    }

    /**
     *
     * Returns a negative
     *
     * @return Integer +1 = date 1 is in the future
     *          Integer -1 = Date 1 is in the past
     *          Integer 0 = Date is the same
     *          Integer -2 = Couldn't parse
     */
    public static int isDatePastOrFuture(Date date1, Date date2){
        if (date1.getTime() == date2.getTime()) return 0;
        if (date1.getTime() > date2.getTime()) return 1; // date1 is in the future to date2
        if (date1.getTime() < date2.getTime()) return -1; // date1 is in the past to date2
        return -2;
    }

    public static int isDatePastOrFuture(String date1, String date2){

        Date string1 = DateFormatter.from(date1).toDate();
        Date string2 = DateFormatter.from(date2).toDate();

        return isDatePastOrFuture(string1, string2);
    }

    public static int isDatePastOrFuture(Date date1, String date2){
        Date string2 = DateFormatter.from(date2).toDate();
        return isDatePastOrFuture(date1, string2);
    }

    public static int isDatePastOrFuture(String date1, Date date2){
        Date string1 = DateFormatter.from(date1).toDate();
        return isDatePastOrFuture(string1, date2);
    }

    public static boolean isWithinDayRange(Date date, int rangeFromToday) {

        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);

        Calendar rangeCal = Calendar.getInstance();
        rangeCal.add(Calendar.DAY_OF_YEAR, rangeFromToday);
        rangeCal.set(Calendar.HOUR_OF_DAY, 0);
        rangeCal.set(Calendar.MINUTE, 0);

        Calendar sourceCal = Calendar.getInstance();
        sourceCal.setTime(date);
        rangeCal.set(Calendar.HOUR_OF_DAY, 0);
        rangeCal.set(Calendar.MINUTE, 0);

        if (rangeFromToday > 0){

            return sourceCal.getTimeInMillis() < rangeCal.getTimeInMillis() && sourceCal.getTimeInMillis() > todayCal.getTimeInMillis();
        } else
            return sourceCal.getTimeInMillis() > rangeCal.getTimeInMillis() && sourceCal.getTimeInMillis() < todayCal.getTimeInMillis();
    }
    public static boolean isWithinDayRange(String dateString, int rangeFromToday) {

        Date date = DateFormatter.from(dateString).toDate();
        return isWithinDayRange(date, rangeFromToday);
    }
}
