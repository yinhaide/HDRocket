package com.de.rocket.helper;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.de.rocket.ue.injector.BindView;
import com.de.rocket.ue.injector.Event;
import com.de.rocket.ue.injector.ViewFinder;
import com.de.rocket.ue.injector.ViewInfo;
import com.de.rocket.utils.DoubleKeyMapUtil;
import com.de.rocket.utils.RoLogUtil;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * view反射的帮助类
 */
public final class ViewInjectHelper {

    //对象锁
    private static final Object lock = new Object();
    //单例
    private static volatile ViewInjectHelper instance;
    //反射忽略的类
    private static final HashSet<Class<?>> IGNORED = new HashSet<>();
    //双Key的Map k1: viewInjectInfo k2: interface Type value: listener
    private final static DoubleKeyMapUtil<ViewInfo, Class<?>, Object> listenerCache = new DoubleKeyMapUtil<>();
    //退出事件事件间隔
    private final static long QUICK_EVENT_TIME_SPAN = 300;
    //事件类型
    private final static HashSet<String> AVOID_QUICK_EVENT_SET = new HashSet<>(2);

    //反射忽略的类集合
    static {
        IGNORED.add(Object.class);
        IGNORED.add(Activity.class);
        IGNORED.add(android.app.Fragment.class);
        IGNORED.add(android.support.v4.app.Fragment.class);
        IGNORED.add(android.support.v4.app.FragmentActivity.class);
        /*try {
            IGNORED.add(Class.forName("android.support.v4.app.Fragment"));
            IGNORED.add(Class.forName("android.support.v4.app.FragmentActivity"));
        } catch (Throwable ignored) {
        }*/
    }
    //事件类型集合
    static {
        AVOID_QUICK_EVENT_SET.add("onClick");
        AVOID_QUICK_EVENT_SET.add("onItemClick");
    }

