package com.ltdung.friendlychat.library.presentation.mvp.presenter;

import com.ltdung.friendlychat.library.data.manager.NetworkManager;
import com.ltdung.friendlychat.library.presentation.mvp.view.View;

import lombok.NonNull;

public abstract class BasePresenter<VIEW extends View> {

    protected NetworkManager networkManager;

    public BasePresenter(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    protected VIEW view;

    public void attachView(@NonNull VIEW view){
        this.view = view;
        onViewAttached();
        networkManager.add(toString(), this::refreshData);
    }

    public void detachView(){
        networkManager.remove(toString());
        onViewDetached();
        this.view = null;
    }

    public void resume() {
    }

    public void pause() {
    }

    public void destroy() {
        onDestroyed();
    }

    public abstract void refreshData();

    protected void onViewAttached() {
    }

    protected void onViewDetached() {
    }

    protected void onDestroyed() {
    }
}
