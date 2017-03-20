package com.example.hp.assistant;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

public class AlarmActivity {

    private AlarmManager am;
    Context cont;
    Calendar cal;
    int hr=0,min=0;


    public AlarmActivity(Context con,String text)
    {
        try {
            int i=0,j=0,f=0;
            cont = con;
            Calendar tempcal=Calendar.getInstance();
            cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            tempcal.setTimeInMillis(System.currentTimeMillis());

            for (i = 1; i <= 12; i++) {
                f=0;
                for (j = 1; j <= 59; j++) {
                    if (RK.match(" "+i + ":" + j+" ", text,13) || RK.match(" "+i + " " + j+" ", text,13)) {
                        hr = i;
                        min = j;
                        f=1;
                        break;
                    }

                }
                if (RK.match(" "+i + " ", text,13) && f!=1) {
                    hr = i;
                    min = 0;
                    break;
                }
            }

            if (RK.match(" a.m.", text,13) || RK.match(" a m", text,13)) {
                cal.set(Calendar.HOUR_OF_DAY, hr);
            } else if (RK.match(" p.m.", text,13) || RK.match(" p m", text,13)) {
                cal.set(Calendar.HOUR_OF_DAY, hr + 12);
            }
            cal.set(Calendar.MINUTE, min);
            cal.set(Calendar.SECOND, 0);

            if(cal.compareTo(tempcal)<=0)
            {
                cal.add(Calendar.DATE,1);
            }

        }
        catch (Exception e)
        {
            Toast.makeText(cont, "AlarmAct Ecxp1", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean setAlarm()
    {
        try {

            if(hr==0 && min==0)
                return false;

            Intent in = new Intent(cont, AlarmReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(cont,1, in, 0);

            am = (AlarmManager) cont.getSystemService(Context.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
            Toast.makeText(cont, "Setting alarm for "+cal.getTime().toString(), Toast.LENGTH_SHORT).show();

            return true;
        }
        catch (Exception e)
        {
            Toast.makeText(cont, "AlarmAct Ecxp2+", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
