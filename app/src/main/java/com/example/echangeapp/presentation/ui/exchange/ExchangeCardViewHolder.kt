package com.example.echangeapp.presentation.ui.exchange

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.RecyclerView
import com.example.echangeapp.R
import com.example.echangeapp.databinding.ListItemCardBinding
import com.example.echangeapp.domain.CurrencyFormatter
import com.example.echangeapp.domain.models.symbol

class ExchangeCardViewHolder(
    private val binding: ListItemCardBinding,
    private val amountChangedCallback: (String, Int) -> (Unit)
) : RecyclerView.ViewHolder(binding.root) {


    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

            val adapterPos = adapterPosition
            if (adapterPos >= 0) {
                amountChangedCallback(binding.enteredAmount.text.toString(), adapterPos)
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

    }

    @SuppressLint("SetTextI18n")
    fun bind(cardViewModel: ExchangeCardViewModel) {
        binding.currency.text = cardViewModel.yourCurrency.base.name
        binding.amount.text = itemView.context.getString(
            R.string.you_have_format,
            cardViewModel.yourCurrency.amount,
            cardViewModel.yourCurrency.base.symbol()
        )
        binding.rate.text = itemView.context.getString(
            R.string.exchange_rate_format,
            cardViewModel.yourCurrency.base.symbol(),
            cardViewModel.requiredCurrency.symbol(),
            CurrencyFormatter.formatCurrency(cardViewModel.rate)
        )
        binding.enteredAmount.removeTextChangedListener(textWatcher)
        if (cardViewModel.enteredAmount.isNotEmpty()) {
            val selection = binding.enteredAmount.selectionStart
            val oldText = binding.enteredAmount.text
            binding.enteredAmount.setText(cardViewModel.enteredAmount)
            if (oldText.length > cardViewModel.enteredAmount.length) {
                binding.enteredAmount.setSelection(binding.enteredAmount.text.length.coerceAtMost(selection))
            } else {
                binding.enteredAmount.setSelection(binding.enteredAmount.text.length.coerceAtMost(selection + 1))
            }
            binding.sign.text = cardViewModel.type.sign()
        } else {
            binding.enteredAmount.setText("")
            binding.sign.text = ""
        }
        binding.enteredAmount.addTextChangedListener(textWatcher)
    }
}