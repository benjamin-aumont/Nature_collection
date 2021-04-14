package com.myapp.naturecollection.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.myapp.naturecollection.MainActivity
import com.myapp.naturecollection.PlantModel
import com.myapp.naturecollection.PlantRepository.Singleton.plantList
import com.myapp.naturecollection.R
import com.myapp.naturecollection.adapter.PlantAdapter
import com.myapp.naturecollection.adapter.PlantItemDecoration

class HomeFragment (
    private val context: MainActivity
): Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_home, container, false)

        // recupereer recycle view
        val horizontalRecyclerView = view.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        horizontalRecyclerView.adapter = PlantAdapter(context,plantList.filter { !it.liked },R.layout.item_horizontal_plant)

        // recuperer 2 eme recycle view
        val verticalRecycleView = view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecycleView.adapter = PlantAdapter(context,plantList,R.layout.item_vertical_plant)
        verticalRecycleView.addItemDecoration(PlantItemDecoration())
        return view
    }

}