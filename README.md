[![Platform](https://img.shields.io/badge/平台-%20Android%20-brightgreen.svg)](https://github.com/yinhaide/Rocket-master/wiki)
[![Feature](https://img.shields.io/badge/特性-%20轻量级%20%7C%20稳定%20%20%7C%20强大%20-brightgreen.svg)](https://github.com/yinhaide/Rocket-master/wiki)
[![LICENSE](https://img.shields.io/hexpm/l/plug.svg)](https://www.apache.org/licenses/LICENSE-2.0)

# Rocket
采用"单Activity+多Fragment"以及"多模块Activity+多Fragment"的设计模式编写的架构。一个非常轻量级又十分强大的Fragment管理框架。

|转场动画|路由栈视图|动态权限|
|:-----------:|:-----:|:---------:|
|<img src="image/animation.gif" width = "360px"/>|<img src="image/router.gif" width = "360px"/>|<img src="image/permission.gif" width = "360px"/>|

|状态栏|崩溃处理|日志|
|:-----------:|:-----:|:---------:|
|<img src="image/statusbar.gif" width = "360px"/>|<img src="image/crash.gif" width = "360px"/>|<img src="image/record.gif" width = "360px"/>|

## 特性
+ **无任何第三方依赖，纯原生编写，无需担心因为版本迭代导致的维护问题**

+ **采用线性路由栈，自行管理回退以及内存回收，提供悬浮球实时查看Fragment的栈视图，降低开发难度**

+ **页面切换以及页面通讯采用原生的commit以及setArguments，性能开销极小，页面切换流畅无卡顿**

+ **支持自定义Fragment转场动画**

+ **集成动态权限管理，包含必须权限（不允许不走生命周期）以及可选权限**

+ **集成状态栏管理，可适配状态栏各种场景，包括沉浸式风格**

+ **集成日志管理，记录崩溃日志、内部日志以及外部日志，可设置日志有效期，超时自动清理**

+ **提供更加人性化的崩溃捕获页面，精准定位根源bug文件以及行数，降低查bug难度**

+ **提供View注解，仅仅300行代码支持了各种View注入和事件绑定**

+ **提供百分比布局，支持PercentRelativeLayout、PercentLinearLayout和PercentFrameLayout**

## 分享设计Rockt架构的思路
**1、为什么要设计这个架构**
> Activity是一个非常令人讨厌的设计！Activity的创建并不能由开发者自己控制，它是通过多进程远程调用并最终通过放射的方式创建的。在此期间，AMS需要做大量的工作，以至于Activity的启动过程极其缓慢。同时，Activity切换的开销也非常重量级，很容易造成卡顿，用户体验不好。另外，在宽屏设备上，如果需要多屏互动时，Activity的局限性也就表现了出来。为此Android团队在Android 3.0的时候引入了Fragment。Fragment的出现解决了上面两个问题。根据词海的翻译可以译为:碎片、片段，可以很好解决Activity间的切换不流畅。因为是轻量切换，性能更好，更加灵活。

**2、如何设计Fragment的路由栈**
> 一个好用的框架必然有一个好用的路由管理、堆栈管理的机制。Rocket的路由栈设计原则为:一个Activity对应一个线性的路由栈，所有的页面回退、内存回收以及Fragemnt生命周期管理都是内部完成，开发者只要专注跳转目标即可，并提供可视化的栈视图，极大简化开发难度。那么如何保证Fragment路由栈是线性的呢？只要能够做到所有的页面切换只有一个transfer的入口，路由栈的出栈入栈都在这里管理即可。

**3、如何处理页面切换、页面通信以及内存回收**
> Fragment页面跳转建议用commit在队列中提交，不建议用commitNowAllowingStateLoss等在线程中提交事务。一旦有快速切换页面逻辑，线程中提交事务很容易出现因为上个事务没消耗完毕导致崩溃。
> 页面通讯有很多方案，包括handle、广播、接口回调、Eventbus等等，都不是最优方案。Fragment提供的setArguments方法非常的轻量级，可以完美实现页面通讯。Rocket采用的就是原生的setArguments方案。
> 虽然Fragment相对Activity内存开销小了很多，但是如果大量Fragment创建没有及时回收的话会造成Activity内存臃肿。Rocket尽可能优的清理无用的Fragment，及时回收，消除应用卡顿。

**4、如何处理转场动画与的Fragment生命周期**
> 当Fragment中有setCustomAnimations转场动画的时候，做页面切换、页面通讯、内存管理等与生命周期相关的就多了很多的坑。Fragment提供的onCreateAnimation方法，不管有无动画都会走这个方法,并且提供完整的动画生命周期与动画详情。Rocket中充分利用这一点，
> 很好的规避了转场动画导致的各类难题。同时，Rocket提供设置转场动画入口给开发者，让开发者随心所欲定制自己的转场动画。

**5、如何实现沉浸式状态栏与正常状态栏的无缝切换**
> 状态栏有两种形态，显示以及隐藏。隐藏的时候整个页面向上顶满屏幕，带来很严重的突兀感。状态栏依附的是window窗体，在Rocket框架中，因为我们的页面单位是Fragment，也就是说只要一个页面切换状态栏都会导致整个窗体一起变化。
> Rocket提供一个方法，让用户自定义的标题栏可以向上或者向下偏移一个状态栏高度，在页面切换前后动态控制，避免突兀。对于沉浸风格的实现，Rocket在隐藏状态栏的情况下会动态创建一个浮在表面的新状态栏，用户可以控制颜色与透明度，达到沉浸效果。

**6、如何优雅处理动态权限申请与处理**
> 我们知道，Fragment一般依赖于Activity存活，并且生命周期跟Activity差不多，因此，我们进行权限申请的时候，可以利用透明的Fragment进行申请，在里面处理完之后，再进行相应的回调。
> 第一步：当我们申请权限申请的时候，先查找我们当前Activity是否存在代理fragment,不存在，进行添加，并使用代理Fragment进行申请权限
> 第二步：在代理 Fragment 的 onRequestPermissionsResult 方法进行相应的处理，判断是否授权成功
> 第三步：进行相应的回调。这些繁琐的步骤封装在空白的Fragment中，降低耦合，方便维护。

## 如何快速集成

**1、导入aar依赖包**
```
1、新建libs目录
2、将aar文件放在libs目录
3、在应用级别的build.gradle加入
repositories {
    flatDir {
        dirs 'libs'
    }
}
4、在应用级别的build.gradle加入
dependencies {
    api fileTree(include: ['*.jar'], dir: 'libs')
    api(name: library.rocket, ext: 'aar')
}
```

**2、继承RoApplication**
```java
public class SampleApplication extends RoApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
```

**3、创建Frag_rocket并且继承RoFragment**
```java
public class Frag_rocket extends RoFragment {

    @Override
    public int onInflateLayout() {
        return R.layout.frag_rocket;
    }

    @Override
    public void initViewFinish(View inflateView) {
        //页面初始化完毕会回调
    }

    @Override
    public void onNexts(Object object) {
        //initViewFinish之后的业务逻辑回调，包括传递页面通讯信息object
    }
}
```

**4、创建RoxxActivity并且继承RoActivity**
+ RoxxActivity.class
```java
public class RoxxActivity extends RoActivity {

    //注册Rofrgment
    private Class[] roFragments = {
            Frag_rocket.class,
    };

    @Override
    public ActivityParamBean initProperty() {
        //配置全局的信息
        ActivityParamBean actParam = new ActivityParamBean();
        actParam.setLayoutId(R.layout.activity_main);//Activity布局
        actParam.setFragmentContainId(R.id.fl_fragment_contaner);//Fragment容器
        actParam.setSaveInstanceState(true);//页面重载是否要恢复之前的页面
        actParam.setToastCustom(true);//用自定义的吐司风格
        actParam.setRoFragments(roFragments);//需要注册Fragment列表
        actParam.setShowViewBall(true);//是否显示悬浮球
        actParam.setRecordBean(new RecordBean(true,true,true,7));//日志配置
        actParam.setEnableCrashWindow(true);//是否隐藏框架自定义的崩溃的窗口
        actParam.setStatusBar(new StatusBarBean(true, Color.argb(0, 0, 0, 0)));//状态栏
        return actParam;
    }

    @Override
    public void initViewFinish() {
        //页面初始化完毕会回调
    }

    @Override
    public void onNexts(Object object) {
        //initViewFinish之后的业务逻辑回调，包括传递页面通讯信息object
    }

    @Override
    public boolean onBackClick() {
        //管理回退逻辑
        return false;
    }
}
```
+ activity_main.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.de.rocket.ue.layout.PercentRelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/colorAccent"
    tools:context=".ue.activity.MainActivity">

    <FrameLayout
        android:id="@+id/fl_fragment_contaner"
        android:background="@color/colorPrimaryDark"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

</com.de.rocket.ue.layout.PercentRelativeLayout>
```
## 详细使用说明
**1、Activity-ActivityParamBean参数**
```java
public class ActivityParamBean{
    //必需，Activity布局的Layout ID
    private int layoutId;
    //必需，Activity布局的Layout装Fragment的容器id
    private int fragmentContainId;
    //必需，支持的Fragment列表
    private Class[] roFragments;
    //Activity恢复是是否从保存的状态中恢复，false的话整个页面重新创建显示第一个Fragment
    private boolean isSaveInstanceState;
    //是否用传统自定义的吐司风格，false表示用系统的吐司，系统风格在通知关闭情况也是禁用的
    private boolean isToastCustom;
    //是否显示线性栈视图悬浮球
    private boolean showViewBall;
    //是否显示框架自定义的崩溃的窗口
    private boolean enableCrashWindow;
    //日志配置(mnt/sdcard/Android/data/<package name>/files/rocket/<date>/xx.log)
    private RecordBean recordBean;
    //状态栏的属性(沉浸式风格以及颜色配置)
    private StatusBarBean statusBar;
}
```
**2、Fragment-页面切换**
```java
//注意：下面只提供带全部参数的方法，Rocket提供阶梯式参数的函数
public class Rofragment{
    /**
     * 带详细参数跳转到新的Fragment
     * @param targetClass 目标Fragment
     * @param isOriginalRemove 是否要回收内存
     * @param isTargetReload 是否要刷新目标Fragment
     * @param object 跳转携带的参数
     * @param clearTop 清掉目标Fragment在栈位置顶端所有的Fragment
     * @param animationBean 转场动画
     */
    public void toFrag(@NonNull Class targetClass, boolean isOriginalRemove, boolean isTargetReload, Object object, boolean clearTop, AnimationBean animationBean);

    /**
     * 带详细参数返回（默认无需主动调用，系统自行返回，除非需要自定义返回，需要重写onBackPresss）
     * @param isTargetReload 刷新目标页面
     * @param object 传递对象
     * @param animationBean 转场动画
     */
    public void back(boolean isTargetReload, Object object,AnimationBean animationBean);

    /**
     * 从Fragment中显示跳转到Activity
     * @param activityClass 对应的Activity
     * @param targetFragTag 对应的Fragment的SimpleName
     * @param object 携带的数据
     */
    public void toAct(Class activityClass,String targetFragTag,Object object);

    /**
     * 从Fragment中隐式跳转到Activity
     * @param action 对应的Activity的Action
     * @param targetFragTag 对应的Fragment的SimpleName
     * @param object 携带的数据
     */
    public void toAct(String action,String targetFragTag,Object object);
}
```
**3、Fragment-吐司**
```java
public class Rofragment{ 
    
    /**
     * 吐司
     * @param tip 吐司内容
     */
    public void toast(String tip);
}
```

**4、Fragment-权限申请**
```java
public class Rofragment{ 
    
    /**
     * 主动请求权限，关注每个权限的允许与拒绝情况
     * 注意：权限必须要在AndroidManifest.xml注册才能正常使用
     * @param permissions 权限列表
     * @param permissionListener 回调函数
     * @return 是否已经在AndroidManifest中注册
     */
    public boolean needPermissison(String[] permissions, PermissionListener permissionListener);
    
    /**
     * 重写权限申请方法，页面进来必须要先要求权限
     * 必须所有权限都通过才会执行生命周期
     * 如果权限没有全部允许,还想执行生命周期的话，需要在子类调用:super.initPermission(rootView);
     */
    @Override
    public String[] initPermission();
}
```
**5、Fragment-APP内部语言国际化**
```java
public class Rofragment{ 
    
    /**
     * 读取APP需要缓存的语言
     *
     * @return locale 当前设置的语言
     */
    public Locale getSaveLocale();
    
    /**
     * 设置APP需要缓存的语言
     *
     * @param locale 需要设置的语言
     */
    public void setSaveLocale(Locale locale);
}
```

**6、Fragment-状态栏切换**
```java
public class Rofragment{ 
    
    /**
     * 设置状态栏颜色
     *
     * @param color         状态栏颜色值
     * @param isImmersion   是否是沉浸式风格，隐藏状态栏创建自定义状态栏
     */
    public void setStatusBarColor(@ColorInt int color,boolean isImmersion);
    
    /**
     * 使指定的View向下Padding偏移一个状态栏高度，留出状态栏空间，主要用于设置沉浸式后,页面顶到顶端有突兀感
     *
     * @param targetView        需要偏移的View
     * @param enable            开启或者关闭
     * @param isPaddingOrMargin 向下偏移是padding还是margin，true的话是padding，false的话是margin
     */
    public void setOffsetStatusBar(View targetView, boolean enable, boolean isPaddingOrMargin);
}
```
**7、Fragment-子类可以重写的方法**
```java
public class Rofragment{ 
    
    /**
     * 是否由内部处理返回事件,默认false,由用户处理
     *
     * @return backAuto
     */
    protected boolean onBackPresss();
    
    /**
     * 从后台进入前台是否要刷新数据,默认false
     *
     * @return isReloadData
     */
    protected boolean isReloadData();
    
    /**
     * Fragment转场动画结束的回调
     *
     * @param  animation 转场动画
     */
    public void onFragAnimationEnd(Animation animation);
}
```
**8、Fragment-View注解**
```java
public class xxfragment{ 
    
    /**
     * 变量注解
     **/
    @BindView(R.id.tv_right)
    private TextView tvRight;
    
    /**
     * 事件注解
     * 1. 方法必须私有限定,
     * 2. 方法参数形式必须和type对应的Listener接口一致.
     * 3. 注解参数value支持数组: value={id1, id2, id3}
     **/
    @Event(value = R.id.tv_right, type = View.OnClickListener.class/*可选参数, 默认是View.OnClickListener.class*/)
    private void toRoxx(View view);
    
    /**
     * 绑定ViewHolder(在Adapter中的事例)
     **/
    private void bindViewHolder(){
        
        /**
         * 绑定ViewHolder,之后可以注解的方式获取绑定viewy以及事件注解.用法如下：
         * ViewHolder(View itemView) {
         *   super(itemView);
         *   Rocket.bindViewHolder(this,itemView);//View注解
         * }
         * @param viewHolder viewHolder
         * @param view itemView
         */
        Rocket.bindViewHolder(viewHolder,view);
    }
}
```

**9、Layout-百分比布局**
+ 支持的百分比布局
> PercentRelativeLayout、PercentLinearLayout和PercentFrameLayout
+ 支持的百分比参数
```xml
<declare-styleable name="PercentLayout_Layout">
    <attr format="string" name="layout_widthPercent"/>
    <attr format="string" name="layout_heightPercent"/>
    <attr format="string" name="layout_marginPercent"/>
    <attr format="string" name="layout_marginLeftPercent"/>
    <attr format="string" name="layout_marginTopPercent"/>
    <attr format="string" name="layout_marginRightPercent"/>
    <attr format="string" name="layout_marginBottomPercent"/>
    <attr format="string" name="layout_marginStartPercent"/>
    <attr format="string" name="layout_marginEndPercent"/>
    <attr format="string" name="layout_textSizePercent"/>
    <attr format="string" name="layout_maxWidthPercent"/>
    <attr format="string" name="layout_maxHeightPercent"/>
    <attr format="string" name="layout_minWidthPercent"/>
    <attr format="string" name="layout_minHeightPercent"/>
    <attr format="string" name="layout_paddingPercent"/>
    <attr format="string" name="layout_paddingTopPercent"/>
    <attr format="string" name="layout_paddingBottomPercent"/>
    <attr format="string" name="layout_paddingLeftPercent"/>
    <attr format="string" name="layout_paddingRightPercent"/>
</declare-styleable>
```
**10、Rocket-工具类**
> 说明:本类作用是暴露内部接口供外部使用，大量的内部方法将放在这里
```java
public class Rocket{
    
    /**
     * 外部写入Log信息
     *
     * @param logString Log信息
     */
    public static writeOuterLog(String logString);
}
```
## LICENSE
````
Copyright 2019 haide.yin(123302687@qq.com)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
````