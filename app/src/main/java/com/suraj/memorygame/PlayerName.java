package com.suraj.memorygame;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;


public class PlayerName extends Fragment {

    private EditText playerName_et;
    private Button btnPlay;
    private String playerName;




    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player_name, container, false);

                playerName_et = (EditText)rootView.findViewById(R.id.name_et);


        Button btnFragment = (Button) rootView.findViewById(R.id.btnPlay);
        btnFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              playerName = playerName_et.getText().toString();

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

//    private EditText memail_et,muser_name_et,mphone_et;
//    private Button sign_up_btn;
//    private String memail,muser_name,mphone_number;
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View rootView=inflater.inflate(R.layout.fragment_main_activity, container, false);
//        memail_et=(EditText)rootView.findViewById(R.id.email_et);
//        muser_name_et=(EditText)rootView.findViewById(R.id.user_name_et);
//        mphone_et=(EditText)rootView.findViewById(R.id.mobile_no_et);
//        sign_up_btn=(Button)rootView.findViewById(R.id.sign_up_btn);
//        sign_up_btn.setOnClickListener(mClickListener);
//        return rootView;
//    }
//    View.OnClickListener mClickListener=new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//            memail=memail_et.getText().toString();
//            muser_name=muser_name_et.getText().toString();
//            mphone_number=mphone_et.getText().toString();
//            FragmentTransaction transection=getFragmentManager().beginTransaction();
//            SecondFragment mfragment=new SecondFragment();

////using Bundle to send data
//            Bundle bundle=new Bundle();
//            bundle.putString("email",memail);
//            bundle.putString("user_name",muser_name);
//            bundle.putString("phone",mphone_number);
//            mfragment.setArguments(bundle); //data being send to SecondFragment
//            transection.replace(R.id.main_fragment, mfragment);
//            transection.commit();
//        }
//    }
//}
