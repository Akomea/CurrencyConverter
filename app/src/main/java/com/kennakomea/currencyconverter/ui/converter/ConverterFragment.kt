package com.kennakomea.currencyconverter.ui.converter

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.kennakomea.currencyconverter.databinding.FragmentConverterBinding


class ConverterFragment : Fragment() {

    private var _binding: FragmentConverterBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val converterViewModel =
            ViewModelProvider(this).get(ConverterViewModel::class.java)

        _binding = FragmentConverterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textConverter
        converterViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.cardViewTop?.setOnClickListener {
            println("Top Clicked")
            view?.findNavController()?.navigate(com.kennakomea.currencyconverter.R.id.navigation_currencies)

        }

        binding.cardViewBottom?.setOnClickListener {
            println("Bottom Clicked")
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}