package com.abitalo.www.gogetit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Lancelot on 2016/3/17.
 */
public class FavoriteFragment extends Fragment {

    View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorite,container,false);
        return view;
    }
}
