package com.de.rocket.app.ue.frag;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.de.rocket.app.R;
import com.de.rocket.bean.RecordBean;
import com.de.rocket.ue.frag.RoFragment;
import com.de.rocket.ue.injector.BindView;
import com.de.rocket.ue.injector.Event;

import java.io.File;

/**
 * 日志相关
 * Created by haide.yin(haide.yin@tcl.com) on 2019/7/8 10:59.
 */
public class Frag_rocket_record extends RoFragment {

    @BindView(R.id.tv_title)
    private TextView tvTitle;
    @BindView(R.id.tv_path)
    private TextView tvPath;
    @BindView(R.id.tv_timeout)
    private TextView tvTimeout;
    @BindView(R.id.sw_inner)
    private Switch swInner;
    @BindView(R.id.sw_outer)
    private Switch swOuter;
    @BindView(R.id.sw_crash)
    private Switch swCrash;

    @Override
    public int onInflateLayout() {
        return R.layout.frag_rocket_record;
    }

    @Override
    public void initViewFinish(View inflateView) {
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        tvTitle.setText("日志系统");
        File file = activity.getExternalFilesDir("rocket");
        if(file != null){
            tvPath.setText(file.getAbsolutePath());
        }
        RecordBean recordBean = activity.getActivityParamBean().getRecordBean();
        String timeoutString = recordBean.getSaveDay() + "天";
        tvTimeout.setText(timeoutString);
        swInner.setChecked(recordBean.isInnerEnable());
        swOuter.setChecked(recordBean.isOuterEnable());
        swCrash.setChecked(recordBean.isCrashEnable());
        swInner.setClickable(false);
        swOuter.setClickable(false);
        swCrash.setClickable(false);
    }


    @Override
    public void onNexts(Object object) {
        if (object instanceof String) {
            toast((String) object);
        }
    }

    @Event(R.id.bt_record_open)
    private void openRecordPath(TextView view) {
        File file = activity.getExternalFilesDir("rocket");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(file), "file/*");
        //只打开文件夹
        //intent.setDataAndType(Uri.fromFile(file), "text/plain");
        startActivity(intent);
    }
}
