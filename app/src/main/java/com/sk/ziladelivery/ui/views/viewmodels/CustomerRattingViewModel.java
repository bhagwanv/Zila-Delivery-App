package com.sk.ziladelivery.ui.views.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sk.ziladelivery.data.api.RestClient;
import com.sk.ziladelivery.data.model.CustomerRattingModel;
import com.sk.ziladelivery.data.model.RatingModel;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CustomerRattingViewModel extends ViewModel {
    private MutableLiveData<CustomerRattingModel> listMutableLiveData;
    private MutableLiveData<Boolean> SubmitRattingLiveData;

    public MutableLiveData<CustomerRattingModel> getRatting(int id) {
        listMutableLiveData = null;
        listMutableLiveData = new MutableLiveData<>();

        callRattingApi(id);

        return listMutableLiveData;
    }

    public MutableLiveData<Boolean> getSubmitRattingLiveData(RatingModel model) {
        SubmitRattingLiveData = null;
        SubmitRattingLiveData = new MutableLiveData<>();

        insertRating(model);

        return SubmitRattingLiveData;
    }

    private void callRattingApi(int id) {
        RestClient.getInstance().getService().getRattingData(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CustomerRattingModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CustomerRattingModel o) {
                        listMutableLiveData.setValue(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listMutableLiveData.setValue(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void insertRating(RatingModel model) {
        RestClient.getInstance().getService().insertRating(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean o) {
                        SubmitRattingLiveData.setValue(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        SubmitRattingLiveData.setValue(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
