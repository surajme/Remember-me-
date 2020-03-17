package com.suraj.memorygame;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    private EditText playerName_et;
    private Button btnPlay;
    private String playerName;
    private static final String TAG = "MainActivity";





    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player_name, container, false);


        playerName_et = (EditText)rootView.findViewById(R.id.name_et);
//        String xyz = playerName_et.getText().toString();
        Button btnFragment = (Button) rootView.findViewById(R.id.btnPlay);




//        if(playerName.isEmpty()) {
//            btnFragment.setEnabled(false);
//        } else {
//            btnFragment.setEnabled(true);
//        }


        btnFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              playerName = playerName_et.getText().toString();

                if(TextUtils.isEmpty(playerName)) {
                    playerName_et.setError("Don't act smart!");
                    return;
                }else {
                    //no content
                }

                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.layoutFragment, new EasyLevel());
                fr.commit();


                //Bundle to send data
                Bundle bundle = new Bundle();
                bundle.putString("Name", playerName);
                setArguments(bundle);

            }
        });


        return rootView;
    }



}

