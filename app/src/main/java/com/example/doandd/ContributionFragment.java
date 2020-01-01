package com.example.doandd;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContributionFragment extends Fragment {


    Button btok;
    EditText etContribution;

    public ContributionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contribution, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        btok = (Button) view.findViewById(R.id.btnOk);
        etContribution = (EditText)view.findViewById((R.id.text_contribution));
        btok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contribute = etContribution.getText().toString();
                if(contribute!=null && !contribute.isEmpty()) {
                    Toast.makeText(getContext(), "Thank you <3", Toast.LENGTH_SHORT).show();
                    etContribution.setText("");
                }
                else
                    Toast.makeText(getContext(),"Say something ...",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
