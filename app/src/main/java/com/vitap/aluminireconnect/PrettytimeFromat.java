package com.vitap.aluminireconnect;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.Locale;

public class PrettytimeFromat {

    public PrettytimeFromat() {
    }

    public String Ago(String time) {
        PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
        Date date = new Date(time);
        return prettyTime.format(date);
    }



}
