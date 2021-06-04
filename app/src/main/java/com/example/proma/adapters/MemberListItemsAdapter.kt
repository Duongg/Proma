package com.example.proma.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proma.R
import com.example.proma.models.User
import com.example.proma.utils.Constants
import kotlinx.android.synthetic.main.item_member.view.*

open class MemberListItemsAdapter(
    private val context: Context,
    private val list: ArrayList<User>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_member, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder) {
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(holder.itemView.iv_member_image)
            holder.itemView.tv_member_name.text = model.name
            holder.itemView.tv_member_email.text = model.email

            if(model.isSelected){
                holder.itemView.iv_selected_member.visibility = View.VISIBLE
            }else{
                holder.itemView.iv_selected_member.visibility = View.GONE
            }

            holder.itemView.setOnClickListener {
                if(onClickListener != null){
                    if(model.isSelected){
                        onClickListener!!.onClick(position, model, Constants.UN_SELECT)
                    }else{
                        onClickListener!!.onClick(position, model, Constants.SELECT)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnclickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    interface OnClickListener{
        fun onClick(position: Int, user: User, action: String)
    }

}