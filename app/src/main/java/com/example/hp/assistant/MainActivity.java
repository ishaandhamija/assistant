package com.example.hp.assistant;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends ActionBarActivity implements TextToSpeech.OnInitListener {

    TextToSpeech tts;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    String text="";
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  tts=new TextToSpeech(this,this);
        btn=(Button)findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Not Supported", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    text=result.get(0);

                    Toast.makeText(this,text, Toast.LENGTH_SHORT).show();

                   if(FA.match("call",text) || FA.match("connect me to",text) || FA.match("connect to",text))
                    {
                        CallingActivity cl=new CallingActivity(this,text);
                        if(cl.makeCall())
                        {
                            Toast.makeText(this,"Calling...!!!",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(this,"Please try again ...!!!",Toast.LENGTH_SHORT).show();

                    }
                    else if(FA.match("message",text) || FA.match("text",text) || FA.match("tell",text) || FA.match("inform",text))
                    {
                        MessagingActivity ma=new MessagingActivity(this,text);
                        if(ma.sendMessage())
                        {
                            Toast.makeText(this,"Messaging...!!!",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(this,"Please try again ...!!!",Toast.LENGTH_SHORT).show();
                    }
                    else if(FA.match("open",text) || FA.match("launch",text))
                    {
                        NewAppActivity napp=new NewAppActivity(this,text);
                        if(napp.openActivty())
                        {
                            Toast.makeText(this,"Opening app...!!!",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(this,"Please try again ...!!!",Toast.LENGTH_SHORT).show();
                    }
                    else if(FA.match("alarm",text) || FA.match("wake",text))
                    {
                        AlarmActivity al=new AlarmActivity(this,text);
                        if(al.setAlarm())
                        {
                            Toast.makeText(this,"Setting alarm...!!!",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(this,"Please try again ...!!!",Toast.LENGTH_SHORT).show();
                    }
                    else if(FA.match("play",text) || FA.match("listen",text))
                    {
                        MediaPlayerActivity mpa=new MediaPlayerActivity(this,text);
                        if(mpa.playMedia())
                        {
                            Toast.makeText(this,"Playing media...!!!",Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(this,"Please try again ...!!!",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(getApplicationContext(),"not recog "+text, Toast.LENGTH_SHORT).show();

                break;
            }

        }
    }


    @Override
    public void onInit(int status) {
        if(status==TextToSpeech.SUCCESS)
        {
            int result=tts.setLanguage(Locale.US);
            if(result==TextToSpeech.LANG_MISSING_DATA || result==TextToSpeech.LANG_NOT_SUPPORTED)
            {
            Toast.makeText(this,"Some problem occurred, try again later...!!!",Toast.LENGTH_SHORT).show();
                finish();
            }
            else
            {
            tts.speak("Hey, what can I do for you",TextToSpeech.QUEUE_FLUSH,null);
                promptSpeechInput();
            }
        }
        else
        {
            Toast.makeText(this,"Some problem occurred, try again later...!!!",Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
