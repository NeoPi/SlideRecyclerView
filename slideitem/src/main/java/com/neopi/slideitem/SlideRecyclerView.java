package com.neopi.slideitem;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_POINTER_DOWN;

/**
 * Author    :  NeoPi
 * Date      :  17-11-23
 * Describe  :
 */

public class SlideRecyclerView extends RecyclerView {


    private boolean canScroll = true ;
    private SlideLinearLayoutManager layoutManager ;

    public SlideRecyclerView(Context context) {
        this(context,null);
    }

    public SlideRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlideRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init ();
    }

    private void init() {

    }


    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (layout instanceof SlideLinearLayoutManager) {
            super.setLayoutManager(layout);
            layoutManager = (SlideLinearLayoutManager) layout;
            layoutManager.setScrollEnabled(canScroll);
        } else {
            throw new IllegalArgumentException("layout must be SlideLinearLayoutManager");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("111","RecyclerView  dispatchTouchEvent");
        switch (ev.getAction()) {
            case ACTION_DOWN :
                return super.dispatchTouchEvent(ev) ;
            case ACTION_POINTER_DOWN:
                return super.dispatchTouchEvent(ev) ;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        Log.e("111","RecyclerView  onInterceptTouchEvent");
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public void onScrollStateChanged(int state) {
        Log.e("111","RecyclerView  onScrollStateChanged :"+state);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        Log.e("111","RecyclerView  onTouchEvent");
        return super.onTouchEvent(e);
    }


    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
        layoutManager.setScrollEnabled(canScroll);
    }
}
