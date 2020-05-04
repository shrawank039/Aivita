package com.matrixdeveloper.aivita.test.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

public class AdTypeViewPager extends ViewPager {

    public AdTypeViewPager(Context context) {
        super(context);
    }

    public AdTypeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) {
                height = h;
            }
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        Toast.makeText(getContext(), ""+widthMeasureSpec+heightMeasureSpec, Toast.LENGTH_SHORT).show();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
