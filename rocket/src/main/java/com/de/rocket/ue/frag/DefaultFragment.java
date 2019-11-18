package com.de.rocket.ue.frag;

import android.view.View;

import com.de.rocket.R;

/**
 * 默认的Fragment
 * Created by haide.yin(haide.yin@tcl.com) on 2019/6/21 13:15.
 */
public class DefaultFragment extends RoFragment {

    @Override
    public int onInflateLayout() {
        return R.layout.rocket_frag_default;
    }

    @Override
    public void initViewFinish(View inflateView) {

    }

    @Override
    public void onNexts(Object object) {

    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public boolean isReloadData() {
        return false;
    }
}
