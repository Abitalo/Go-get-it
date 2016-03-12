package com.abitalo.www;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mikepenz.materialdrawer.DrawerBuilder;

/**
 * Created by Lancelot on 2016/3/12.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new DrawerBuilder().withActivity(this).build();
    }
}
