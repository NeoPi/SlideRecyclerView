package com.neopi.slideitem;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Author    :  NeoPi
 * Date      :  17-11-23
 * Describe  :
 */

public class SlideItemView extends FrameLayout {

    private static final String TAG = "SlideItemView";

    private final ViewDragHelper mDragHelper;

    private View mDragView;
    private View mRightView;

    private boolean mDragEdge = true;
    private boolean mDragHorizontal = true;
    private boolean mDragCapture = true;
    private boolean mDragVertical;


    private int mTouchSlop;
    private int mVelocity;

    private int DEFAULT_MENU_WIDTH = 80; // 单位dp
    private int DEFAULT_HEIGHT = 60;
    private float density;
    private int screenWidth;
    private int screenHeight;

    private float mDownX;
    private float mDownY;


    OnSlideStateChangeListener listener;

    public SlideItemView(Context context) {
        this(context, null);
    }

    public SlideItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDragHelper = ViewDragHelper.create(this, 1f, new DragHelperCallback());
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        density = displayMetrics.density;

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);

        int childCount = getChildCount();
        if (childCount > 1) {
            int menuWidth = (int) (DEFAULT_MENU_WIDTH * density + 0.5f);
            mRightView = getChildAt(0);
            ViewGroup.LayoutParams params = mRightView.getLayoutParams();
            params.width = menuWidth;
            params.height = measureHeight;

            measureChild(mRightView,
                    MeasureSpec.makeMeasureSpec(menuWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(measureHeight, MeasureSpec.EXACTLY));

            mDragView = getChildAt(childCount - 1);
            mDragView.setLayoutParams(new LayoutParams(measureWidth, measureHeight));
            measureChild(mDragView,
                    MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(measureHeight, MeasureSpec.EXACTLY));

            Log.e("TAG", measureWidth + ",,,," + measureHeight + ",,," + mDragView.getMeasuredWidth() + ",,,," + mDragView.getMeasuredHeight());
        }

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public void setDragHorizontal(boolean dragHorizontal) {
        mDragHorizontal = dragHorizontal;
    }

    public void setDragVertical(boolean dragVertical) {
        mDragVertical = dragVertical;
    }

    public void setDragEdge(boolean dragEdge) {
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        mDragEdge = dragEdge;
    }

    public void setDragCapture(boolean dragCapture) {
        mDragCapture = dragCapture;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("TAG", "SlideItemView  onInterceptTouchEvent");
        final int action = ev.getAction();
        if (mDragView == null) {
            return super.onInterceptTouchEvent(ev);
        }
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
//            case MotionEvent.ACTION_MOVE:
//                float dx = ev.getX() - mDownX;
//                float dy = ev.getY() - mDownY;
//                boolean isLeftDrag = dx < -mTouchSlop && Math.abs(dx) > Math.abs(dy);
//                if (isLeftDrag) {
//                    mDragHelper.shouldInterceptTouchEvent(ev);
//                    ViewParent parent = getParent();
//                    if (parent != null) {
//                        parent.requestDisallowInterceptTouchEvent(true);
//                    }
//                }
//                return false;
            case MotionEvent.ACTION_UP:
                mDragHelper.cancel();
                return false;
            case MotionEvent.ACTION_CANCEL:
                mDragHelper.cancel();
                return false;
        }

        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e("TAG", "SlideItemView  onTouchEvent");
        if (mDragView == null) {
            return super.onTouchEvent(ev);
        }
        mDragHelper.processTouchEvent(ev);
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = ev.getX() - mDownX;
                float dy = ev.getY() - mDownY;
                boolean isLeftDrag = dx < -mTouchSlop && Math.abs(dx) > Math.abs(dy);
                if (isLeftDrag) {
                    ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 关闭菜单
     */
    public void close() {
        if (mDragView == null) {
            return;
        }

        if (mDragHelper.smoothSlideViewTo(mDragView, getPaddingLeft(), getPaddingTop())) {
            ViewCompat.postInvalidateOnAnimation(this);
            postInvalidate();
        }
    }

    /**
     * 开启菜单
     */
    public void open() {
        if (mDragView == null) {
            return;
        }

        if (mDragHelper.smoothSlideViewTo(mDragView, -mRightView.getWidth(), getPaddingTop())) {
            ViewCompat.postInvalidateOnAnimation(this);
            postInvalidate();
        }
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            Log.e("DragHelperCallback", "tryCaptureView " + pointerId);
            if (mDragCapture) {
                return child == mDragView;
            }
            return true;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            Log.e("DragHelperCallback", "onViewPositionChanged " + left + "," + top + "," + dx + "," + dy);
            invalidate();
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
            Log.e("DragHelperCallback", "onViewCaptured " + activePointerId);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            Log.e("DragHelperCallback", "onViewReleased " + xvel + ",," + yvel);
            if (xvel < -mVelocity) {
                open();
            } else if (xvel > mVelocity) {
                close();
            } else {
                if (mDragView.getLeft() < -mRightView.getWidth() / 3 * 2) {
                    open();
                } else {
                    close();
                }
            }
        }

        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);

            Log.e("DragHelperCallback", "onEdgeTouched " + pointerId);
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            Log.e("DragHelperCallback", "onEdgeDragStarted " + edgeFlags + ",," + pointerId);
            if (mDragEdge) {
                mDragHelper.captureChildView(mDragView, pointerId);
            }
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            Log.e("DragHelperCallback", "clampViewPositionVertical " + top + ",," + dy);
            if (mDragVertical) {
                final int topBound = getPaddingTop();
                final int bottomBound = getHeight() - mDragView.getHeight();

                final int newTop = Math.min(Math.max(top, topBound), bottomBound);

                return newTop;
            }
            return super.clampViewPositionVertical(child, top, dy);
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.e("DragHelperCallback", "clampViewPositionHorizontal " + left + ",," + dx);
            final int leftBound = getPaddingLeft();
            final int rightBound = getWidth() - mDragView.getWidth();
//            Log.e("TAG",left+",,,"+dx+",,,,"+rightBound+",,,,"+leftBound) ;
            if (mDragHorizontal || mDragCapture || mDragEdge) {
                if (left > 0) {
                    return 0;
                }

                int rightWidth = mRightView.getWidth();
                if (left < 0 && Math.abs(left) > rightWidth) {
                    return -rightWidth;
                }
                return left;
            }
            return super.clampViewPositionHorizontal(child, left, dx);
        }

        @Override
        public void onViewDragStateChanged(int state) {
            Log.e("DragHelperCallback", "onViewDragStateChanged " + state);
            super.onViewDragStateChanged(state);
            if (listener != null) {
                listener.onSlideStateChange(state);
            }
        }
    }

    public interface OnSlideStateChangeListener {
        void onSlideStateChange(int state);
    }

}