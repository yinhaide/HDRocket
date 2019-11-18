package com.de.rocket.listener;

import com.de.rocket.bean.PermissionBean;

import java.util.List;

/**
 * 多个权限同时请求，关注单个权限允许情况，包括拒绝、同意、不再允许三种情况，分别回调
 */
public interface PermissionListener {
    /**
     * 回调
     *
     * @param requestCode 请求码
     * @param allAccept   是否全部允许
     * @param permissionBeans  用户点击详情列表
     */
    void onResult(int requestCode, boolean allAccept, List<PermissionBean> permissionBeans);
}
