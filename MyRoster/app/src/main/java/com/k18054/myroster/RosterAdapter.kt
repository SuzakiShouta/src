package com.k18054.myroster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import java.time.Year
import java.util.*
import java.util.Calendar.*

class RosterAdapter(data: OrderedRealmCollection<Roster>) : RealmRecyclerViewAdapter<Roster, RosterAdapter.ViewHolder>(data, true){

    private var listener: ((Long?) -> Unit)? = null

    //高階関数 コールバック　FirstFragmentにlistenerが飛ぶ
    fun setOnItemClickListener(listener:(Long?) -> Unit ) {
        this.listener = listener
    }

    init {
        //更新されたデータの特定1
        setHasStableIds(true)
    }

    class ViewHolder(cell: View) : RecyclerView.ViewHolder(cell) {
        // onCreateViewHolderでsimple_list_item_2を選択している。
        val firstName: TextView = cell.findViewById(android.R.id.text1)
        val lastName: TextView = cell.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RosterAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RosterAdapter.ViewHolder, position: Int) {
        val roster: Roster? = getItem(position)
        val calender: Calendar = Calendar.getInstance().apply {
            time = roster?.birthday
        }
        holder.firstName.text = roster?.firstName.plus(roster?.lastName)
        //理由がわからないがMONTHが-1されている。
        holder.lastName.text = calender.get(YEAR).toString().plus("/")
                .plus(calender.get(MONTH)+1).plus("/")
                .plus(calender.get(DAY_OF_MONTH))
        holder.itemView.setOnClickListener {
            listener?.invoke(roster?.id)
        }
    }

    //更新されたデータの特定2
    override fun getItemId(position: Int): Long {
        return getItem(position)?.id ?: 0
    }

}