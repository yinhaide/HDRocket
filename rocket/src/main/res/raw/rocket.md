## rocket

### 集成说明

#### 1、导入aar依赖包

```
//1、将aar文件放在libs目录
//2、配置libs路径
repositories {
    flatDir {
        dirs 'libs'
    }
}
//3、加载aar
dependencies {
    api fileTree(include: ['*.jar'], dir: 'libs')
    api(name: library.rocket, ext: 'aar')
}
```
#### 2、继承RoApplication

```
public class SampleApplication extends RoApplication {

}
```


#### 3、继承RoActivity

```
public class MainActivity extends RoActivity {

    private Class[] roFragments = {
            Frag_rocket.class,
            Frag_rohttp_image.class,
            Frag_rodao.class,
            Frag_rohttp.class,
    };

    @Override
    public ActivityParamBean initProperty() {
        ActivityParamBean activityParamBean = new ActivityParamBean();
        activityParamBean.setLayoutId(R.layout.activity_main);//Activity布局
        activityParamBean.setFragmentContainId(R.id.fl_fragment_contaner);//Fragment容器
        activityParamBean.setSaveInstanceState(false);//页面不要重新创建
        activityParamBean.setToastCustom(true);//用自定义的吐司风格
        activityParamBean.setRoFragments(roFragments);//需要注册Fragment列表
        activityParamBean.setFragAnimType(FragAnimHelper.TYPE.NONE);//默认没有动画
        activityParamBean.setShowViewBall(true);//是否显示悬浮球
        activityParamBean.setStatusBar(new StatusBarBean(true,false,Color.parseColor("#77000000")));//状态栏
        return activityParamBean;
    }

    @Override
    public void initViewFinish() {

    }

    @Override
    public void onNexts() {

    }

    @Override
    public boolean onBackClick() {
        return false;
    }

}

//布局文件
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

/* ************************************************************* */
/*                     供子类重写与继承的方法
/* ************************************************************* */

/**
 * Activity全局配置信息
 *
 * @return ActivityParamBean
 */
public abstract ActivityParamBean initProperty();

/**
 * 页面初始化完成
 */
public abstract void initViewFinish();

/**
 * 业务逻辑初始化完成
 */
public abstract void onNexts();

/**
 * 返回事件是否由Activity内部消费
 *
 * @return true表示内部消费
 */
public abstract boolean onBackClick();
```

#### 4、继承RoFragment
```
public class Frag_rocket extends RoFragment {

    @Override
    public int onInflateLayout() {
        return R.layout.frag_rocket;
    }

    @Override
    public void initViewFinish(View inflateView) {

    }

    @Override
    public void onNexts(Object object) {

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
protected boolean onBackPresss(){
    return false;
}

/**
 * 从后台进入前台是否要刷新数据,默认false
 *
 * @return isReloadData
 */
protected boolean isReloadData(){
    return false;
}

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
 * 开始处理业务逻辑
 *
 * @param object 跳转传递的对象，需要实现Serializable序列化
 * @param view 跳转传递的对象，需要实现Serializable序列化
 * @param s getClass().getName()
 */
public abstract void onNexts(Object object, View view, String s);
```

### 内部方法说明

#### 1、View注解(Activity与Fragment)

```
//绑定
@BindView(R.id.tv_result)
private TextView tvResult;

//事件
@Event(R.id.bt_login)
private void login(View view){}

@Event(value = R.id.lv_img, type = AdapterView.OnItemClickListener.class)
private void onImageItemClick(AdapterView<?> parent, View view, int position, long id) {}

```

#### 2、View注解(Adapter)

```
@Override
public View getView(final int position, View view, ViewGroup parent) {
    ImageItemHolder holder = null;
    if (view == null) {
        view = mInflater.inflate(R.layout.item_image, parent, false);
        holder = new ImageItemHolder();
        Rocket.bindViewHolder(holder, view);
        view.setTag(holder);
    } else {
        holder = (ImageItemHolder) view.getTag();
    }
    ...
    return view;
}

private class ImageItemHolder {
    @BindView(R.id.img_item)
    private ImageView imgItem;
    @BindView(R.id.img_pb)
    private ProgressBar imgPb;
}
```

#### 3、权限申请(Fragment中)

```
//请求权限,只关注是否全部同意
askPermissison(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, (requestCode, allAccept) -> {

});
//请求权限,关心每个权限的允许拒绝情况
askEachPermissison(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, (requestCode, allAccept, permission) -> {
    
});
```

#### 4、吐司(Fragment中)

```
toast("content or id");
```

#### 5、APP内部语言国际化

```
//设置APP需要缓存的语言
setSaveLocale(Locale locale);
//读取APP需要缓存的语言
getSaveLocale();
```