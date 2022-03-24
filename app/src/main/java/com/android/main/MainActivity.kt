package com.android.main

import android.annotation.SuppressLint
import androidx.activity.viewModels
import com.android.main.databinding.ActivityMainBinding
import com.llj.baselib.IOTViewModel
import com.llj.baselib.ui.IOTMainActivity
import com.llj.baselib.utils.ToastUtils

class MainActivity : IOTMainActivity<ActivityMainBinding>() {

    override fun getLayoutId() = R.layout.activity_main

    private val vm by viewModels<MainVM>()

    override fun init() {
        super.init()
        vm.connect(this, MainDataBean::class.java)
        initMainView()
    }

    private fun initMainView() {
//        initToolbar()
    }

//    private fun initToolbar() {
//        mDataBinding.toolbar.apply {
//            toolbarBaseTitle.text = getString(R.string.app_name)
//            toolbarBase.inflateMenu(R.menu.toolbar_menu)
//            toolbarBase.setOnMenuItemClickListener { item ->
//                when (item.itemId) {
//                    R.id.quit_app -> {
//                        startCommonActivity<LoginActivity>()
//                        finish()
//                    }
//                }
//                false
//            }
//        }
//    }

    @SuppressLint("ResourceAsColor")
    override fun offDevLine() {
        mDataBinding.tvDevState.setTextColor(R.color.red)
        mDataBinding.tvDevState.text = "设备离线"
    }

    @SuppressLint("ResourceAsColor")
    override fun onDevLine() {
        mDataBinding.tvDevState.setTextColor(R.color.greenDark)
        mDataBinding.tvDevState.text = "设备在线"
    }

    override fun realData(data: Any?) {
        ToastUtils.toastShort("data:${(data as MainDataBean).toString()}")
    }

    override fun webState(state: IOTViewModel.WebSocketType) {
        mDataBinding.tvState.text = state.name
    }

}