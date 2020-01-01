package com.example.doandd;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelper  extends SQLiteOpenHelper {

    private  Context mContext;

    public static final String DATABASE_NAME = "mew.db";
    public static final int DATABASE_VERSION = 1;

    private String DATABASE_LOCATION = "";
    private String DATABASE_FULLPATH = "";

    private final String TBL_EN_VI = "en_vi";
    private final String TBL_VI_EN = "vi_en";
   // private final String TBL_SAVEWORD = "saveword";

    private final String COL_KEY = "key";
    private final String COL_VALUE = "value";
    private final String COL_HTML = "html";
    private final String COL_PRONOUNCE = "pronounce";
   // private final String COL_ID= "id";

    public SQLiteDatabase mDB;

    public DBHelper(Context context){
        super(context,DATABASE_NAME, null,DATABASE_VERSION);
        mContext =context;

        DATABASE_LOCATION = "data/data/"+mContext.getPackageName()+"/database/";
        DATABASE_FULLPATH = DATABASE_LOCATION + DATABASE_NAME;

        if(!isExistingDB())
        {
            try {
               File dbLocation = new File(DATABASE_LOCATION);
               dbLocation.mkdir();

               extractAssetToDatabaseDirectory(DATABASE_NAME);
            } catch (IOException e)  {
                e.printStackTrace();
            }

        }

        mDB = SQLiteDatabase.openOrCreateDatabase(DATABASE_FULLPATH,null);

    }

    boolean isExistingDB()
    {
        File file = new File(DATABASE_FULLPATH);
        return file.exists();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void extractAssetToDatabaseDirectory(String fileName)
        throws IOException {
        int length;
        InputStream sourceDatabase = this.mContext.getAssets().open(fileName);
        File destinationPath = new File(DATABASE_FULLPATH);
        OutputStream destination = new FileOutputStream(destinationPath);

        byte[] buffer = new byte[4096];
        while ((length = sourceDatabase.read(buffer))>0)
        {
            destination.write(buffer,0,length);
        }

        sourceDatabase.close();
        destination.flush();
        destination.close();
    }

    public ArrayList<String> getWord(int dicType)
    {
        String tableName = getTableName(dicType);
            String q ="select * from " + tableName;
            Cursor result = mDB.rawQuery(q,null);

            ArrayList<String> source = new ArrayList<>();
            while (result.moveToNext()){
                source.add(result.getString(result.getColumnIndex(COL_KEY)));
            }

            return source;
    }

    public Words getWords(String key, int dicType)
    {
        String tableName = getTableName(dicType);
        String q ="select * from " + tableName+ " where upper([key]) = upper(?);";
        Cursor result = mDB.rawQuery(q,new String [] {key});

        Words word = new Words();
        while (result.moveToNext()){
            //word.id = result.getString(result.getColumnIndex(COL_ID));
            word.key = result.getString(result.getColumnIndex(COL_KEY));
            word.value = result.getString(result.getColumnIndex(COL_VALUE));
            word.html = result.getString(result.getColumnIndex(COL_HTML));
            word.pronounce = result.getString(result.getColumnIndex(COL_PRONOUNCE));
        }

        return word;
    }

    public void addSavingWords(Words word)
    {
        try {
            String q = " insert into saveword ([" +COL_KEY+ "],[" +COL_HTML+ "],[" +COL_VALUE+ "],[" +COL_PRONOUNCE+ "]) VALUES (?,?,?,?);";
            mDB.execSQL(q,new Object[]{word.key,word.html,word.value,word.pronounce});
        }
        catch (SQLException ex) {

        }

    }

    public void delSavingWords(Words word)
    {
        try {
            String q = " delete from saveword where upper(["+COL_KEY+"]) = upper(?) and ["+COL_HTML+"] = ? and ["+COL_VALUE+"] = ? and ["+COL_PRONOUNCE+"] = ?;";
            mDB.execSQL(q,new Object[]{word.key,word.html,word.value,word.pronounce});
        }
        catch (SQLException ex) {

        }

    }


    public void delSavingWords(String key)
    {
        try {
            String q = " delete from saveword where upper(["+COL_KEY+"]) = upper(?);";
            mDB.execSQL(q,new Object[]{key});
        }
        catch (SQLException ex) {

        }

    }


    public String getTableName(int dicType)
    {
        String tableName = "";
        if(dicType==R.id.action_en_vi)
        {
            tableName = TBL_EN_VI;
        }
        else if(dicType==R.id.action_vi_en)
        {
          tableName = TBL_VI_EN;
        }

        return tableName;
    }

    public ArrayList<String> getAllWordsFromSavewords()
    {
        String q ="select * from  saveword order by [date] desc;";
        Cursor result = mDB.rawQuery(q,null);

        ArrayList<String> source = new ArrayList<>();
        while (result.moveToNext()){
            source.add(result.getString(result.getColumnIndex(COL_KEY)));
        }

        return source;
    }

    public boolean isWordMark(Words word) {

        String q ="select * from saveword where upper([key]) = upper(?) and [html] = ? and [value] = ? and [pronounce] = ? ;";
        Cursor result = mDB.rawQuery(q,new String[] {word.key,word.html,word.value,word.pronounce}); //null

        return result.getCount() >0;

    }

    public Words getWordFromSaveWords(String key){
        String q ="select * from saveword where upper([key]) = upper(?) ;";
        Cursor result = mDB.rawQuery(q,new String[]{key});
        Words word = null;
        while(result.moveToNext())
        {
            word = new Words();
            //word.id = result.getString(result.getColumnIndex(COL_ID));
            word.key = result.getString(result.getColumnIndex(COL_KEY));
            word.value = result.getString(result.getColumnIndex(COL_VALUE));
            word.html = result.getString(result.getColumnIndex(COL_HTML));
            word.pronounce = result.getString(result.getColumnIndex(COL_PRONOUNCE));
        }

        return word;
    }

    public void clearSavingWords() {
        try {
            String q = "delete from saveword ";
            mDB.execSQL(q);
        }
        catch (SQLException ex) {

        }
    }
}
