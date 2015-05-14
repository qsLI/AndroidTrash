package com.air.ball;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by KL on 2015/5/10.
 */
public class SettingFrag extends Fragment {
    private TextView txtContent;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contentfragment, container, false);
        return view;
    }
}
