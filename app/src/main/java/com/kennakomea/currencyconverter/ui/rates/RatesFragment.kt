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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        )

        currencyCode = arrayOf(
            "EUR",
            "GBP",
            "USD",
        )

        convertedAmount = arrayOf(
        )

        currencySymbol = arrayOf(
            "€",
            "£",
            "$",

        )

        currencyName = arrayOf(
            "Euro",
            "British Pounds",
            "US Dollars",

        )

        newRecylerview = binding.recyclerView
        newRecylerview.layoutManager = LinearLayoutManager(this.context)
        newRecylerview.setHasFixedSize(true)

        //getUserdata(baseCurrency)
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
                    println("Hello from spinner code "+baseCurrency)
                    getUserdata(baseCurrency)
                    //getApiResult()
                }

            })
        }
    }

//    private fun getApiResult(items: ArrayList<String>) {
//        //val APIKey = ""
//        val API = "https://open.er-api.com/v6/latest/$baseCurrency"
//
//        //https://v6.exchangerate-api.com/v6/YOUR-API-KEY/history/USD/YEAR/MONTH/DAY
//
//        GlobalScope.launch(Dispatchers.IO) {
//            try {
//                val apiResult = URL(API).readText()
//                val jsonObject = JSONObject(apiResult)
//                conversionRate =
//                    jsonObject.getJSONObject("rates").getString(convertedToCurrency)
//                        .toFloat()
//
//                Log.d("Main", "$conversionRate")
//                Log.d("Main", apiResult)
//
//                withContext(Dispatchers.Main) {
//                    var fromConversion = 100;
//                    text =
//                        ((binding.etFirstConversion.text.toString()
//                            .toFloat()) * conversionRate).toString()
//                    binding.etSecondConversion?.setText(text)
//
//
//                    String newValue = text;
//                    int updateIndex = 3;
//                    data.set(updateIndex, newValue);
//                    adapter.notifyItemChanged(updateIndex);
//                }
//
//            } catch (e: Exception) {
//                Log.e("Main", "$e")
//            }
//        }
//    }


    private fun getUserdata(base: String) {
        val API = "https://open.er-api.com/v6/latest/$base"
        newArrayList = arrayListOf<Currency>()
       // newArrayList.clear()

        for((i, cc) in currencyCode.withIndex()){
            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val apiResult = URL(API).readText()
                    val jsonObject = JSONObject(apiResult)
                    conversionRate =
                        jsonObject.getJSONObject("rates").getString(cc)
                            .toFloat()
                    println("THIS IS BASE CURRENCY!!!!" + baseCurrency)
//                    println(jsonObject.getJSONObject("rates"))

                    Log.d("CONVERSION RATE!!!", "$conversionRate")
                    Log.d("CURRENCY CODE!!!!", cc)

                    withContext(Dispatchers.Main) {

                        val currency = Currency(imageId[i],
                            cc,
                            currencyName[i],
                            currencySymbol[i],
                            (100*conversionRate).toString())
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}