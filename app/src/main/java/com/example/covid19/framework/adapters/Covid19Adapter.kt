package com.example.covid19.framework.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covid19.R
import com.example.covid19.model.CovidCaseObject // Make sure you're using the correct import

class Covid19Adapter(private val dataList: List<CovidCaseObject>) :
    RecyclerView.Adapter<Covid19Adapter.Covid19ViewHolder>() {

    // ViewHolder que contiene las vistas para cada elemento
    class Covid19ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewDate: TextView = view.findViewById(R.id.textViewDate)
        val textViewTotalCases: TextView = view.findViewById(R.id.textViewTotalCases)
        val textViewNewCases: TextView = view.findViewById(R.id.textViewNewCases)
    }

    // Crea nuevas vistas
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Covid19ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_covid19, parent, false)
        return Covid19ViewHolder(itemView)
    }

    // Reemplaza el contenido de una vista
    override fun onBindViewHolder(holder: Covid19ViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.textViewDate.text = currentItem.date
        holder.textViewTotalCases.text = currentItem.totalCases.toString()
        holder.textViewNewCases.text = currentItem.newCases.toString() // If you have this TextView in your layout
    }

    // Devuelve el tamaño del dataset
    override fun getItemCount() = dataList.size
}
