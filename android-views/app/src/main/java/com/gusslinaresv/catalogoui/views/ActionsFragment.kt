package com.gusslinaresv.catalogoui.views

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

class ActionsFragment : Fragment(R.layout.fragment_actions) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fun feedback(message: String) = Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        listOf(R.id.filled_button, R.id.outlined_button, R.id.text_button, R.id.icon_button, R.id.icon_text_button, R.id.fab, R.id.extended_fab).forEach { id -> view.findViewById<View>(id).setOnClickListener { feedback("Acción ejecutada") } }
        view.findViewById<View>(R.id.action_switch).setOnClickListener { feedback("Alternancia modificada") }
        view.findViewById<View>(R.id.loading_button).setOnClickListener { view.findViewById<ProgressBar>(R.id.loading_progress).visibility = View.VISIBLE; Toast.makeText(requireContext(), "Acción en curso", Toast.LENGTH_SHORT).show() }
    }
}
