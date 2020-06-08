package com.de.rocket.ue.frag;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.de.rocket.R;
import com.de.rocket.bean.FragParamBean;
import com.de.rocket.cons.RoKey;
import com.de.rocket.helper.FragHelper;
import com.de.rocket.helper.LocaleHelper;
import com.de.rocket.helper.StatusBarHelper;
import com.de.rocket.listener.PermissionListener;
import com.de.rocket.helper.ViewInjectHelper;
import com.de.rocket.helper.PermissionHelper;
import com.de.rocket.ue.activity.RoActivity;
import com.de.rocket.helper.ToastHelper;
import com.de.rocket.ue.animation.FragAnimation;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Fragment基类
 */
public abstract class RoFragment extends Fragment {

    //缓存activity
    public RoActivity activity;
    //是否已经反射
    private boolean injected = false;
    //是否进入后台
    private boolean isOnStop = false;
    //当前的Class,记录下来用于回退处理,防止getClass()得到的不是当前的Fragment
    private Class aClass;
    //权限回调
    private PermissionListener permissionListener;
    //是否需要优先处理权限（true会阻断生命周期:initViewFinish与onNext）
    private boolean needHanlePermission;
    //父类的View
    private View rootView;
    //转场动画是否结束,只有结束才会响应返回逻辑
    private boolean isAnimationEnd = true;
    //是否允许跳转，锁住，防止跳转过快。页面切换的逻辑是串行的，必须等待上个动作执行完，太快会被丢弃，防止误操作
    private boolean toFragEnable = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(getActivity() instanceof RoActivity)) throw new RuntimeException(getRoString(R.string.rocket_activity_extend_error));
        //如果片段尚未附加，或在其生命周期结束期间分离，则 getActivity() 将返回 null,防止这种情况需要缓存activity
        this.activity = (RoActivity) context;
        //记录当前的Class
        this.aClass = getClass();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            ViewInjectHelper.getInstance().injectFragment(this, view);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutID = onInflateLayout();
        if (layoutID <= 0) layoutID = R.layout.rocket_frag_default;
        rootView = inflater.inflate(layoutID, container, false);
        //View绑定
        ViewInjectHelper.getInstance().injectFragment(this, rootView);
        //标记已经绑定过了
        injected = true;
        //读取是否申请权限
        if(initPermission() != null && initPermission().length > 0){
            needHanlePermission = true;
        }
        //不需要权限的话直接执行initViewFinish
        handleViewFinish();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isOnStop && isReloadData() && isVisible()) {
            //从后台回来需要刷新数据的业务
            isOnStop = false;
            handleNext(getArguments());//处理数据传递
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        this.isOnStop = true;
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        //Fragment show and hide 都会执行这个回调，用来处理页面跳转逻辑
        //成功跳转才开锁，允许下次跳转
        toFragEnable = true;
        //只处理进入页面的情况
        if(enter){
            //处理业务逻辑与数据传递
            handleNext(getArguments());
            //处理权限申请
            if(needHanlePermission){
                handlePermission();
            }
            if(nextAnim > 0){//有转场动画的情况
                Animation anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                        isAnimationEnd = false;
                    }
                    public void onAnimationRepeat(Animation animation) {}
                    public void onAnimationEnd(Animation animation) {
                        //清除源Fragment
                        handleRemoveOrigin(getArguments());
                        //动画结束了
                        isAnimationEnd = true;
                        //回调转场动画结束
                        onFragAnimationEnd(animation);
                    }
                });
                return anim;
            }else{//没有转场动画的情况
                handleRemoveOrigin(getArguments());
            }
        }
        return null;
    }

    /**
     * 处理初始化权限
     */
    private boolean handlePermission(){
        //有权限处理的任务才会执行
        if(needHanlePermission){
            boolean hasRegistManifest = needPermissison(initPermission(), (requestCode, allAccept, permissionBeans) -> {
                //必须所有权限都通过才会执行生命周期
                //如果权限没有全部允许,还想执行生命周期的话，需要在子类调用:super.initPermission(rootView);
                if(allAccept){
                    needHanlePermission = false;
                    handleViewFinish();
                    handleNext(getArguments());
                }
                //执行父类的回调
                if(permissionListener != null){
                    permissionListener.onResult(requestCode,allAccept,permissionBeans);
                }
            });
            if(!hasRegistManifest){
                back();
            }
            return true;
        }
        return false;
    }

    /**
     * 处理页面初始化完成
     */
    private void handleViewFinish() {
        //没有权限处理的任务才会执行
        if(!needHanlePermission){
            initViewFinish(rootView);
        }
    }

    /**
     * 处理页面传参
     */
    private void handleNext(Bundle bundle) {
        //没有权限处理的任务才会执行
        if(!needHanlePermission){
            Serializable serializable = null;
            if (bundle != null) {
                serializable = bundle.getSerializable(RoKey.ARGUMENT_OBJECT_KEY);
            }
            //setArguments(null);//处理完毕之后要清空数据
            onNexts(serializable);//进行业务逻辑
        }
    }

    /**
     * 处理页面传参
     */
    private void handleRemoveOrigin(Bundle bundle) {
        if (bundle != null) {
            //需要清理上个Fragment
            if(bundle.getBoolean(RoKey.ARGUMENT_ORIGIN_REMOVE_KEY)){
                String originalTag = bundle.getString(RoKey.ARGUMENT_ORIGIN_CLASS_KEY);
                if(!TextUtils.isEmpty(originalTag)){
                    //删除源Fragment
                    FragHelper.getInstance().removeFrag(activity,originalTag);
                }
            }
        }
    }

    /**
     * Activity分发给Fragment的虚拟按键返回事件
     */
    public void handleBackPressed() {
        //需要动画结束才能返回
        if(isAnimationEnd){
            if(!onBackPressed()){
                List<String> rocketStackList = activity.getStack().getRocketStackList();
                if(rocketStackList.size() > 1){//大于两个则执行回页面退操作
                    back();
                }else{
                    if (!activity.onRoBackPressed()) {//执行完Fragment的回退逻辑再执行Activity的回退逻辑
                        activity.finish();//finish()关闭页面
                    }
                }
            }
        }
    }

    /**
     * 检查跳转参数是否合法
     *
     * @param targetClazz 目标Class
     * @param object      跳转携带的参数
     */
    private boolean checkParamAvalible(Class targetClazz, Object object) {
        if (object != null && !(object instanceof Serializable)) {
            toast(R.string.rocket_object_extend_serializable);
            return false;
        }
        if (!RoFragment.class.isAssignableFrom(targetClazz)) {
            toast(R.string.rocket_fragment_extend_error);
            return false;
        }
        Class[] roFragments = activity.getActivityParamBean().getRoFragments();
        if (roFragments != null) {
            boolean isAvalible = false;
            for (Class mClass : roFragments) {
                if (mClass == targetClazz) {
                    isAvalible = true;
                }
            }
            if (!isAvalible) {
                toast(R.string.rocket_fragment_not_register);
                return false;
            }
        }
        return true;
    }

    /* ************************************************************* */
    /*                          页面跳转
    /* ************************************************************* */

    /**
     * 默认跳转
     * @param targetClass 已经注册的Fragment
     */
    public void toFrag(Class targetClass) {
        toFrag(targetClass, new FragParamBean.Builder().build());
    }

    /**
     * 带详细参数跳转
     * @param targetClass 目标Fragment
     * @param isOriginalRemove 是否要回收内存
     * @param isTargetReload 是否要刷新目标Fragment
     * @param object 跳转携带的参数
     * @param clearTop 清掉目标Fragment在栈位置顶端所有的Fragment
     * @param fragAnimation 转场动画
     */
    private void toFrag(@NonNull Class targetClass, boolean isOriginalRemove, boolean isTargetReload, Object object, boolean clearTop, FragAnimation fragAnimation) {
        if(!isAdded()){//如果Fragment没有存在Activity中，不应该执行页面切换动作
            return;
        }
        if(!isAnimationEnd) {//必须要等待动画结束才能回退
            return;
        }
        if (!checkParamAvalible(targetClass, object)) {//参数不合理不跳转
            return;
        }
        if(!toFragEnable){//跳转锁住丢弃
            return;
        }
        boolean result = FragHelper.getInstance().transfer(activity,
                aClass,
                targetClass,
                activity.getActivityParamBean().getFragmentContainId(),
                isOriginalRemove,
                isTargetReload,
                clearTop,
                object,
                activity.getActivityParamBean().getRoFragments(),
                fragAnimation);
        //跳转成功先锁住
        if(result){
            toFragEnable = false;
        }
    }

    /**
     * 带详细参数跳转
     * @param targetClass 目标Fragment
     * @param fragParamBean 页面切换参数集合
     */
    public void toFrag(@NonNull Class targetClass,@NonNull FragParamBean fragParamBean) {
        //如果Fragment没有存在Activity中，不应该执行页面切换动作
        if(!isAdded()){
            return;
        }
        //必须要等待动画结束才能回退
        if(!isAnimationEnd) {
            return;
        }
        //参数不合理不跳转
        if (fragParamBean.getTranslateObject() != null && !checkParamAvalible(targetClass, fragParamBean.getTranslateObject())) {
            return;
        }
        //跳转锁住丢弃
        if(!toFragEnable){
            return;
        }
        boolean result = FragHelper.getInstance().transfer(activity,
                aClass,
                targetClass,
                activity.getActivityParamBean().getFragmentContainId(),
                fragParamBean.isOriginalRemove(),
                fragParamBean.isTargetReload(),
                fragParamBean.isClearTop(),
                fragParamBean.getTranslateObject(),
                activity.getActivityParamBean().getRoFragments(),
                fragParamBean.getFragAnimation());
        //跳转成功先锁住
        if(result){
            toFragEnable = false;
        }
    }

    /* ************************************************************* */
    /*                          跳转到activity相关
    /* ************************************************************* */

    /**
     * Acticity显示跳转
     */
    public void toAct(Class activityClass){
        toAct(activityClass,null,null);
    }

    /**
     * Acticity显示跳转
     */
    public void toAct(Class activityClass,String targetFragTag){
        toAct(activityClass,targetFragTag,null);
    }

    /**
     * 从Fragment中显示跳转到Activity
     * @param activityClass 对应的Activity
     * @param targetFragTag 对应的Fragment的SimpleName
     * @param object 携带的数据
     */
    public void toAct(Class activityClass,String targetFragTag,Object object){
        if (object != null && !(object instanceof Serializable)) throw new RuntimeException("Object Must implements Serializable");
        toAct( new Intent(this.activity,activityClass),targetFragTag,object);
    }

    /**
     * Acticity隐式跳转
     */
    public void toAct(String action){
        toAct(action,null,null);
    }

    /**
     * Acticity隐式跳转
     */
    public void toAct(String action,String targetFragTag){
        toAct(action,targetFragTag,null);
    }

    /**
     * 从Fragment中隐式跳转到Activity
     * @param action 对应的Activity的Action
     * @param targetFragTag 对应的Fragment的SimpleName
     * @param object 携带的数据
     */
    public void toAct(String action,String targetFragTag,Object object){
        if (object != null && !(object instanceof Serializable)) throw new RuntimeException("Object Must implements Serializable");
        toAct(new Intent().setAction(action),targetFragTag,object);
    }

    /**
     * Acticity跳转
     */
    private void toAct(Intent intent,String targetFragTag,Object object){
        if(!isAdded()){//如果Fragment没有存在Activity中，不应该执行页面切换动作
            return;
        }
        if(!isAnimationEnd) {//必须要等待动画结束才能回退
            return;
        }
        Bundle bundle = new Bundle();
        if(!TextUtils.isEmpty(targetFragTag)){
            bundle.putString(RoKey.ARGUMENT_TARGET_CLASS_KEY,targetFragTag);
        }
        if(object != null){
            bundle.putSerializable(RoKey.ARGUMENT_OBJECT_KEY,(Serializable) object);
        }
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    /* ************************************************************* */
    /*                          返回相关
    /* ************************************************************* */

    /**
     * 返回上一个页面
     */
    public void back() {
        back(false);
    }

    /**
     * 返回
     */
    public void back(boolean isTargetReload) {
        back(isTargetReload,null);
    }

    /**
     * 返回
     */
    public void back(boolean isTargetReload, Object object) {
        back(isTargetReload,object,null);
    }

    /**
     * 带详细参数返回
     * @param isTargetReload 刷新目标页面
     * @param object 传递对象
     * @param fragAnimation 转场动画
     */
    public void back(boolean isTargetReload, Object object,FragAnimation fragAnimation) {
        if(!isAdded()){//如果Fragment没有存在Activity中，不应该执行页面切换动作
            return;
        }
        if(!isAnimationEnd) {//必须要等待动画结束才能回退
            return;
        }
        if(!toFragEnable){//跳转锁住丢弃
            return;
        }
        //处理页面跳转
        FragmentManager fm = activity.getSupportFragmentManager();
        List<String> rocketStackList = activity.getStack().getRocketStackList();
        //只有Fragment的数量在两个以上才会作回退的操作
        if (rocketStackList.size() > 1) {
            String lastTag = rocketStackList.get(rocketStackList.size() - 1);
            String backTag = rocketStackList.get(rocketStackList.size() - 2);
            if (!TextUtils.isEmpty(lastTag) && !TextUtils.isEmpty(backTag)) {
                RoFragment backFragment = (RoFragment)fm.findFragmentByTag(backTag);
                RoFragment lastFragment = (RoFragment)fm.findFragmentByTag(lastTag);
                if (backFragment != null && lastFragment != null) {
                    boolean result = FragHelper.getInstance().transfer(activity,
                            lastFragment.getClass(),
                            backFragment.getClass(),
                            activity.getActivityParamBean().getFragmentContainId(),
                            true,
                            isTargetReload,
                            false,
                            object,
                            activity.getActivityParamBean().getRoFragments(),
                            fragAnimation);
                    //跳转成功先锁住
                    if(result){
                        toFragEnable = false;
                    }
                }
            }
        }else if(rocketStackList.size() == 1){
            //如果只有一个Fragment则直接调用activity.onBackPressed()
            activity.onBackPressed();
        }
    }

    /* ************************************************************* */
    /*                          吐司相关
    /* ************************************************************* */

    /**
     * 资源ID吐司
     */
    public void toast(@StringRes int stringId) {
        toast(getRoString(stringId));
    }

    /**
     * 吐司
     * @param tip 吐司内容
     */
    public void toast(String tip) {
        toast(tip, ToastHelper.DEFAULT_MILTIME);
    }

    /**
     * 资源ID加显示时间的吐司
     */
    public void toast(@StringRes int stringId, int duration) {
        toast(getRoString(stringId), duration);
    }

    /**
     * 字符串加显示时间的吐司
     */
    public void toast(String tip, int duration) {
        if(!isAdded()){//如果Fragment没有存在Activity中，不应该执行吐司动作
            return;
        }
        if (activity.getActivityParamBean().isToastCustom()) {
            ToastHelper.toastCustom(tip, duration);
        } else {
            ToastHelper.toastSystem(activity, tip, duration, ToastHelper.DEFAULT_GRAVATY, ToastHelper.DEFAULT_XOFFSET, ToastHelper.DEFAULT_YOFFSET);
        }
    }

    /* ************************************************************* */
    /*                          权限相关
    /* ************************************************************* */

    /**
     * 请求权限，关注每个权限的允许与拒绝情况
     * 注意：权限必须要在AndroidManifest.xml注册才能正常使用
     * @param permissions 权限列表
     * @param permissionListener 回调函数
     * @return 是否已经在AndroidManifest中注册
     */
    public boolean needPermissison(String[] permissions, PermissionListener permissionListener) {
        boolean hasRegistManifest = PermissionHelper.isPermissionRegistManifest(activity,permissions);
        if(hasRegistManifest){
            PermissionHelper.getInstance().requestPermission(activity, permissions, permissionListener);
        }else{
            toast("请先在AndroidManifest.xml中注册全部权限:"+ Arrays.toString(permissions));
        }
        return hasRegistManifest;
    }

    /**
     * 打开系统设置,让用户强制打开权限
     * @param requestCode requestCode
     */
    public void startSettingActivity(int requestCode) {
        PermissionHelper.startSettingActivity(activity,requestCode);
    }

    /**
     * 页面进来必须要先要求权限
     * 必须所有权限都通过才会执行生命周期
     * 如果权限没有全部允许,还想执行生命周期的话，需要在子类调用:super.initPermission(rootView);
     */
    @CallSuper
    public String[] initPermission(){
        if(needHanlePermission){
            needHanlePermission = false;
            handleViewFinish();
            handleNext(getArguments());
        }
        return null;
    }

    /**
     * 设置权限申请回调
     */
    public void  setPermissionListener(PermissionListener permissionListener){
        this.permissionListener = permissionListener;
    }

    /* ************************************************************* */
    /*                           国际化相关
    /* ************************************************************* */

    /**
     * 读取APP需要缓存的语言
     *
     * @return locale 当前设置的语言
     */
    public Locale getSaveLocale() {
        return LocaleHelper.getLocale(activity);
    }

    /**
     * 设置APP需要缓存的语言
     *
     * @param locale 需要设置的语言
     */
    public void setSaveLocale(Locale locale) {
        LocaleHelper.setLocale(activity, locale);
    }

    /* ************************************************************* */
    /*                          状态栏相关
    /* ************************************************************* */

    /**
     * 设置状态栏颜色
     *
     * @param color         状态栏颜色值
     * @param isImmersion   是否是沉浸式风格，隐藏状态栏创建自定义状态栏
     */
    public void setStatusBarColor(@ColorInt int color,boolean isImmersion) {
        StatusBarHelper.setStatusBarColor(activity,color,isImmersion);
    }

    /**
     * 使指定的View向下Padding偏移一个状态栏高度，留出状态栏空间，主要用于设置沉浸式后,页面顶到顶端有突兀感
     *
     * @param targetView        需要偏移的View
     * @param enable            开启或者关闭
     * @param isPaddingOrMargin 向下偏移是padding还是margin，true的话是padding，false的话是margin
     */
    public void setOffsetStatusBar(View targetView, boolean enable, boolean isPaddingOrMargin) {
        StatusBarHelper.setOffsetStatusBar(activity,targetView,enable,isPaddingOrMargin);
    }

    /* ************************************************************* */
    /*                     资源工具类的方法
    /* ************************************************************* */

    /**
     * 通过resId找出图片的方法，推荐方法，其他的已经过期
     * @param resId 资源id
     */
    public Drawable getRoDrawable(@DrawableRes int resId) {
        return ContextCompat.getDrawable(activity, resId);
    }

    /**
     * 通过resId找出颜色的方法，推荐方法，其他的已经过期
     * @param resId 资源id
     */
    public int getRoColor(@ColorRes int resId) {
        return ContextCompat.getColor(activity, resId);
    }

    /**
     * 防止在Fragment销毁的时候调用资源导致崩溃
     *
     * @return String
     */
    public String getRoString(@StringRes int resId) {
        return getRoResources().getString(resId);
    }

    /**
     * 防止在Fragment销毁的时候调用资源导致崩溃
     *
     * @return Resources
     */
    public Resources getRoResources() {
        if(isAdded()){
            return requireContext().getResources();
        }else{
            return activity.getResources();
        }
    }

    /* ************************************************************* */
    /*                     供子类重写的方法
    /* ************************************************************* */

    /**
     * 是否由内部处理返回事件,默认false,由用户处理
     *
     * @return backAuto
     */
    protected boolean onBackPressed() {
        return false;
    }

    /**
     * 从后台进入前台是否要刷新数据,默认false
     *
     * @return isReloadData
     */
    protected boolean isReloadData() {
        return false;
    }

    /**
     * Fragment转场动画结束的回调
     *
     * @param  animation 转场动画
     */
    public void onFragAnimationEnd(Animation animation){}

    /* ************************************************************* */
    /*                     供子类继承的抽象方法
    /* ************************************************************* */

    /**
     * 传递页面Layout
     *
     * @return the layoutID
     */
    public abstract int onInflateLayout();

    /**
     * 页面初始化完成
     *
     * @param inflateView 初始化完成的View
     */
    public abstract void initViewFinish(View inflateView);

    /**
     * 开始处理业务逻辑,不管有没有数据都会回调
     *
     * @param object 跳转传递的对象，需要实现Serializable序列化
     */
    public abstract void onNexts(Object object);
}
