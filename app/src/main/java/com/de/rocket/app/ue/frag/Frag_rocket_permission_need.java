package com.de.rocket.app.ue.frag;

import android.Manifest;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.de.rocket.app.R;
import com.de.rocket.bean.PermissionBean;
import com.de.rocket.listener.PermissionListener;
import com.de.rocket.ue.frag.RoFragment;
import com.de.rocket.ue.injector.BindView;
import com.de.rocket.ue.injector.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * 按需权限
 * Created by haide.yin(haide.yin@tcl.com) on 2019/7/8 10:59.
 */
public class Frag_rocket_permission_need extends RoFragment {

    @BindView(R.id.tv_title)
    private TextView tvTitle;
    @BindView(R.id.tv_detail)
    private TextView tvContent;

    @Override
    public int onInflateLayout() {
        return R.layout.frag_rocket_permission_need;
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
        tvContent.setText("用户在需要的时候才会请求这个权限，属于可选权限");
    }

    @Override
    public void onNexts(Object object) {

    }

    @Event(R.id.bt_permission_need)
    private void permissionNeed(TextView view) {//可选权限
        String[] permissions = new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.RECEIVE_SMS};
        needPermissison(permissions, (i, b, list) -> toast("requestCode:"+i+" allAccept:"+b+" permissionBeans:"+list.toString()));
    }
}
