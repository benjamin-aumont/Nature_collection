package com.myapp.naturecollection

import android.net.Uri
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.myapp.naturecollection.PlantRepository.Singleton.databaseRef
import com.myapp.naturecollection.PlantRepository.Singleton.downloadUri
import com.myapp.naturecollection.PlantRepository.Singleton.plantList
import com.myapp.naturecollection.PlantRepository.Singleton.storageReference
import java.net.URI
import java.util.*
import javax.security.auth.callback.Callback

class PlantRepository {

    object Singleton {
        // Lien bucket
        private val BUCKET_URL:String = ""

        // se connecter a storage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)

        // se connecter a la ref plants
        val databaseRef = FirebaseDatabase.getInstance().getReference("plants")

        // creer liste de plants
        val plantList = arrayListOf<PlantModel>()

        //contenir lien image
        var downloadUri:Uri? = null
    }

    fun updateData(callback :() -> Unit){
        // absorber les données
        databaseRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                plantList.clear()
                // recolter la liste
                for(ds in snapshot.children) {
                    // construire objet plante
                    val plant = ds.getValue(PlantModel::class.java)

                    // verifier plante !null
                    if(plant != null) {
                        // ajouter plante a liste
                        plantList.add(plant)
                    }
                }
                // actionner callback
                callback()
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    //fonction pour envoer fichier su storage
    fun uploadImage(file: Uri, callback: () -> Unit){
        // verifier si null
        if(file != null){
            val fileName = UUID.randomUUID().toString() + ".jpg"
            val ref = storageReference.child(fileName)
            val uploadTask = ref.putFile(file)

            // demarer tache envoie
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                // veriier pb
                if(!task.isSuccessful){
                    task.exception?.let { throw it }
                }

                return@Continuation ref.downloadUrl
            }).addOnCompleteListener{task ->
                // verifiersi tout a ben fonctioner
                if(task.isSuccessful){
                    //recuperer image
                    downloadUri = task.result
                    callback()
                }
            }
        }
    }

    // mettre a jour un objet plante en base
    fun updatePlant(plant: PlantModel){
        databaseRef.child(plant.id).setValue(plant)
    }

    // inserer plante en base
    fun insertPlant(plant: PlantModel){
        databaseRef.child(plant.id).setValue(plant)
    }

    // suprmer une plante de la bse
    fun deletePant(plant: PlantModel) = databaseRef.child(plant.id).removeValue()
}
