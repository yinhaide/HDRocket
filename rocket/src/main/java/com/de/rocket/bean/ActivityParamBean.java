package com.de.rocket.bean;

import com.de.rocket.R;
import com.de.rocket.ue.frag.DefaultFragment;

import java.util.Arrays;

public class ActivityParamBean extends RoBean {

    private boolean isSaveInstanceState;//Activity恢复是是否从保存的状态中恢复，false的话整个页面重新创建显示第一个Fragment
    private int layoutId;//Activity布局的Layout ID
    private int fragmentContainId;//Activity布局的Layout装Fragment的容器id
    private Class[] roFragments;//支持的Fragment列表
    private boolean isToastCustom;//是否用传统自定义的吐司风格，false表示用系统的吐司，系统风格在通知关闭情况也是禁用的
    private boolean showViewBall;//是否显示悬浮球
    private boolean enableCrashWindow;//是否显示框架自定义的崩溃的窗口
    private RecordBean recordBean;//日志配置
    private StatusBarBean statusBar;//状态栏的属性

    public ActivityParamBean() {
        //默认值
        this.isSaveInstanceState = true;
        this.fragmentContainId = R.id.fl_fragment_contaner;
        this.layoutId = R.layout.rocket_activity_default;
        this.roFragments = new Class[]{DefaultFragment.class};
        this.isToastCustom = true;
        this.showViewBall = true;
        this.enableCrashWindow = true;
        this.statusBar = new StatusBarBean();
        this.recordBean = new RecordBean();
    }

    public boolean isEnableCrashWindow() {
        return enableCrashWindow;
    }

    public void setEnableCrashWindow(boolean enableCrashWindow) {
        this.enableCrashWindow = enableCrashWindow;
    }

    public Class[] getRoFragments() {
        return roFragments;
    }

    public void setRoFragments(Class[] roFragments) {
        this.roFragments = roFragments;
    }

    public boolean isSaveInstanceState() {
        return isSaveInstanceState;
    }

    public void setSaveInstanceState(boolean saveInstanceState) {
        isSaveInstanceState = saveInstanceState;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public int getFragmentContainId() {
        return fragmentContainId;
    }

    public void setFragmentContainId(int fragmentContainId) {
        this.fragmentContainId = fragmentContainId;
    }

    public StatusBarBean getStatusBar() {
        return statusBar;
    }

    public void setStatusBar(StatusBarBean statusBar) {
        this.statusBar = statusBar;
    }

    public boolean isToastCustom() {
        return isToastCustom;
    }

    public void setToastCustom(boolean toastCustom) {
        isToastCustom = toastCustom;
    }

    public boolean isShowViewBall() {
        return showViewBall;
    }

    public void setShowViewBall(boolean showViewBall) {
        this.showViewBall = showViewBall;
    }

    public RecordBean getRecordBean() {
        return recordBean;
    }

    public void setRecordBean(RecordBean recordBean) {
        this.recordBean = recordBean;
    }

    @Override
    public String toString() {
        return "ActivityParamBean{" +
                "isSaveInstanceState=" + isSaveInstanceState +
                ", layoutId=" + layoutId +
                ", fragmentContainId=" + fragmentContainId +
                ", statusBar=" + statusBar +
                ", roFragments=" + Arrays.toString(roFragments) +
                ", isToastCustom=" + isToastCustom +
                ", showViewBall=" + showViewBall +
                ", recordBean=" + recordBean +
                ", enableCrashWindow=" + enableCrashWindow +
                '}';
    }
}
