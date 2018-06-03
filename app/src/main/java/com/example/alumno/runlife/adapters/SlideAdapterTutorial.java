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
    String head1,head2,head3,body1,body2,body3;

    public SlideAdapterTutorial(Context context) {
        head1 = context.getResources().getString(R.string.tutorial1H_string);
        head2 = context.getResources().getString(R.string.tutorial2H_string);
        head3 = context.getResources().getString(R.string.tutorial3H_string);
        body1 = context.getResources().getString(R.string.tutorial1B_string);
        body2 = context.getResources().getString(R.string.tutorial2B_string);
        body3 = context.getResources().getString(R.string.tutorial3B_string);
        this.context = context;

    }


    public int[] slide_images = {
            R.drawable.runcartoon,
            R.drawable.runcartoon,
            R.drawable.runcartoon
    };
    public String[] slide_heads = {
            head1,
            head2,
            head3
    };
    public String[] slide_description = {
            body1,
            body2,
            body3
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
