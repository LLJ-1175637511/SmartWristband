package com.location.main

import android.Manifest
import com.llj.baselib.ui.IOTLoginActivity
import com.location.main.databinding.ActivityLoginBinding


class LoginActivity : IOTLoginActivity<ActivityLoginBinding>() {

    override fun getLayoutId() = R.layout.activity_login

    override fun initPermission() = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_PHONE_STATE
    )

    override fun init() {
        super.init()
        mDataBinding.apply {
            etUserNameLogin.setText(getUserInfo().first)
            etUserPwdLogin.setText(getUserInfo().second)
            btLogin.setOnClickListener {
                login(etUserNameLogin.text.toString(),etUserPwdLogin.text.toString(), MainActivity::class.java)
            }
        }
    }

}