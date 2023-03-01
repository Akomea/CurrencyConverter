package com.kennakomea.currencyconverter.ui.converter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kennakomea.currencyconverter.databinding.FragmentConverterBinding
import org.json.JSONObject
import java.lang.Exception
import java.net.URL
import com.kennakomea.currencyconverter.R
import kotlinx.coroutines.*


class ConverterFragment : Fragment() {

    private var _binding: FragmentConverterBinding? = null

    var baseCurrency = "EUR"
    var convertedToCurrency = "USD"
    private var conversionRate = 0f
    var currencyNameTop = "Euro"
    var currencyNameBottom = "American Dollar"
    lateinit var text: String


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentConverterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.currencyName.text = currencyNameTop
        binding.currencyName2.text = currencyNameBottom

        spinnerSetup()
        textChanged()

        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun textChanged() {
        binding.etFirstConversion?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    getApiResult()
                } catch (e: Exception) {
                    Log.d("Main", "Error getting response!!!!")
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("Main", "Before Text Changed")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("Main", "OnTextChanged")
            }
        })
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getApiResult() {
        if (binding.etFirstConversion.text?.isNotEmpty() == true && binding.etFirstConversion.text?.isNotBlank()!!) {

            //val APIKey = ""
            val API = "https://open.er-api.com/v6/latest/$baseCurrency"

            //https://v6.exchangerate-api.com/v6/YOUR-API-KEY/history/USD/YEAR/MONTH/DAY

            if (baseCurrency == convertedToCurrency) {
                Toast.makeText(
                    context,
                    "Please pick a currency to convert",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val apiResult = URL(API).readText()
                        val jsonObject = JSONObject(apiResult)
                        conversionRate =
                            jsonObject.getJSONObject("rates").getString(convertedToCurrency)
                                .toFloat()

                        Log.d("Main", "$conversionRate")
                        Log.d("Main", apiResult)

                        withContext(Dispatchers.Main) {
                                 text =
                                    ((binding.etFirstConversion.text.toString()
                                        .toFloat()) * conversionRate).toString()
                                binding.etSecondConversion?.setText(text)


                        }

                    } catch (e: Exception) {
                        Log.e("Main", "$e")
                    }
                }
            }
        }
    }

    private fun spinnerSetup() {
        val spinner: Spinner? = _binding?.spinnerFirstConversion
        val spinner2: Spinner? = _binding?.spinnerSecondConversion

        this.context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.currencies,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                if (spinner != null) {
                    spinner.adapter = adapter
                }

            }
        }

        this.context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.currencies2,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                if (spinner2 != null) {
                    spinner2.adapter = adapter
                }

            }
        }

        if (spinner != null) {
            binding.button.setOnClickListener {
                var topItem = spinner.selectedItemPosition
                var bottomItem = spinner2?.selectedItemPosition
                spinner2?.setSelection(topItem)
                if (bottomItem != null) {
                    spinner.setSelection(bottomItem)
                }

                println("base currency: $baseCurrency")
                println("converted currency: $convertedToCurrency")

            }


            spinner.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    baseCurrency = parent?.getItemAtPosition(position).toString()
                    getApiResult()
                    when (baseCurrency) {
                        "GBP" -> updateTopUI("British Pounds",R.drawable.gbp_flag, R.drawable.ic_currency_pound_24dp)
                        "USD" -> updateTopUI("US Dollar",R.drawable.flag_of_the_united_states, R.drawable.ic_usd_symbol_24dp)
                        else -> { // Note the block
                            updateTopUI("Euro",R.drawable.flag_of_europe, R.drawable.ic_euro_symbol_24dp)
                        }
                    }


                }

            })
        }

        if (spinner2 != null) {
            spinner2.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    convertedToCurrency = parent?.getItemAtPosition(position).toString()
                    getApiResult()

                    when (convertedToCurrency) {
                        "GBP" -> updateBottomUI("British Pounds",R.drawable.gbp_flag, R.drawable.ic_currency_pound_24dp)
                        "USD" -> updateBottomUI("US Dollar",R.drawable.flag_of_the_united_states, R.drawable.ic_usd_symbol_24dp)
                        else -> { // Note the block
                            updateBottomUI("Euro",R.drawable.flag_of_europe, R.drawable.ic_euro_symbol_24dp)
                        }
                    }

                }

            })
        }
    }

    fun updateBottomUI(currencyName: String, currencyFlag: Int, currencySymbol: Int) {
        currencyNameBottom = currencyName
        binding.currencyName2.text = currencyNameBottom
        binding.currencyFlag2?.setImageResource(currencyFlag)
        binding.currencySymbol2?.setImageResource(currencySymbol)
    }
    fun updateTopUI(currencyName: String, currencyFlag: Int, currencySymbol: Int) {
        currencyNameTop = currencyName
        binding.currencyName.text = currencyNameTop
        binding.currencyFlag1?.setImageResource(currencyFlag)
        binding.currencySymbol1?.setImageResource(currencySymbol)
    }
}