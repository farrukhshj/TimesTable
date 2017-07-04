package com.farrukh.timestable;

import android.annotation.TargetApi;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.IntegerRes;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private int maxTables=20;       //max tables to calculate i.e 1 to 20
    private int minTable=1;         //min tables to calculate i.e 1 to 20
    private int defaultTable=10;    //when you start application this table is displayed
    private int maxValforTable=10;  //no.of values to be calculated per table

    private ArrayList<String> timesTableData; //array list to store data

    protected SeekBar timesTableSeekBar;
    protected ListView timesTableListView;
    protected TextToSpeech textToSpeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timesTableListView=(ListView)findViewById(R.id.timeTableListView);
        timesTableSeekBar=(SeekBar)findViewById(R.id.timesTableSeekBar);

        timesTableSeekBar.setMax(maxTables);
        timesTableSeekBar.setProgress(defaultTable);
        timesTableSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
               if(i < minTable)
               {
                   i=1;
                   timesTableSeekBar.setProgress(i);
               }
               else
               {
                    calculateTables(i);
               }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        calculateTables(defaultTable);
        textToSpeech=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });
    }

    public void calculateTables(int whichTable)
    {

        timesTableData=new ArrayList<String>();
        for(int x=1;x<=maxValforTable;x++)
        {
            timesTableData.add(whichTable+" times "+x+" is "+Integer.toString(x*whichTable));
        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(this,R.layout.custom_listview,timesTableData);
        timesTableListView.setAdapter(arrayAdapter);
    }


    public void speakText(View view)
    {
        TextView temp=(TextView)view;
        String text=temp.getText().toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ttsGreater21(text);
        } else {
            ttsUnder20(text);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void ttsGreater21(String text)
    {
        String utteranceId=this.hashCode() + "";
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    @SuppressWarnings("deprecation")
    public void ttsUnder20(String text)
    {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,map);
    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