    /**
     * 单例
     */
    public static ViewInjectHelper getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ViewInjectHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 反射
     */
    @SuppressWarnings("ConstantConditions")
    private static void injectObject(Object handler, Class<?> handlerType, ViewFinder finder) {
        if (handlerType == null || IGNORED.contains(handlerType)) {
            return;
        }
        // 从父类到子类递归
        injectObject(handler, handlerType.getSuperclass(), finder);
        //反射View
        Field[] fields = handlerType.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                Class<?> fieldType = field.getType();
                //不注入静态字段
                boolean isStatic = Modifier.isStatic(field.getModifiers());
                //不注入final字段
                boolean isFinal = Modifier.isFinal(field.getModifiers());
                //不注入基本类型字段
                boolean isPrimitive = fieldType.isPrimitive();
                //不注入数组类型字段
                boolean isArray = fieldType.isArray();
                if ( isStatic || isFinal || isPrimitive || isArray) {
                    continue;
                }
                BindView bindView = field.getAnnotation(BindView.class);
                if (bindView != null) {
                    try {
                        View view = finder.findViewById(bindView.value(), bindView.parentId());
                        if (view != null) {
                            field.setAccessible(true);
                            //初始化以及绑定变量
                            field.set(handler, view);
                        } else {
                            throw new RuntimeException("Invalid @BindView for " + handlerType.getSimpleName() + "." + field.getName());
                        }
                    } catch (Throwable ex) {
                        RoLogUtil.e("ViewInjectHelper::injectObject-->"+ex.toString());
                    }
                }
            }
        }
        //反射Event
        Method[] methods = handlerType.getDeclaredMethods();
        if (methods != null && methods.length > 0) {
            for (Method method : methods) {
                //不注入静态方法以及私有方法
                if (Modifier.isStatic(method.getModifiers()) || !Modifier.isPrivate(method.getModifiers())) {
                    continue;
                }
                //检查当前方法是否是event注解的方法
                Event event = method.getAnnotation(Event.class);
                if (event != null) {
                    try {
                        // id参数
                        int[] values = event.value();
                        int[] parentIds = event.parentId();
                        int parentIdsLen = parentIds == null ? 0 : parentIds.length;
                        //循环所有id，生成ViewInfo并添加代理反射
                        for (int i = 0; i < values.length; i++) {
                            int value = values[i];
                            if (value > 0) {
                                ViewInfo info = new ViewInfo();
                                info.value = value;
                                info.parentId = parentIdsLen > i ? parentIds[i] : 0;
                                method.setAccessible(true);
                                addEventMethod(finder, info, event, handler, method);
                            }
                        }
                    } catch (Throwable ex) {
                        RoLogUtil.e("ViewInjectHelper::injectObject-->"+ex.toString());
                    }
                }
            }
        }
    }

    /**
     * 添加事件方法
     * @param finder 根据页面或view holder生成的ViewFinder
     * @param info 根据当前注解ID生成的ViewInfo
     * @param event 注解对象
     * @param handler 页面或view holder对象
     * @param method 当前注解方法
     */
    private static void addEventMethod(ViewFinder finder,ViewInfo info,Event event,Object handler,Method method) {
        try {
            View view = finder.findViewByInfo(info);
            if (view != null) {
                // 注解中定义的接口，比如Event注解默认的接口为View.OnClickListener
                Class<?> listenerType = event.type();
                // 默认为空，注解接口对应的Set方法，比如setOnClickListener方法
                String listenerSetter = event.setter();
                if (TextUtils.isEmpty(listenerSetter)) {
                    listenerSetter = "set" + listenerType.getSimpleName();
                }
                String methodName = event.method();
                boolean addNewMethod = false;
                //根据View的ID和当前的接口类型获取已经缓存的接口实例对象，比如根据View.id和View.OnClickListener.class两个键获取这个View的OnClickListener对象
                Object listener = listenerCache.get(info, listenerType);
                DynamicHandler dynamicHandler;
                //如果接口实例对象不为空，获取接口对象对应的动态代理对象。如果动态代理对象的handler和当前handler相同，则为动态代理对象添加代理方法
                if (listener != null) {
                    dynamicHandler = (DynamicHandler) Proxy.getInvocationHandler(listener);
                    addNewMethod = handler.equals(dynamicHandler.getHandler());
                    if (addNewMethod) {
                        dynamicHandler.addMethod(methodName, method);
                    }
                }
                // 如果还没有注册此代理
                if (!addNewMethod) {
                    dynamicHandler = new DynamicHandler(handler);
                    dynamicHandler.addMethod(methodName, method);
                    // 生成的代理对象实例，比如View.OnClickListener的实例对象
                    listener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class<?>[]{listenerType}, dynamicHandler);
                    listenerCache.put(info, listenerType, listener);
                }
                Method setEventListenerMethod = view.getClass().getMethod(listenerSetter, listenerType);
                setEventListenerMethod.invoke(view, listener);
            }
        } catch (Throwable ex) {
            RoLogUtil.e("ViewInjectHelper::addEventMethod-->"+ex.toString());
        }
    }

    /* ***************************************** 内部类 ******************************************** */

    /**
     * 反射类
     */
    private static class DynamicHandler implements InvocationHandler {

        //上次点击的时间
        private static long lastClickTime = 0;
        // 存放代理方法
        private final HashMap<String, Method> methodMap = new HashMap<>(1);
        // 存放代理对象，比如Fragment或view holder
        private WeakReference<Object> handlerRef;

        DynamicHandler(Object handler) {
            this.handlerRef = new WeakReference<>(handler);
        }

        /**
         * 新增方法
         */
        void addMethod(String name, Method method) {
            methodMap.put(name, method);
        }

        /**
         * 读取代理
         */
        Object getHandler() {
            return handlerRef.get();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            Object handler = handlerRef.get();
            if (handler != null) {
                String eventMethod = method.getName();
                if ("toString".equals(eventMethod)) {
                    return DynamicHandler.class.getSimpleName();
                }
                method = methodMap.get(eventMethod);
                if (method == null && methodMap.size() == 1) {
                    for (Map.Entry<String, Method> entry : methodMap.entrySet()) {
                        if (TextUtils.isEmpty(entry.getKey())) {
                            method = entry.getValue();
                        }
                        break;
                    }
                }
                if (method != null) {
                    if (AVOID_QUICK_EVENT_SET.contains(eventMethod)) {
                        long timeSpan = System.currentTimeMillis() - lastClickTime;
                        if (timeSpan < QUICK_EVENT_TIME_SPAN) {
                            return null;
                        }
                        lastClickTime = System.currentTimeMillis();
                    }
                    try {
                        return method.invoke(handler, args);
                    } catch (Throwable ex) {
                        throw new RuntimeException("invoke method error:" + handler.getClass().getName() + "#" + method.getName(), ex);
                    }
                } else {
                    //打印
                    RoLogUtil.w("ViewInjectHelper::addEventMethod-->"+"method not impl:"+eventMethod+"(" +handler.getClass().getSimpleName()+")");
                    //写日志
                    RecordHelper.writeInnerLog(ActivityHelper.getTopActivity(),"ViewInjectHelper::addEventMethod-->"+"method not impl:"+eventMethod+"(" +handler.getClass().getSimpleName()+")");
                }
            }
            return null;
        }
    }

    /* ***************************************** 对外提供注解的方法 ******************************************** */

    /**
     * 注入view
     *
     * @param view view
     */
    public void injectView(View view) {
        injectObject(view, view.getClass(), new ViewFinder(view));
    }

    /**
     * 注入activity
     *
     * @param activity activity
     */
    public void injectActivity(Activity activity) {
        //获取Activity的ContentView的注解
        Class<?> handlerType = activity.getClass();
        injectObject(activity, handlerType, new ViewFinder(activity));
    }

    /**
     * 注入view holder/fragment
     *
     * @param handler view holder
     * @param view    view
     */
    public void injectFragment(Object handler, View view) {
        injectObject(handler, handler.getClass(), new ViewFinder(view));
    }

    /**
     * 注入view holder/fragment
     *
     * @param handler view holder
     * @param view    view
     */
    public void injectViewHolder(Object handler, View view) {
        injectObject(handler, handler.getClass(), new ViewFinder(view));
    }
}
