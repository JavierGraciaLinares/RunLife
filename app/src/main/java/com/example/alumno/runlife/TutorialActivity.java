package com.example.alumno.runlife;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.alumno.runlife.adapters.SlideAdapterTutorial;

public class TutorialActivity extends AppCompatActivity {

    private ViewPager viewPagerTutorial;
    private LinearLayout dotsLayoutTutorial;
    private SlideAdapterTutorial slideAdapterTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        viewPagerTutorial = (ViewPager)findViewById(R.id.slideWiewPagerTutorial);
        dotsLayoutTutorial = (LinearLayout)findViewById(R.id.dotsLayoutTutorial);

        slideAdapterTutorial = new SlideAdapterTutorial(this);
        viewPagerTutorial.setAdapter(slideAdapterTutorial);
    }

    public void addDotsIndicator(){

    }
}
