package com.example.doandd;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    MenuItem menuSetting;
    Toolbar toolbar;

    DBHelper dbHelper;

    DictionaryFragment dictFragment;
    YourWordsFragment yourwordsFragment;
    HelpFragment helpFragment;
    ContributionFragment contributionFragment;
    AboutUsFragment aboutUsFragment;

 //   TextToSpeech mTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dictFragment = new DictionaryFragment();
        yourwordsFragment = YourWordsFragment.getNewInstance(dbHelper);
        helpFragment = new HelpFragment();
        contributionFragment = new ContributionFragment();
        aboutUsFragment = new AboutUsFragment();


        goToFragment(dictFragment,true);

        dictFragment.setOnFragmentListener(new FragmentListener() {
            @Override
            public void onItemClick(String value) {
                String id = Global.getState(MainActivity.this,"dic_type");
                int dicType = id == null? R.id.action_en_vi: Integer.valueOf(id); //
                goToFragment(DetailFragment.getNewInstance(value,dbHelper,dicType),false);
            }
        });

        yourwordsFragment.setOnFragmentListener(new FragmentListener() {
            @Override
            public void onItemClick(String value) {
                String id = Global.getState(MainActivity.this,"dic_type");
                int dicType = id == null? R.id.action_en_vi: Integer.valueOf(id);
                goToFragment(DetailFragment.getNewInstance(value,dbHelper,dicType),false);
            }
        });

        EditText edit_search = findViewById(R.id.edit_search);
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dictFragment.filterValue(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        mTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if(status!=TextToSpeech.ERROR)
//                    mTTS.setLanguage(Locale.ENGLISH);
//
//            }
//        });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menuSetting = menu.findItem(R.id.action_settings);
        String id = Global.getState(this,"dic_type");
        if(id!=null)
            onOptionsItemSelected(menu.findItem(Integer.valueOf(id)));
        else {
            ArrayList<String> source = dbHelper.getWord(R.id.action_en_vi);
            dictFragment.resetDataSource(source);
            //DB.getData(R.id.action_en_vi);
        }
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.action_en_vi)
        {
            Global.saveState(this,"dic_type",String.valueOf(id));
            ArrayList<String> source =dbHelper.getWord(id);
           // mTTS.setLanguage(Locale.ENGLISH);
            dictFragment.resetDataSource(source);
            menuSetting.setIcon(getDrawable(R.drawable.uk64));
            return true;
        }
        else if(id==R.id.action_vi_en)
        {
            Global.saveState(this,"dic_type",String.valueOf(id));
            ArrayList<String> source =dbHelper.getWord(id);
            //mTTS.setLanguage(Locale.TRADITIONAL_CHINESE);
            dictFragment.resetDataSource(source);
            menuSetting.setIcon(getDrawable(R.drawable.vn64));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        String activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
        if(id == R.id.action_save){
            if(!activeFragment.equals(YourWordsFragment.class.getSimpleName()))
            {
                goToFragment(yourwordsFragment,false);
            }
        }
        else if(id==R.id.action_help){
            //String activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
            if(!activeFragment.equals(HelpFragment.class.getSimpleName()))
            {
                goToFragment(helpFragment,false);
            }
        }
        else if(id==R.id.action_contribution){
            //String activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
            if(!activeFragment.equals(ContributionFragment.class.getSimpleName()))
            {
                goToFragment(contributionFragment,false);
            }
        }
        else if(id==R.id.action_about_us){
            //String activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
            if(!activeFragment.equals(AboutUsFragment.class.getSimpleName()))
            {
                goToFragment(aboutUsFragment,false);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void goToFragment(Fragment fragment,boolean isTop){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        if(!isTop)
            fragmentTransaction.addToBackStack(null);//String.valueOf(dictFragment));
        fragmentTransaction.commit();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        String activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
        if(activeFragment.equals(YourWordsFragment.class.getSimpleName()))
        {
            menuSetting.setVisible(false);
            toolbar.findViewById(R.id.edit_search).setVisibility(View.GONE);
            toolbar.setTitle("Your Words");
        }
////        else if(activeFragment.equals(HelpFragment.class.getSimpleName()))
////        {
////
////        }
//        else if(activeFragment.equals(ContributionFragment.class.getSimpleName()))
//        {
//            menuSetting.setVisible(false);
//            toolbar.findViewById(R.id.edit_search).setVisibility(View.GONE);
//            toolbar.setTitle("Your Contribution");
//        }
//        else if(activeFragment.equals(AboutUsFragment.class.getSimpleName()))
//        {
//            menuSetting.setVisible(false);
//            toolbar.findViewById(R.id.edit_search).setVisibility(View.GONE);
//            toolbar.setTitle("About Us");
//        }
        else
        {
            menuSetting.setVisible(true);
            toolbar.findViewById(R.id.edit_search).setVisibility(View.VISIBLE);
            toolbar.setTitle("");
        }
        return true;
    }


}
