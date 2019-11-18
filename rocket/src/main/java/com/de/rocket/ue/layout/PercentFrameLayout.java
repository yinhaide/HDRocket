package com.de.rocket.ue.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.de.rocket.helper.PercentLayoutHelper;

/**
 * 帧百分比布局
 */
public class PercentFrameLayout extends FrameLayout {

    //布局帮助类
    private PercentLayoutHelper mHelper;

    public PercentFrameLayout(Context context) {
        this(context, null);
    }

    public PercentFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PercentFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化
        mHelper = new PercentLayoutHelper(this);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mHelper.adjustChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHelper.handleMeasuredStateTooSmall()) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHelper.restoreOriginalParams();
    }

    /**
     * 布局参数
     */
    public static class LayoutParams extends FrameLayout.LayoutParams implements PercentLayoutHelper.PercentLayoutParams {

        private PercentLayoutHelper.PercentLayoutInfo mPercentLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            mPercentLayoutInfo = PercentLayoutHelper.getPercentLayoutInfo(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height, gravity);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(FrameLayout.LayoutParams source) {
            super((MarginLayoutParams) source);
            gravity = source.gravity;
        }

        public LayoutParams(LayoutParams source) {
            this((FrameLayout.LayoutParams) source);
            mPercentLayoutInfo = source.mPercentLayoutInfo;
        }

        @Override
        public PercentLayoutHelper.PercentLayoutInfo getPercentLayoutInfo() {
            return mPercentLayoutInfo;
        }

        @Override
        protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            PercentLayoutHelper.fetchWidthAndHeight(this, a, widthAttr, heightAttr);
        }
    }
}
