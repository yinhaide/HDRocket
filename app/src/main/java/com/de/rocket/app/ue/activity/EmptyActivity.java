package com.de.rocket.app.ue.activity;

import android.app.Activity;
import android.os.Bundle;
import com.de.rocket.app.R;
import com.de.rocket.app.tools.MemoryUtil;

public class EmptyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        //打印对比activity跳转之前的时间以及内存情况
        MemoryUtil.printMemoryMsg("activity_end");
    }
}
