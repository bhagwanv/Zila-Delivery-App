package com.sk.ziladelivery.ui.views.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.model.UnloadItemListModel
import com.sk.ziladelivery.databinding.ItemUnloadBinding
import com.sk.ziladelivery.listener.UnloadItemInterface
import java.util.*

class UnloadItemAdapter(
    private val context: Activity,
    var unloadItemList: ArrayList<UnloadItemListModel>,
    private val unloadItemInterface: UnloadItemInterface
) : RecyclerView.Adapter<UnloadItemAdapter.ViewHolder>(), Filterable {
    private var layoutInflater: LayoutInflater? = null
    var unloadItemListFilter: ArrayList<UnloadItemListModel>

    init {
        unloadItemListFilter = unloadItemList
    }

    fun updateList(list: ArrayList<UnloadItemListModel>) {
        unloadItemList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        return ViewHolder(
            DataBindingUtil.inflate(
                layoutInflater!!, R.layout.item_unload, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val unloadItemListModel = unloadItemList[position]
        holder.mBinding.itemCheckBox.text = unloadItemListModel.itemname
        holder.mBinding.tvQty.text = unloadItemListModel.qty.toString()



        if (unloadItemListModel.isUnloaded) {
            holder.mBinding.itemCheckBox.isChecked = true
            holder.mBinding.tvItemName.setTextColor(context.resources.getColor(R.color.gray))
            holder.mBinding.tvQty.setTextColor(context.resources.getColor(R.color.gray))
        } else {
            holder.mBinding.itemCheckBox.isChecked = false
            holder.mBinding.tvItemName.setTextColor(context.resources.getColor(R.color.black))
            holder.mBinding.tvQty.setTextColor(context.resources.getColor(R.color.black))
        }

        //holder.mBinding.itemCheckBox.isChecked =unloadItemListModel.ischeck
        holder.mBinding.itemCheckBox.setOnClickListener {
            if (holder.mBinding.itemCheckBox.isChecked) {
                holder.mBinding.itemCheckBox.setTextColor(context.resources.getColor(R.color.gray))
                holder.mBinding.tvQty.setTextColor(context.resources.getColor(R.color.gray))
                unloadItemInterface.checkBoxClicked(
                    true,
                    unloadItemListModel,
                    holder.adapterPosition
                )
                // unloadItemList.get(getAdapterPosition()).setSelected(true);
            } else {
                holder.mBinding.itemCheckBox.setTextColor(context.resources.getColor(R.color.black))
                holder.mBinding.tvQty.setTextColor(context.resources.getColor(R.color.black))
                unloadItemInterface.checkBoxClicked(
                    false,
                    unloadItemListModel,
                    holder.adapterPosition
                )
                //unloadItemList.get(getAdapterPosition()).setSelected(false);
            }
        }
    }

    override fun getItemCount(): Int {
        return unloadItemList.size
    }

    inner class ViewHolder(var mBinding: ItemUnloadBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val filterResults = FilterResults()
                if ((charSequence == null) or (charSequence.length == 0)) {
                    filterResults.count = unloadItemListFilter.size
                    filterResults.values = unloadItemListFilter
                } else {
                    val searchChr =
                        charSequence.toString().lowercase(Locale.getDefault())
                    val resultData: MutableList<UnloadItemListModel> = ArrayList()
                    for (userModel in unloadItemListFilter) {
                        if (userModel.itemname!!.lowercase(Locale.getDefault())
                                .contains(searchChr)
                        ) {
                            resultData.add(userModel)
                        }
                    }
                    filterResults.count = resultData.size
                    filterResults.values = resultData
                }
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                unloadItemList = filterResults.values as ArrayList<UnloadItemListModel>
                notifyDataSetChanged()
            }
        }
    }
}