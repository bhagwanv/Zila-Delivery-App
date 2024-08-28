package com.sk.ziladelivery.ui.views.fragment.unloadReturnItem

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sk.ziladelivery.R
import com.sk.ziladelivery.databinding.ItemUnloadReturnBinding
import java.util.*

class UnloadItemReturnAdapter(
    private val context: Activity,
    private var returnItemList: ArrayList<ReturnItemListResponseModel>,
    private val unloadItemReturnInterface: UnloadItemReturnActivity,
) : RecyclerView.Adapter<UnloadItemReturnAdapter.ViewHolder>(), Filterable {

    private var layoutInflater: LayoutInflater? = null
    var unloadItemListFilter: ArrayList<ReturnItemListResponseModel> = returnItemList

    fun updateList(list: ArrayList<ReturnItemListResponseModel>) {
        returnItemList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.context)
        }
        return ViewHolder(
            DataBindingUtil.inflate(
                layoutInflater!!, R.layout.item_unload_return, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = returnItemList[position]
        holder.mBinding.tvItemName.text = model.Itemname
        holder.mBinding.tvQty.text = model.Qty.toString()
        holder.mBinding.etEditQuntity.setText(model.returnQty.toString())
        holder.mBinding.tvBachCode.setText(model.BatchCode)

        if (model.isChecked) {
            holder.mBinding.itemCheckBox.isChecked = true
            holder.mBinding.etEditQuntity.isEnabled = false
            holder.mBinding.etEditQuntity.isClickable = false
        } else {
            holder.mBinding.itemCheckBox.isChecked = false
            holder.mBinding.etEditQuntity.isEnabled = true
            holder.mBinding.etEditQuntity.isClickable = true
            holder.mBinding.etEditQuntity.setText("")
        }

        if (!model.ImageURlmessage.isNullOrEmpty()) {
            if (model.isChecked && model.returnQty>0){
                holder.mBinding.tvImageUrl.visibility=View.VISIBLE
                holder.mBinding.tvImageUrl.setTextColor(context.resources.getColor(R.color.green))
                holder.mBinding.tvImageUrl.setText(model.ImageURlmessage)
            }else{
                holder.mBinding.tvImageUrl.visibility=View.GONE
                model.ImageURlmessage=""
            }

        }else{
            holder.mBinding.tvImageUrl.visibility=View.GONE
        }

        holder.mBinding.itemCheckBox.setOnClickListener {
            val requestQty = holder.mBinding.etEditQuntity.text.toString()
            if (requestQty.isNullOrEmpty()) {
                holder.mBinding.itemCheckBox.isChecked = false
                Toast.makeText(context, "Please Enter Quantity", Toast.LENGTH_SHORT).show()
            } else if (model.Qty < requestQty.toInt()) {
                Toast.makeText(context, "Please Enter valid Quantity", Toast.LENGTH_SHORT).show()
                holder.mBinding.itemCheckBox.isChecked = false
            } else {
                if (holder.mBinding.itemCheckBox.isChecked) {
                    model.returnQty = requestQty.toInt()
                    model.isChecked = true
                } else {
                    model.isChecked = false
                }

            }
            if (model.isChecked){
                if (requestQty.toInt()!=0){
                    unloadItemReturnInterface.llCamera(model,true)
                    unloadItemReturnInterface.checkBoxClick(
                        true,
                        model,
                        holder.adapterPosition,
                    )

                }else{
                   // model.isChecked = false
                   // model.returnQty=0
                    unloadItemReturnInterface.checkBoxClick(
                        true,
                        model,
                        holder.adapterPosition,
                    )
                    //Toast.makeText(context, "Please enter item Quantity greater than 0", Toast.LENGTH_SHORT).show()
                }

            }else{
               // model.returnQty=0
                unloadItemReturnInterface.checkBoxClick(
                    false,
                    model,
                    holder.adapterPosition,
                )
            }
        }

    }


    override fun getItemCount(): Int {
        return returnItemList.size
    }

    inner class ViewHolder(var mBinding: ItemUnloadReturnBinding) : RecyclerView.ViewHolder(
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
                    val resultData: MutableList<ReturnItemListResponseModel> = ArrayList()
                    for (userModel in unloadItemListFilter) {
                        if (userModel.Itemname!!.lowercase(Locale.getDefault())
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
                returnItemList = filterResults.values as ArrayList<ReturnItemListResponseModel>
                notifyDataSetChanged()
            }
        }
    }
}