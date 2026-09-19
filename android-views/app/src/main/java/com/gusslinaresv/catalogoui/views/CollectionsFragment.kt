package com.gusslinaresv.catalogoui.views
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar

class CollectionsFragment : Fragment(R.layout.fragment_collections) {
 override fun onViewCreated(v: View,s: Bundle?) { val model=ViewModelProvider(requireActivity())[CatalogViewModel::class.java]; val rv=v.findViewById<RecyclerView>(R.id.recycler); val adapter=CatalogAdapter{Snackbar.make(v,"Detalle: ${it.title}",Snackbar.LENGTH_SHORT).show()}; rv.adapter=adapter; rv.layoutManager=LinearLayoutManager(requireContext())
  model.items.observe(viewLifecycleOwner){ items->adapter.submit(items); v.findViewById<View>(R.id.empty_state).visibility=if(items.isEmpty())View.VISIBLE else View.GONE }
  ItemTouchHelper(object:ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){override fun onMove(r:RecyclerView,h:RecyclerView.ViewHolder,t:RecyclerView.ViewHolder)=false;override fun onSwiped(h:RecyclerView.ViewHolder,d:Int){adapter.itemAt(h.bindingAdapterPosition)?.let { item -> model.remove(item.id) }}}).attachToRecyclerView(rv)
  val refresh=v.findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.swipe_refresh); refresh.setOnRefreshListener { model.reset(); refresh.isRefreshing=false }
  v.findViewById<View>(R.id.clear_button).setOnClickListener{model.clear()};v.findViewById<View>(R.id.restore_button).setOnClickListener{model.reset()}
  v.findViewById<com.google.android.material.tabs.TabLayout>(R.id.list_tabs).addOnTabSelectedListener(object:com.google.android.material.tabs.TabLayout.OnTabSelectedListener{override fun onTabSelected(t:com.google.android.material.tabs.TabLayout.Tab){rv.layoutManager=if(t.position==0)LinearLayoutManager(requireContext()) else GridLayoutManager(requireContext(),2)};override fun onTabUnselected(t:com.google.android.material.tabs.TabLayout.Tab){};override fun onTabReselected(t:com.google.android.material.tabs.TabLayout.Tab){}})
 }
}
class CatalogAdapter(val click:(CatalogItem)->Unit):RecyclerView.Adapter<CatalogAdapter.H>(){private var data=emptyList<CatalogItem>();fun submit(x:List<CatalogItem>){data=x;notifyDataSetChanged()};fun itemAt(p:Int)=data.getOrNull(p);override fun onCreateViewHolder(p:ViewGroup,t:Int)=H(LayoutInflater.from(p.context).inflate(R.layout.item_catalog,p,false));override fun getItemCount()=data.size;override fun onBindViewHolder(h:H,p:Int)=h.bind(data[p]);inner class H(v:View):RecyclerView.ViewHolder(v){fun bind(x:CatalogItem){itemView.findViewById<TextView>(R.id.item_title).text=x.title;itemView.findViewById<TextView>(R.id.item_category).text=x.category;itemView.setOnClickListener{click(x)}}}}
