package com.de.rocket.app.ue.frag;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.de.rocket.app.R;
import com.de.rocket.app.ue.activity.RoxxActivity;
import com.de.rocket.bean.FragParamBean;
import com.de.rocket.ue.animation.BottomBothFragAnimation;
import com.de.rocket.ue.animation.BottomPushFragAnimation;
import com.de.rocket.ue.animation.LeftBothFragAnimation;
import com.de.rocket.ue.animation.LeftPushFragAnimation;
import com.de.rocket.ue.animation.RightBothFragAnimation;
import com.de.rocket.ue.animation.RightPushFragAnimation;
import com.de.rocket.ue.animation.TopBothFragAnimation;
import com.de.rocket.ue.animation.TopPushFragAnimation;
import com.de.rocket.ue.frag.RoFragment;
import com.de.rocket.ue.injector.BindView;
import com.de.rocket.ue.injector.Event;

/**
 * 转场动画相关
 * Created by haide.yin(haide.yin@tcl.com) on 2019/7/8 10:59.
 */
public class Frag_rocket_anim extends RoFragment {

    @BindView(R.id.tv_right)
    private TextView tvRight;
    @BindView(R.id.tv_title)
    private TextView tvTitle;

    @Override
    public int onInflateLayout() {
        return R.layout.frag_rocket_anim;
    }

    @Override
    public void initViewFinish(View inflateView) {
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        tvTitle.setText("转场动画");
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("RoxxActivity");
    }


    @Override
    public void onNexts(Object object) {
        if (object instanceof String) {
            toast((String) object);
        }
    }

    @Event(R.id.tv_right)
    private void toRoxx(TextView view) {
        activity.startActivity(new Intent(activity,RoxxActivity.class));
    }

    @Event(R.id.bt_anim_left_to_right)
    private void animLeftToRight(TextView view) {
        FragParamBean fragParamBean = new FragParamBean
                .Builder()
                .isTargetReload(true)
                .translateObject(new RightBothFragAnimation())
                .fragAnimation(new LeftBothFragAnimation())
                .build();
        toFrag(Frag_rocket_anim_detail.class,fragParamBean);
    }

    @Event(R.id.bt_anim_left_to_right_enter)
    private void animLeftToRightEnter(TextView view) {
        FragParamBean fragParamBean = new FragParamBean
                .Builder()
                .isTargetReload(true)
                .translateObject(new RightPushFragAnimation())
                .fragAnimation(new LeftPushFragAnimation())
                .build();
        toFrag(Frag_rocket_anim_detail.class,fragParamBean);
    }

    @Event(R.id.bt_anim_right_to_left)
    private void animRightToLeft(TextView view) {
        FragParamBean fragParamBean = new FragParamBean
                .Builder()
                .isTargetReload(true)
                .translateObject(new LeftBothFragAnimation())
                .fragAnimation(new RightBothFragAnimation())
                .build();
        toFrag(Frag_rocket_anim_detail.class,fragParamBean);
    }

    @Event(R.id.bt_anim_right_to_left_enter)
    private void animRightToLeftEnter(TextView view) {
        FragParamBean fragParamBean = new FragParamBean
                .Builder()
                .isTargetReload(true)
                .translateObject(new LeftPushFragAnimation())
                .fragAnimation(new RightPushFragAnimation())
                .build();
        toFrag(Frag_rocket_anim_detail.class,fragParamBean);
    }

    @Event(R.id.bt_anim_up_to_down)
    private void animUpToDown(TextView view) {
        FragParamBean fragParamBean = new FragParamBean
                .Builder()
                .isTargetReload(true)
                .translateObject(new BottomBothFragAnimation())
                .fragAnimation(new TopBothFragAnimation())
                .build();
        toFrag(Frag_rocket_anim_detail.class,fragParamBean);
    }

    @Event(R.id.bt_anim_up_to_down_enter)
    private void animUpToDownEnter(TextView view) {
        FragParamBean fragParamBean = new FragParamBean
                .Builder()
                .isTargetReload(true)
                .translateObject(new BottomPushFragAnimation())
                .fragAnimation(new TopPushFragAnimation())
                .build();
        toFrag(Frag_rocket_anim_detail.class,fragParamBean);
    }

    @Event(R.id.bt_anim_down_to_up)
    private void animDownToUp(TextView view) {
        FragParamBean fragParamBean = new FragParamBean
                .Builder()
                .isTargetReload(true)
                .translateObject(new TopBothFragAnimation())
                .fragAnimation(new BottomBothFragAnimation())
                .build();
        toFrag(Frag_rocket_anim_detail.class,fragParamBean);
    }

    @Event(R.id.bt_anim_down_to_up_enter)
    private void animDownToUpEnter(TextView view) {
        FragParamBean fragParamBean = new FragParamBean
                .Builder()
                .isTargetReload(true)
                .translateObject(new TopPushFragAnimation())
                .fragAnimation(new BottomPushFragAnimation())
                .build();
        toFrag(Frag_rocket_anim_detail.class,fragParamBean);
    }
}
