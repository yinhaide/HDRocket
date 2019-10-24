package com.de.rocket.app.ue.frag;

import android.view.View;

import com.de.rocket.app.R;
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
        toFrag(Frag_rocket.class, true, true, null);
    }

    @Override
    public void onNexts(Object object) {

    }
}
