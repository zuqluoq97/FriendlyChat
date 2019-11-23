package com.ltdung.friendlychat.presentation.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ltdung.friendlychat.data.util.StringUtil;
import com.ltdung.friendlychat.library.presentation.mvp.view.ViewImpl;
import com.ltdung.friendlychat.library.presentation.ui.activity.BaseActivity;
import com.ltdung.friendlychat.presentation.R;
import com.ltdung.friendlychat.presentation.databinding.ActivityUsersBinding;
import com.ltdung.friendlychat.presentation.di.component.ViewComponent;
import com.ltdung.friendlychat.presentation.mvp.model.UserModel;
import com.ltdung.friendlychat.presentation.mvp.presenter.UsersPresenter;
import com.ltdung.friendlychat.presentation.mvp.view.UsersView;
import com.ltdung.friendlychat.presentation.mvp.view.impl.UsersViewImpl;
import com.ltdung.friendlychat.presentation.ui.adapter.UsersAdapter;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

public class UsersActivity extends BaseDaggerActivity<UsersView, UsersPresenter, ActivityUsersBinding> {

    @Inject
    Lazy<UsersPresenter> usersPresenter;

    private UsersAdapter usersAdapter;

    public static void start(Context context){
        Intent intent = BaseActivity.getBaseStartIntent(context, UsersActivity.class, false);
        context.startActivity(intent);
    }

    @Override
    protected void injectViewComponent(ViewComponent viewComponent) {
        viewComponent.inject(this);
    }

    @Override
    protected UsersView initView() {
        return new UsersViewImpl(this) {
            @Override
            public void renderCurrentUser(UserModel user) {
                binding.tvUsername.setText(StringUtil.concatLinearly(" ", user.getFirstName(), user.getLastName()));
            }

            @Override
            public void renderUsers(List<UserModel> users) {
                usersAdapter.setItems(users);
            }

            @Override
            public void navigateToDialog(String peerId) {
                DialogActivity.start(UsersActivity.this, peerId);
            }

            @Override
            public void navigateToEditProfile() {
                EditProfileActivity.start(UsersActivity.this, presenter.getCurrentUser());
            }

            @Override
            public void navigateToSplash() {
                SplashActivity.start(UsersActivity.this);
            }
        };
    }

    @Override
    protected Lazy<UsersPresenter> initPresenter() {
        return usersPresenter;
    }

    @Override
    protected ActivityUsersBinding initBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_users);
    }

    @Override
    public void onLoadFinished() {
        super.onLoadFinished();
        initUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_logout:
                presenter.signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initUI() {
        initUserRecylerView();
        initEditProfileButton();
        initSwipeToRefresh();
    }

    private void initSwipeToRefresh() {
        usersAdapter = new UsersAdapter(view);
        binding.rvUsers.setAdapter(usersAdapter);
        binding.rvUsers.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initEditProfileButton() {
        binding.btnEdit.setOnClickListener(view1 -> UsersActivity.this.view.navigateToEditProfile());
    }

    private void initUserRecylerView() {
        ((ViewImpl) view).initSwipeToRefresh(binding.swipeToRefresh, presenter);
    }
}
