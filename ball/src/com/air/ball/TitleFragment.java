package com.air.ball;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by KL on 2015/5/10.
 */
public class TitleFragment extends Fragment {
    private ImageButton mLeftMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.titlefragment,container,false);
        mLeftMenu = (ImageButton)view.findViewById(R.id.id_title_left_btn);
        mLeftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),
                        "i am an ImageButton in TitleFragment !",
                        Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
