package com.neopi.slideitem;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Author    :  NeoPi
 * Date      :  17-11-23
 * Describe  :
 */

public class SlideLinearLayoutManager extends LinearLayoutManager {


    private boolean canScrollEnabled = true ;

    public SlideLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.canScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return canScrollEnabled && super.canScrollVertically();
    }

}
