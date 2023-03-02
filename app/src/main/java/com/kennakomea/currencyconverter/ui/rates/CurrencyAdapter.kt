package com.kennakomea.currencyconverter.ui.rates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.kennakomea.currencyconverter.R

class CurrencyAdapter(private val currencyList : ArrayList<Currency>) : RecyclerView.Adapter<CurrencyAdapter.MyViewHolder>(),Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item,
        parent,false)

        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = currencyList[position]
        holder.titleImage.setImageResource(currentItem.image)
        holder.heading.text = currentItem.currencyCode
        holder.currencyName.text = currentItem.currencyName
        holder.currencySymbol.text = currentItem.currencySymbol
        holder.convertedAmount.text = currentItem.convertedAmount

    }


    override fun getItemCount(): Int {

        return currencyList.size
    }

    class MyViewHolder(itemView : View, ) : RecyclerView.ViewHolder(itemView){

        val titleImage : ShapeableImageView = itemView.findViewById(R.id.title_image)
        val heading : TextView = itemView.findViewById(R.id.currencyHeading)
        val currencyName : TextView = itemView.findViewById(R.id.currency_name)
        val currencySymbol : TextView = itemView.findViewById(R.id.baseCurrencySymbol)
        val convertedAmount : TextView = itemView.findViewById(R.id.convertedAmount)

        }

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }

}
