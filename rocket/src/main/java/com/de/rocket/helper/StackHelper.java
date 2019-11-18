package com.de.rocket.helper;

import android.text.TextUtils;

import com.de.rocket.utils.RoLogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment堆栈管理的工具类
 */
public class StackHelper {

    private List<String> rocketStackList = new ArrayList<>();
    private List<String> activityStackList = new ArrayList<>();

    /**
     * 入栈
     */
    public void pushRocket(String stackKey) {
        //先出栈
        popRocket(stackKey);
        //加到栈顶端
        rocketStackList.add(stackKey);
        RoLogUtil.v("StackHelper::pushRocket-->"+"stackKey:"+ stackKey);
    }

    /**
     * 出栈
     */
    public boolean popRocket(String stackKey) {
        if (rocketStackList.contains(stackKey)) {
            rocketStackList.remove(stackKey);
            RoLogUtil.v("StackHelper::popRocket-->"+"stackKey:"+ stackKey);
            return true;
        }
        return false;
    }

    /**
     * 得到需要清理的栈顶元素，如果不存在则清理除去源意外的所有
     */
    public List<String> getClearTopRocketStackList(String originStack, String targetStack) {
        List<String> topStackList = new ArrayList<>();
        if(rocketStackList.contains(targetStack)){
            int index = rocketStackList.indexOf(targetStack);
            if(index < rocketStackList.size() - 1){
                for(int i = index + 1;i < rocketStackList.size();i++){
                    topStackList.add(rocketStackList.get(i));
                }
            }
        }else{//不存在就清理除去源意外的所有
            for(String clearStack : rocketStackList){
                if(!clearStack.equals(originStack)){
                    topStackList.add(clearStack);
                }
            }
        }
        return topStackList;
    }

    /**
     * 拿栈顶
     */
    public String peekRocket() {
        if (rocketStackList.size() > 0) {
            RoLogUtil.v("StackHelper::peekRocket-->"+"stackKey:"+ rocketStackList.get(rocketStackList.size() - 1));
            return rocketStackList.get(rocketStackList.size() - 1);
        }
        return "";
    }

    /**
     * 当前的fragmentTag是否在栈顶
     */
    public boolean isTopRocketStack(String fragmentTag){
        if(!TextUtils.isEmpty(fragmentTag)){
            return peekRocket().equals(fragmentTag);
        }
        return false;
    }

    /**
     * 读取rocket堆栈列表
     */
    public List<String> getRocketStackList() {
        return rocketStackList;
    }

    /**
     * 读取activity堆栈列表
     */
    public List<String> getActivityStackList() {
        return activityStackList;
    }

    /**
     * 设置activity堆栈列表
     */
    public void setActivityStackList(List<String> activityStackList) {
        this.activityStackList = activityStackList;
    }
}
