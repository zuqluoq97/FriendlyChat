package com.ltdung.friendlychat.library.presentation.mvp.view;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.ltdung.friendlychat.library.R;
import com.ltdung.friendlychat.library.presentation.mvp.presenter.BasePresenter;
import com.ltdung.friendlychat.library.presentation.util.ProgressDialogHelper;

public class ViewImpl implements com.ltdung.friendlychat.library.presentation.mvp.view.View {

    private Activity activity;

    private Fragment fragment;

    private View view;

    private Service service;

    private ProgressDialogHelper progressDialogHelper;

    public ViewImpl(Activity activity) {
        this.activity = activity;
        init();
    }

    public ViewImpl(Fragment fragment) {
        this.fragment = fragment;
        init();
    }

    public ViewImpl(View view) {
        this.view = view;
    }

    public ViewImpl(Service service) {
        this.service = service;
        init();
    }

    private void init(){
        progressDialogHelper = new ProgressDialogHelper();
    }

    @Override
    public void showMessage(String message) {
        if(getSnackBarBackground() == null){
            return;
        }
        Snackbar.make(getSnackBarBackground(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(int messageResId) {
        if (getContext() == null) {
            return;
        }
        showMessage(getContext().getString(messageResId));
    }

    @Override
    public void showProgress() {
        if (getContext() == null) {
            return;
        }
        progressDialogHelper.showProgress(getContext(), getContext().getString(R.string.loading));
    }

    @Override
    public void showProgress(String message) {
        progressDialogHelper.showProgress(getContext(), message);
    }

    @Override
    public void showProgress(int messageResId) {
        if (getContext() == null) {
            return;
        }
        showProgress(getContext().getString(messageResId));
    }

    @Override
    public void showProgress(String message, String title) {
        progressDialogHelper.showProgress(getContext(), message, title);
    }

    @Override
    public void showProgress(int messageResId, int titleResId) {
        if (getContext() == null) {
            return;
        }
        showProgress(getContext().getString(messageResId), getContext().getString(titleResId));
    }

    @Override
    public void hideProgress() {
        progressDialogHelper.hideProgress();
    }

    @Override
    public void hideKeyboard() {
        if (getContext() == null || getWindowToken() == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindowToken(), 0);
    }

    public void initSwipeToRefresh(SwipeRefreshLayout swipeRefreshLayout,
                                   BasePresenter presenter){
        swipeRefreshLayout.setOnRefreshListener(() -> {
            swipeRefreshLayout.setRefreshing(false);
            presenter.refreshData();
        });
    }

    private Context getContext() {
        if (activity != null) {
            return activity;
        } else if (fragment != null) {
            return fragment.getContext();
        } else if (view != null) {
            return view.getContext();
        } else if (service != null) {
            return service;
        }
        return null;
    }

    private View getSnackBarBackground() {
        if (activity != null) {
            return activity.findViewById(android.R.id.content);
        } else if (view != null) {
            return view;
        } else if (fragment != null) {
            return fragment.getView();
        }
        return null;
    }

    private IBinder getWindowToken() {
        if (activity != null) {
            View view = activity.getCurrentFocus();
            return view == null ? null : view.getWindowToken();
        } else if (fragment != null) {
            Activity activity = fragment.getActivity();
            if (activity == null) {
                return null;
            }
            View view = activity.getCurrentFocus();
            return view == null ? null : view.getWindowToken();
        } else if (view != null) {
            return view.getWindowToken();
        }
        return null;
    }
}
