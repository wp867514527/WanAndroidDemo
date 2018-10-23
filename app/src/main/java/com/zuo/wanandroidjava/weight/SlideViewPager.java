package com.zuo.wanandroidjava.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zuo.wanandroidjava.R;

public class SlideViewPager extends ViewPager {
    protected  boolean slide;
    public SlideViewPager(@NonNull Context context) {
        super(context);
    }

    public SlideViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlideViewPager);
        slide = a.getBoolean(R.styleable.SlideViewPager_svp_slide, true);
        a.recycle();
    }

    // 拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return  slide &&super.onInterceptTouchEvent(ev);
    }

    public void setSlide(boolean slide) {
        this.slide = slide;
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item,false);
    }
}
