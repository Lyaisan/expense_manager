package com.lalmeeva.expense.utils;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Scheduler;

public class RxUtils {

    private final Scheduler mMainScheduler;
    private final Scheduler mBackgroundScheduler;

    public RxUtils(Scheduler mainScheduler, Scheduler backgroundScheduler) {
        mMainScheduler = mainScheduler;
        mBackgroundScheduler = backgroundScheduler;
    }

    private final FlowableTransformer flowableTransformer = new FlowableTransformer() {

        @Override
        public Flowable apply(Flowable upstream) {
            return upstream.subscribeOn(mBackgroundScheduler).observeOn(mMainScheduler);
        }
    };

    @SuppressWarnings("unchecked")
    public <T> FlowableTransformer<T, T> applySchedulersFlowable() {
        return (FlowableTransformer<T, T>) flowableTransformer;
    }

    public Scheduler getMainScheduler() {
        return mMainScheduler;
    }

    public Scheduler getBackgroundScheduler() {
        return mBackgroundScheduler;
    }

}
