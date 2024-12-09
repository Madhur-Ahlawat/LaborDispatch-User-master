package com.UsreLaborDispatch1.www.sync.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.UsreLaborDispatch1.www.UsreLaborDispatch1.R
import com.UsreLaborDispatch1.www.sync.model.FileInfo
import com.UsreLaborDispatch1.www.sync.model.JobLog


class FilesAdapter : ListAdapter<FileInfo, FilesAdapter.MyViewHolder?> {

    //
    var listener: OnItemClickListener? = null

    inner class MyViewHolder(var main: View) : RecyclerView.ViewHolder(main) {
        var tvTitle: TextView


        init {
            tvTitle = main.findViewById(R.id.tvTitle)

        }
    }


    constructor() : super(DIFF_CALLBACK) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val word: FileInfo? = getItem(position)
        holder.tvTitle.setText(word?.getFileName())
        holder.main.setOnClickListener {
            listener?.OnItemClicked(word)
        }


    }

      fun getItemName(adapterPosition: Int): String {
        return getItem(adapterPosition).getFileName()
    }


    interface OnItemClickListener {
        fun OnItemClicked(file: FileInfo?)
    }

    fun setOnItemClickListener(listener: OnItemClickListener)
    {
        this.listener = listener
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<FileInfo?> =
            object : DiffUtil.ItemCallback<FileInfo?>() {
                override fun areItemsTheSame(
                    oldItem: FileInfo,
                    newItem: FileInfo
                ): Boolean {
                    return oldItem === newItem
                }

                override fun areContentsTheSame(
                    oldItem: FileInfo,
                    newItem: FileInfo
                ): Boolean {
                    return true
                }
            }
    }
}
