package com.de.rocket.ue.widget;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.de.rocket.R;

public class RoToast extends RelativeLayout {

    //内容
    private TextView textView;
    //图标
    private ImageView imageView;

    public RoToast(Context context) {
        super(context, null);
        // 在构造函数中将Xml中定义的布局解析出来。
        View toastView = LayoutInflater.from(context).inflate(R.layout.rocket_widget_toast, this, true);
        textView = toastView.findViewById(R.id.text);
        //支持点击滑动
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        imageView = toastView.findViewById(R.id.iv);
    }

    /**
     * 读取文本框
     */
    public TextView getTextView() {
        return this.textView;
    }

    /**
     * 渐变显示
     */
    public ImageView getImageView() {
        return this.imageView;
    }

    /**
     * 渐变显示
     */
    public RoToast setText(String content) {
        textView.setText(content);
        return this;
    }
}
