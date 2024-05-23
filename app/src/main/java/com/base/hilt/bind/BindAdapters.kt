package com.base.hilt.bind

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.base.hilt.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import java.io.File

/**
 * Bind data used for data binding
 */
class BindAdapters {
    companion object {

        /**
         * Note: Do not use prefix if possible
         * The warning occurs if you use any namespace prefix,
         * regardless of whether it matches the one used in your layout or not. If you omit the namespace, like so:
         */
   /*     @BindingAdapter(value = ["android:vendor_items_list"], requireAll = false)
        @JvmStatic
        fun bindVendorItemList(
            view: RecyclerView,
            subscription_meal_items_list: ArrayList<Vendor>?
        ) {
            if (subscription_meal_items_list != null) {
                val adapter = object :
                    GenericRecyclerViewAdapter<Vendor, RowItemHomeListBinding>(
                        view.context,
                        subscription_meal_items_list
                    ) {
                    override val layoutResId: Int
                        get() = R.layout.row_item_home_list

                    override fun onBindData(
                        model: Vendor,
                        position: Int,
                        dataBinding: RowItemHomeListBinding
                    ) {
                        dataBinding.model = model
                        dataBinding.executePendingBindings()
                    }

                    override fun onItemClick(model: Vendor, position: Int) {

                    }
                }
                view.adapter = adapter
            }
        }*/


        /**
         * Load Image in imageview using Glide
         * @param imageView AppCompatImageView imageview object
         * @param url String? URL
         */
        @SuppressLint("CheckResult")
        @BindingAdapter(value = ["imageSet", "placeHolder"], requireAll = false)
        @JvmStatic
        fun bindImageData(imageView: AppCompatImageView, url: String?, placeHolder: Drawable?) {
            if (url == null || url.isEmpty()) {
                if (placeHolder != null) {
                    imageView.setImageDrawable(placeHolder)
                } else {
                    imageView.setImageResource(R.drawable.ic_launcher_background)
                }
                return
            } else {
                var placeHolder1: Drawable =
                    ContextCompat.getDrawable(
                        imageView.context,
                        R.drawable.ic_launcher_background
                    )!!
                if (placeHolder != null)
                    placeHolder1 = placeHolder
                val requestOptions = RequestOptions()
                requestOptions.placeholder(placeHolder1)
                requestOptions.error(placeHolder1)
                requestOptions.dontAnimate()
                requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                when {
                    url.contains("https://") || url.contains("http://") -> {
                        Glide.with(imageView.context).setDefaultRequestOptions(requestOptions)
                            .load(url)
                            .into(imageView)
                    }
                    url.contains("R.drawable") -> {
                        Glide.with(imageView.context).setDefaultRequestOptions(requestOptions).load(
                            imageView.context.resources.getIdentifier(
                                url.replace(
                                    "R.drawable.",
                                    ""
                                ), "drawable", imageView.context.packageName
                            )
                        )
                            .into(imageView)
                    }
                    else -> {
                        Glide.with(imageView.context).setDefaultRequestOptions(requestOptions)
                            .load(File(url)).into(imageView)
                    }
                }
            }
        }

    }
}
