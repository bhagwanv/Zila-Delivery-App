package com.sk.ziladelivery.utilities;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by User on 29-11-2018.
 */

public class RxBus {

    private static RxBus instance;

    public static RxBus getInstance(){
        if (instance == null){
            instance = new RxBus();
        }
        return instance;
    }

    private RxBus() {
    }

    private PublishSubject<Object> bus = PublishSubject.create();

    public void sendEvent(Object o) {
        bus.onNext(o);
    }

    public Observable<Object> getEvent() {
        return bus;
    }
    public void sendTimerEvent(Object o) {
        bus.onNext(o);
    }
    public Observable<Object> getTimerEvent() {
        return bus;
    }



}