package com.myapp.naturecollection

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.myapp.naturecollection.adapter.PlantAdapter

class PlantPopup(private val adapter: PlantAdapter, private val currentPlant: PlantModel) : Dialog(adapter.context){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_plants_details)
        setupComponents()
        setupCloseButon()
        setupDeleteButton()
        setupStarButton()
    }

    private fun updateStar(button: ImageView) {
        if(currentPlant.liked){
            button.setImageResource(R.drawable.ic_star)
        }
        else{
            button.setImageResource(R.drawable.ic_unstar)
        }
    }
    private fun setupStarButton() {
        // like la plante
        val starButton = findViewById<ImageView>(R.id.star_button)
        updateStar(starButton)

        // iteraction
        starButton.setOnClickListener {
            currentPlant.liked = !currentPlant.liked
            val repo = PlantRepository()
            repo.updatePlant(currentPlant)
            updateStar(starButton)
        }
    }

    private fun setupDeleteButton() {
        //delete plante
        findViewById<ImageView>(R.id.delete_button).setOnClickListener {
            // suprimer la plate de la base
            val repo = PlantRepository()
            repo.deletePant(currentPlant)
            dismiss()
        }
    }

    private fun setupCloseButon() {
        // close bouton
        findViewById<ImageView>(R.id.close_button).setOnClickListener {
            // fermer popup
            dismiss()
        }
    }

    private fun setupComponents() {
        // actualiser l'image de la plante
        val plantImage = findViewById<ImageView>(R.id.image_item)
        Glide.with(adapter.context).load(Uri.parse(currentPlant.imageUrl)).into(plantImage)

        // actualiser nom plante
        findViewById<TextView>(R.id.popup_plant_name).text = currentPlant.name

        // actualiser descrption plante
        findViewById<TextView>(R.id.popup_plant_description_subtitle).text = currentPlant.description

        // actualiser croissance plante
        findViewById<TextView>(R.id.popup_plant_grow_subtitle).text = currentPlant.grow

        // actualiser consomation eau plante
        findViewById<TextView>(R.id.popup_plant_water_subtitle).text = currentPlant.water
    }
}