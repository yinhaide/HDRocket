package com.de.rocket.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.PermissionChecker;

import com.de.rocket.bean.PermissionBean;
import com.de.rocket.listener.PermissionListener;
import com.de.rocket.ue.activity.RoActivity;
import com.de.rocket.ue.frag.PermissionFragment;
import com.de.rocket.utils.RoLogUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 权限请求的帮助类，有两种业务逻辑
 * 1、requestPermissions：多个权限同时请求，只关注是否全部允许权限，不关心个别
 * 2、requestEachPermissions：多个权限同时请求，关注单个权限允许情况，包括拒绝、同意、不再允许三种情况，分别回调
 */
public class PermissionHelper {

    private static volatile PermissionHelper instance;//单例

    /**
     * 创建单例
     */
    public static PermissionHelper getInstance() {
        if (instance == null) {
            synchronized (PermissionHelper.class) {
                if (instance == null) {
                    instance = new PermissionHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 用户勾选不再显示并点击拒绝，弹出打开设置页面申请权限，也可以自定义实现
     *
     * @param context Context
     * @param title   弹窗标题
     * @param message 申请权限解释说明
     * @param confirm 确认按钮的文字，默认OK
     * @param cancel  取消按钮呢的文字，默认不显示取消按钮
     */
    public static void requestDialogAgain(final Activity context, @NonNull String title, @NonNull String message, @NonNull String confirm, @NonNull String cancel, int requestCode) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle(title).setMessage(message);
            builder.setPositiveButton(confirm, (dialog, which) -> {
                startSettingActivity(context, requestCode);
                dialog.dismiss();
            });
            builder.setNegativeButton(cancel, (dialog, which) -> dialog.dismiss());
            builder.setCancelable(false);
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开设置页面打开权限
     *
     * @param activity    activity
     * @param requestCode 这里的requestCode和onActivityResult中requestCode要一致
     */
    public static void startSettingActivity(@NonNull Activity activity, int requestCode) {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 多个权限同时请求，关注单个权限允许情况，包括拒绝、同意、不再允许三种情况，分别回调
     *
     * @param activity               FragmentActivity
     * @param permissions            权限组
     * @param permissionListener 回调
     */
    public void requestPermission(RoActivity activity, String[] permissions, PermissionListener permissionListener) {
        RoLogUtil.v("PermissionHelper::requestPermission-->"+"permissions:"+ Arrays.toString(permissions));
        if (!needReqPermission(activity, permissions)) {
            if (permissionListener != null) {
                permissionListener.onResult(-1, true, getPermissionBean(activity,permissions));
            }
        } else {
            getPermissionFragment(activity, fragment -> fragment.requestPermission(permissions, permissionListener));
        }
    }

    /**
     * 读取代理Fragment，存在就取出来，不存在就创建，保证全局唯一性
     *
     * @param activity FragmentActivity
     */
    private void getPermissionFragment(RoActivity activity,AttachListener attachListener) {
        PermissionFragment fragment = (PermissionFragment) activity.getSupportFragmentManager().findFragmentByTag(PermissionFragment.class.getSimpleName());
        if (fragment == null) {
            try {
                final PermissionFragment newFragment = PermissionFragment.class.newInstance();
                FragmentManager fragmentManager = activity.getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.add(newFragment, PermissionFragment.class.getSimpleName());
                ft.commit();
                //加入队列执行，保证在Fragment Attach To Activity之后执行
                new Handler().post(() -> attachNext(attachListener,newFragment));
                //通知堆栈有更新
                List<String> activityStacks = new ArrayList<>();
                for(Fragment tempFragment : fragmentManager.getFragments()){
                    activityStacks.add(tempFragment.getClass().getSimpleName());
                }
                activity.getStack().setActivityStackList(activityStacks);
                FragHelper.getInstance().onFragmentStackChangeNext(activity.getStack().getRocketStackList(),activityStacks);
            } catch (IllegalAccessException | InstantiationException e) {
                //打印
                RoLogUtil.v("PermissionHelper::getPermissionFragment-->"+"e:"+ e.toString());
                //写日志
                RecordHelper.writeInnerLog(activity,"PermissionHelper::getPermissionFragment-->"+"e:"+ e.toString());
            }
        }else{
            attachNext(attachListener,fragment);
        }
    }

    /**
     * 是否需要请求权限
     *
     * @param activity FragmentActivity
     */
    private boolean needReqPermission(Activity activity, String[] permissions) {
        if (permissions == null) {
            return false;
        } else {
            List<Integer> permissionInt = new ArrayList<>();
            for (String permission : permissions) {
                permissionInt.add(PermissionChecker.checkSelfPermission(activity, permission));
            }
            Iterator result = permissionInt.iterator();
            Integer permissionDenied;
            do {
                if (!result.hasNext()) {
                    return false;
                }
                permissionDenied = (Integer) result.next();
            } while (permissionDenied != -1);
            return true;
        }
    }

    /**
     * 获取相关权限的情况
     *
     * @param activity Activity
     */
    private List<PermissionBean> getPermissionBean(Activity activity, String[] permissions) {
        List<PermissionBean> permissionBeans = new ArrayList<>();
        if (permissions != null) {
            for (String permission : permissions) {
                PermissionBean permissionBean = new PermissionBean();
                int result = PermissionChecker.checkSelfPermission(activity, permission);
                boolean dontAsk = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
                permissionBean.setName(permission);
                permissionBean.setGranted(result != -1);
                permissionBean.setDontAskAgain(dontAsk);
                permissionBeans.add(permissionBean);
            }
        }
        return permissionBeans;
    }

    /**
     * 相关权限是否在Manifest中注册了
     * @param activity Activity
     * @param permissions 相关权限
     */
    public static boolean isPermissionRegistManifest(Activity activity,String[] permissions){
        try {
            String[] manifestPermissions = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions;
            if(manifestPermissions != null && permissions != null) {
                List<String> manifestPermissionList = new ArrayList<>(Arrays.asList(manifestPermissions));
                List<String> permissionList = new ArrayList<>(Arrays.asList(permissions));
                return manifestPermissionList.containsAll(permissionList);
            }
        }catch (Exception e) {
            //打印
            RoLogUtil.e("PermissionHelper::isPermissionRegist-->"+ "e:" +e.toString());
            //写日志
            RecordHelper.writeInnerLog(activity,"PermissionHelper::isPermissionRegist-->"+ "e:" +e.toString());
        }
        return false;
    }

    /**
     * Fragment Attach to Activity 回调执行
     */
    private void attachNext(AttachListener attachListener,PermissionFragment fragment){
        if(attachListener != null){
            attachListener.onReady(fragment);
        }
    }

    /**
     * Fragment Attach to Activity 回调
     */
    private interface AttachListener{
        void onReady(PermissionFragment fragment);
    }
}
