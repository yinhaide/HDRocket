package com.de.rocket.app.ue.frag;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.de.rocket.app.R;
import com.de.rocket.app.ue.activity.RoxxActivity;
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
        toFrag(Frag_rocket_anim_detail.class,false,true, new RightBothFragAnimation(),false,new LeftBothFragAnimation());
    }

    @Event(R.id.bt_anim_left_to_right_enter)
    private void animLeftToRightEnter(TextView view) {
        toFrag(Frag_rocket_anim_detail.class,false,true,new RightPushFragAnimation(),false,new LeftPushFragAnimation());
    }

    @Event(R.id.bt_anim_right_to_left)
    private void animRightToLeft(TextView view) {
        toFrag(Frag_rocket_anim_detail.class,false,true,new LeftBothFragAnimation(),false,new RightBothFragAnimation());
    }

    @Event(R.id.bt_anim_right_to_left_enter)
    private void animRightToLeftEnter(TextView view) {
        toFrag(Frag_rocket_anim_detail.class,false,true,new LeftPushFragAnimation(),false,new RightPushFragAnimation());
    }

    @Event(R.id.bt_anim_up_to_down)
    private void animUpToDown(TextView view) {
        toFrag(Frag_rocket_anim_detail.class,false,true,new BottomBothFragAnimation(),false,new TopBothFragAnimation());
    }

    @Event(R.id.bt_anim_up_to_down_enter)
    private void animUpToDownEnter(TextView view) {
        toFrag(Frag_rocket_anim_detail.class,false,true,new BottomPushFragAnimation(),false,new TopPushFragAnimation());
    }

    @Event(R.id.bt_anim_down_to_up)
    private void animDownToUp(TextView view) {
        toFrag(Frag_rocket_anim_detail.class,false,true,new TopBothFragAnimation(),false,new BottomBothFragAnimation());
    }

    @Event(R.id.bt_anim_down_to_up_enter)
    private void animDownToUpEnter(TextView view) {
        toFrag(Frag_rocket_anim_detail.class,false,true,new TopPushFragAnimation(),false,new BottomPushFragAnimation());
    }
}
