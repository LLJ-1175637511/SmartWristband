package com.location.main

import com.llj.baselib.ui.IOTBaseActivity
import com.location.main.databinding.ActivityUserBinding

class MineActivity:IOTBaseActivity<ActivityUserBinding>() {

    override fun getLayoutId() = R.layout.activity_user

    override fun init() {
        super.init()
        mDataBinding.toolbar.toolbarBaseTitle.text = "个人信息"
        mDataBinding.tvQuit.setOnClickListener {
            startActivityAndFinish<LoginActivity>()
        }
    }
}