package me.buddyoruna.app.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.GeoPoint
import me.buddyoruna.map.R
import kotlinx.android.synthetic.main.fragment_form_direccion_dialog.*
import me.buddyoruna.app.map.domain.Direccion
import me.buddyoruna.app.map.viewmodel.DireccionViewModel

private const val ARG_PARAM_KEY = "key"
private const val ARG_PARAM_NOMBRE = "nombre"
private const val ARG_PARAM_LATITUD = "latitud"
private const val ARG_PARAM_LONGITUD = "longitud"
private const val ARG_PARAM_REFERENCIA = "referencia"
private const val ARG_PARAM_APODO = "apodo"

class FormDireccionDialogFragment : DialogFragment() {

    private var key: String? = null
    private var nombre: String? = null
    private var latitud: Double? = null
    private var longitud: Double? = null
    private var referencia: String? = null
    private var apodo: String? = null

    private var direccionViewModel: DireccionViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            nombre = it.getString(ARG_PARAM_NOMBRE)
            latitud = it.getDouble(ARG_PARAM_LATITUD)
            longitud = it.getDouble(ARG_PARAM_LONGITUD)
            referencia = it.getString(ARG_PARAM_REFERENCIA)
            apodo = it.getString(ARG_PARAM_APODO)
        }

        direccionViewModel = ViewModelProviders.of(this).get(DireccionViewModel::class.java)
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form_direccion_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        nombre_edittext.setText(nombre)
        latitud_edittext.setText(latitud.toString())
        longitud_edittext.setText(longitud.toString())
        referencia_edittext.setText(referencia)
        apodo_edittext.setText(apodo)

        guardar_button.setOnClickListener {
            val direccion = Direccion()
            direccion.position = GeoPoint(latitud!!, longitud!!)
            direccion.referencia = referencia_edittext.text.toString()
            direccion.nombre = nombre_edittext.text.toString()
            direccion.apodo = apodo_edittext.text.toString()

            if (key.isNullOrEmpty()) {
                direccionViewModel?.addDireccion(direccion)
                    ?.observe(activity!!, Observer { msg ->
                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
                        dismiss()
                    })
            } else {
                direccion.key = key!!
                direccionViewModel?.updateDireccion(direccion)
                    ?.observe(activity!!, Observer { msg ->
                        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
                        dismiss()
                    })
            }
        }


        super.onActivityCreated(savedInstanceState)
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(
            key: String,
            nombre: String,
            latitud: Double,
            longitud: Double,
            referencia: String,
            apodo: String
        ) =
            FormDireccionDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM_KEY, key)
                    putString(ARG_PARAM_NOMBRE, nombre)
                    putDouble(ARG_PARAM_LATITUD, latitud)
                    putDouble(ARG_PARAM_LONGITUD, longitud)
                    putString(ARG_PARAM_REFERENCIA, referencia)
                    putString(ARG_PARAM_APODO, apodo)
                }
            }
    }
}
