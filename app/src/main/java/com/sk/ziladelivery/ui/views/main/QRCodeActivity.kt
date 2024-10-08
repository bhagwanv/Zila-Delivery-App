package com.sk.ziladelivery.ui.views.main

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sk.ziladelivery.R
import com.sk.ziladelivery.data.api.ApiHelper
import com.sk.ziladelivery.data.api.RestClient
import com.sk.ziladelivery.data.model.GenerateDeliveryQRCodeReqModel
import com.sk.ziladelivery.data.model.GenerateDeliveryQRCodeResModel
import com.sk.ziladelivery.data.model.QRCodeModel
import com.sk.ziladelivery.data.model.QRCodeResponceModel
import com.sk.ziladelivery.data.model.QRCodeResquestModel
import com.sk.ziladelivery.databinding.ActivityQrCodeBinding
import com.sk.ziladelivery.databinding.PopupQrCodePaymentBinding
import com.sk.ziladelivery.ui.views.viewmodels.QRCodeViewModel
import com.sk.ziladelivery.utilities.*
import com.sk.ziladelivery.viewfactory.QRCodeFactory
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver

class QRCodeActivity : AppCompatActivity(), View.OnClickListener {
    private var mBinding: ActivityQrCodeBinding? = null
    private var qrCodeViewModel: QRCodeViewModel? = null
    private var OrderIdList: List<Int>? = null
    private var sCaseAmount:Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityQrCodeBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        OrderIdList = intent.getSerializableExtra("OrderID") as List<Int>?
        sCaseAmount = intent.getDoubleExtra("sCaseAmount", 0.0)

