package me.buddyoruna.app.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import me.buddyoruna.app.map.domain.Direccion
import me.buddyoruna.app.map.viewmodel.DireccionViewModel
import me.buddyoruna.map.R

class RegisterAddressActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerDragListener {

    companion object {
        var INTENT_ACTION_REGISTER: String = "intent_action_register"
        var INTENT_ACTION_DATA: String = "intent_action_data"
        private const val MY_LOCATION_REQUEST_CODE = 99999
    }

    private lateinit var mMap: GoogleMap
    private lateinit var mGeocoder: Geocoder
    private lateinit var markerDrag: Marker
    private lateinit var mPosition: LatLng
    var direccion: Direccion? = null

    private var direccionViewModel: DireccionViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        direccionViewModel = ViewModelProviders.of(this).get(DireccionViewModel::class.java)

        if (intent.extras != null) {
            if (!intent.getStringExtra(INTENT_ACTION_DATA).isNullOrEmpty()) {
                direccion = Gson().fromJson(
                    intent.getStringExtra(INTENT_ACTION_DATA),
                    Direccion::class.java
                )
            }
        }
    }


    private fun validatePermisionGps() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    MY_LOCATION_REQUEST_CODE
                )
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mGeocoder = Geocoder(this)
        mMap = googleMap
        validatePermisionGps()
        // Add a marker in Academia Moviles and move the camera
        //marker por default
        mPosition = LatLng(
            Places.miPosicion.latitude,
            Places.miPosicion.longitude
        )
        markerDrag = mMap.addMarker(
            MarkerOptions().position(mPosition).title("Mi Posición").snippet("Yo estoy aquí")
                .draggable(true)
        )

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mPosition, 10f))
        //googleMap.setOnMarkerClickListener(this)
        googleMap.setOnMarkerDragListener(this)
        //googleMap.setOnInfoWindowClickListener(this)
    }

    override fun onMarkerDragEnd(p0: Marker?) {
        if (p0?.equals(markerDrag)!!) {
            mPosition = p0?.position
            getAddress(p0.position.latitude, p0.position.longitude)
        }
    }

    override fun onMarkerDragStart(p0: Marker?) {

    }

    override fun onMarkerDrag(p0: Marker?) {
        if (p0?.equals(markerDrag)!!) {
            supportActionBar?.title = getString(
                R.string.marker_detail_latlng,
                p0.position.latitude,
                p0.position.longitude
            )
        }
    }

    private fun getAddress(lat: Double, long: Double) {
        try {
            mGeocoder.getFromLocation(lat, long, 1).apply {
                if (size > 0 && this != null) {
                    val lastAddress = this[0]
                    supportActionBar?.title = lastAddress.getAddressLine(0)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> {
                if (direccion != null) {
                    FormDireccionDialogFragment.newInstance(
                            direccion!!.key!!,
                            direccion!!.nombre,
                            mPosition.latitude,
                            mPosition.longitude,
                            direccion!!.referencia,
                            direccion!!.apodo
                        )
                        .show(supportFragmentManager, "Formulario")
                } else {
                    FormDireccionDialogFragment.newInstance(
                            "",
                            "",
                            mPosition.latitude,
                            mPosition.longitude,
                            "",
                            ""
                        )
                        .show(supportFragmentManager, "Formulario")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.size == 1 &&
                permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                mMap.isMyLocationEnabled = true
            } else {
                Toast.makeText(
                    this,
                    "No tienes permiso para acceder a la ubicacion",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}
