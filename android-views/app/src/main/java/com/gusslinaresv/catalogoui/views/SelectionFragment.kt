package com.gusslinaresv.catalogoui.views
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.slider.RangeSlider
import com.google.android.material.slider.Slider
import java.util.Calendar
import com.google.android.material.checkbox.MaterialCheckBox

class SelectionFragment : Fragment(R.layout.fragment_selection) {
 override fun onViewCreated(v: View, s: Bundle?) { val c=Calendar.getInstance(); val out=v.findViewById<TextView>(R.id.date_time_value)
  v.findViewById<MaterialCheckBox>(R.id.tri_checkbox).apply { checkedState=MaterialCheckBox.STATE_INDETERMINATE; setOnClickListener { checkedState=MaterialCheckBox.STATE_CHECKED } }
  v.findViewById<Spinner>(R.id.selection_spinner).adapter=ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,listOf("Sistema","Claro","Oscuro"))
  v.findViewById<Slider>(R.id.slider).addOnChangeListener { _,x,_ -> v.findViewById<TextView>(R.id.slider_value).text="Valor: ${x.toInt()}" }
  v.findViewById<RangeSlider>(R.id.range_slider).apply { values=listOf(20f,75f); addOnChangeListener { r,_,_ -> v.findViewById<TextView>(R.id.range_value).text="Rango: ${r.values[0].toInt()} a ${r.values[1].toInt()}" } }
  v.findViewById<View>(R.id.date_button).setOnClickListener { DatePickerDialog(requireContext(),{_,y,m,d->out.text="$d/${m+1}/$y"},c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show() }
  v.findViewById<View>(R.id.time_button).setOnClickListener { TimePickerDialog(requireContext(),{_,h,m->out.text="%02d:%02d".format(h,m)},c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show() }
 }
}
