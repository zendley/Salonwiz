package com.speckpro.salonwiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.speckpro.salonwiz.R;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private final Context context;
    private final List<Integer> images;
    private final List<String> texts;

    public SliderAdapter(Context context, List<Integer> images, List<String> texts) {
        this.context = context;
        this.images = images;
        this.texts = texts;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        ImageView imageView = view.findViewById(R.id.imageView_slider);
        TextView textView = view.findViewById(R.id.textView_sliderText);
        RelativeLayout relativeLayout = view.findViewById(R.id.linearLayout);

        imageView.setImageResource(images.get(position));
        textView.setText(texts.get(position));

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
