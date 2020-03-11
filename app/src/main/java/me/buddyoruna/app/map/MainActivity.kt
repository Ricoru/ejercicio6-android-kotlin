package me.buddyoruna.app.map

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import me.buddyoruna.map.R

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import me.buddyoruna.app.map.contract.OnClick
import me.buddyoruna.app.map.domain.Direccion
import me.buddyoruna.app.map.viewmodel.DireccionViewModel

class MainActivity : AppCompatActivity(), OnClick<Direccion> {

    private var direccionViewModel: DireccionViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ui()

        fab.setOnClickListener {
            val intent = Intent(this, RegisterAddressActivity::class.java)
            val extra = Bundle()
            extra.putString(RegisterAddressActivity.INTENT_ACTION_REGISTER, "register")
            intent.putExtras(extra)
            startActivity(intent)
        }

        favoritos_recyclverview.adapter = direccionViewModel?.getRecyclerDireccionsAdapter()
        direccionViewModel?.setListenerClick(this)
    }

    private fun ui() {
        supportActionBar?.title = "Lista de Direcciones"

        direccionViewModel = ViewModelProviders.of(this).get(DireccionViewModel::class.java)

        direccionViewModel?.getDirecciones()
            ?.observe(this, Observer { direcciones: List<Direccion> ->
                direccionViewModel?.setDireccionInRecyclerAdapter(direcciones)
            })
    }

    override fun onClickItem(obj: Direccion, position: Int) {

        val intent = Intent(this, RegisterAddressActivity::class.java)
        val extra = Bundle()
        extra.putString(RegisterAddressActivity.INTENT_ACTION_REGISTER, "edit")
        extra.putString(RegisterAddressActivity.INTENT_ACTION_DATA, Gson().toJson(obj))
        intent.putExtras(extra)
        startActivity(intent)
    }

}
