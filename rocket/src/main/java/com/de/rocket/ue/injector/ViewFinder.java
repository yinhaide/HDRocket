package com.de.rocket.ue.injector;

import android.app.Activity;
import android.view.View;

/**
 * 通过id找到view
 * 如果想让这个类为内部类，其他地方不能引用的话用final修饰
 * final class ViewFinder{}
 */
public class ViewFinder {

    private View view;
    private Activity activity;

    public ViewFinder(View view) {
        this.view = view;
    }

    public ViewFinder(Activity activity) {
        this.activity = activity;
    }

    /**
     * 通过id找到view
     * @param id id
     */
    public View findViewById(int id) {
        if (view != null) return view.findViewById(id);
        if (activity != null) return activity.findViewById(id);
        return null;
    }

    /**
     * 通过ViewInfo找到view
     * @param info info
     */
    public View findViewByInfo(ViewInfo info) {
        return findViewById(info.value, info.parentId);
    }

    /**
     * 优先通过父类的id找到view
     * @param id viewid
     * @param pid 父类的viewid
     */
    public View findViewById(int id, int pid) {
        View pView = null;
        if (pid > 0) {
            pView = this.findViewById(pid);
        }
        View view;
        if (pView != null) {
            view = pView.findViewById(id);
        } else {
            view = this.findViewById(id);
        }
        return view;
    }
}
