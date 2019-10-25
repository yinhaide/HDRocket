package com.de.rocket.app.ue.frag;

import android.Manifest;
import android.view.View;
import android.widget.TextView;

import com.de.rocket.app.R;
import com.de.rocket.bean.AnimationBean;
import com.de.rocket.ue.frag.RoFragment;
import com.de.rocket.ue.injector.BindView;

import java.util.PrimitiveIterator;

/**
 * 动画详情页
 * Created by haide.yin(haide.yin@tcl.com) on 2019/7/8 10:59.
 */
public class Frag_rocket_anim_detail extends RoFragment {

    @BindView(R.id.tv_detail)
    private TextView tvContent;
    @BindView(R.id.tv_title)
    private TextView tvTitle;

    private AnimationBean animationBean;

    @Override
    public int onInflateLayout() {
        return R.layout.frag_rocket_anim_detail;
    }

    @Override
    public void initViewFinish(View inflateView) {
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        tvTitle.setText("转场动画详情");
    }

    @Override
    public boolean onBackPresss(){
        back(false,null,animationBean);
        return true;
    }

    /**
     * 页面进来必须要先要求权限
     * 必须所有权限都通过才会执行生命周期,否则建议子类执行:back()返回上个页面,或者执行:startSettingActivity(requestCode)打开系统权限设置
     * 如果权限没有全部允许,还想执行生命周期的话，需要在子类调用:super.initPermission(rootView);
     */
    @Override
    public String[] initPermission() {
        super.initPermission();
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        setPermissionListener((requestCode, allAccept, permissionBeans) -> {
            if (!allAccept) {
                back();
            }
        });
        //return permissions;
        return null;
    }

    @Override
    public void onNexts(Object object) {
        if(object instanceof AnimationBean){
            animationBean = (AnimationBean)object;
            tvContent.setText(animationBean.toString());
            toast(animationBean.toString());
        }
    }
}
