package com.kennakomea.currencyconverter.ui.rates

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kennakomea.currencyconverter.R
import com.kennakomea.currencyconverter.databinding.FragmentRatesBinding
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL

class RatesFragment : Fragment() {

    private var _binding: FragmentRatesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    private lateinit var newRecylerview : RecyclerView
    private lateinit var newArrayList : ArrayList<Currency>
    lateinit var imageId : Array<Int>
    lateinit var currencyCode : Array<String>
    lateinit var currencySymbol : Array<String>
    lateinit var currencyName : Array<String>
    lateinit var convertedAmount : Array<Float>
    var baseCurrency = "EUR"
    var convertedToCurrency = "USD"
    private var conversionRate = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRatesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        imageId = arrayOf(
            R.drawable.flag_of_europe,
            R.drawable.gbp_flag,
            R.drawable.flag_of_the_united_states,
            R.drawable.flag_of_denmark,
            R.drawable.flag_of_sweden,
            R.drawable.flag_of_australia,
            R.drawable.flag_of_canada,
            R.drawable.flag_of_japan,
        )
        currencyCode = arrayOf(
            "EUR",
            "GBP",
            "USD",
            "DKK",
            "SEK",
            "AUD",
            "CAD",
            "JPY",
        )
        convertedAmount = arrayOf(
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            0.0F,
            0.0F,

        )
        currencySymbol = arrayOf(
            "€",
            "£",
            "$",
            "kr",
            "kr",
            "$",
            "$",
            "¥",
        )
        currencyName = arrayOf(
            "Euro",
            "British Pounds",
            "US Dollars",
            "Danish Krone",
            "Swedish Krone",
            "Australian Dollars",
            "Canadian Dollars",
            "Japanese Yen",

        )

        newRecylerview = binding.recyclerView
        newRecylerview.layoutManager = LinearLayoutManager(this.context)
        newRecylerview.setHasFixedSize(true)
        spinnerSetup()

        return root
    }

    private fun spinnerSetup() {
        val spinner: Spinner? = _binding?.ratesSpinner

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
        if (spinner != null) {
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
                    getCurrencyData()
                    when (baseCurrency) {
                        "EUR" -> updateTopUI(R.drawable.flag_of_europe)
                        "GBP" -> updateTopUI(R.drawable.gbp_flag)
                        "USD" -> updateTopUI(R.drawable.flag_of_the_united_states)
                        "DKK" -> updateTopUI(R.drawable.flag_of_denmark)
                        "SEK" -> updateTopUI(R.drawable.flag_of_sweden)
                        "AUD" -> updateTopUI(R.drawable.flag_of_australia)
                        "CAD" -> updateTopUI(R.drawable.flag_of_canada)
                        "JPY" -> updateTopUI(R.drawable.flag_of_japan)
                        else -> { // Note the block
                            updateTopUI(R.drawable.flag_of_europe)
                        }
                    }
                    //getApiResult()
                }

            })
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun getCurrencyData() {
        val API = "https://open.er-api.com/v6/latest/$baseCurrency"
        newArrayList = arrayListOf<Currency>()
       // newArrayList.clear()

        for(i in imageId.indices){
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val apiResult = URL(API).readText()
                    val jsonObject = JSONObject(apiResult)
                    conversionRate =
                        jsonObject.getJSONObject("rates").getString(currencyCode[i])
                            .toFloat()
                    println("THIS IS BASE CURRENCY!!!!" + baseCurrency)
//                    println(jsonObject.getJSONObject("rates"))

                    Log.d("CONVERSION RATE!!!", "$conversionRate")
                    Log.d("CURRENCY CODE!!!!", currencyCode[i])

                    withContext(Dispatchers.Main) {

                        val currency = Currency(imageId[i],
                            currencyCode[i],
                            currencyName[i],
                            currencySymbol[i],
                            (100* conversionRate).toString())
                        newArrayList.add(currency)
                        newRecylerview.adapter?.notifyDataSetChanged()
                    }

                } catch (e: Exception) {
                    Log.e("Main", "$e")
                }
            }



        }

        newRecylerview.adapter = CurrencyAdapter(newArrayList)

    }

    fun updateTopUI( currencyFlag: Int) {
        binding.spinnerFlag.setImageResource(currencyFlag)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}