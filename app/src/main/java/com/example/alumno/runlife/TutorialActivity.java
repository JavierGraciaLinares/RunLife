package com.example.alumno.runlife;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.alumno.runlife.adapters.SlideAdapter;

public class TutorialActivity extends AppCompatActivity {

    private ViewPager viewPagerTutorial;
    private LinearLayout dotsLayoutTutorial;
    private SlideAdapter slideAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        viewPagerTutorial = (ViewPager)findViewById(R.id.slideWiewPagerTutorial);
        dotsLayoutTutorial = (LinearLayout)findViewById(R.id.dotsLayoutTutorial);

        slideAdapter = new SlideAdapter(this);
        viewPagerTutorial.setAdapter(slideAdapter);
    }

    public void addDotsIndicator(){

    }
}
