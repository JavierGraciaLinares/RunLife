package com.example.alumno.runlife.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alumno.runlife.R;

/**
 * Created by javi on 19/05/2018.
 */

public class SlideAdapterTutorial extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    public SlideAdapterTutorial(Context context) {
        this.context = context;
    }

    public int[] slide_images = {
            R.drawable.runcartoon,
            R.drawable.runcartoon,
            R.drawable.runcartoon
    };
    public String[] slide_heads = {
            "TEST 1",
            "TEST 2",
            "TEST 3"
    };
    public String[] slide_description = {
            "TEST TESTTESTTESTTESTTEST TEST TEST TEST TEST  TEST TEST TEST",
            "TEST TESTTESTTESTTESTTEST TEST TEST TEST TEST  TEST TEST TEST",
            "TEST TESTTESTTESTTESTTEST TEST TEST TEST TEST  TEST TEST TEST"
    };


    @Override
    public int getCount() {
        return slide_heads.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout_tutorial, container, false);

        ImageView imagenTutorial = (ImageView) view.findViewById(R.id.imageViewSlideTutorial);
        TextView textoCabeceraTutorial = (TextView) view.findViewById(R.id.textViewHeadingTutorial);
        TextView textoDescripcionTutorial = (TextView) view.findViewById(R.id.textViewDescriptionTutorial);

        imagenTutorial.setImageResource(slide_images[position]);
        textoCabeceraTutorial.setText(slide_heads[position]);
        textoDescripcionTutorial.setText(slide_description[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}
