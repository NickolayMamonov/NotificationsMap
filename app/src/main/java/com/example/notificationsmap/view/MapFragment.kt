package com.example.notificationsmap.view

import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.notificationsmap.MapViewModel
import com.example.notificationsmap.R
import com.example.notificationsmap.SharedViewModel
import com.example.notificationsmap.databinding.FragmentMapBinding
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
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

// Session.SearchListener
class MapFragment : Fragment(), UserLocationObjectListener, InputListener,
    CameraListener,Session.SearchListener {
    private lateinit var viewModel: MapViewModel

    private val sharedViewModel: SharedViewModel by activityViewModels()
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
        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        lifecycleScope.launch {
            val tasks = viewModel.getAllMarkers()
            for (task in tasks) {
                if(task.isActive){
                    mapView.map.mapObjects.addPlacemark(Point(task.marker.lat, task.marker.lng))
                }

            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding.mapview
        val mapkit: MapKit = MapKitFactory.getInstance()
        locationMapkit = mapkit.createUserLocationLayer(mapView.mapWindow)
        mapView.getMap().setRotateGesturesEnabled(false)
        mapView.map.move(CameraPosition(Point(0.0, 0.0), 14f, 0f, 0f))
        locationMapkit.isVisible = true
        locationMapkit.setObjectListener(this)

//        SearchFactory.initialize(this.requireContext())
//        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
//        mapView.map.addCameraListener(this)
//        searchEdit = binding.searchEdit
//        searchEdit.setOnEditorActionListener { v, actionId, event ->
//            if(actionId == EditorInfo.IME_ACTION_SEARCH){
//                submitQuery(searchEdit.text.toString())
//            }
//            false
//        }
//        mapkit.createLocationManager().requestSingleUpdate(object : LocationListener {
//            override fun onLocationUpdated(location: Location) {
//                mapView.map.move(
//                    CameraPosition(location.position, 25.0f, 0.0f, 0.0f),
//                    Animation(Animation.Type.SMOOTH, 5F),
//                    null
//                )
//            }
//
//            override fun onLocationStatusUpdated(p0: LocationStatus) {
//
//            }
//        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        SearchFactory.initialize(context)

        super.onCreate(savedInstanceState)
//        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
//
//        mapView.map.addCameraListener(this)
//
//        searchEdit = binding.searchEdit
//        searchEdit.setOnEditorActionListener { textView, actionId, keyEvent ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                submitQuery(searchEdit.text.toString())
//            }
//            false
//        }
//
//        mapView.map.move(
//            CameraPosition(Point(59.945933, 30.320045), 14.0f, 0.0f, 0.0f)
//        )
//
//        submitQuery(searchEdit.text.toString())
    }

    override fun onStart() {
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()
        mapView.map.addInputListener(this)

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        locationMapkit.setAnchor(
            PointF((mapView.width() * 0.5).toFloat(), (mapView.height() * 0.5).toFloat()),
            PointF((mapView.width() * 0.5).toFloat(), (mapView.height() * 0.5).toFloat())
        )
        val pinIcon = userLocationView.pin.useCompositeIcon()


        pinIcon.setIcon(
            "pin",
            ImageProvider.fromResource(context, R.drawable.search_result),
            IconStyle().setAnchor(PointF(0.5f, 0.5f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(1f)
                .setScale(0.5f)
        )

    }

    override fun onObjectRemoved(p0: UserLocationView) {

    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {

    }

    override fun onMapTap(map: Map, point: Point) {
//        binding.btnAdd.visibility = View.VISIBLE
//        setFragmentResult("mapPoint",Bundle().apply {
//            putDouble("latitude",latitude)
//            putDouble("longitude",longitude)
//        })
        sharedViewModel.latCoord.value = point.latitude.toString()

        sharedViewModel.lngCoord.value = point.longitude.toString()
//        binding.btnAdd.setOnClickListener {
////            createTaskViewModel.latCoord.value = point.latitude
////            createTaskViewModel.lngCoord.value = point.longitude
//            findNavController().navigate(R.id.action_mainFragment_to_createTaskFragment)
//        }
//        val marker = MarkerEntity.from(point.latitude, point.longitude)
//        viewModel.insertMarkerPos(marker)
//        mapView.map.mapObjects.addPlacemark(Point(marker.lat, marker.lng))

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

//    override fun onSearchResponse(response: Response) {
////        val mapObjects:MapObjectCollection = mapView.map.mapObjects
////        mapObjects.clear()
//        for(searchResult in response.collection.children){
//            val resultLocation = searchResult.obj?.geometry?.get(0)?.point
//            resultLocation?.let { CameraPosition(it, 25.0f, 0.0f, 0.0f) }?.let {
////                mapView.map.move(
////                    it,
////                    Animation(Animation.Type.SMOOTH, 5F),
////                    null
////                )
//            }
//            //resultLocation?.let { mapObjects.addPlacemark(it, ImageProvider.fromResource(this.context,R.drawable.pin)) }
//        }
//    }
//
//    override fun onSearchError(p0: Error) {
//
//    }
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