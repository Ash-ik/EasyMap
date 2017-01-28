package com.askme.smart_map.activity.HomePageBuilder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.askme.mapmyday.R;
import com.askme.smart_map.activity.AddToFavourite.AddToFavourite;
import com.askme.smart_map.activity.AtlasActivity;
import com.askme.smart_map.activity.GalaryActivity;
import com.askme.smart_map.activity.MainActivity;
import com.askme.smart_map.activity.TaskToDo;
import com.mxn.soul.flowingdrawer_core.LeftDrawerLayout;
import com.mxn.soul.flowingdrawer_core.MenuFragment;


public class MyMenuFragment extends MenuFragment{

    public LeftDrawerLayout leftDrawerLayout;
    public NavigationView vNavigation;
    Context x;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container,
                false);
        vNavigation = (NavigationView) view.findViewById(R.id.vNavigation);
        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                x=getActivity().getApplicationContext();
                ItemSelected(item);
                return false;
            }
        });

        return setupReveal(view);

    }

    private void setupHeader() {
    }

    public void onOpenMenu(){
    }
    public void onCloseMenu(){
    }

    public boolean ItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent i;
        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.menu_home:
/*                    i = new Intent(MainActivity.this, nomaratmanfi.class);
                    startActivity(i);*/
                break;
            case R.id.menu_galary:
                startActivity(new Intent(x, GalaryActivity.class));
                break;
            case R.id.menu_atlas:
                startActivity(new Intent(x, AtlasActivity.class));
                break;
            case R.id.menu_taskToDo:
                startActivity(new Intent(x, TaskToDo.class));
                break;
            case R.id.menu_addToFavourites:
                startActivity(new Intent(x, AddToFavourite.class));
                break;
            case R.id.menu_personalize:
/*
                    i = new Intent(MainActivity.this, Contactus.class);
                    startActivity(i);
*/
                break;
            case R.id.menu_help:
/*
                    i = new Intent(MainActivity.this, Contactus.class);
                    startActivity(i);
*/
                break;
            case R.id.menu_aboutUs:
/*
                    i = new Intent(MainActivity.this, Contactus.class);
                    startActivity(i);
*/
                break;

        }

        return false;
    }

}
