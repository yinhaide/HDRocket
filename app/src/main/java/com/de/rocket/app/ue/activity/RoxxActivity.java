package com.de.rocket.app.ue.activity;

import android.graphics.Color;
import android.util.Log;

import com.de.rocket.Rocket;
import com.de.rocket.app.R;
import com.de.rocket.bean.ActivityParamBean;
import com.de.rocket.bean.RecordBean;
import com.de.rocket.bean.StatusBarBean;
import com.de.rocket.ue.activity.RoActivity;

public class RoxxActivity extends RoActivity {

    private Class[] roFragments = {
            //Frag_splash.class,
    };

    @Override
    public ActivityParamBean initProperty() {
        ActivityParamBean activityParamBean = new ActivityParamBean();
        activityParamBean.setLayoutId(R.layout.activity_main);//Activity布局
        activityParamBean.setFragmentContainId(R.id.fl_fragment_contaner);//Fragment容器
        activityParamBean.setSaveInstanceState(true);//页面重载是否要恢复之前的页面
        activityParamBean.setToastCustom(true);//用自定义的吐司风格
        activityParamBean.setRoFragments(roFragments);//需要注册Fragment列表
        activityParamBean.setShowViewBall(true);//是否显示悬浮球
        activityParamBean.setRecordBean(new RecordBean(true,true,true,7));
        activityParamBean.setEnableCrashWindow(true);//是否隐藏框架自定义的崩溃的窗口
        activityParamBean.setStatusBar(new StatusBarBean(true, Color.argb(0, 0, 0, 0)));//状态栏
        return activityParamBean;
    }

    @Override
    public void initViewFinish() {
        //恢复状态栏,因为启动Activity的Theme里面清楚了状态栏,需要恢复
        //<item name="android:windowFullscreen">true</item>
        Rocket.clearWindowFullscreen(this);
    }

    @Override
    public void onNexts(Object object) {

    }
}
