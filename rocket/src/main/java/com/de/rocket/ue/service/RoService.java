package com.de.rocket.ue.service;

import android.app.Service;
import android.content.Context;

import com.de.rocket.helper.LocaleHelper;

public abstract class RoService extends Service {

    @Override
    protected void attachBaseContext(Context base) {
        //多国语言适配
        super.attachBaseContext(LocaleHelper.attachBaseContext(base));
    }
}
