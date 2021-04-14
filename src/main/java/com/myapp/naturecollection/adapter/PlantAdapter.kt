package com.myapp.naturecollection.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myapp.naturecollection.*
import kotlin.coroutines.coroutineContext

class PlantAdapter (
        val context: MainActivity,
        private val plantList: List<PlantModel>,
        private val layoutId: Int
): RecyclerView.Adapter<PlantAdapter.ViewHolder>(){

    // boite pour ranger tous les composants
    class ViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val plantImage = view.findViewById<ImageView>(R.id.image_item)
        val plantName: TextView? = view.findViewById(R.id.name_item)
        val plantDescription: TextView? = view.findViewById(R.id.description_item)
        val starIcon = view.findViewById<ImageView>(R.id.star_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = plantList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // recuperer les infos plantes
        val currentPlant = plantList[position]

        // recuperer le repository
        val repo = PlantRepository()

        // mettre a jour nom de la plante
        holder.plantName?.text = currentPlant.name

        // mettre a jour description de la plante
        holder.plantDescription?.text = currentPlant.description

        // utiliser glide
        Glide.with(context).load(Uri.parse(currentPlant.imageUrl)).into(holder.plantImage)

        // verifier si plante like
        if(currentPlant.liked){
            holder.starIcon.setImageResource(R.drawable.ic_star)
        }
        else{
            holder.starIcon.setImageResource(R.drawable.ic_unstar)
        }

        // ajouter interaction sur cette etoile
        holder.starIcon.setOnClickListener {
            // inverser si like ou non
            currentPlant.liked = !currentPlant.liked
            //mettre a jour l'objet plante
            repo.updatePlant(currentPlant)
        }

        // Interaction click sur plante
        holder.itemView.setOnClickListener{
            // afficher popup
            PlantPopup(this, currentPlant).show()
        }
    }
}