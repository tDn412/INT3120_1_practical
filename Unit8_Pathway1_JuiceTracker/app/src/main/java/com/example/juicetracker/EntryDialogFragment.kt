package com.example.juicetracker

import android.R
import androidx.fragment.app.viewModels
import com.example.juicetracker.ui.AppViewModelProvider
import com.example.juicetracker.ui.EntryViewModel
import com.example.juicetracker.databinding.FragmentEntryDialogBinding
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.navigation.fragment.navArgs
import com.example.juicetracker.data.JuiceColor

class EntryDialogFragment : BottomSheetDialogFragment() {

    private val entryViewModel by viewModels<EntryViewModel> { AppViewModelProvider.Factory }
    var selectedColor: JuiceColor = JuiceColor.Red

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEntryDialogBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentEntryDialogBinding.bind(view)
        val args: EntryDialogFragmentArgs by navArgs()
        val juiceId = args.itemId

        val colorOptions = JuiceColor.values().map { it.name }
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item, colorOptions)
        binding.colorSpinner.adapter = adapter

        binding.colorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedColor = JuiceColor.valueOf(colorOptions[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedColor = JuiceColor.valueOf(colorOptions[0])
            }
        }

        binding.saveButton.isEnabled = false

        binding.name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Bật nút Save chỉ khi người dùng đã nhập gì đó vào name
                binding.saveButton.isEnabled = !s.isNullOrBlank()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.saveButton.setOnClickListener {
            entryViewModel.saveJuice(
                juiceId,
                binding.name.text.toString(),
                binding.description.text.toString(),
                selectedColor.name,
                binding.ratingBar.rating.toInt()
            )
            dismiss()
        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}