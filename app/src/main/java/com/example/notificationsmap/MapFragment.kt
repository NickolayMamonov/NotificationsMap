package com.example.notificationsmap

import android.graphics.PointF
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.notificationsmap.databinding.FragmentMapBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider


class MapFragment : Fragment(), UserLocationObjectListener {
    lateinit var mapView: MapView
    lateinit var locationMapkit: UserLocationLayer
    lateinit var binding: FragmentMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("0a7e76da-0513-4064-b71e-ba004f5d6160")
        MapKitFactory.initialize(this.context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.mapview
        var mapkit : MapKit = MapKitFactory.getInstance()
        locationMapkit = mapkit.createUserLocationLayer(mapView.mapWindow)
        locationMapkit.isVisible = true
        locationMapkit.setObjectListener(this)

        mapkit.createLocationManager().requestSingleUpdate(object: LocationListener {
            override fun onLocationUpdated(location: Location) {
                mapView.map.move(
                    CameraPosition(location.getPosition(), 15.0f, 0.0f, 0.0f),
                    Animation(Animation.Type.SMOOTH, 5F),
                    null
                )
            }

            override fun onLocationStatusUpdated(p0: LocationStatus) {

            }
        })
    }

    override fun onStart() {
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()

    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        locationMapkit.setAnchor(
            PointF((mapView.width() *0.5).toFloat(),(mapView.height() *0.5).toFloat()),
            PointF((mapView.width() *0.5).toFloat(),(mapView.height() *0.83).toFloat())
        )

        userLocationView.pin.setIcon(ImageProvider.fromResource(requireContext(),R.drawable.pin))
    }

    override fun onObjectRemoved(p0: UserLocationView) {

    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {

    }

}