package com.gusslinaresv.catalogoui.views

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fun open(id: Int, section: CatalogSection) { view.findViewById<View>(id).setOnClickListener { (activity as MainActivity).showSection(section) } }
        open(R.id.card_text, CatalogSection.TEXT)
        open(R.id.card_actions, CatalogSection.ACTIONS)
        open(R.id.card_selection, CatalogSection.SELECTION)
        open(R.id.card_lists, CatalogSection.LISTS)
        open(R.id.card_feedback, CatalogSection.FEEDBACK)
        open(R.id.card_layout, CatalogSection.LAYOUT)
    }
}
