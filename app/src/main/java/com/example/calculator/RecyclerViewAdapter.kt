package com.example.calculator

import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val context: Context,
                          private val data: List<ThemeListItemData>,
    ) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    // variable that can hold a function that takes an Int as an argument and returns Unit.
    // It is null to that is can initialized later in MainActivity
    var onItemClickListener: ((Int) -> Unit)? = null

    // -> itemView is the list element view
    // -> View.OnClickListener is an interface from Android which needs the onClick() function to be
    // implemented, which is called when the view is clicked
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val tvThemeName = itemView.findViewById<TextView>(R.id.tvThemeName)

        var initialItemColor: Int? = null
        var selectedItemColor: Int? = null
        var lastSelectedItem: View? = null

        init {
            // this is of type View.OnClickListener because ViewHolder implements that interface
            itemView.setOnClickListener(this)

            // initializing initialItemColor
            var typedValue = TypedValue() // holds the value of the attribute we want to resolve
            context.theme.resolveAttribute(R.attr.backgroundColor, typedValue, true)
            initialItemColor = ContextCompat.getColor(context, typedValue.resourceId)

            // initializing selectedItemColor
            typedValue = TypedValue()
            // todo add a new attribute and color for each theme for a selected item of the theme list
            context.theme.resolveAttribute(R.attr.themeSelectedColor, typedValue, true)
            selectedItemColor = ContextCompat.getColor(context, typedValue.resourceId)
        }

        override fun onClick(v: View?) {
            // adapterPosition is the position of the item within the adapter
            val position = adapterPosition
            // tests if the adapter position is valid
            if (position != RecyclerView.NO_POSITION) {
                // reset the color of the last selected list item to the initial color when a new
                // item is selected
                if(lastSelectedItem != null) {
                    //Log.d("themes", "lastSelectedItem id: ${lastSelectedItem!!}")
                    lastSelectedItem!!.setBackgroundColor(initialItemColor!!)
                }
                // calls the lambda function with position as the Int argument
                // invoke() is used because onItemClickListener is nullable
                // -> does nothing if the function holder variable is null
                v?.setBackgroundColor(selectedItemColor!!)
                lastSelectedItem = v
                onItemClickListener?.invoke(position)

                // set the background of the items to another color when clicked
            }
        }

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

    }

    override fun getItemCount(): Int {
        return data.size
    }

}
