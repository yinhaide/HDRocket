package com.de.rocket.ue.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.de.rocket.R;
import com.de.rocket.bean.ActivityParamBean;
import com.de.rocket.bean.StatusBarBean;
import com.de.rocket.cons.RoKey;
import com.de.rocket.helper.CrashHelper;
import com.de.rocket.helper.FragHelper;
import com.de.rocket.helper.StackHelper;
import com.de.rocket.helper.ViewInjectHelper;
import com.de.rocket.ue.frag.DefaultFragment;
import com.de.rocket.ue.widget.FragRouterWidget;
import com.de.rocket.helper.LocaleHelper;
import com.de.rocket.ue.frag.RoFragment;
import com.de.rocket.helper.StatusBarHelper;
import com.de.rocket.helper.RecordHelper;
import com.de.rocket.utils.RoLogUtil;
import com.de.rocket.utils.WidgetUtil;

import java.io.Serializable;
import java.util.List;

/**
 * Activity基类
 */
public abstract class RoActivity extends FragmentActivity {

    /**
     * Activity->onSaveInstanceState时缓存的Fragment的key
     * Fragment 在显示或者隐藏，移除是出现Can not perform this action after onSaveInstanceState 
     * #解决办法：onSaveInstanceState方法是在该Activity即将被销毁前调用，来保存Activity数据的，如果在保存玩状态后 再给它添加Fragment就会出错。
     * 解决办法一就是把commit（）方法替换成 commitAllowingStateLoss()，不采用
     * 解决办法二就是在Activity 回收时 onSaveInstanceState 中不缓存Fragment ,在OnCreate 中移除缓存相应Fragment数据，采用
     */
    private static final String BUNDLE_SURPOTR_FRAGMENTS_KEY = "android:support:fragments";
    private static final String BUNDLE_FRAGMENTS_KEY = "android:fragments";
    //全局配置参数
    private ActivityParamBean activityParamBean = new ActivityParamBean();
    //自定义一个栈来维护Fragment回退
    private StackHelper stack = new StackHelper();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.attachBaseContext(base));//多国语言适配
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (activityParamBean.isSaveInstanceState()) {//是否恢复页面
            super.onSaveInstanceState(outState);
            if (outState == null) {
                outState = new Bundle();
            }
            //缓存栈顶的Tag
            outState.putString(RoKey.SAVE_TOP_FRAG, stack.peekRocket());
            //缓存栈顶携带的数据
            outState.putSerializable(RoKey.ARGUMENT_OBJECT_KEY,FragHelper.getInstance().getPeekRocketObject(this));
            //重建时清除系统缓存的fragment的状态
            outState.remove(BUNDLE_SURPOTR_FRAGMENTS_KEY);
            outState.remove(BUNDLE_FRAGMENTS_KEY);
        }
        //打印
        RoLogUtil.v("RoActivity::onSaveInstanceState-->"+"outState:"+outState.toString());
        //写日志
        RecordHelper.writeInnerLog(this,"RoActivity::onSaveInstanceState-->"+"outState:"+outState.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //重建时清除系统缓存的fragment的状态
            savedInstanceState.remove(BUNDLE_FRAGMENTS_KEY);
            savedInstanceState.remove(BUNDLE_SURPOTR_FRAGMENTS_KEY);
        }
        super.onCreate(savedInstanceState);
        //初始化配置
        initActivityParam();
        //View绑定
        ViewInjectHelper.getInstance().injectActivity(this);
        //显示第一个页面
        if (savedInstanceState == null) {
            //正常情况下去加载根Fragment
            String topFragTag = null;
            Serializable topFragObject = null;
            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                topFragTag = bundle.getString(RoKey.ARGUMENT_TARGET_CLASS_KEY);
                topFragObject = bundle.getSerializable(RoKey.ARGUMENT_OBJECT_KEY);
            }
            initFragment(topFragTag,topFragObject);
        } else {
            // “内存重启”时调用
            //如果你想恢复到用户离开时的那个Fragment的界面，需
            // 要在onSaveInstanceState(Bundle outState)里
            //保存离开时的那个可见的tag或下标，在onCreate“内存重启”代码块中，取出tag/下标，进行恢复。
            String topFragTag = savedInstanceState.getString(RoKey.SAVE_TOP_FRAG);
            Serializable topFragObject = savedInstanceState.getSerializable(RoKey.ARGUMENT_OBJECT_KEY);
            initFragment(topFragTag,topFragObject);
        }
    }

    @Override
    public void onBackPressed() {//返回虚拟按键
        List<String> rocketStacks = stack.getRocketStackList();
        if(rocketStacks != null && rocketStacks.size() >= 1){
            FragmentManager fragmentManager = getSupportFragmentManager();
            //Rocket栈顶
            String rocketStackTop = stack.peekRocket();
            if(!TextUtils.isEmpty(rocketStackTop)){
                Fragment topFragment = fragmentManager.findFragmentByTag(rocketStackTop);
                //找出前台活动的fragment,向Fragment分发回退事件
                boolean isActiveRoFragment = topFragment != null && topFragment.isVisible() && topFragment.getUserVisibleHint() && topFragment instanceof RoFragment;
                if(isActiveRoFragment){
                    RoFragment roFragment = (RoFragment) topFragment;
                    //将点击事件下发
                    roFragment.handleBackPressed();
                }else{
                    super.onBackPressed();//默认的返回逻辑
                }
            }else{
                super.onBackPressed();//默认的返回逻辑
            }
        }else{//如果当前的Fragment数量小于等于1，则由Activity处理
            if (!onRoBackPressed()) {//Activity内部消费
                super.onBackPressed();//默认的返回逻辑
            }
        }
    }

    /**
     * 配置页面参数
     */
    private void initActivityParam() {
        //检查参数是否合法
        checkParamAvalible(initProperty());
        //设置页面布局
        setContentView(activityParamBean.getLayoutId());
        //状态栏
        StatusBarBean statusBarBean = activityParamBean.getStatusBar();
        if (statusBarBean != null) {
            if (statusBarBean.isImmersion()) {//沉浸式风格，隐藏状态栏
                StatusBarHelper.setStatusBarColor(this, statusBarBean.getColor(),true);
            } else {//不隐藏状态栏
                StatusBarHelper.setStatusBarColor(this, statusBarBean.getColor(),false);
            }
        }
        //显示悬浮球
        if (activityParamBean.isShowViewBall()) {
            WidgetUtil.showGloaBall(FragRouterWidget::new);
        }
        //设置日志信息以及崩溃的配置
        CrashHelper.getInstance().setEnableCrashWindow(activityParamBean.isEnableCrashWindow());
        RecordHelper.setRecordBean(this,activityParamBean.getRecordBean());
    }

    /**
     * 检查配置参数是否合法
     *
     * @param activityParamBean activityParamBean
     */
    private void checkParamAvalible(ActivityParamBean activityParamBean) {
        //填充参数
        if (activityParamBean == null) {
            activityParamBean = new ActivityParamBean();
        }
        this.activityParamBean = activityParamBean;
        //检测是否都是继承Rofragment
        Class[] roFragments = this.activityParamBean.getRoFragments();
        if (roFragments != null && roFragments.length > 0) {
            boolean isAvalible = false;
            for (Class mClass : roFragments) {
                if (RoFragment.class.isAssignableFrom(mClass)) {
                    isAvalible = true;
                }
            }
            if (!isAvalible) throw new RuntimeException(getString(R.string.rocket_fragment_extend_error));
        }else{
            activityParamBean.setRoFragments(new Class[]{DefaultFragment.class});
        }
    }

    /**
     * 外部使用获取全局配置信息
     *
     * @return ActivityParamBean activity param bean
     */
    public ActivityParamBean getActivityParamBean() {
        return this.activityParamBean;
    }

    /**
     * 获得栈对象
     */
    public StackHelper getStack() {
        return stack;
    }

    /**
     * 加载Fragment信息
     */
    private void initFragment(String topFragTag, Serializable serializable) {
        int containid = this.activityParamBean.getFragmentContainId();
        Class[] frags = this.activityParamBean.getRoFragments();
        Class firstFrag = frags[0];
        //找出栈顶的Class
        if (!TextUtils.isEmpty(topFragTag)) {
            for (Class tempClass : frags) {
                if (topFragTag.equals(tempClass.getSimpleName())) {
                    firstFrag = tempClass;
                }
            }
        }
        FragHelper.getInstance().transfer(this, firstFrag, firstFrag, containid, false, true, false,serializable, frags,null);
        ActivityParamBean activityParamBean = initProperty();
        if (activityParamBean != null) {
            if (activityParamBean.getLayoutId() != 0 && activityParamBean.getFragmentContainId() != 0) {
                //页面初始化完毕
                initViewFinish();
                //业务逻辑完成
                onNexts(null);
            }
        }
    }

    /* ************************************************************* */
    /*                     供子类重写的方法
    /* ************************************************************* */

    /**
     * 返回事件是否由Activity内部消费
     *
     * @return true表示内部消费
     */
    public boolean onRoBackPressed(){
        return false;
    }

    /* ************************************************************* */
    /*                     供子类重写与继承的方法
    /* ************************************************************* */

    /**
     * Activity全局配置信息
     *
     * @return ActivityParamBean
     */
    public abstract ActivityParamBean initProperty();

    /**
     * 页面初始化完成
     */
    public abstract void initViewFinish();

    /**
     * 业务逻辑初始化完成
     */
    public abstract void onNexts(Object object);
}
