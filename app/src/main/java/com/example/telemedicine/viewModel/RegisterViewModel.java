package com.example.telemedicine.viewModel;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.telemedicine.model.ServerResponse;
import com.example.telemedicine.model.UnregisteredUser;
import com.example.telemedicine.request.RetrofitCall;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RegisterViewModel extends AndroidViewModel {

    private MutableLiveData<ServerResponse> unregisteredUserMutableLiveData = new MutableLiveData<>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public RegisterViewModel(@NonNull Application application) {
        super(application);
    }

    public void makeRegisterRequest(UnregisteredUser user) {
        compositeDisposable.add(registerNewUser(user)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.getResponseStatus().equals("SUCCESS")) {
                        Toast.makeText(getApplication(), "Successful", Toast.LENGTH_SHORT).show();
                        unregisteredUserMutableLiveData.postValue(response);
                    } else {
                        Toast.makeText(getApplication(), response.getResponseToken(), Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> Toast.makeText(getApplication(), throwable.getMessage(), Toast.LENGTH_SHORT).show()));
    }

    public MutableLiveData<ServerResponse> getUnregisteredUserLiveData() {
        return unregisteredUserMutableLiveData;
    }

    private Single<ServerResponse> registerNewUser(UnregisteredUser user) {
        return RetrofitCall.getInstance().registerNewUser(user);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}