package com.gusslinaresv.catalogoui.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.gusslinaresv.catalogoui.views.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) showSection(CatalogSection.HOME)
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            showSection(
                when (item.itemId) {
                    R.id.menu_text -> CatalogSection.TEXT
                    R.id.menu_selection -> CatalogSection.SELECTION
                    R.id.menu_lists -> CatalogSection.LISTS
                    R.id.menu_feedback -> CatalogSection.FEEDBACK
                    else -> CatalogSection.HOME
                }
            )
            true
        }
    }

    fun showSection(section: CatalogSection) {
        binding.toolbar.title = section.title
        binding.toolbar.navigationIcon = if (section == CatalogSection.HOME) null else getDrawable(android.R.drawable.ic_menu_revert)
        binding.toolbar.setNavigationOnClickListener { showSection(CatalogSection.HOME) }
        val fragment: Fragment = when (section) {
            CatalogSection.HOME -> HomeFragment()
            else -> PlaceholderFragment.newInstance(section)
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}

enum class CatalogSection(val title: String, val description: String) {
    HOME("Catálogo UI", "Explora componentes móviles y sus demostraciones interactivas."),
    TEXT("Entrada de texto", "Campos, validación, contraseñas, teclados y búsqueda."),
    ACTIONS("Botones y acciones", "Acciones con estados y respuestas visibles."),
    SELECTION("Elementos de selección", "Opciones, interruptores, rangos y selectores."),
    LISTS("Listas y colecciones", "Listas, cuadrículas, gestos y pestañas."),
    FEEDBACK("Información y retroalimentación", "Mensajes, progreso, imágenes y diálogos."),
    LAYOUT("Contenedores y estructura", "Filas, columnas, superposiciones y navegación.")
}

class PlaceholderFragment : Fragment(R.layout.fragment_placeholder) {
    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val section = CatalogSection.valueOf(requireArguments().getString("section")!!)
        view.findViewById<android.widget.TextView>(R.id.section_title).text = section.title
        view.findViewById<android.widget.TextView>(R.id.section_description).text = section.description
    }

    companion object {
        fun newInstance(section: CatalogSection) = PlaceholderFragment().apply {
            arguments = Bundle().apply { putString("section", section.name) }
        }
    }
}
