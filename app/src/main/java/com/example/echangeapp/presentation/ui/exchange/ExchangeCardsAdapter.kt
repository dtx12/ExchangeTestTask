package com.example.echangeapp.presentation.ui.exchange

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.echangeapp.R
import com.example.echangeapp.databinding.ListItemCardBinding

class ExchangeCardsAdapter(private val amountChangedCallback: (String, ExchangeCardViewModel) -> (Unit)) :
    RecyclerView.Adapter<ExchangeCardViewHolder>() {


    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return (items[position].yourCurrency.base.name + items[position].type.name).hashCode()
            .toLong()
    }

    var items: List<ExchangeCardViewModel> = listOf()
        set(value) {
            val old = field
            field = value
            DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return old[oldItemPosition] == value[newItemPosition]
                }

                override fun getOldListSize(): Int {
                    return old.size
                }

                override fun getNewListSize(): Int {
                    return value.size
                }

                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ): Boolean {
                    return old[oldItemPosition] == value[newItemPosition]
                }

            }).dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeCardViewHolder {
        return ExchangeCardViewHolder(
            ListItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        ) { text, position ->
            amountChangedCallback(text, items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun onBindViewHolder(holder: ExchangeCardViewHolder, position: Int) {
        holder.bind(items[position])
    }
}