package com.de.rocket.app.ue.frag;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.de.rocket.Rocket;
import com.de.rocket.app.R;
import com.de.rocket.ue.frag.RoFragment;
import com.de.rocket.ue.injector.BindView;
import com.de.rocket.ue.injector.Event;

/**
 * 类作用描述
 * Created by haide.yin(haide.yin@tcl.com) on 2019/7/8 10:59.
 */
public class Frag_rocket extends RoFragment {

    @BindView(R.id.tv_right)
    private TextView tvRight;

    @Override
    public int onInflateLayout() {
        return R.layout.frag_rocket;
    }

    @Override
    public void initViewFinish(View inflateView) {
        initView();
        initConfig();
    }

    /**
     * 初始化View
     */
    private void initView() {
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("toActivity");
    }

    /**
     * 初始化配置信息
     */
    private void initConfig() {
        String content = "测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据测试写数据";
        /*for(int i = 0 ; i < 50;i++){
            final int index = i;
            new Thread(() -> {
                Rocket.writeOuterLog("一、"+index+" "+content);
            }).start();
        }
        for(int i = 0 ; i < 50;i++){
            final int index = i;
            new Thread(() -> {
                Rocket.writeOuterLog("二、"+index+" "+content);
            }).start();
        }
        for(int i = 0 ; i < 50;i++){
            final int index = i;
            new Thread(() -> {
                Rocket.writeOuterLog("三、"+index+" "+content);
            }).start();
        }
        for(int i = 0 ; i < 50;i++){
            final int index = i;
            new Thread(() -> {
                Rocket.writeOuterLog("四、"+index+" "+content);
            }).start();
        }*/
    }

    @Override
    public void onNexts(Object object) {
        if (object instanceof String) {
            toast((String) object);
        }
    }

    /**
     * 1. 方法必须私有限定,
     * 2. 方法参数形式必须和type对应的Listener接口一致.
     * 3. 注解参数value支持数组: value={id1, id2, id3}
     **/
    @Event(value = R.id.tv_right, type = View.OnClickListener.class/*可选参数, 默认是View.OnClickListener.class*/)
    private void toRoxx(View view) {
        //toAct(RoxxActivity.class,"Frag_roxx_widget","我是显示跳转Activity之间传递的对象");
        toAct("com.de.rocket.RoxxActivity","Frag_roxx","我是隐式跳转Activity之间传递的对象");
    }

    @Event(R.id.bt_animation)
    private void animation(View view) {//转场动画
        toFrag(Frag_rocket_anim.class);
    }

    @Event(R.id.bt_permission)
    private void permission(View view) {//动态权限
        toFrag(Frag_rocket_permission.class);
    }

    @Event(R.id.bt_crash)
    private void crash(View view) {//崩溃处理
        TextView textView = null;
        textView.setText(String.valueOf("crash test"));
    }

    @Event(R.id.bt_statusbar)
    private void statusbar(TextView view) {//状态栏
        toFrag(Frag_rocket_statusbar.class);
    }

    @Event(R.id.bt_record)
    private void record(TextView view) {//状态栏
        toFrag(Frag_rocket_record.class);
    }

    @Event(R.id.bt_router)
    private void router(TextView view) {//状态栏
        Rocket.showGloaBall();
    }
}
