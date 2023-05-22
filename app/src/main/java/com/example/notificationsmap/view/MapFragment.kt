package com.example.notificationsmap.view

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.PointF
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.notificationsmap.MapViewModel
import com.example.notificationsmap.R
import com.example.notificationsmap.SharedViewModel
import com.example.notificationsmap.TaskBroadcastReceiver
import com.example.notificationsmap.databinding.FragmentMapBinding
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.layers.ObjectEvent

import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.*
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import kotlinx.coroutines.launch
import java.lang.Math.sqrt

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


// Session.SearchListener
class MapFragment : Fragment(), InputListener,
    CameraListener,Session.SearchListener {
    private lateinit var viewModel: MapViewModel

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var mapView: MapView
    private lateinit var locationMapkit: UserLocationLayer
    private lateinit var locationManager: LocationManager
    private lateinit var binding: FragmentMapBinding
    private lateinit var searchEdit: EditText
    private lateinit var searchManager: SearchManager
    private lateinit var searchSession: Session

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMapBinding.inflate(inflater)
        viewModel = ViewModelProvider(this)[MapViewModel::class.java]


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.mapview
        val mapkit: MapKit = MapKitFactory.getInstance()
        locationMapkit = mapkit.createUserLocationLayer(mapView.mapWindow)
//        mapView.map.move(CameraPosition(Point(0.0, 0.0), 14f, 0f, 0f))
        locationMapkit.isVisible = true
//        locationMapkit = mapkit.createUserLocationLayer(mapView.mapWindow)

        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        SearchFactory.initialize(context)
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()
        val taskBroadcastReceiver = TaskBroadcastReceiver()
        val intentFilter = IntentFilter("GEOFENCE_TRIGGERED")
        requireContext().registerReceiver(taskBroadcastReceiver,intentFilter)
        mapView.map.addInputListener(this)
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ){
            val permissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            ActivityCompat.requestPermissions(this.requireActivity(), permissions, 0)
        }
        val executor = Executors.newSingleThreadScheduledExecutor()
        val locationRunnable = Runnable {
            lifecycleScope.launch{
                val markers = viewModel.getAllMarkers()
                val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if(lastKnownLocation != null){
                    val point = Point(lastKnownLocation.latitude,lastKnownLocation.longitude)
                    mapView.map.mapObjects.clear()
                    mapView.map.mapObjects.addPlacemark(point, ImageProvider.fromResource(context,R.drawable.search_result))

                    val circle = mapView.map.mapObjects.addCircle(
                        Circle(point, 100.0f),
                        Color.GREEN,
                        2.0f,
                        Color.RED
                    )
                for (marker in markers) {
                    val markerX = marker.marker.lat
                    val markerY = marker.marker.lng
                    mapView.map.mapObjects.addPlacemark(Point(markerX,markerY), ImageProvider.fromResource(context, R.drawable.search_result))
                    mapView.map.move(
                        CameraPosition(point,14.0f,0.0f,0.0f),
                        Animation(Animation.Type.SMOOTH,0f),
                        null
                    )
                    val circleX = circle.geometry.center.longitude
                    val circleY = circle.geometry.center.latitude
                    val radius = circle.geometry.radius
                    val distance = sqrt((markerX - circleX) * (markerX - circleX) + (markerY - circleY) * (markerY - circleY))
                    if (distance <= radius) {
                        val intent = Intent("GEOFENCE_TRIGGERED")
                        context?.sendBroadcast(intent)

                    }
                }

            }





            }
        }
        executor.scheduleAtFixedRate(locationRunnable, 0, 2, TimeUnit.MINUTES)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()

    }



    override fun onMapTap(map: Map, point: Point) {
        sharedViewModel.latCoord.value = point.latitude.toString()
        sharedViewModel.lngCoord.value = point.longitude.toString()
    }

    override fun onMapLongTap(p0: Map, p1: Point) {

    }

    private fun submitQuery(query: String) {
        searchSession = searchManager.submit(
            query,
            VisibleRegionUtils.toPolygon(mapView.map.visibleRegion),
            SearchOptions(),
            this
        )
    }

//
    override fun onCameraPositionChanged(
        map: Map,
        cameraPosition: CameraPosition,
        cameraUpdateReason: CameraUpdateReason,
        finished: Boolean
    ) {
        if(finished){
            submitQuery(searchEdit.text.toString())
        }
    }

    override fun onSearchResponse(response: Response) {
        val mapObjects = mapView.map.mapObjects
        mapObjects.clear()

        for (searchResult in response.collection.children) {
            val resultLocation = searchResult.obj!!.geometry[0].point
            if (resultLocation != null) {
                mapObjects.addPlacemark(
                    resultLocation,
                    ImageProvider.fromResource(context, R.drawable.search_result)
                )
            }
        }
    }

    override fun onSearchError(error: Error) {
        var errorMessage = "Unknown Error"
        if (error is RemoteError) {
            errorMessage = "Remote server error"
        } else if (error is NetworkError) {
            errorMessage = "Network Error"
        }

        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }


}