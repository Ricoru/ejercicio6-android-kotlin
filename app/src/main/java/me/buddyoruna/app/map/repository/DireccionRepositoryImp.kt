package me.buddyoruna.app.map.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import me.buddyoruna.app.map.domain.Direccion

class DireccionRepositoryImp : IDireccionRepository {

    private var dataMutableList = MutableLiveData<List<Direccion>>()

    private var messageMutable = MutableLiveData<String>()

    private val remoteDB = FirebaseFirestore.getInstance().apply {
        firestoreSettings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()
    }

    companion object {
        private const val ADDRESS_COLLECTION = "direcciones"
    }

    override fun getDirecciones(): MutableLiveData<List<Direccion>> {
        val direcciones = ArrayList<Direccion>()
        remoteDB.collection(ADDRESS_COLLECTION)
            .get()
            .addOnSuccessListener {
                for (e in it.documents) {
                    val result = e.toObject(Direccion::class.java)
                    result?.key = e.id
                    direcciones.add(result!!)
                }
                dataMutableList.value = direcciones
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
        return dataMutableList
    }

    override fun addDireccion(direccion: Direccion) : MutableLiveData<String> {
        remoteDB.collection(ADDRESS_COLLECTION)
            .add(direccion)
            .addOnSuccessListener {
                messageMutable.value = "Registro exitoso"
            }
            .addOnFailureListener {
                messageMutable.value = it.toString()
            }

        return messageMutable
    }

    override fun updDireccion(direccion: Direccion) : MutableLiveData<String> {
        val data = HashMap<String, Any>()
        data["nombre"] = direccion.nombre
        data["position"] = direccion.position!!
        data["referencia"] = direccion.referencia
        data["apodo"] = direccion.apodo

        remoteDB.collection(ADDRESS_COLLECTION)
            .document(direccion.key)
            .update(data)
            .addOnSuccessListener {
                messageMutable.value = "Actualización exitosa"
            }
            .addOnFailureListener {
                messageMutable.value = it.toString()
            }

        return messageMutable
    }

    override fun deleteDireccion(key: String) : MutableLiveData<String> {
        remoteDB.collection(ADDRESS_COLLECTION)
            .document(key)
            .delete()
            .addOnSuccessListener {
                messageMutable.value = "Eliminación exitosa"
            }
            .addOnFailureListener {
                messageMutable.value = it.toString()
            }

        return messageMutable
    }

}