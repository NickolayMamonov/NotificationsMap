package com.example.notificationsmap

import android.graphics.PointF
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.notificationsmap.data.App

import com.example.notificationsmap.data.database.MarkerDatabase
import com.example.notificationsmap.data.entities.Marker
import com.example.notificationsmap.databinding.FragmentMapBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.location.Location
import com.yandex.mapkit.location.LocationListener
import com.yandex.mapkit.location.LocationStatus
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MapFragment : Fragment(), UserLocationObjectListener,MapObjectTapListener, InputListener {
    lateinit var mapView: MapView
    lateinit var locationMapkit: UserLocationLayer
    lateinit var binding: FragmentMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(API_KEY)
        MapKitFactory.initialize(context)
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
//        val markerPosDao = App.getdb(requireContext()).markerPosDao()
//        MapViewModel = ViewModelProvider(
//            this,
//            MapViewModelFactory(markerPosDao)
//        ).get(MapViewModel::class.java)

        mapView = binding.mapview
        var mapkit : MapKit = MapKitFactory.getInstance()
        locationMapkit = mapkit.createUserLocationLayer(mapView.mapWindow)
        locationMapkit.isVisible = true
        locationMapkit.setObjectListener(this)

        mapkit.createLocationManager().requestSingleUpdate(object: LocationListener {
            override fun onLocationUpdated(location: Location) {
                mapView.map.move(
                    CameraPosition(location.position, 15.0f, 0.0f, 0.0f),
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
            PointF((mapView.width() *0.5).toFloat(),(mapView.height() *0.5).toFloat()),
            PointF((mapView.width() *0.5).toFloat(),(mapView.height() *0.83).toFloat())
        )

        userLocationView.pin.setIcon(ImageProvider.fromResource(requireContext(),R.drawable.pin))
    }

    override fun onObjectRemoved(p0: UserLocationView) {

    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {

    }

    override fun onMapTap(map: Map, point: Point) {
        var db = Room.databaseBuilder(
            requireContext(),
            MarkerDatabase::class.java,
            "markers"
        ).build()
//        addMarkerToMap()
        locationMapkit.setAnchor(
            PointF((mapView.width() * 0.5).toFloat(),(mapView.height() * 0.5).toFloat()),
            PointF((mapView.width() * 0.5).toFloat(),(mapView.height() * 0.5).toFloat())
        )


        GlobalScope.launch {
            val marker = Marker.from(point.latitude,point.latitude)
            db.mapDao().insertMarkerPos(marker)
            mapView.map.mapObjects.addPlacemark(Point(marker.lat,marker.lng))
        }
    }

//    private fun addMarkerToMap(point: Point) {
//        val marker = Marker(0,point.latitude,point.longitude)
//        MapViewModel.
//    }

    override fun onMapLongTap(map: Map, point: Point) {

    }

    override fun onMapObjectTap(p0: MapObject, p1: Point): Boolean {
        TODO("Not yet implemented")
    }

}