package com.suraj.memorygame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;


public class Result<runnable> extends Fragment {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int bestEasyScore;
    private TextView playerName;




    public Result() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);

        Button btnFragment = (Button) rootView.findViewById(R.id.btnhome);
        btnFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.layoutFragment, new Start());
                fr.commit();

            }
        });


        pref = getActivity().getSharedPreferences("HighScore",0);
        editor= pref.edit();

        bestEasyScore = pref.getInt(Constants.EASY_HIGH_KEY,22);

        Bundle b=getArguments();
        if (b.getString("Data").equals("win")){

            if (Integer.valueOf(b.get("level").toString()) == Constants.LEVEL_EASY){
                if (Integer.valueOf(b.get("Time").toString()) < bestEasyScore){
                    editor.putInt(Constants.EASY_HIGH_KEY,Integer.valueOf(b.get("Time").toString())).apply();
                    ((TextView) rootView.findViewById(R.id.newhigh)).setText("New High Score!");
                }
            }

            ((TextView) rootView.findViewById(R.id.desc1)).setText("You won!");
            ((TextView) rootView.findViewById(R.id.time)).setText("Your time : "+b.get("Time").toString());
        }

        else{

            ((TextView) rootView.findViewById(R.id.desc1)).setText("Nice  try, but you lost.");
            ((TextView) rootView.findViewById(R.id.time)).setText("Your time : "+b.get("Time").toString());
        }


        return rootView;
    }


}
