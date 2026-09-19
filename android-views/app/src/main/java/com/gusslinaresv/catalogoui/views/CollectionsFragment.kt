package com.gusslinaresv.catalogoui.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CollectionsFragment : Fragment(R.layout.fragment_collections) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val model = ViewModelProvider(requireActivity())[CatalogViewModel::class.java]
        val pagerAdapter = CollectionPagerAdapter { item -> Snackbar.make(view, "Detalle: ${item.title}", Snackbar.LENGTH_SHORT).show() }
        view.findViewById<ViewPager2>(R.id.list_pager).adapter = pagerAdapter
        TabLayoutMediator(view.findViewById<TabLayout>(R.id.list_tabs), view.findViewById(R.id.list_pager)) { tab, position -> tab.text = if (position == 0) "Lista" else "Cuadrícula" }.attach()
        model.items.observe(viewLifecycleOwner) { items -> pagerAdapter.submit(items); view.findViewById<View>(R.id.empty_state).visibility = if (items.isEmpty()) View.VISIBLE else View.GONE }
        val refresh = view.findViewById<androidx.swiperefreshlayout.widget.SwipeRefreshLayout>(R.id.swipe_refresh)
        refresh.setOnRefreshListener { model.reset(); refresh.isRefreshing = false }
        view.findViewById<View>(R.id.clear_button).setOnClickListener { model.clear() }
        view.findViewById<View>(R.id.restore_button).setOnClickListener { model.reset() }
        pagerAdapter.onRemove = model::remove
    }
}

class CollectionPagerAdapter(private val click: (CatalogItem) -> Unit) : RecyclerView.Adapter<CollectionPagerAdapter.PageHolder>() {
    private var items = emptyList<CatalogItem>()
    var onRemove: (Int) -> Unit = {}
    private val adapters = mutableListOf<CatalogAdapter>()
    fun submit(value: List<CatalogItem>) { items = value; adapters.forEach { it.submit(value) } }
    override fun getItemCount() = 2
    override fun getItemViewType(position: Int) = position
    override fun onCreateViewHolder(parent: ViewGroup, type: Int): PageHolder {
        val recycler = RecyclerView(parent.context).apply { layoutParams = ViewGroup.LayoutParams(-1, -1); setPadding(12, 12, 12, 12) }
        val adapter = CatalogAdapter(click).also { it.submit(items) }
        recycler.adapter = adapter
        if (type == 0) recycler.layoutManager = LinearLayoutManager(parent.context) else recycler.layoutManager = GridLayoutManager(parent.context, 2).also { grid -> grid.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() { override fun getSpanSize(position: Int) = if (adapter.isHeader(position)) 2 else 1 } }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) { override fun onMove(r: RecyclerView, h: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false; override fun onSwiped(h: RecyclerView.ViewHolder, d: Int) { adapter.itemAt(h.bindingAdapterPosition)?.let { onRemove(it.id) } ?: adapter.notifyItemChanged(h.bindingAdapterPosition) } }).attachToRecyclerView(recycler)
        adapters += adapter
        return PageHolder(recycler)
    }
    override fun onBindViewHolder(holder: PageHolder, position: Int) = Unit
    class PageHolder(view: View) : RecyclerView.ViewHolder(view)
}

sealed interface CollectionRow { data class Header(val title: String) : CollectionRow; data class Item(val value: CatalogItem) : CollectionRow }
class CatalogAdapter(private val click: (CatalogItem) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var data = emptyList<CollectionRow>()
    fun submit(items: List<CatalogItem>) { data = items.groupBy { it.category }.flatMap { (category, entries) -> listOf(CollectionRow.Header(category)) + entries.map { CollectionRow.Item(it) } }; notifyDataSetChanged() }
    fun isHeader(position: Int) = data.getOrNull(position) is CollectionRow.Header
    fun itemAt(position: Int) = (data.getOrNull(position) as? CollectionRow.Item)?.value
    override fun getItemCount() = data.size
    override fun getItemViewType(position: Int) = if (isHeader(position)) 0 else 1
    override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder = if (type == 0) HeaderHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_header, parent, false)) else ItemHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_catalog, parent, false))
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) { when (val row = data[position]) { is CollectionRow.Header -> (holder as HeaderHolder).bind(row); is CollectionRow.Item -> (holder as ItemHolder).bind(row.value) } }
    class HeaderHolder(view: View) : RecyclerView.ViewHolder(view) { fun bind(row: CollectionRow.Header) { itemView.findViewById<TextView>(R.id.header_title).text = row.title } }
    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) { fun bind(item: CatalogItem) { itemView.findViewById<TextView>(R.id.item_title).text = item.title; itemView.findViewById<TextView>(R.id.item_category).text = item.category; itemView.setOnClickListener { click(item) } } }
}
