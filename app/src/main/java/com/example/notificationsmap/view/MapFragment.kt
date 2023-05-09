package com.example.notificationsmap.view

import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.notificationsmap.MapViewModel
import com.example.notificationsmap.R
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
import com.yandex.mapkit.map.*
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.*
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.launch

class MapFragment : Fragment(), UserLocationObjectListener, Session.SearchListener, InputListener,
    CameraListener {
    private lateinit var viewModel: MapViewModel
    private lateinit var mapView: MapView
    private lateinit var locationMapkit: UserLocationLayer
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

        SearchFactory.initialize(this.requireContext())
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        mapView.map.addCameraListener(this)
        searchEdit = binding.searchEdit
        searchEdit.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                submitQuery(searchEdit.text.toString())
            }
            false
        }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()
        mapView.map.addInputListener(this)

    }

    override fun onResume() {
        super.onResume()
//        binding.btnAdd.setOnClickListener{
//            childFragmentManager.commit {
//                replace(R.id.content, CreateTaskFragment())
//            }
//
//        }
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

            userLocationView.pin.setIcon(
                ImageProvider.fromResource(
                    requireContext(),
                    R.drawable.pin
                )
            )
    }

    override fun onObjectRemoved(p0: UserLocationView) {

    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {

    }

    override fun onMapTap(map: Map, point: Point) {
        val latitude = point.latitude
        val longitude = point.longitude
        binding.btnAdd.visibility = View.VISIBLE
        setFragmentResult("mapPoint",Bundle().apply {
            putDouble("latitude",latitude)
            putDouble("longitude",longitude)
        })
        binding.btnAdd.setOnClickListener {

        }
//        val marker = MarkerEntity.from(point.latitude, point.longitude)
//        viewModel.insertMarkerPos(marker)
//        mapView.map.mapObjects.addPlacemark(Point(marker.lat, marker.lng))

    }

    override fun onMapLongTap(p0: Map, p1: Point) {

    }

    private fun submitQuery(query:String){
        searchSession = searchManager.submit(query,
            VisibleRegionUtils.toPolygon(mapView.map.visibleRegion),
            SearchOptions(), this)
    }

    override fun onSearchResponse(response: Response) {
//        val mapObjects:MapObjectCollection = mapView.map.mapObjects
//        mapObjects.clear()
        for(searchResult in response.collection.children){
            val resultLocation = searchResult.obj?.geometry?.get(0)?.point
            resultLocation?.let { CameraPosition(it, 25.0f, 0.0f, 0.0f) }?.let {
//                mapView.map.move(
//                    it,
//                    Animation(Animation.Type.SMOOTH, 5F),
//                    null
//                )
            }
            //resultLocation?.let { mapObjects.addPlacemark(it, ImageProvider.fromResource(this.context,R.drawable.pin)) }
        }
    }

    override fun onSearchError(p0: Error) {

    }

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


}