package com.example.alumno.runlife.tutorial;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alumno.runlife.R;

public class TutorialActivity extends AppCompatActivity {

    private ViewPager viewPagerTutorial;
    private LinearLayout dotsLayoutTutorial;
    private SlideAdapterTutorial slideAdapterTutorial;

    private TextView[] mDots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        viewPagerTutorial = (ViewPager)findViewById(R.id.slideWiewPagerTutorial);
        dotsLayoutTutorial = (LinearLayout)findViewById(R.id.dotsLayoutTutorial);

        slideAdapterTutorial = new SlideAdapterTutorial(this);
        viewPagerTutorial.setAdapter(slideAdapterTutorial);

        anyadirPuntosIndicadores(0);
        viewPagerTutorial.addOnPageChangeListener(viewListener);
    }

    public void anyadirPuntosIndicadores(int position){
        mDots = new TextView[3];
        dotsLayoutTutorial.removeAllViews();
        for(int i = 0 ; i< mDots.length;i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            dotsLayoutTutorial.addView(mDots[i]);
        }
        if(mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            anyadirPuntosIndicadores(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
