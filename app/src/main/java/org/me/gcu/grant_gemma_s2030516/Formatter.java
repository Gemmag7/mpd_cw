package org.me.gcu.grant_gemma_s2030516;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;

public class Formatter {
    public String getDescription(String desc) {
        int result = desc.lastIndexOf("<br />");
        if (result == -1) {
            return desc;
        } else {
            return desc.substring(result+6, desc.length());
        }
    }

    public String[] getDates(String date) throws StringIndexOutOfBoundsException {
        if (date.indexOf("Start Date: ") == -1 || date.indexOf("End Date: ") == -1) {
            return null;
        }

        else {
            String startDateIndex = date.substring(date.indexOf("Start Date: "), date.indexOf(':'));
            String data1 = date.substring(startDateIndex.length() + 2, date.indexOf('<'));
            String leftOverString = date.substring(date.indexOf('>'));

            String endDateIndex = leftOverString.substring(leftOverString.indexOf("End Date: "), date.indexOf(':'));
            String data2 = "";
            if (date.indexOf("<br />Delay") != -1) {
                data2 = leftOverString.substring(endDateIndex.length() + 2, leftOverString.indexOf('<'));
            } else {
                data2 = leftOverString.substring(endDateIndex.length() + 2);
            }
            String[] results = new String[2];
            results[0] = data1;
            results[1] = data2;
            Log.d("dateTAG","DATES: "+ results);
            return results;
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate convertLongDateToShort(String dateString) {
        String regex = "\\d+";
        String result = "";
        String[] words = dateString.split(" ");
        for (String word : words) {
            if (word.matches(regex)) {
                result+= word+ "/";
            } else if(!getNumOfMonth(word).isEmpty()) {
                result+= getNumOfMonth(word) + "/";
            }
        }
        return LocalDate.parse(result.substring(0, result.length()-1)); // take the last / off the end
    }
    public String getNumOfMonth(String month) {
        switch(month) {
            case "January":
                return "01";
            case "February":
                return "02";
            case "March":
                return "03";
            case "April":
                return "04";
            case "May":
                return "05";
            case "June":
                return "06";
            case "July":
                return "07";
            case "August":
                return "08";
            case "September":
                return "09";
            case "October":
                return "10";
            case "November":
                return "11";
            case "December":
                return "12";
            default:
                return "";
        }
    }
}