        notificationEvent
        initView()
    }

    private fun initView() {
        qrCodeViewModel = ViewModelProviders.of(this, QRCodeFactory(ApiHelper(RestClient.getInstance().service))).get(QRCodeViewModel::class.java)
        mBinding!!.ivBack.setOnClickListener(this)
        qrCodeViewModel = ViewModelProviders.of(this)[QRCodeViewModel::class.java]
        val userID: Int = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.PEOPLE_ID)
        mBinding!!.UserName.text=SharePrefs.getInstance(applicationContext).getString(SharePrefs.PEAOPLE_FIRST_NAME)
        //getQRCodeApi(QRCodeResquestModel(OrderIdList,userID,sCaseAmount))

        //newQR
        getQRCodeApi(GenerateDeliveryQRCodeReqModel(OrderIdList!!.get(0),sCaseAmount,SharePrefs.getInstance(this).getInt(SharePrefs.PEOPLE_ID)))

        mBinding!!.btTransctinStaus.setOnClickListener {
            checkStatusMethod()
        }

    }

    private fun checkStatusMethod() {
        if (!Utils.checkInternetConnection(applicationContext)) {
            Toast.makeText(
                applicationContext,
                resources.getString(R.string.network_error),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            var orderId =0
            for (index in OrderIdList!!) {
                orderId =index
                break
            }
            checkDeliveryResponse(orderId,sCaseAmount)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(
            Intent(
                this@QRCodeActivity,
                CollectPaymentActivity::class.java
            ).putExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,SharePrefs.getInstance(this).getInt(SharePrefs.TRIPPLANERMASTERID))
        )
        finish()
    }

   /* private fun getQRCodeApi(model: QRCodeResquestModel) {
        qrCodeViewModel!!.getQrCode(model)
            .observe(this@QRCodeActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { qrCodeData ->
                                getQRCodeData(qrCodeData)
                            }
                        }
                        Status.ERROR -> {
                            Utils.setToast(applicationContext, it.message)
                        }
                        Status.LOADING -> {

                        }
                    }
                }
            }


    }*/

   /* private fun getCheckTransactionStatus(orderId:Int) {
        qrCodeViewModel!!.getCheckTransactionStatus(orderId)
            .observe(this@QRCodeActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { CheckTransactionStatus ->
                                getCheckTransaction(CheckTransactionStatus)
                            }
                        }
                        Status.ERROR -> {
                            Utils.setToast(applicationContext, it.message)
                        }
                        Status.LOADING -> {

                        }
                    }
                }
            }


    }*/


    //NewQR
     private fun getQRCodeApi(model: GenerateDeliveryQRCodeReqModel) {
        qrCodeViewModel!!.getQrCode(model)
            .observe(this@QRCodeActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { qrCodeData ->
                                getQRCodeData(qrCodeData)
                            }
                        }
                        Status.ERROR -> {
                            Utils.setToast(applicationContext, it.message)
                        }
                        Status.LOADING -> {

                        }
                    }
                }
            }


    }

     private fun checkDeliveryResponse(OrderId: Int,amount: Double) {
         qrCodeViewModel!!.checkDeliveryResponse(OrderId,amount)
             .observe(this@QRCodeActivity) {
                 it?.let { resource ->
                     when (resource.status) {
                         Status.SUCCESS -> {
                             resource.data?.let { CheckTransactionStatus ->
                                 getCheckTransaction(CheckTransactionStatus)
                             }
                         }
                         Status.ERROR -> {
                             Utils.setToast(applicationContext, it.message)
                         }
                         Status.LOADING -> {

                         }
                     }
                 }
             }


     }

    private fun getCheckTransaction(CheckTransactionStatus: JsonObject) {

        if (CheckTransactionStatus.asJsonObject["IsSuccess"].asBoolean) {
           // Utils.setToast(this, CheckTransactionStatus.asJsonObject["Message"].asString)
            Utils.setToast(this, "Payment Success!")
            startActivity(
                Intent(
                    this@QRCodeActivity,
                    CollectPaymentActivity::class.java
                ).putExtra(Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,SharePrefs.getInstance(this).getInt(SharePrefs.TRIPPLANERMASTERID))
            )
            finish()
        } else {

           //Utils.setToast(this, CheckTransactionStatus.asJsonObject["Message"].asString)
            Utils.setToast(this, "Payment is Awating!!")
        }
    }

    private fun getQRCodeData(qrCodeData: JsonObject) {
        println(qrCodeData.toString())
        val qrCodeModel: GenerateDeliveryQRCodeResModel = Gson().fromJson(qrCodeData.toString(), GenerateDeliveryQRCodeResModel::class.java)
        if (qrCodeModel.Status!!) {
            Picasso.get().load(qrCodeModel.QRCodeurl).into(mBinding!!.ivScanQrCode, object : Callback {
                    override fun onSuccess() {
                        if (mBinding!!.progressBar != null) {
                            mBinding!!.progressBar.visibility = View.GONE
                            mBinding!!.tvAmount.text = "Amount:  ₹${qrCodeModel.Amount}"
                            mBinding!!.tvOrderId.text = "Order ID : ${qrCodeModel.OrderId}"
                        }
                    }

                    override fun onError(e: Exception) {}
                })
            mBinding!!.btTransctinStaus.visibility=View.VISIBLE
        } else {
            Toast.makeText(this, qrCodeModel.msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_back -> {
                onBackPressed()
            }
        }
    }

    private val notificationEvent: Unit
        get() {
            RxBus.getInstance().event.observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DisposableObserver<Any?>() {
                    override fun onNext(o: Any) {
                        if (o is String) {
                            val TxnNo = o
                            if (!TextUtils.isNullOrEmpty(TxnNo)) {
                                deliveryTransactionDetail(TxnNo)
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                    override fun onComplete() {}
                })
        }

   /* private fun callTransactionDetail(TnXNo: String) {
        qrCodeViewModel!!.callTransactionDetail(TnXNo)
            .observe(this@QRCodeActivity) {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            resource.data?.let { qrCodeData ->
                                getQRCodeDataDetail(qrCodeData)
                            }
                        }
                        Status.ERROR -> {
                            Utils.setToast(applicationContext, it.message)
                        }
                        Status.LOADING -> {

                        }
                    }
                }
            }
    }
*/
    //NewQR
     private fun deliveryTransactionDetail(UPITxnID: String) {
      qrCodeViewModel!!.deliveryTransactionDetail(UPITxnID)
          .observe(this@QRCodeActivity) {
              it?.let { resource ->
                  when (resource.status) {
                      Status.SUCCESS -> {
                          resource.data?.let { qrCodeData ->
                              getQRCodeDataDetail(qrCodeData)
                          }
                      }
                      Status.ERROR -> {
                          Utils.setToast(applicationContext, it.message)
                      }
                      Status.LOADING -> {

                      }
                  }
              }
          }
  }

    private fun getQRCodeDataDetail(qrCodeData: JsonObject) {
        val qRCodeResponceModel: QRCodeResponceModel =
            Gson().fromJson(qrCodeData.toString(), QRCodeResponceModel::class.java)
        showPopUp(qRCodeResponceModel)

    }


    private fun showPopUp(qrCodeModel: QRCodeResponceModel) {
        val inflater = layoutInflater
        val popupQrCodePaymentBinding: PopupQrCodePaymentBinding = DataBindingUtil.inflate(inflater, R.layout.popup_qr_code_payment, null, false)
        val customDialog = Dialog(this, R.style.CustomDialog)
        customDialog.setCancelable(false)
        customDialog.setContentView(popupQrCodePaymentBinding.root)
        if (qrCodeModel.txnStatus.equals("SUCCESS")) {
            popupQrCodePaymentBinding.tvPaymentStatus.text = "Your payment has been " + qrCodeModel.txnStatus
            popupQrCodePaymentBinding.ivpaymentType.background = resources.getDrawable(R.drawable.ic_check_green)
            popupQrCodePaymentBinding.tvTittle.text="Thank You"
            popupQrCodePaymentBinding.tvTittle.setTextColor(resources.getColor(R.color.green));
        } else {
            popupQrCodePaymentBinding.tvPaymentStatus.text = "Your payment has been " + qrCodeModel.txnStatus
            popupQrCodePaymentBinding.ivpaymentType.background = resources.getDrawable(R.drawable.cross_button)
            popupQrCodePaymentBinding.tvTittle.text="Error"
            popupQrCodePaymentBinding.tvTittle.setTextColor(resources.getColor(R.color.red));
        }
        popupQrCodePaymentBinding.paymentAmount.text = " ₹ " + qrCodeModel.txnAmount
        popupQrCodePaymentBinding.payRefrence.text = "" + qrCodeModel.upiTxnID
        popupQrCodePaymentBinding.paymentTime.text = Utils.getDateFormat(qrCodeModel.txnDate)
        popupQrCodePaymentBinding.btCloseDialog.setOnClickListener {
            customDialog.dismiss()
            startActivity(
                Intent(this@QRCodeActivity,
                    CollectPaymentActivity::class.java
                ).putExtra(
                    Constant.TRIP_PLANNER_CONFIRMED_DETAIL_Id,
                    SharePrefs.getInstance(applicationContext).getInt(SharePrefs.TRIPPLANERMASTERID)
                )
            )
            finish()

        }
        customDialog.show()
    }
}