package com.kennakomea.currencyconverter.ui.rates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kennakomea.currencyconverter.databinding.FragmentRatesBinding

class RatesFragment : Fragment() {

    private var _binding: FragmentRatesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val ratesViewModel =
            ViewModelProvider(this).get(RatesViewModel::class.java)

        _binding = FragmentRatesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textRates
        ratesViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}