package com.sk.ziladelivery.ui.views.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sk.ziladelivery.data.apprepository.AppRepository
import com.sk.ziladelivery.utilities.Resource

import kotlinx.coroutines.Dispatchers

class SplashViewModel(private val appRepository: AppRepository) : ViewModel() {

    fun getVersion() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getVersion()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun getCompanyDetails() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getCompanyDetails()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getUserActivie(mobileNo: String, type: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = appRepository.getUserActivie(mobileNo, type)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    /* private var responseLiveData: MutableLiveData<ApiResponseList>? = null
     private var companyDetailLiveData: MutableLiveData<ApiResponse>? = null
     private val disposables = CompositeDisposable()
     fun versionResponse(): LiveData<ApiResponseList> {
         if (responseLiveData == null) {
             responseLiveData = MutableLiveData()
         }
         return responseLiveData!!
     }

     val companyDetail: LiveData<ApiResponse>
         get() {
             if (companyDetailLiveData == null) {
                 companyDetailLiveData = MutableLiveData()
             }
             return companyDetailLiveData!!
         }

     // Version API calling
     fun hitVersionApi() {
         disposables.add(RestClient.getInstance().service.doSplashVersion()
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(
                 { result: JsonArray? ->
                     responseLiveData!!.setValue(
                         ApiResponseList.success(
                             result!!
                         )
                     )
                 }
             ) { throwable: Throwable ->
                 responseLiveData!!.value = ApiResponseList.error(throwable)
                 println("Error$throwable")
             })
     }

     fun hitgetCompanyinfo() {
         val observerDes: DisposableObserver<JsonElement> =
             RestClient.getInstance().service.companyInfo
                 .subscribeOn(Schedulers.io())
                 .doOnSubscribe { disposable: Disposable? ->
                     companyDetailLiveData!!.setValue(
                         ApiResponse.loading()
                     )
                 }
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribeWith(object : DisposableObserver<JsonElement?>() {
                     override fun onNext(result: JsonElement) {
                         companyDetailLiveData!!.setValue(ApiResponse.success(result))
                     }

                     override fun onError(throwable: Throwable) {
                         companyDetailLiveData!!.setValue(ApiResponse.error(throwable))
                     }

                     override fun onComplete() {}
                 })
         disposables.add(observerDes)
     }

     override fun onCleared() {
         disposables.clear()
     }*/
}