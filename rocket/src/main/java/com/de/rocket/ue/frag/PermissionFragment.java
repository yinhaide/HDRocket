package com.de.rocket.ue.frag;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.SparseArray;
import android.view.View;

import com.de.rocket.R;
import com.de.rocket.bean.PermissionBean;
import com.de.rocket.listener.PermissionListener;
import com.de.rocket.utils.RoLogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 我们知道,Fragment 一般依赖于Activity存活,并且生命周期跟Activity差不多。
 * 因此,我们进行权限申请的时候，可以利用透明的 Fragment 进行申请,在里面处理完之后,再进行相应的回调。
 * 第一步：当我们申请权限申请的时候,先查找我们当前 Activity 是否存在代理 fragment,不存在，进行添加,并使用代理 Fragment 进行申请权限
 * 第二步：在代理 Fragment 的 onRequestPermissionsResult 方法进行相应的处理,判断是否授权成功
 * 第三步：进行相应的回调
 */
public class PermissionFragment extends RoFragment {

    private SparseArray<PermissionListener> mEachCallbacks = new SparseArray<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        RoLogUtil.v("PermissionFragment::onAttach");
    }

    @Override
    public int onInflateLayout() {
        return R.layout.rocket_frag_default;
    }

    @Override
    public void initViewFinish(View inflateView) {
        /**
         * Control whether a fragment instance is retained across Activity re-creation (such as from a configuration change)
         * 表示当 Activity 重新创建的时候， fragment 实例是否会被重新创建（比如横竖屏切换），设置为 true，表示 configuration change 的时候，fragment 实例不会背重新创建，这样，有一个好处，即
         * configuration 变化的时候，我们不需要再做额外的处理。因此， fragment 该方法也常常被用来处理 Activity re-creation 时候数据的保存
         */
        setRetainInstance(true);
    }

    @Override
    public void onNexts(Object object) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        handlePermissionCallBack(requestCode, permissions, grantResults);
    }

    /**
     * 多个权限同时请求，关注单个权限允许情况，包括拒绝、同意、不再允许三种情况，分别回调
     *
     * @param permissions 权限组
     * @param permissionListener    回调
     */
    public void requestPermission(@NonNull String[] permissions, PermissionListener permissionListener) {
        int requestCode = makeRequestCode();
        mEachCallbacks.put(requestCode, permissionListener);
        requestPermissions(permissions, requestCode);
    }


    /**
     * 随机生成唯一的requestCode，最多尝试10次
     *
     * @return requestCode
     */
    private int makeRequestCode() {
        Random mCodeGenerator = new Random();
        int requestCode;
        int tryCount = 0;
        do {
            requestCode = mCodeGenerator.nextInt(0x0000FFFF);
            tryCount++;
        } while (mEachCallbacks.indexOfKey(requestCode) >= 0 && tryCount < 10);
        return requestCode;
    }

    /**
     * 处理权限回调
     *
     * @param requestCode  请求码
     * @param permissions  权限组
     * @param grantResults 授权情况
     */
    private void handlePermissionCallBack(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionListener permissionListener = mEachCallbacks.get(requestCode);
        if (permissionListener != null) {
            //存在才需要移除
            mEachCallbacks.remove(requestCode);
            boolean allGranted = true;
            List<PermissionBean> permissionBeans = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                int grantResult = grantResults[i];
                String name = permissions[i];
                if (grantResult == PackageManager.PERMISSION_GRANTED) {//单个权限允许
                    permissionBeans.add(new PermissionBean(name, true, false));
                } else {//单个权限拒绝
                    //只要有一个拒绝就会false
                    allGranted = false;
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, name)) {//下次还会询问
                        permissionBeans.add(new PermissionBean(name, false, false));
                    } else {//选择不再询问
                        permissionBeans.add(new PermissionBean(name, false, true));
                    }
                }
            }
            permissionListener.onResult(requestCode, allGranted, permissionBeans);
        }
    }
}
