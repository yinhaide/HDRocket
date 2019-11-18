package com.de.rocket.helper;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.de.rocket.bean.AnimationBean;
import com.de.rocket.cons.RoKey;
import com.de.rocket.ue.activity.RoActivity;
import com.de.rocket.ue.frag.RoFragment;
import com.de.rocket.utils.RoLogUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Fragment的帮助类
 * 知识点:(https://blog.csdn.net/Small_Lee/article/details/50589165)、https://www.jianshu.com/p/d9143a92ad94
 * <p>
 * FragmentTransaction.commit():
 * 1、每个FragmentTransaction只能commit一次
 * 2、Activity执行完onSaveInstanceState()方法后不能再执行commit()方法
 * 3、你不应该在FragmentActivity的onResume()方法中提交transactions。因为有些时候这个函数可以在Activity的状态恢复前被调用。如果你的应用要求在除onCreate()函数之外的其他Activity生命周期函数中提交transaction，你可以在FragmentActivity的onResumeFragments()函数或者Activity的onPostResume()函数中提交。
 * 4、避免在异步回调函数中提交transactions。
 * <p>
 * FragmentTransaction.commitAllowingStateLoss：
 * commit()函数和commitAllowingStateLoss()函数的唯一区别就是当发生状态丢失的时候，后者不会抛出一个异常。通常你不应该使用这个函数，因为它意味可能发生状态丢失。当然，更好的解决方案是commit函数确保在Activity的状态保存之前调用，这样会有一个好的用户体验。除非状态丢失的可能无可避免，否则就不应该使用commitAllowingStateLoss()函数。
 * <p>
 * FragmentTransaction.executePendingTransactions():
 * 调用commit()方法并不立即执行这个事务，而是在Activity的UI线程之上（”main”线程）调度运行，等到消息队列准备好才会执行。但是，如果需要，可以调用来自UI线程的executePendingTransactions()方法，直接执行被commit()方法提交的事务。通常直到事务依赖其他线程的工作时才需要这样做。
 * <p>
 * commitNow()和commitNowAllowingStateLoss()
 * 调用这两个方法类似于先执行commit()/commitAllowingStateLoss()然后执行executePendingTransactions()方法。但也有区别。
 * 区别一:不支持添加到回退栈的操作，如果还调用addToBackStack(String name)方法会报一个IllegalStateException异常
 * 区别二:源码没有再使用Handler，而是直接执行
 * 官方更推荐使用commitNow（）和commitNowAllowingStateLoss()来代替先执行commit（）/commitAllowingStateLoss()然后执行executePendingTransactions()这种方式，因为后者会有不可预料的副作用。
 * <p>
 * 异常：Can not perform this action after onSaveInstanceState
 * 在你离开当前Activity等情况下，系统会调用onSaveInstanceState()帮你保存当前Activity的状态、数据等，直到再回到该Activity之前（onResume()之前），你执行Fragment事务，就会抛出该异常！（一般是其他Activity的回调让当前页面执行事务的情况，会引发该问题）
 * 解决方法：
 * 1、该事务使用commitAllowingStateLoss()方法提交，但是有可能导致该次提交无效！（宿主Activity被强杀时）
 * 2、利用onActivityForResult()/onNewIntent()，可以做到事务的完整性，不会丢失事务
 */
public class FragHelper {

    private static volatile FragHelper instance;//单例

    /**
     * 创建单例
     */
    public static FragHelper getInstance() {
        if (instance == null) {
            synchronized (FragHelper.class) {
                if (instance == null) {
                    instance = new FragHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 读取栈顶Fragment携带的数据
     */
    public Serializable getPeekRocketObject(RoActivity activity){
        String peekRocket = activity.getStack().peekRocket();
        if(!TextUtils.isEmpty(peekRocket)){
            FragmentManager fm = activity.getSupportFragmentManager();
            Fragment peekRocketFragment = fm.findFragmentByTag(peekRocket);
            if(peekRocketFragment != null){
                Bundle bundle = peekRocketFragment.getArguments();
                if(bundle != null){
                    return bundle.getSerializable(RoKey.ARGUMENT_OBJECT_KEY);
                }
            }
        }
        return null;
    }

    /**
     * 返回顶部的Fragment
     */
    public Fragment getTopRocketStackFragment(RoActivity activity){
        String peekRocket = activity.getStack().peekRocket();
        if(!TextUtils.isEmpty(peekRocket)){
            return activity.getSupportFragmentManager().findFragmentByTag(peekRocket);
        }
        return null;
    }

    /**
     * Fragment切换
     * @return 是否切换成功
     */
    public boolean transfer(RoActivity activity, Class originalClazz, Class targetClazz, @IdRes int containid, boolean isOriginalRemove, boolean isTargetReload, boolean clearTop, Object object, Class[] tags, AnimationBean animationBean) {
        //重要参数不能为空
        if (activity == null || originalClazz == null || tags == null || targetClazz == null)
            throw new RuntimeException("transfer params must not be null");
        //传递对象必须要继承Serializable
        if (object != null && !(object instanceof Serializable))
            throw new RuntimeException("Object must implements Serializable");
        //切换的class必须在tags内
        List<Class> tagsList = Arrays.asList(tags);
        if (!tagsList.contains(originalClazz) || !tagsList.contains(targetClazz))
            throw new RuntimeException("Transfer class must regist first");
        //切换的class必须继承Rofragment
        if (!RoFragment.class.isAssignableFrom(originalClazz) || !RoFragment.class.isAssignableFrom(targetClazz))
            throw new RuntimeException("Fragment must extend RoFragment");
        try {
            //获得管理器
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            //Fragment之间传递的Bundle
            Bundle targetBundle = new Bundle();

            /* *************************** 处理转场动画 ******************************* */
            //找出目标Fragment
            String targetTag = targetClazz.getSimpleName();
            RoFragment targetFragment = (RoFragment) fm.findFragmentByTag(targetTag);
            //如果设置了转场动画,需要刷新页面,isTargetReload强制设置为true
            if(animationBean != null){
                isTargetReload = true;
                if(targetFragment != null && targetFragment.getArguments() != null){
                    object = targetFragment.getArguments().getSerializable(RoKey.ARGUMENT_OBJECT_KEY);
                }
            }
            //设置转场动画
            if(animationBean != null){
                if(animationBean.getTransitionID() > 0){
                    ft.setTransition(animationBean.getTransitionID());
                }else{
                    ft.setCustomAnimations(animationBean.getEnter(), animationBean.getExit());
                }
            }
            /* *************************** 处理转场动画 ******************************* */

            /* *************************** 隐藏全部的fragment ******************************* */
            for (Class tempClass : tags) {
                Fragment hideFragment = fm.findFragmentByTag(tempClass.getSimpleName());
                if (hideFragment != null && hideFragment.isAdded()) {
                    ft.hide(hideFragment);
                }
            }
            /* *************************** 隐藏全部的fragment ******************************* */

            /* *************************** 移除源Fragment ******************************* */
            //是否要移除源Fragment放在转场动画之后执行,为了适配转场动画
            if(!originalClazz.equals(targetClazz)){//如果两个Class相同，延迟移除会出现空白页面
                targetBundle.putBoolean(RoKey.ARGUMENT_ORIGIN_REMOVE_KEY, isOriginalRemove);
                targetBundle.putString(RoKey.ARGUMENT_ORIGIN_CLASS_KEY,originalClazz.getSimpleName());
            }
            /* *************************** 移除源Fragment ******************************* */

            /* *************************** 清除目标的顶端的fragment ******************************* */
            if(clearTop){
                //得到目标class顶端的所有franment
                List<String> topStackList = activity.getStack().getClearTopRocketStackList(originalClazz.getSimpleName(),targetClazz.getSimpleName());
                for(String clearTag : topStackList){
                    activity.getStack().popRocket(clearTag);
                    Fragment clearFragment = fm.findFragmentByTag(clearTag);
                    if (clearFragment != null) {
                        ft.remove(clearFragment);
                    }
                }
                //通知栈变化
                notifyStackChange(activity,fm);
            }
            /* *************************** 清除目标的顶端的fragment ******************************* */

            /* *************************** 开始处理目标Fragment ******************************* */
            if (isTargetReload && targetFragment != null) {
                ft.remove(targetFragment);
                targetFragment = null;
            }
            if ((object != null)) {
                targetBundle.putSerializable(RoKey.ARGUMENT_OBJECT_KEY, (Serializable) object);
            }
            if (targetFragment == null) {
                targetFragment = (RoFragment) targetClazz.newInstance();
                ft.add(containid, targetFragment, targetTag);//添加
            }
            //设置参数
            targetFragment.setArguments(targetBundle);
            //设置转场动画
            //targetFragment.trAnimation = trAnimation;
            //显示目标Fragment
            ft.show(targetFragment);
            /* *************************** 开始处理目标Fragment ******************************* */

            //提交事务，相当于commitAllowingStateLoss()然后执行executePendingTransactions()方法，更安全
            //这个提交必须在主线程当中，不然会报错
            //ft.commitNowAllowingStateLoss();
            //ft.disallowAddToBackStack();
            //这里建议用commit，如果用commitNowAllowingStateLoss的话，当快速跳转就会报错，因为上次的跳转事物还没有完成
            //这时候立刻执行下个事务就会抛出异常。commit能保证在消息队列中提交事务，可以在线程中提交
            ft.commit();
            //目标Class入堆栈,要放在最后，保证成功commit之后才放入堆栈中
            activity.getStack().pushRocket(targetTag);
            //通知栈变化
            notifyStackChange(activity,fm);
            return true;
        } catch (Exception e) {
            RoLogUtil.e("FragHelper::transfer-->"+e.toString());
            //写日志
            RecordHelper.writeInnerLog(activity,"FragHelper::transfer-->"+"失败跳转:"+e.toString());
        }
        return false;
    }

    /**
     * 移除Fragment
     */
    public void removeFrag(RoActivity activity, String originalTag){
        //重要参数不能为空
        if (activity == null || TextUtils.isEmpty(originalTag))
            throw new RuntimeException("transfer params must not be null");
        //获得管理器
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        try {
            Fragment origianlFragment = fm.findFragmentByTag(originalTag);
            if (origianlFragment != null) {
                ft.remove(origianlFragment);
            }
            //源Class出堆栈，需要放最后，等到remove成功才pop
            activity.getStack().popRocket(originalTag);
            //提交事务，这里建议用commit，如果用commitNowAllowingStateLoss的话，当快速跳转就会报错，因为上次的跳转事物还没有完成
            //这时候立刻执行下个事务就会抛出异常。commit能保证在消息队列中提交事务，可以在线程中提交
            ft.commit();
            //通知栈变化
            notifyStackChange(activity,fm);
        } catch (Exception e) {
            RoLogUtil.e("FragHelper::removeFrag-->"+e.toString());
            //写日志
            RecordHelper.writeInnerLog(activity,"FragHelper::removeFrag-->"+"删除跳转:"+e.toString());
        }
    }

    /**
     * 向上通知有栈变化
     */
    private void notifyStackChange(RoActivity activity,FragmentManager fragmentManager){
        //fragmentManager.getFragments()需要在队列中获取的才是最新的
        new Handler().post(() -> {
            List<String> activityStacks = new ArrayList<>();
            for(Fragment fragment : fragmentManager.getFragments()){
                activityStacks.add(fragment.getClass().getSimpleName());
            }
            activity.getStack().setActivityStackList(activityStacks);
            onFragmentStackChangeNext(activity.getStack().getRocketStackList(),activityStacks);
            RoLogUtil.v("FragHelper::notifyStackChange-->"+"activityStacks:"+activityStacks.toString());
        });
    }

    /* ***************************** FragmentStackChange Fragment栈有变化 ***************************** */

    private OnFragmentStackChangeListener onFragmentStackChangeListener;

    // 接口类 -> OnFragmentStackChangeListener
    public interface OnFragmentStackChangeListener {
        void onFragmentStackChange(List<String> rocketStacks,List<String> activityStacks);
    }

    // 对外暴露接口 -> setOnFragmentStackChangeListener
    public void setOnFragmentStackChangeListener(OnFragmentStackChangeListener onFragmentStackChangeListener) {
        this.onFragmentStackChangeListener = onFragmentStackChangeListener;
    }

    // 内部使用方法 -> FragmentStackChangeNext
    public void onFragmentStackChangeNext(List<String> rocketStacks,List<String> activityStacks) {
        if (onFragmentStackChangeListener != null) {
            onFragmentStackChangeListener.onFragmentStackChange(rocketStacks,activityStacks);
        }
    }
}
