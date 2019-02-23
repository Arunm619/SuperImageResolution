package com.intelligentdream.superimageresolution.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
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

        Glide.with(mContext).load(img.original).into(holder.iv_original)

        //Toast.makeText(mContext, "link is ${img.Superimg}", Toast.LENGTH_LONG).show()

        holder.iv_super.setBackgroundResource(R.drawable.processing);

        /* if (!img.Superimg.equals("link to super image"))
             Glide.with(mContext).load(img.Superimg).into(holder.iv_super)
         else
         {
            // Glide.with(mContext).load().into(holder.iv_super)
         }*/

        holder.setItemClickListener(object : ItemClickListener {
            override fun onClick(view: View, position: Int, isLongClick: Boolean) {
                if (!isLongClick) {

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
