package com.de.rocket.cons;

/**
 * 定义全局的Key
 * Created by haide.yin(haide.yin@tcl.com) on 2019/8/30 8:59.
 */
public class RoKey {

    //Fragment之间传递：对象的key
    public static final String ARGUMENT_OBJECT_KEY = "argument_object_key";
    //Fragment之间传递：是否移除源Fragment的key
    public static final String ARGUMENT_ORIGIN_REMOVE_KEY = "argument_original_remove_key";
    //Fragment之间传递：移除源Fragment的key
    public static final String ARGUMENT_ORIGIN_CLASS_KEY = "argument_original_class_key";
    //Activity之间传递：目标Fragment的key
    public static final String ARGUMENT_TARGET_CLASS_KEY = "argument_target_class_key";

    //Log的tag标记的key
    public static final String TAG_ROCKET = "tag_rocket";
    public static final String TAG_HAIDE = "tag_haide";

    //onSaveInstanceState用户缓存栈顶的Fragment的key
    public static final String SAVE_TOP_FRAG = "save_top_frag";
}
