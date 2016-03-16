package com.abitalo.www.gogetit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.octicons_typeface_library.Octicons;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnCheckedChangeListener{

    private static Handler handler=new Handler();

    private static TextView mainInfo= null;
    private AccountHeader headerResult = null;//test
    private static final int PROFILE_SETTING = 100000;//test

    public static TextView debugInfo=null;//test

    public static MapView mMapView= null;
    private static AMap mAMap=null;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//      init view
        mMapView=(MapView)findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);

        debugInfo=(TextView)findViewById(R.id.debug_info);// only for debug
        mainInfo=(TextView)findViewById(R.id.main_info);
        if(mAMap == null){
            mAMap=mMapView.getMap();
        }

        Context context = getApplicationContext();
        Map params = new HashMap();
        params.put("map",mAMap);
        params.put("mainInfo",mainInfo);
        params.put("debugInfo",debugInfo);

        new LocationThread(context,params,handler).start();

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        final IProfile profile3 = new ProfileDrawerItem().withName("Max Muster").withEmail("max.mustermann@gmail.com").withIcon(R.drawable.profile2).withIdentifier(102);
        final IProfile profile4 = new ProfileDrawerItem().withName("Felix House").withEmail("felix.house@gmail.com").withIcon(R.drawable.profile3).withIdentifier(103);
        final IProfile profile5 = new ProfileDrawerItem().withName("Mr. X").withEmail("mister.x.super@gmail.com").withIcon(R.drawable.profile4).withIdentifier(104);
        final IProfile profile6 = new ProfileDrawerItem().withName("Batman").withEmail("batman@gmail.com").withIcon(R.drawable.profile5).withIdentifier(105);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header)
//                .withCompactStyle(true)
                .addProfiles(
                        profile3,
                        profile4,
                        profile5,
                        profile6,
                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                        new ProfileSettingDrawerItem().withName("Add Account").withDescription("Add new GitHub Account").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_plus).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(PROFILE_SETTING),
                        new ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(100001)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_SETTING) {
                            int count = 100 + headerResult.getProfiles().size() + 1;
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman" + count).withEmail("batman" + count + "@gmail.com").withIcon(R.drawable.profile5).withIdentifier(count);
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }

                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
        new DrawerBuilder().withActivity(this).withToolbar(toolbar).withAccountHeader(headerResult)
                .addDrawerItems(
                new PrimaryDrawerItem().withName(R.string.drawer_item_home).withDescription(R.string.drawer_item_home_desc).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1).withSelectable(true),
                new PrimaryDrawerItem().withName(R.string.drawer_item_favorite).withDescription(R.string.drawer_item_favorite_desc).withIcon(GoogleMaterial.Icon.gmd_favorite_outline).withIdentifier(2).withSelectable(true),
                new PrimaryDrawerItem().withName(R.string.drawer_item_discovery).withDescription(R.string.drawer_item_discovery_desc).withIcon(GoogleMaterial.Icon.gmd_sun).withIdentifier(3).withSelectable(true),
                new SectionDrawerItem(),
                new SwitchDrawerItem().withName(R.string.drawer_item_night).withIcon(Octicons.Icon.oct_tools).withChecked(false).withOnCheckedChangeListener(this))
        .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                if (drawerItem != null) {
                    Intent intent = null;
                    if (drawerItem.getIdentifier() == 1) {
                        intent = new Intent(MainActivity.this, MainActivity.class);
                    } else if (drawerItem.getIdentifier() == 2) {
                        intent = new Intent(MainActivity.this, FavoriteActivity.class);
                    } else if (drawerItem.getIdentifier() == 3) {
                        intent = new Intent(MainActivity.this, DiscoveryActivity.class);
                    }
                    if (intent != null) {
                        MainActivity.this.startActivity(intent);
                    }
                } return false;
            }
        }).build();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
        setTheme(R.style.Night);
    }
}