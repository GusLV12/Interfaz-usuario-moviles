package com.gusslinaresv.catalogoui.views

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

class TextInputsFragment : Fragment(R.layout.fragment_text_inputs) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val model = ViewModelProvider(requireActivity())[CatalogViewModel::class.java]
        val validation = view.findViewById<EditText>(R.id.input_validation)
        val validationLayout = view.findViewById<TextInputLayout>(R.id.layout_validation)
        validation.addTextChangedListener { validationLayout.error = if ((it?.length ?: 0) in 1..2) "Escribe al menos 3 caracteres" else null }
        view.findViewById<AutoCompleteTextView>(R.id.input_suggestions).setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, listOf("Ciudad de México", "Guadalajara", "Monterrey")))
        val search = view.findViewById<EditText>(R.id.input_search)
        val result = view.findViewById<TextView>(R.id.search_result)
        search.addTextChangedListener { text -> result.text = listOf("Botón", "Interruptor", "Tarjeta", "Deslizador").filter { it.contains(text?.toString().orEmpty(), true) }.joinToString(" · ").ifEmpty { "Sin coincidencias" } }
        val pending = view.findViewById<EditText>(R.id.input_pending)
        model.pendingText.observe(viewLifecycleOwner) { if (pending.text.toString() != it) pending.setText(it) }
        pending.addTextChangedListener { model.updatePendingText(it.toString()) }
        view.findViewById<View>(R.id.button_add_item).setOnClickListener { Snackbar.make(view, if (model.addPendingText()) "Elemento agregado a la colección" else "Escribe al menos 3 caracteres", Snackbar.LENGTH_SHORT).show() }
    }
}
