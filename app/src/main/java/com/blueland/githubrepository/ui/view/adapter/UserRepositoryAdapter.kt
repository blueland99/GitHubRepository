package com.blueland.githubrepository.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blueland.githubrepository.databinding.ItemUserRepositoryBinding
import com.blueland.githubrepository.data.model.UserRepositoryEntity
import com.blueland.githubrepository.ui.view.adapter.holder.UserRepositoryHolder

class UserRepositoryAdapter(
    private val itemOnClickListener: ItemOnClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: MutableList<UserRepositoryEntity> = arrayListOf()

    fun setRepositoryList(items: ArrayList<UserRepositoryEntity>) {
        this.items = items
        notifyDataSetChanged()
    }

    interface ItemOnClickListener {
        fun itemOnClick(pos: Int, item: UserRepositoryEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return UserRepositoryHolder(ItemUserRepositoryBinding.inflate(inflater, parent, false), itemOnClickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserRepositoryHolder -> holder.bind(position, items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}