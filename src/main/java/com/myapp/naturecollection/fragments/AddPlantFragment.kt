package com.myapp.naturecollection.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.myapp.naturecollection.MainActivity
import com.myapp.naturecollection.PlantModel
import com.myapp.naturecollection.PlantRepository
import com.myapp.naturecollection.PlantRepository.Singleton.downloadUri
import com.myapp.naturecollection.R
import java.util.*

class AddPlantFragment(
        private val context : MainActivity
) :Fragment(){

    private var file: Uri? = null
    private var uploadedImge:ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_add_plant, container, false )

        // recuperer uploadedImage
        uploadedImge = view.findViewById(R.id.preview_image)

        // recupere bouton charger image
        val pickupImageButton = view.findViewById<Button>(R.id.upload_button)

        //click ouvre image telephone
        pickupImageButton.setOnClickListener{pickupImage()}

        // recuperer le bouton confirmer
        val confirmButton = view.findViewById<Button>(R.id.confirm_button)
        confirmButton.setOnClickListener{sendForm(view)}
        return view
    }

    private fun sendForm(view: View) {
        val repo = PlantRepository()
        repo.uploadImage(file!!){
            val plantName = view.findViewById<EditText>(R.id.name_input).text.toString()
            val plantDescription = view.findViewById<EditText>(R.id.description_input).text.toString()
            val grow = view.findViewById<Spinner>(R.id.grow_spinner).selectedItem.toString()
            val water = view.findViewById<Spinner>(R.id.water_spinner).selectedItem.toString()
            val downloadImageUrl = downloadUri

            // créer nouvel objet plantModel
            val plant = PlantModel(
                  UUID.randomUUID().toString(),
                    plantName,
                    plantDescription,
                    downloadImageUrl.toString(),
                    grow,
                    water
            )

            // envyer en base
            repo.insertPlant(plant)

        }

    }

    private fun pickupImage() {
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 182)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 182 && resultCode == Activity.RESULT_OK){

            // verifier si datas null
            if(data == null || data.data == null)return

            // recuperer image selecionner
            file = data.data

            // update aperçu image
            uploadedImge?.setImageURI(file)
        }
    }
}