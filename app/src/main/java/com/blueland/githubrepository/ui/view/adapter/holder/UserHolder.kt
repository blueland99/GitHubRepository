package com.blueland.githubrepository.ui.view.adapter.holder

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView
import com.blueland.githubrepository.databinding.ItemUserBinding
import com.blueland.githubrepository.data.model.UserEntity
import com.blueland.githubrepository.ui.view.adapter.UserAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget

class UserHolder(
    private val binding: ItemUserBinding,
    private val itemOnClickListener: UserAdapter.ItemOnClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(pos: Int, item: UserEntity) {
        binding.apply {
            tvUser.text = item.login
            loadImage(ivProfile, item.avatarURL)
            itemView.setOnClickListener {
                itemOnClickListener.itemOnClick(pos, item)
            }
        }
    }

    private fun loadImage(imageView: ImageView, imageUrl: String?) {
        Glide.with(imageView.context)
            .asBitmap()
            .load(imageUrl)
            .centerCrop().into(object : BitmapImageViewTarget(imageView) {
                override fun setResource(resource: Bitmap?) {
                    val circularBitmapDrawable: RoundedBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.resources, resource)
                    circularBitmapDrawable.isCircular = true
                    imageView.setImageDrawable(circularBitmapDrawable)
                }
            })
    }
}