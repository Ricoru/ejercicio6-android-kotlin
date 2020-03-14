package me.buddyoruna.app.map.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.buddyoruna.app.map.MainActivity
import me.buddyoruna.app.map.contract.OnClick
import me.buddyoruna.app.map.domain.Direccion
import me.buddyoruna.map.R

class DireccionesRecyclerAdapter :
    RecyclerView.Adapter<DireccionesRecyclerAdapter.DireccionesHolder>() {

    var direccionesList: List<Direccion> = ArrayList()
    var mListener: OnClick<Direccion>? = null

    fun setDominioList(direccionesList: List<Direccion>) {
        Log.i("INFO", "direccionesList!!.size ${direccionesList!!.size}")
        this.direccionesList = direccionesList
        Log.i("INFO", "direccionesList!!.size ${direccionesList.size}")
    }

    fun setListener(mListener: OnClick<Direccion>) {
        this.mListener = mListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DireccionesRecyclerAdapter.DireccionesHolder {
        return DireccionesHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_item_direccion, parent, false)
        )
    }

    override fun getItemCount(): Int = direccionesList!!.size

    override fun onBindViewHolder(holder: DireccionesHolder, position: Int) =
        holder.setDataPallet(direccionesList!![position], position)

    inner class DireccionesHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tituloTextview = itemView.findViewById<TextView>(R.id.titulo_textview)
        val positionTextview = itemView.findViewById<TextView>(R.id.position_textview)
        val referenciaTextview = itemView.findViewById<TextView>(R.id.referencia_textview)
        val apodoTextview = itemView.findViewById<TextView>(R.id.apodo_textview)
        var eliminarButton = itemView.findViewById<Button>(R.id.eliminar_button)
        var editarButton = itemView.findViewById<Button>(R.id.editar_button)

        fun setDataPallet(item: Direccion, position: Int) {
            tituloTextview.text = item.nombre
            positionTextview.text = "${item.position?.latitude} , ${item.position?.longitude}"
            referenciaTextview.text = item.referencia
            apodoTextview.text = item.apodo

            eliminarButton.setOnClickListener {
                mListener?.onClickItem(item, position, MainActivity.Companion.Actiones.Eliminar)
            }
            editarButton.setOnClickListener {
                mListener?.onClickItem(item, position, MainActivity.Companion.Actiones.Editar)
            }
        }

    }

}