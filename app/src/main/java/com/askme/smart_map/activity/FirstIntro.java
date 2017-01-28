package com.askme.smart_map.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.askme.mapmyday.R;
import com.askme.smart_map.activity.HomePageBuilder.StartupSlide;
import com.github.paolorotolo.appintro.AppIntro;

public class FirstIntro extends AppIntro {
    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(StartupSlide.newInstance(R.layout.intro));
        addSlide(StartupSlide.newInstance(R.layout.intro2));
        addSlide(StartupSlide.newInstance(R.layout.intro3));
        addSlide(StartupSlide.newInstance(R.layout.intro4));
    }

    private void loadMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNextPressed() {
    }

    @Override
    public void onSkipPressed() {
        loadMainActivity();
    }

    @Override
    public void onDonePressed() {
        loadMainActivity();
    }

    @Override
    public void onSlideChanged() {
    }

    public void getStarted(View v) {
        loadMainActivity();
    }
}
