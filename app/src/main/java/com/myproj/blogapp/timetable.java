package com.myproj.blogapp;

import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



/**
 * Created by user on 8/9/2017.
 */


public class timetable extends AppCompatActivity {

    private TextView text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);
        Calendar cal = Calendar.getInstance();

        int thisMonth = cal.get(Calendar.MONTH);

        InputStream is = null;


        if (thisMonth == Calendar.JANUARY) {
            is = getResources().openRawResource(R.raw.salah_jan);

        } else if (thisMonth == Calendar.FEBRUARY) {
            is = getResources().openRawResource(R.raw.salah_feb);

        } else if (thisMonth == Calendar.MARCH) {
            is = getResources().openRawResource(R.raw.salah_mrch);

        } else if (thisMonth == Calendar.APRIL) {
            is = getResources().openRawResource(R.raw.salah_apr);

        } else if (thisMonth == Calendar.MAY) {
            is = getResources().openRawResource(R.raw.salah_may);

        } else if (thisMonth == Calendar.JUNE) {
            is = getResources().openRawResource(R.raw.salah_jun);

        } else if (thisMonth == Calendar.JULY) {
            is = getResources().openRawResource(R.raw.salah_jul);

        } else if (thisMonth == Calendar.AUGUST) {
            is = getResources().openRawResource(R.raw.salah_aug);

        } else if (thisMonth == Calendar.SEPTEMBER) {
            is = getResources().openRawResource(R.raw.salah_sep);

        } else if (thisMonth == Calendar.OCTOBER) {
            is = getResources().openRawResource(R.raw.salah_oct);

        } else if (thisMonth == Calendar.NOVEMBER) {
            is = getResources().openRawResource(R.raw.salah_nov);

        } else if (thisMonth == Calendar.DECEMBER) {
            is = getResources().openRawResource(R.raw.salah_dec);

        }

        TextView dateSalah = (TextView) findViewById(R.id.salah);
        String date = new SimpleDateFormat("d MMM").format(new Date());
        dateSalah.setText(date);


        Writer writer = new StringWriter();
        char[] buffer = new char[1024];


        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();

        //fajr string
        String fjrBegin = "";
        String fjrJamat = "";

        //zuhur string
        String zhrBegin = "";
        String zhrJamat = "";

        //asr string
        String aBegin = "";
        String aJamat = "";

        //magrib string
        String mgrbBegin = "";
        String mgrbJamat = "";

        //eisha string
        String eshaBegin = "";
        String eshaJamat = "";


        //fajr
        TextView fajrBegin = (TextView) findViewById(R.id.fajr_begins);
        TextView fajrJamat = (TextView) findViewById(R.id.fajr_jamat);

        //zuhur
        TextView zuhurBegin = (TextView) findViewById(R.id.zuhur_begin);
        TextView zuhurJamat = (TextView) findViewById(R.id.zuhur_jamat);

        //asr
        TextView asrBegin = (TextView) findViewById(R.id.asr_begin);
        TextView asrJamat = (TextView) findViewById(R.id.asr_jamat);

        //magrib
        TextView magribBegin = (TextView) findViewById(R.id.magrib_begin);
        TextView magribJamat = (TextView) findViewById(R.id.magrib_jamat);

        //eisha
        TextView eishaBegin = (TextView) findViewById(R.id.eisha_begin);
        TextView eishaJamat = (TextView) findViewById(R.id.eisha_jamat);

        //err.setText(jsonString);

        try {
            JSONObject array = new JSONObject(jsonString);
            JSONArray jNode = null;

            if (thisMonth == Calendar.AUGUST) {
                jNode = array.optJSONArray("salah_august");

            } else if (thisMonth == Calendar.JANUARY) {

            }


            if (thisMonth == Calendar.JANUARY) {
                jNode = array.optJSONArray("salah_january");

            } else if (thisMonth == Calendar.FEBRUARY) {
                jNode = array.optJSONArray("salah_febuary");

            } else if (thisMonth == Calendar.MARCH) {
                jNode = array.optJSONArray("salah_march");

            } else if (thisMonth == Calendar.APRIL) {
                jNode = array.optJSONArray("salah_april");

            } else if (thisMonth == Calendar.MAY) {
                jNode = array.optJSONArray("salah_may");

            } else if (thisMonth == Calendar.JUNE) {
                jNode = array.optJSONArray("salah_june");

            } else if (thisMonth == Calendar.JULY) {
                jNode = array.optJSONArray("salah_july");

            } else if (thisMonth == Calendar.AUGUST) {
                jNode = array.optJSONArray("salah_august");

            } else if (thisMonth == Calendar.SEPTEMBER) {
                jNode = array.optJSONArray("salah_september");

            } else if (thisMonth == Calendar.OCTOBER) {
                jNode = array.optJSONArray("salah_october");

            } else if (thisMonth == Calendar.NOVEMBER) {
                jNode = array.optJSONArray("salah_november");

            } else if (thisMonth == Calendar.DECEMBER) {
                jNode = array.optJSONArray("salah_december");

            }


            int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
            int today = dayOfMonth - 1;
            for (int i = today; i < dayOfMonth; i++) {


                JSONObject childObject = jNode.getJSONObject(today);

                //fajr
                fjrBegin += childObject.optString("fajrstart");
                fjrJamat += childObject.optString("fajrjamat");

                //zuhur
                zhrBegin += childObject.optString("zuhurstart");
                zhrJamat += childObject.optString("zuhurjamat");

                //asr
                aBegin += childObject.optString("asrstart");
                aJamat += childObject.optString("asrjamat");

                //magrib
                mgrbBegin += childObject.optString("magribstart");
                mgrbJamat += childObject.optString("magribstart");

                //eisha
                eshaBegin += childObject.optString("ishastart");
                eshaJamat += childObject.optString("ishaend");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        //fajr
        fajrBegin.setText(fjrBegin);
        fajrJamat.setText(fjrJamat);

        //zuhur
        zuhurBegin.setText(zhrBegin);
        zuhurJamat.setText(zhrJamat);

        //asr
        asrBegin.setText(aBegin);
        asrJamat.setText(aJamat);

        //magrib
        magribBegin.setText(mgrbBegin);
        magribJamat.setText(mgrbJamat);

        //eisha
        eishaBegin.setText(eshaBegin);
        eishaJamat.setText(eshaJamat);


        Date userDate = new Date();


        //Current date variable for each salah
        Date currentDateFajr = null;
        Date currentDateEisha = null;
        Date currentDateZuhur = null;
        Date currentDateMagrib = null;
        Date currentDateAsr = null;


        TableRow tableFajr = (TableRow) findViewById(R.id.fajr);
        TableRow tableZuhur = (TableRow) findViewById(R.id.zuhur);
        TableRow tableAsr = (TableRow) findViewById(R.id.asr);
        TableRow tableMarib = (TableRow) findViewById(R.id.magrib);
        TableRow tableEisha = (TableRow) findViewById(R.id.eisha);


        try {
            currentDateFajr = new SimpleDateFormat("HH:mm").parse(fjrBegin);
            currentDateZuhur = new SimpleDateFormat("HH:mm").parse(zhrBegin);
            currentDateAsr = new SimpleDateFormat("HH:mm").parse(aBegin);
            currentDateMagrib = new SimpleDateFormat("HH:mm").parse(mgrbBegin);
            currentDateEisha = new SimpleDateFormat("HH:mm").parse(eshaBegin);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        //Change in salah rows
        final String timeStamp = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(timeStamp);
        } catch (ParseException e) {
            //TODO Auto-generated catch block
            e.printStackTrace();
        }


        //fajr
        if (convertedDate.getTime() <= currentDateZuhur.getTime() && convertedDate.getTime() >= currentDateFajr.getTime()) {
            //set zuhur green
            tableFajr.setBackgroundResource(R.drawable.right);
            fajrBegin.setTypeface(Typeface.DEFAULT_BOLD);
            fajrJamat.setTypeface(Typeface.DEFAULT_BOLD);


            //set all salah deafualt
            tableMarib.setBackgroundResource(R.drawable.my_shape_file);
            tableZuhur.setBackgroundResource(R.drawable.my_shape_file);
            tableAsr.setBackgroundResource(R.drawable.my_shape_file);
            tableEisha.setBackgroundResource(R.drawable.my_shape_file);

        }


        //zuhur
        if (convertedDate.getTime() <= currentDateAsr.getTime() && convertedDate.getTime() >= currentDateZuhur.getTime()) {
            //set zuhur green
            tableZuhur.setBackgroundResource(R.drawable.right);
            zuhurBegin.setTypeface(Typeface.DEFAULT_BOLD);
            zuhurJamat.setTypeface(Typeface.DEFAULT_BOLD);

            //set all salah deafualt
            tableMarib.setBackgroundResource(R.drawable.my_shape_file);
            tableAsr.setBackgroundResource(R.drawable.my_shape_file);
            tableFajr.setBackgroundResource(R.drawable.my_shape_file);
            tableEisha.setBackgroundResource(R.drawable.my_shape_file);

        }


        //asr
        if (convertedDate.getTime() <= currentDateMagrib.getTime() && convertedDate.getTime() >= currentDateAsr.getTime()) {
            //set asr green
            tableAsr.setBackgroundResource(R.drawable.right);
            asrBegin.setTypeface(Typeface.DEFAULT_BOLD);
            asrJamat.setTypeface(Typeface.DEFAULT_BOLD);

            //set all salah deafualt
            tableMarib.setBackgroundResource(R.drawable.my_shape_file);
            tableZuhur.setBackgroundResource(R.drawable.my_shape_file);
            tableFajr.setBackgroundResource(R.drawable.my_shape_file);
            tableEisha.setBackgroundResource(R.drawable.my_shape_file);

        }

        //magrib
        if (convertedDate.getTime() <= currentDateEisha.getTime() && convertedDate.getTime() >= currentDateMagrib.getTime())
        //if (currentDateMagrib.before(userDate) &&  userDate.after(currentDateAsr))
        {
            //set magrib green
            tableMarib.setBackgroundResource(R.drawable.right);
            magribBegin.setTypeface(Typeface.DEFAULT_BOLD);
            magribJamat.setTypeface(Typeface.DEFAULT_BOLD);


            //set all salah deafualt
            tableZuhur.setBackgroundResource(R.drawable.my_shape_file);
            tableAsr.setBackgroundResource(R.drawable.my_shape_file);
            tableFajr.setBackgroundResource(R.drawable.my_shape_file);
            tableEisha.setBackgroundResource(R.drawable.my_shape_file);

        }

        //eisha
        if (convertedDate.getTime() >= currentDateEisha.getTime() || convertedDate.getTime() <= currentDateFajr.getTime()) {
            //set eisha green
            tableEisha.setBackgroundResource(R.drawable.right);
            eishaBegin.setTypeface(Typeface.DEFAULT_BOLD);
            eishaJamat.setTypeface(Typeface.DEFAULT_BOLD);

            //set all salah deafualt
            tableMarib.setBackgroundResource(R.drawable.my_shape_file);
            tableZuhur.setBackgroundResource(R.drawable.my_shape_file);
            tableFajr.setBackgroundResource(R.drawable.my_shape_file);
            tableAsr.setBackgroundResource(R.drawable.my_shape_file);

        }


    }


}




