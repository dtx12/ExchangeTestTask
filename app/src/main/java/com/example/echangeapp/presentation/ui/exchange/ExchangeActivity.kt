package com.example.echangeapp.presentation.ui.exchange

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.echangeapp.ExchangeApp
import com.example.echangeapp.R
import com.example.echangeapp.databinding.ActivityExchangeBinding
import com.example.echangeapp.di.exchange.ExchangeModule
import javax.inject.Inject


class ExchangeActivity : AppCompatActivity() {


    private lateinit var binding: ActivityExchangeBinding

    @Inject
    protected lateinit var viewModeFactory: ExchangeViewModelFactory

    private val viewModel: ExchangeViewModel by viewModels { viewModeFactory }


    private val amountChangedCallback: (String, ExchangeCardViewModel) -> (Unit) = { amount, card ->
        viewModel.onChangeEnteredAmount(amount, card)
    }

    private val fromAdapter = ExchangeCardsAdapter(amountChangedCallback)
    private val toAdapter = ExchangeCardsAdapter(amountChangedCallback)

    private fun setupRecyclerView(view: RecyclerView, adapter: ExchangeCardsAdapter) {
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(view)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        view.layoutManager = layoutManager
        view.adapter = adapter
        view.itemAnimator = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ExchangeApp.appComponent.plus(ExchangeModule()).inject(this)
        binding = ActivityExchangeBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        val yourCurrenciesList = binding.topCurrenciesRecyclerView
        val requiredCurrenciesList = binding.bottomCurrenciesRecyclerView

        setupRecyclerView(yourCurrenciesList, fromAdapter)
        setupRecyclerView(requiredCurrenciesList, toAdapter)

        yourCurrenciesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = yourCurrenciesList.layoutManager as LinearLayoutManager
                    val itemPos = layoutManager.findFirstVisibleItemPosition()
                    if (itemPos >= 0 && itemPos < fromAdapter.items.size) {
                        val item = fromAdapter.items[itemPos]
                        viewModel.onYourCurrencyChanged(item.yourCurrency.base)
                    }
                }
            }
        })

        requiredCurrenciesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = requiredCurrenciesList.layoutManager as LinearLayoutManager
                    val itemPos = layoutManager.findFirstVisibleItemPosition()
                    if (itemPos >= 0 && itemPos < toAdapter.items.size) {
                        val item = toAdapter.items[itemPos]
                        viewModel.onRequiredCurrencyChanged(item.yourCurrency.base)
                    }
                }
            }
        })
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.executePendingBindings()

        viewModel.screenModel.observe(this, Observer {
            displayMainScreenModel(it)
        })
        viewModel.exchangeSuccessEvent.observe(this, Observer {
            displayTransactionCompleted()
        })
        viewModel.exchangeFailEvent.observe(this, Observer {
            displayTransactionError()
        })
    }

    private fun displayMainScreenModel(model: ExchangeScreenModel) {
        fromAdapter.items = model.yourCurrencies
        toAdapter.items = model.targetCurrencies
        binding.topCurrenciesRecyclerView.scrollToPosition(model.yourCurrencies.indexOfFirst { it.selected })
        binding.bottomCurrenciesRecyclerView.scrollToPosition(model.targetCurrencies.indexOfFirst { it.selected })
        binding.titleTextView.text = model.title
    }

    private fun displayTransactionCompleted() {
        Toast.makeText(this, R.string.transaction_completed, Toast.LENGTH_LONG).show()
    }

    private fun displayTransactionError() {
        Toast.makeText(this, R.string.insufficient_funds, Toast.LENGTH_LONG).show()
    }
}
