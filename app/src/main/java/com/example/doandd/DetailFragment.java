package com.example.doandd;

import android.app.ActionBar;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.TooManyListenersException;


public class DetailFragment extends Fragment {

    private String value = "";
    private TextView tvWord;
    private ImageButton btnBookmark,btnVolume;
    private WebView tvWordTranslate;
    private DBHelper mDBHelper;
    private int mDicType;
    private TextToSpeech mTTS;

    public DetailFragment() {

    }

    public static DetailFragment getNewInstance(String value,DBHelper dbHelper, int dicType) {
        DetailFragment fragment = new DetailFragment();
        fragment.value = value;
        fragment.mDBHelper = dbHelper;
        fragment.mDicType = dicType;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvWord = (TextView) view.findViewById(R.id.tvWord);
        tvWordTranslate = (WebView) view.findViewById(R.id.tvWordTranslate);
        btnBookmark = (ImageButton) view.findViewById(R.id.btnBookmark);
        btnVolume = (ImageButton) view.findViewById(R.id.btnVolume);


        final Words words = mDBHelper.getWords(value,mDicType);
        tvWord.setText(words.key);
        tvWordTranslate.loadDataWithBaseURL(null,words.html,"text/html","utf-8",null);

       Words bookmarkWord =  mDBHelper.getWordFromSaveWords(value);
       int isMark = bookmarkWord == null? 0:1;

        btnBookmark.setTag(isMark);

        int icon = bookmarkWord ==null?R.drawable.ic_star_border:R.drawable.ic_star_black_fill;

        btnBookmark.setImageResource(icon);

        btnBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int i = (int) btnBookmark.getTag();
               if(i==0)
               {
                   btnBookmark.setImageResource(R.drawable.ic_star_black_fill);
                   btnBookmark.setTag(1);
                   mDBHelper.addSavingWords(words);
               } else if (i==1) {
                   btnBookmark.setImageResource(R.drawable.ic_star_border);
                   btnBookmark.setTag(0);
                   mDBHelper.delSavingWords(words);
               }
            }
        });


        mTTS = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.UK);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Log.e("TSS", "Language not supported");
                    } else {
                        btnVolume.setEnabled(true);
                    }
                } else
                {
                    Log.e("TTS","Init failed");
                }

            }
        });
        btnVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = tvWord.getText().toString();
                mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null,null);
            }
        });
    }

    @Override
    public void onDestroy() {
        if (mTTS !=null)
        {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

  @Override
   public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}
