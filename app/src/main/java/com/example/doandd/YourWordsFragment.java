package com.example.doandd;

import android.content.Context;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

public class YourWordsFragment extends Fragment{

    private FragmentListener listener;
    private  DBHelper mDBHelper;
    private YourWordsAdapter adapter;

    public YourWordsFragment() {

    }

    public static YourWordsFragment getNewInstance(DBHelper dbHelper)
    {
        YourWordsFragment fragment = new YourWordsFragment();
        fragment.mDBHelper = dbHelper;
        return  fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_your_words, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);



        ListView yourwordsList =  view.findViewById(R.id.yourwordsList);
        adapter = new YourWordsAdapter(getActivity(),mDBHelper.getAllWordsFromSavewords());

        yourwordsList.setAdapter(adapter);


        adapter.setOnItemClick(new ListItemListener() {
            @Override
            public void onItemClick(int position) {
                if(listener!=null)
                   listener.onItemClick(String.valueOf(adapter.getItem(position)));
            }
        });

        adapter.setOnItemDeleteClick(new ListItemListener() {
            @Override
            public void onItemClick(int position) {
                String key = (String) adapter.getItem(position);
                mDBHelper.delSavingWords(key);
                adapter.removeItem(position);
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setOnFragmentListener(FragmentListener listener){
        this.listener = listener;
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_clear,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_clear)
        {
            mDBHelper.clearSavingWords();
            adapter.clear();
            adapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }
}

