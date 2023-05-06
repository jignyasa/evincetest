package com.example.myapplication.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ItemUsersBinding
import com.example.myapplication.model.DataItem

class UserAdapter(val listener:UserAdapter.onItemClick) : Adapter<UserViewHolder>() {
    private var list = ArrayList<DataItem>()
    private lateinit var binding: ItemUsersBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_users,
            parent,
            false
        )
        return UserViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        var data = list.get(position)
        binding.tvEmail.text = data.email
        binding.tvName.text = data.firstName.plus("\t").plus(data.lastName)
        Glide.with(holder.itemView.context).load(data.avatar).placeholder(R.mipmap.ic_launcher)
            .into(binding.ivUser)
        binding.root.setOnClickListener {
            listener.itemClick(data)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addData(dataList:ArrayList<DataItem>){
        list=dataList
        notifyDataSetChanged()
    }

    interface onItemClick{
        fun itemClick(data:DataItem)
    }
}

class UserViewHolder(view: View) : ViewHolder(view)
