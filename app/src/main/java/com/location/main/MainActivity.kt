package com.location.main

import android.annotation.SuppressLint
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
    private var mRequestParams = ""

    override fun init() {
        super.init()
//        vm.connect(this, MainDataBean::class.java)

        startLocation()
        initMainView()
    }

    private fun initMainView() {
        initMapView()
    }

    private fun initMapView() {
        mMapView = mDataBinding.mapView
        mTencentMap = mMapView.map
        val sp = IOTLib.getSP(SP_LOC)
        if (sp.contains(SP_LOC_L)){
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
        mLocationManager.removeUpdates(this)
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
        stopLocation()
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
        ToastUtils.toastShort("data:${(data as MainDataBean).toString()}")
    }

    override fun webState(state: IOTViewModel.WebSocketType) {
    }

    private fun showDialog(df: DialogFragment, tag: String) {
        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        val prev: Fragment? = supportFragmentManager.findFragmentByTag(tag)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        df.show(ft, tag)
    }

    override fun onLocationChanged(
        location: TencentLocation, error: Int,
        reason: String?
    ) {
        if (error == TencentLocation.ERROR_OK) {
            mLocation = location

            // 定位成功
            val sb = StringBuilder()
            sb.append("定位参数=").append(mRequestParams).append("\n")
            sb.append("(纬度=").append(location.latitude).append(",经度=")
                .append(location.longitude).append(",精度=")
                .append(location.accuracy).append("), 来源=")
                .append(location.provider).append(", 地址=")
                .append(location.address)
            val latLngLocation = LatLng(location.latitude, location.longitude)

            // 更新 status
            mDataBinding.tvDevState.setText(sb.toString())

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

    private fun startLocation() {
        mLocationManager = TencentLocationManager.getInstance(this)
        // 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
        mLocationManager.coordinateType = TencentLocationManager.COORDINATE_TYPE_GCJ02
        val request = TencentLocationRequest.create()
        request.interval = 1500
        mLocationManager.requestLocationUpdates(request, this)
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