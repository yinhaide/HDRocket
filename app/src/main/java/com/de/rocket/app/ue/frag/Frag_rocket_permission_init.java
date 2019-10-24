package com.de.rocket.app.ue.frag;

import android.Manifest;
import android.view.View;
import android.widget.TextView;

import com.de.rocket.app.R;
import com.de.rocket.ue.frag.RoFragment;
import com.de.rocket.ue.injector.BindView;

/**
 * 必须权限
 * Created by haide.yin(haide.yin@tcl.com) on 2019/7/8 10:59.
 */
public class Frag_rocket_permission_init extends RoFragment {

    @BindView(R.id.tv_title)
    private TextView tvTitle;
    @BindView(R.id.tv_detail)
    private TextView tvContent;

    @Override
    public int onInflateLayout() {
        return R.layout.frag_rocket_permission_init;
    }

    @Override
    public void initViewFinish(View inflateView) {
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        tvTitle.setText("必须权限");
        tvContent.setText("如果用户不给与这个权限，这个页面将不可用，属于强制权限");
    }

    /**
     * 页面进来必须要先要求权限
     * 必须所有权限都通过才会执行生命周期,否则建议子类执行:back()返回上个页面,或者执行:startSettingActivity(requestCode)打开系统权限设置
     * 如果权限没有全部允许,还想执行生命周期的话，需要在子类调用:super.initPermission(rootView);
     */
    @Override
    public String[] initPermission() {
        super.initPermission();
        String[] permissions = new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.RECORD_AUDIO};
        setPermissionListener((requestCode, allAccept, permissionBeans) -> {
            if (!allAccept) {
                back();
            }
        });
        return permissions;
    }

    @Override
    public void onNexts(Object object) {

    }
}
