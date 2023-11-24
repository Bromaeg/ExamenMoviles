package com.example.covid19.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covid19.R
import com.example.covid19.model.Covid19Object

class Covid19Adapter(private val dataList: List<Covid19Object>) :
    RecyclerView.Adapter<Covid19Adapter.Covid19ViewHolder>() {

    // ViewHolder que contiene las vistas para cada elemento
    class Covid19ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewRegion: TextView = view.findViewById(R.id.textViewRegion)
        val textViewCountry: TextView = view.findViewById(R.id.textViewCountry)
        val textViewDate: TextView = view.findViewById(R.id.textViewDate)
        val textViewTotalCases: TextView = view.findViewById(R.id.textViewTotalCases)

    }

    // Crea nuevas vistas (invocado por el layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Covid19ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_covid19, parent, false)
        return Covid19ViewHolder(itemView)
    }

    // Reemplaza el contenido de una vista (invocado por el layout manager)
    override fun onBindViewHolder(holder: Covid19ViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.textViewRegion.text = currentItem.region
        holder.textViewCountry.text = currentItem.country
        holder.textViewDate.text = currentItem.date
        holder.textViewTotalCases.text = currentItem.totalCases

    }

    // Devuelve el tamaño de tu dataset (invocado por el layout manager)
    override fun getItemCount() = dataList.size
}
