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

class MainActivity : IOTMainActivity<ActivityMainBinding>(), TencentLocationListener {

    override fun getLayoutId() = R.layout.activity_main

    private val vm by viewModels<MainVM>()

    private lateinit var mLocationManager: TencentLocationManager
    private lateinit var mMapView: MapView
    private lateinit var mTencentMap: TencentMap
    private lateinit var mLocation: TencentLocation
    private var mLocationMarker: Marker? = null

    override fun init() {
        super.init()
//        vm.connect(this, MainDataBean::class.java)
        initMainView()
    }

    private fun initMainView() {
        mMapView = mDataBinding.mapView
        mTencentMap = mMapView.map
        mDataBinding.btShowMap.setOnClickListener {
            showMap()
            initLocation()
        }
        mDataBinding.ivHideMap.setOnClickListener {
            hideMap()
            stopLocation()
        }

    }

    private fun showMap() {
        mDataBinding.mapView.visibility = View.VISIBLE
        mDataBinding.btShowMap.visibility = View.INVISIBLE
        mDataBinding.ivHideMap.visibility = View.VISIBLE
        mDataBinding.tvLocationString.visibility = View.VISIBLE
    }

    private fun hideMap() {
        mDataBinding.mapView.visibility = View.INVISIBLE
        mDataBinding.btShowMap.visibility = View.VISIBLE
        mDataBinding.ivHideMap.visibility = View.INVISIBLE
        mDataBinding.tvLocationString.visibility = View.INVISIBLE
    }

    private fun initLocation() {
        mLocationManager = TencentLocationManager.getInstance(this)
        // 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
        mLocationManager.coordinateType = TencentLocationManager.COORDINATE_TYPE_GCJ02
        val request = TencentLocationRequest.create()
        request.interval = 1500
        mLocationManager.requestLocationUpdates(request, this)
        val sp = IOTLib.getSP(SP_LOC)
        if (sp.contains(SP_LOC_L) && mLocationMarker == null){
            val l = sp.getString(SP_LOC_L,"")?.toDouble() ?: 41.144295
            val r = sp.getString(SP_LOC_R,"")?.toDouble() ?: 121.122749
            mLocationMarker = mTencentMap.addMarker(
                MarkerOptions(LatLng(l,r))
            )
        }
        val cameraUpdate = CameraUpdateFactory.zoomTo(18f)
        mTencentMap.moveCamera(cameraUpdate)
    }

    override fun onResume() {
        mMapView.onResume()
        super.onResume()
    }

    override fun onPause() {
        mMapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        stopLocation()
        mMapView.onDestroy()
        super.onDestroy()
    }

    override fun onStart() {
        mMapView.onStart()
        super.onStart()
    }

    override fun onStop() {
        mMapView.onStop()
        super.onStop()
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

    override fun realData(data: Any?) {
        val d = data as MainDataBean ?: return
        val rn = "${(95..99).random() + (1..9).random()/10.0} %"
        mDataBinding.tvBloodO2.text = rn
        val h = "${d.data} 次/分"
        mDataBinding.tvHeart.text = h
    }

    override fun webState(state: IOTViewModel.WebSocketType) {
    }

    override fun onLocationChanged(
        location: TencentLocation, error: Int,
        reason: String?
    ) {
        if (error == TencentLocation.ERROR_OK) {
            mLocation = location

            val latLngLocation = LatLng(location.latitude, location.longitude)
            mDataBinding.tvLocationString.text = location.address

            // 更新 location Marker
            if (mLocationMarker == null) {
                mLocationMarker = mTencentMap.addMarker(
                    MarkerOptions(latLngLocation)
                )
            } else {
                mLocationMarker?.position = latLngLocation
                val cameraUpdate = CameraUpdateFactory.newLatLng(latLngLocation)
                mTencentMap.moveCamera(cameraUpdate)
                IOTLib.getSP(SP_LOC).save {
                    putString(SP_LOC_L,location.latitude.toString())
                    putString(SP_LOC_R,location.longitude.toString())
                }
            }
        }
    }

    override fun onStatusUpdate(name: String?, status: Int, desc: String?) {

    }


    private fun stopLocation() {
        mLocationManager.removeUpdates(this)
    }

    companion object{
        const val SP_LOC = "SP_LOC"
        const val SP_LOC_L = "SP_LOC_L"
        const val SP_LOC_R = "SP_LOC_R"
    }

}