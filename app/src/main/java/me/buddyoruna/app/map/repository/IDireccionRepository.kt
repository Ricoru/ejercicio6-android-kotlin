package me.buddyoruna.app.map.repository

import androidx.lifecycle.MutableLiveData
import me.buddyoruna.app.map.domain.Direccion

interface IDireccionRepository {

    fun getDirecciones(): MutableLiveData<List<Direccion>>

    fun addDireccion(direccion: Direccion) : MutableLiveData<String>

    fun updDireccion(direccion: Direccion) : MutableLiveData<String>

    fun deleteDireccion(key: String) : MutableLiveData<String>
}