package com.de.rocket.app.ue.frag;

import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.de.rocket.app.R;
import com.de.rocket.app.tools.MemoryUtil;
import com.de.rocket.bean.FragParamBean;
import com.de.rocket.ue.frag.RoFragment;

/**
 * 开始页面
 * Created by haide.yin(haide.yin@tcl.com) on 2019/6/6 16:12.
 */
public class Frag_splash extends RoFragment {


    @Override
    public int onInflateLayout() {
        return R.layout.frag_splash;
    }

    @Override
    public void initViewFinish(View inflateView) {
        new Handler().postDelayed(() -> {
            //打印对比fragment跳转之前的时间以及内存情况
            MemoryUtil.printMemoryMsg("fragment_begin");
            toFrag(Frag_rocket.class, new FragParamBean.Builder().isOriginalRemove(true).build());
        },1000);
    }

    @Override
    public void onNexts(Object object) {

    }
}
