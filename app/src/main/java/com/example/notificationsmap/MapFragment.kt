package com.example.notificationsmap

import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.notificationsmap.data.entities.MarkerEntity
import com.example.notificationsmap.databinding.FragmentMapBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.launch


class MapFragment : Fragment(), UserLocationObjectListener, InputListener {
    private lateinit var viewModel: MapViewModel
    lateinit var mapView: MapView
    private lateinit var locationMapkit: UserLocationLayer
    private lateinit var searchManager: SearchManager
    private lateinit var binding: FragmentMapBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMapBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[MapViewModel::class.java]
        lifecycleScope.launch {
            val markers = viewModel.getAllMarkers()
            for (marker in markers) {
                mapView.map.mapObjects.addPlacemark(Point(marker.lat, marker.lng))
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.mapview
        val mapkit: MapKit = MapKitFactory.getInstance()
        locationMapkit = mapkit.createUserLocationLayer(mapView.mapWindow)
        locationMapkit.isVisible = true
        locationMapkit.setObjectListener(this)

        mapkit.createLocationManager().requestSingleUpdate(object : LocationListener {
            override fun onLocationUpdated(location: Location) {
                mapView.map.move(
                    CameraPosition(location.position, 25.0f, 0.0f, 0.0f),
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
        mapView.map.addInputListener(this)

    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        locationMapkit.setAnchor(
            PointF((mapView.width() * 0.5).toFloat(), (mapView.height() * 0.5).toFloat()),
            PointF((mapView.width() * 0.5).toFloat(), (mapView.height() * 0.83).toFloat())
        )

            userLocationView.pin.setIcon(ImageProvider.fromResource(requireContext(), R.drawable.pin))
    }

    override fun onObjectRemoved(p0: UserLocationView) {

    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {

    }

    override fun onMapTap(map: Map, point: Point) {
        val marker = MarkerEntity.from(point.latitude, point.longitude)
        viewModel.insertMarkerPos(marker)
        mapView.map.mapObjects.addPlacemark(Point(marker.lat, marker.lng))

    }

    override fun onMapLongTap(p0: Map, p1: Point) {

    }


}