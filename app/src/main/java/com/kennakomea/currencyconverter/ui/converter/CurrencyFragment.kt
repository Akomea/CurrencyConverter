
package com.kennakomea.currencyconverter.ui.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.kennakomea.currencyconverter.R
import com.kennakomea.currencyconverter.databinding.FragmentCurrenciesBinding

class CurrencyFragment : Fragment() {

    private var _binding: FragmentCurrenciesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val currencyViewModel =
            ViewModelProvider(this).get(CurrencyViewModel::class.java)

        _binding = FragmentCurrenciesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textCurrencies
        currencyViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}