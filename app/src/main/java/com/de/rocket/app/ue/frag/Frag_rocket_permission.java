package com.de.rocket.app.ue.frag;

import android.view.View;
import android.widget.TextView;

import com.de.rocket.app.R;
import com.de.rocket.ue.frag.RoFragment;
import com.de.rocket.ue.injector.BindView;
import com.de.rocket.ue.injector.Event;

/**
 * 权限相关
 * Created by haide.yin(haide.yin@tcl.com) on 2019/7/8 10:59.
 */
public class Frag_rocket_permission extends RoFragment {

    @BindView(R.id.tv_title)
    private TextView tvTitle;

    @Override
    public int onInflateLayout() {
        return R.layout.frag_rocket_permission;
    }

    @Override
    public void initViewFinish(View inflateView) {
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        tvTitle.setText("动态权限");
    }

    @Override
    public void onNexts(Object object) {

    }

    @Event(R.id.bt_permission_init)
    private void permissionInit(TextView view) {//强制权限
        toFrag(Frag_rocket_permission_init.class);
    }

    @Event(R.id.bt_permission_need)
    private void permissionNeed(TextView view) {//可选权限
        toFrag(Frag_rocket_permission_need.class);
    }
}
