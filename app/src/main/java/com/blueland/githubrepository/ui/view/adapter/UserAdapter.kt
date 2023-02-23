package com.blueland.githubrepository.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueland.githubrepository.databinding.ItemUserBinding
import com.blueland.githubrepository.data.model.UserEntity
import com.blueland.githubrepository.ui.view.adapter.holder.UserHolder

class UserAdapter(
    private val itemOnClickListener: ItemOnClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: MutableList<UserEntity> = arrayListOf()

    fun setUserList(items: ArrayList<UserEntity>) {
        this.items = items
        notifyDataSetChanged()
    }

    interface ItemOnClickListener {
        fun itemOnClick(pos: Int, item: UserEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserHolder(ItemUserBinding.inflate(inflater, parent, false), itemOnClickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserHolder -> holder.bind(position, items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}