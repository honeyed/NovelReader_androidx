package com.example.newbiechen.ireader.ui.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * Created by newbiechen on 17-5-13.
 */

public abstract class BaseService extends Service {

    private CompositeDisposable mDisposable;

    protected void addDisposable(Disposable disposable){
        if (mDisposable == null){
            mDisposable = new CompositeDisposable();
        }
        mDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null){
            mDisposable.dispose();
        }
    }
}
