package com.example.sub1intermediate.ui.map

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.sub1intermediate.R
import com.example.sub1intermediate.ViewModelFactory
import com.example.sub1intermediate.data.model.ListStoryItem
import com.example.sub1intermediate.data.repository.Result
import com.example.sub1intermediate.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val viewModelFactory : ViewModelFactory = ViewModelFactory.getInstance(this)
        val mapsViewModel : MapsViewModel by viewModels{ viewModelFactory }

        mapsViewModel.getToken().observe(this){token ->
            if(token != null){
                mapsViewModel.getLocation(token).observe(this){
                    when(it){
                        is Result.Error -> Toast.makeText(this,"ERROR", Toast.LENGTH_SHORT).show()
                        is Result.Success -> addMark(it.data.listStory)
                        is Result.Loading -> {}
                    }
                }
            }

        }

        getMyLocation()
        val indonesia = LatLng(-6.200000, 106.816666)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(indonesia, 5f))

    }

    private  fun addMark(listlocation : List<ListStoryItem>){
        listlocation.forEach{
            if (it.lat != null && it.lon != null ){
                val latLng = LatLng(it.lat as Double, it.lon as Double)

                mMap.addMarker(MarkerOptions().position(latLng).title(it.name).snippet(it.description))
                boundsBuilder.include(latLng)
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }



}