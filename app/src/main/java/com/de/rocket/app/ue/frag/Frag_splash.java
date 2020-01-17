package com.de.rocket.app.ue.frag;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

    /*测试
    Google Play:https://play.google.com/store/apps/details?id=com.trackerandroid.trackerandroid
    App Store: https://apps.apple.com/cn/app/tcl-connect/id1456360744

    作为TCL通讯智能穿戴业务的All in one集大成的应用，承载了几乎所有的智能硬件载体，所以整个框架的搭建也是非常重要。在参与框架搭建的过程中，吸取了大量的项目经验，
    并逐渐形成以下架构模式，并开源到Github上。
        1、TCL Connect APP采用组件化思想，整体由app集成组件+common通用组件+多业务组件组成，业务组件之间互不干扰，单独调试开发。
        2、其中每个组件内部采用"单Activity+多Fragment架构，由Fragment单元管理页面逻辑。
        3、每个Fragment页面采用MVP思想进行业务与逻辑分。
        4、彻底放弃Dialog，用自定义Widget+<merge>布局方案取代。
        5、页面布局全部采用百分比布局的方案。
        6、网络请求采用轻量的xutils做二次封装。
        7、网络请求JSON数据实例化
        8、各种机型适配，各个Android版本适配
        9、适配国际化20多种语言
        12、
    */
}
