package com.de.rocket.ue.widget;

import android.content.Context;
import android.graphics.Path;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.de.rocket.R;
import com.de.rocket.Rocket;
import com.de.rocket.helper.FragHelper;
import com.de.rocket.ue.activity.RoActivity;
import com.de.rocket.ue.layout.PercentLinearLayout;
import com.de.rocket.ue.layout.PercentRelativeLayout;
import com.de.rocket.utils.ViewAnimUtil;

import java.util.List;

public class FragRouterWidget extends RelativeLayout {

    //用户缓存的ViewID
    public static final int ROUTER_WIDGET_ID = R.id.rocket_global_widget_view;

    private TextView tvTitle;// 标题
    private TextView tvContent;// 内容
    private TextView tvSwitch;// 切换视图栈
    private PercentRelativeLayout prlBg;//灰色背景
    private PercentRelativeLayout rlContent;
    private PercentLinearLayout pllBall;//整个悬浮球视图
    private ImageView ivBall;//悬浮球
    private boolean isRocketStack = true;//是否是显示Rocket的视图栈，不然就是Activity视图栈
    private boolean enableMove = false;//是否允许悬浮球随着手指移动

    public FragRouterWidget(Context context) {
        this(context, null, 0);
    }

    public FragRouterWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FragRouterWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
        initListener();
    }

    /**
     * 初始化View
     */
    private void initView(Context context) {
        View inflate = View.inflate(context, R.layout.rocket_widget_frag_router, this);
        prlBg = inflate.findViewById(R.id.rl_bg);
        ivBall = inflate.findViewById(R.id.iv_ball);
        tvTitle = inflate.findViewById(R.id.tv_title);
        tvContent = inflate.findViewById(R.id.tv_router);
        tvSwitch = inflate.findViewById(R.id.tv_switch);
        rlContent = inflate.findViewById(R.id.prl_content);
        pllBall = inflate.findViewById(R.id.pll_ball);
        //支持点击滑动
        tvContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        //长按
        ivBall.setOnLongClickListener(v -> {
            if(!enableMove){
                ivBall.setBackgroundResource(R.drawable.rocket_round_primary_pd10);
            }else{
                ivBall.setBackgroundResource(R.drawable.rocket_round_rocket_pd10);
            }
            enableMove = !enableMove;
            return true;
        });
    }

    /**
     * 悬浮球与背景的点击事件处理
     */
    private void initListener() {
        //内容页面点击无需响应
        rlContent.setOnClickListener(v -> {
            //不做任何操作
        });
        //背景点击
        prlBg.setOnClickListener(v -> {
            if (prlBg.isShown()) {
                ViewAnimUtil.hideFade(prlBg, 1f, 0f, 400);
            }
        });
        //悬浮球的点击
        ivBall.setOnClickListener(v -> {
            RoActivity activity = Rocket.getTopActivity();
            if (!prlBg.isShown() && activity != null) {
                List<String> framentStacts = isRocketStack
                        ? activity.getStack().getRocketStackList()
                        : activity.getStack().getActivityStackList();
                setStackContent(framentStacts);
                ViewAnimUtil.showFade(prlBg, 0f, 1f, 400);
            }
        });
        //切换视图栈
        tvSwitch.setOnClickListener(v -> {
            RoActivity activity = Rocket.getTopActivity();
            if(activity != null){
                isRocketStack = !isRocketStack;
                switchStack();
                List<String> framentStacts = isRocketStack
                        ? activity.getStack().getRocketStackList()
                        : activity.getStack().getActivityStackList();
                setStackContent(framentStacts);
            }
        });
        //路由栈的变化
        FragHelper.getInstance().setOnFragmentStackChangeListener((rocketStacks, activityStacks) -> {
            List<String> framentStacts = isRocketStack ? rocketStacks : activityStacks;
            setStackContent(framentStacts);
        });
    }

    /**
     * 显示栈视图
     */
    public void showStackView() {
        ivBall.performClick();
    }

    /**
     * 切换视图的内容
     */
    private void switchStack(){
        if(isRocketStack){
            tvTitle.setText(R.string.rocket_router_rocket);
            tvSwitch.setText(R.string.rocket_router_activity);
        }else{
            tvTitle.setText(R.string.rocket_router_activity);
            tvSwitch.setText(R.string.rocket_router_rocket);
        }
    }

    /**
     * 设置视图的内容
     */
    private void setStackContent(List<String> stackList) {
        if (stackList != null && stackList.size() > 0) {
            StringBuilder temp = new StringBuilder();
            //倒序添加
            for (int i = stackList.size() - 1; i >= 0; i--) {
                temp.append(stackList.get(i));
                //最后一个不需要换行
                if(i != 0){
                    temp.append("\n");
                }
            }
            tvContent.setText(temp);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(enableMove){
            int x = getWidth() - (int) event.getX();
            int y = (int) event.getY();
            if(y < pllBall.getHeight()/2 || (getHeight() - y) < pllBall.getHeight()/2){
                return false;
            }
            if(x < pllBall.getWidth()/2 || (getWidth() - x) < pllBall.getWidth()/2){
                return false;
            }
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) pllBall.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin, y - pllBall.getHeight()/2,layoutParams.rightMargin, layoutParams.bottomMargin);
            layoutParams.setMarginEnd(x - pllBall.getWidth()/2);
            pllBall.setLayoutParams(layoutParams);
        }
        return false;
    }
}
