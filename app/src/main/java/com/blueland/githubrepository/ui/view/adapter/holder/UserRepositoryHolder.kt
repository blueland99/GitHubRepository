package com.blueland.githubrepository.ui.view.adapter.holder

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.blueland.githubrepository.databinding.ItemUserRepositoryBinding
import com.blueland.githubrepository.data.model.UserRepositoryEntity
import com.blueland.githubrepository.ui.view.adapter.UserRepositoryAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget

class UserRepositoryHolder(
    private val binding: ItemUserRepositoryBinding,
    private val itemOnClickListener: UserRepositoryAdapter.ItemOnClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(pos: Int, item: UserRepositoryEntity) {
        binding.apply {
            tvFullName.text = item.fullName
            tvDescription.text = item.description ?: ""
            item.language.apply {
                tvLanguage.text = this
                ivLanguage.isGone = isNullOrBlank()
                tvLanguage.isGone = isNullOrBlank()
            }
            tvStar.text = item.stargazersCount.toString()
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