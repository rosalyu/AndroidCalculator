package com.example.calculator

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val context: Context, private val data: List<ThemeListItemData>) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvThemeName = itemView.findViewById<TextView>(R.id.tvThemeName)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter.ViewHolder {
        // create ViewHolder
        val view = LayoutInflater.from(parent.context).inflate(R.layout.theme_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {
        val item = data[position]
        holder.tvThemeName.text = item.themeName

        // set onClickListeners for all list elements
        if(context is MainActivity) {
            context.setListenerListItem(item, position)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}
