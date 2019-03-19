package com.intelligentdream.superimageresolution.Adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.intelligentdream.superimageresolution.Activity.ViewPictureActivity
import com.intelligentdream.superimageresolution.Interface.ItemClickListener
import com.intelligentdream.superimageresolution.Model.Image
import com.intelligentdream.superimageresolution.R

class HistoryAdapter(var list: MutableList<Image>, private val mContext: Context) :
    RecyclerView.Adapter<HistoryViewHolder>() {
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): HistoryViewHolder {

        val itemView = mLayoutInflater.inflate(R.layout.list_row_history_item, parent, false)
        return HistoryViewHolder(itemView = itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {

        val img = list[position]

        //loading original image with first loader and then using glide
        holder.iv_original.setBackgroundResource(R.drawable.loadingoriginal)
        holder.iv_super.setBackgroundResource(R.drawable.processing)



        Glide.with(mContext).load(img.original).into(holder.iv_original)
        if (img.Superimg != mContext.getString(R.string.dummylink)) {
            holder.iv_super.setBackgroundResource(0)
            Glide.with(mContext).load(img.Superimg).into(holder.iv_super)

        }

        holder.setItemClickListener(object : ItemClickListener {
            override fun onClick(view: View, position: Int, isLongClick: Boolean) {
                if (!isLongClick) {

                    val img: Image = list[position]
                    val intent = Intent(mContext, ViewPictureActivity::class.java)
                    intent.putExtra(mContext.getString(R.string.KEY_ORIGINAL_LINK), img.original)
                    intent.putExtra(mContext.getString(R.string.KEY_SUPER_LINK), img.Superimg)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    mContext.startActivity(intent)

                }
            }

        })

    }
}

class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener,
    View.OnLongClickListener {
    override fun onClick(v: View?) {
        itemClickListener!!.onClick(v!!, adapterPosition, false)

    }

    override fun onLongClick(v: View?): Boolean {
        itemClickListener!!.onClick(v!!, adapterPosition, true)
        return true
    }

    fun setItemClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }


    var iv_super: ImageView
    var iv_original: ImageView

    private var itemClickListener: ItemClickListener? = null


    init {
        iv_original = itemView.findViewById(R.id.iv_original)
        iv_super = itemView.findViewById(R.id.iv_super)

        itemView.setOnClickListener(this)
        itemView.setOnLongClickListener(this)
    }

}
