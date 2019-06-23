package com.tutor.seektutor.Utils;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by AliAh on 14/05/2018.
 */

public class CommonUtils {


    private CommonUtils() {
        // This utility class is not publicly instantiable
    }

    public static void showToast(final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressLint("WrongConstant")
            public void run() {
                Toast.makeText(ApplicationClass.getInstance().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public static String getFormattedDate(long smsTimeInMilis) {
        Calendar smsTime = Calendar.getInstance();
        smsTime.setTimeInMillis(smsTimeInMilis);

        Calendar now = Calendar.getInstance();

        final String timeFormatString = "h:mm aa";
        final String dateTimeFormatString = "dd MMM ";
        final long HOURS = 60 * 60 * 60;
        if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            return "" + DateFormat.format(timeFormatString, smsTime);
        } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
            return "Yesterday ";
        } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
            return DateFormat.format(dateTimeFormatString, smsTime).toString();
        } else {
            return DateFormat.format("dd MMM , h:mm aa", smsTime).toString();
        }
    }


    public static ArrayList<String> subjectsList() {
        ArrayList subjectList = new ArrayList<>();

        subjectList.add("Physics 9th class");
        subjectList.add("Physics 10th class");
        subjectList.add("Physics 1st year");
        subjectList.add("Physics 2nd year");
        subjectList.add("Maths Matric");
        subjectList.add("Maths Olevels");
        subjectList.add("Maths Alevels");
        subjectList.add("English");
        subjectList.add("Urdu");
        return subjectList;
    }

    public static ArrayList<String> subjectsListWithPrice() {
        ArrayList subjectList = new ArrayList<>();

        subjectList.add("Physics 9th class-Rs 1000");
        subjectList.add("Physics 10th class-Rs 1200");
        subjectList.add("Physics 1st year-Rs 1500");
        subjectList.add("Physics 2nd year-Rs 1500");
        subjectList.add("Maths Matric-Rs 2000");
        subjectList.add("Maths Olevels-Rs 3000");
        subjectList.add("Maths Alevels-Rs 4000");
        subjectList.add("English-Rs 1200");
        subjectList.add("Chemistry-Rs 1200");
        subjectList.add("Urdu-Rs 900");
        subjectList.add("Urdu-Rs 900");
        subjectList.add("Urdu-Rs 900");
        subjectList.add("Urdu-Rs 900");
        subjectList.add("Urdu-Rs 900");
        return subjectList;
    }


}
