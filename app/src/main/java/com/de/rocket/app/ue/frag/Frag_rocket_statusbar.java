package com.de.rocket.app.ue.frag;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.de.rocket.app.R;
import com.de.rocket.ue.frag.RoFragment;
import com.de.rocket.ue.injector.BindView;
import com.de.rocket.ue.injector.Event;

/**
 * 状态栏
 * Created by haide.yin(haide.yin@tcl.com) on 2019/7/8 10:59.
 */
public class Frag_rocket_statusbar extends RoFragment {

    @BindView(R.id.sb_color)
    private SeekBar sbColor;
    @BindView(R.id.iv_rocket_bg)
    private ImageView ivRocketBg;

    //记录当前状态栏的模式
    private boolean isImmersion = true;

    @Override
    public int onInflateLayout() {
        return R.layout.frag_rocket_statusbar;
    }

    @Override
    public void initViewFinish(View inflateView) {
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        sbColor.setMax(255);
        sbColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(isImmersion){
                    if(progress == 0){
                        setStatusBarColor(Color.TRANSPARENT,true);
                    }else{
                        setStatusBarColor(Color.argb(progress, 0, 0, 0),true);
                    }
                }else{
                    if(progress == 0){
                        setStatusBarColor(Color.TRANSPARENT,false);
                    }else{
                        setStatusBarColor(Color.argb(progress, 0, 0, 0),false);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onNexts(Object object) {

    }

    @Event(R.id.bt_immersion)
    private void immersion(TextView view) {//沉浸式
        isImmersion = true;
        setStatusBarColor(Color.TRANSPARENT,true);
        //setOffsetStatusBar(ivRocketBg,false,true);
    }

    @Event(R.id.bt_not_immersion)
    private void notImmersion(TextView view) {//非沉浸式
        isImmersion = false;
        setStatusBarColor(Color.parseColor("#55ffffff"),false);
        //setOffsetStatusBar(ivRocketBg,true,true);
    }
}
