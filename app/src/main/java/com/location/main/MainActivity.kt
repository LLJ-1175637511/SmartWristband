package com.location.main

import android.annotation.SuppressLint
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.llj.baselib.IOTLib
import com.llj.baselib.IOTViewModel
import com.llj.baselib.save
import com.llj.baselib.ui.IOTMainActivity
import com.llj.baselib.utils.ToastUtils
import com.location.main.databinding.ActivityMainBinding
import com.tencent.map.geolocation.TencentLocation
import com.tencent.map.geolocation.TencentLocationListener
import com.tencent.map.geolocation.TencentLocationManager
import com.tencent.map.geolocation.TencentLocationRequest
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory
import com.tencent.tencentmap.mapsdk.maps.MapView
import com.tencent.tencentmap.mapsdk.maps.TencentMap
import com.tencent.tencentmap.mapsdk.maps.model.*

class MainActivity : IOTMainActivity<ActivityMainBinding>() {

    override fun getLayoutId() = R.layout.activity_main

    private val vm by viewModels<MainVM>()

    override fun init() {
        super.init()
        initMainView()
        vm.connect(this, MainDataBean::class.java)
    }

    private fun initMainView() {
        mDataBinding.toolbar.toolbarBaseTitle.text = "主页"
        mDataBinding.clUserInfo.setOnClickListener {
            startCommonActivity<MineActivity>()
        }
        mDataBinding.btShowMap.setOnClickListener {
            startCommonActivity<MapActivity>()
        }
        mDataBinding.btBloodO2Data.isChecked = vm.isTesting
        mDataBinding.btBloodO2Data.setOnClickListener {
            if (mDataBinding.btBloodO2Data.isChecked) {
                vm.startTesting()
            } else {
                vm.stopTesting()
            }
        }
    }

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

    @SuppressLint("SetTextI18n")
    override fun realData(data: Any?) {
        if (!vm.isTesting || data == null) return
        val d = data as MainDataBean
//        val rn = "${(95..99).random() + (1..9).random()/10.0} %"
//        mDataBinding.tvBloodO2.text = rn
        mDataBinding.tvBloodO2.text = "${(d.o2 * 10).toInt() / 10}%"

        val h = "${(d.blood * 10).toInt() / 10} 次/分"
        mDataBinding.tvHeart.text = h

        val state = if (d.tilt == 1) "摔倒" else "正常"
        mDataBinding.tvFallDown.text = state
    }

    override fun webState(state: IOTViewModel.WebSocketType) {
    }


}