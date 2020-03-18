package com.suraj.memorygame;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;


public class PlayerName extends Fragment {

    EditText playerName_et;
    Button btnPlay;
    String playerName;



    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player_name, container, false);


        playerName_et = (EditText) rootView.findViewById(R.id.name_et);
        Button btnFragment = (Button) rootView.findViewById(R.id.btnPlay);





        btnFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String playerName = playerName_et.getText().toString();

                if(TextUtils.isEmpty(playerName)) {
                    playerName_et.setError("Enter some value!!");
                    return;

                }else {

                    //Bundle to send data
                    Bundle bundle = new Bundle();
                    bundle.putString("Name", playerName);
                    FragmentTransaction fr = getFragmentManager().beginTransaction();

                    EasyLevel mfragment = new EasyLevel();
                    mfragment.setArguments(bundle);
                    fr.replace(R.id.layoutFragment, mfragment);
                    fr.commit();

                }

            }
        });


        return rootView;
    }



}

