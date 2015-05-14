package com.air.ball;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MainActivity extends Activity implements View.OnClickListener {

    private ImageButton btn1,btn2,btn3,btn4;
    private FriendFrag friend;
    private SettingFrag setting;
    private Fragment currFrag = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Fresco.initialize(MainActivity.this);
        setContentView(R.layout.activity_main);
        btn1 = (ImageButton)findViewById(R.id.id_btn1);
        btn2 = (ImageButton)findViewById(R.id.id_btn2);
        btn3 = (ImageButton)findViewById(R.id.id_btn3);
        btn4 = (ImageButton)findViewById(R.id.id_btn4);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);

        if(savedInstanceState == null)
            setDefaultFragment();
    }

    public void setDefaultFragment(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        friend = new FriendFrag();
        transaction.replace(R.id.id_fragment_content,friend);
        transaction.commit();
        currFrag = friend;

    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        switch (v.getId()){
            case R.id.id_btn1:
                transaction.hide(currFrag);
                if(friend == null) {
                    friend = new FriendFrag();
                    transaction.add(R.id.id_fragment_content, friend);
                }
                else
                    transaction.show(friend);
                currFrag = friend;
                break;
            case R.id.id_btn2:
                transaction.hide(currFrag);
                if(setting == null) {
                    setting = new SettingFrag();
                    transaction.add(R.id.id_fragment_content, setting);
                }else {
                    transaction.show(setting);
                }
                currFrag = setting;
                break;
        }
        transaction.commit();
    }


}
