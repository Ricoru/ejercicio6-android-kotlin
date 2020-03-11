package me.buddyoruna.app.map.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.buddyoruna.app.map.adapter.DireccionesRecyclerAdapter
import me.buddyoruna.app.map.contract.OnClick
import me.buddyoruna.app.map.domain.Direccion
import me.buddyoruna.app.map.repository.DireccionRepositoryImp
import me.buddyoruna.app.map.repository.IDireccionRepository

class DireccionViewModel : ViewModel() {

    private var direccionRepository : IDireccionRepository = DireccionRepositoryImp()
    private var direccionesRecyclerAdapter : DireccionesRecyclerAdapter? = null

    fun getDirecciones(): MutableLiveData<List<Direccion>> {
        return direccionRepository.getDirecciones()
    }

    fun addDireccion(direccion: Direccion): MutableLiveData<String> {
        return direccionRepository.addDireccion(direccion)
    }

    fun updateDireccion(direccion: Direccion): MutableLiveData<String> {
        return direccionRepository.updDireccion(direccion)
    }

    fun deleteDireccion(key: String): MutableLiveData<String> {
        return direccionRepository.deleteDireccion(key)
    }

    fun setDireccionInRecyclerAdapter(direcciones: List<Direccion>) {
        direccionesRecyclerAdapter?.setDominioList(direcciones)
        direccionesRecyclerAdapter?.notifyDataSetChanged()
    }

    fun setListenerClick(mListener: OnClick<Direccion>) {
        direccionesRecyclerAdapter?.setListener(mListener)
    }

    fun getRecyclerDireccionsAdapter(): DireccionesRecyclerAdapter? {
        direccionesRecyclerAdapter = DireccionesRecyclerAdapter()
        return direccionesRecyclerAdapter
    }

}