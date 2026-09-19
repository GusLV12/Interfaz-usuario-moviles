package com.gusslinaresv.catalogoui.views
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar

class FeedbackFragment:Fragment(R.layout.fragment_feedback){override fun onViewCreated(v:View,s:Bundle?){v.findViewById<ImageView>(R.id.remote_image).load("https://images.unsplash.com/photo-1551650975-87deedd944c3?auto=format&fit=crop&w=640&q=80")
v.findViewById<View>(R.id.toast_button).setOnClickListener{Toast.makeText(requireContext(),"Toast mostrado",Toast.LENGTH_SHORT).show()};v.findViewById<View>(R.id.snackbar_button).setOnClickListener{Snackbar.make(v,"Elemento guardado",Snackbar.LENGTH_SHORT).setAction("Deshacer"){}.show()};v.findViewById<View>(R.id.dialog_button).setOnClickListener{AlertDialog.Builder(requireContext()).setTitle("¿Confirmar acción?").setMessage("Esta demostración muestra un diálogo.").setPositiveButton("Aceptar",null).setNegativeButton("Cancelar",null).show()};v.findViewById<View>(R.id.sheet_button).setOnClickListener{BottomSheetDialog(requireContext()).apply{setContentView(TextView(requireContext()).apply{setPadding(48,48,48,48);text="Hoja inferior\nContenido contextual que se puede descartar."});show()}}}}
