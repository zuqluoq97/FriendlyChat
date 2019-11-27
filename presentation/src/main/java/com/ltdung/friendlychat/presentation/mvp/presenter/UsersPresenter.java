package com.ltdung.friendlychat.presentation.mvp.presenter;

import com.google.firebase.messaging.FirebaseMessaging;
import com.ltdung.friendlychat.data.manager.AuthManager;
import com.ltdung.friendlychat.domain.dto.UserDto;
import com.ltdung.friendlychat.domain.interactor.user.GetUser;
import com.ltdung.friendlychat.domain.interactor.user.GetUsers;
import com.ltdung.friendlychat.library.data.manager.NetworkManager;
import com.ltdung.friendlychat.library.presentation.DefaultSubscriber;
import com.ltdung.friendlychat.library.presentation.mvp.presenter.BasePresenter;
import com.ltdung.friendlychat.presentation.R;
import com.ltdung.friendlychat.presentation.di.scope.ViewScope;
import com.ltdung.friendlychat.presentation.mapper.UserDtoModelMapper;
import com.ltdung.friendlychat.presentation.mvp.model.UserModel;
import com.ltdung.friendlychat.presentation.mvp.view.UsersView;

import java.util.List;

import javax.inject.Inject;

import lombok.Getter;
import rx.Subscriber;

@ViewScope
public class UsersPresenter extends BasePresenter<UsersView> {

    private GetUsers getUsers;

    private GetUser getUser;

    private UserDtoModelMapper userDtoModelMapper;

    private AuthManager authManager;

    private Subscriber<String> signOutSubscriber;

    @Getter
    private UserModel currentUser;

    @Inject
    public UsersPresenter(NetworkManager networkManager,
                          AuthManager authManager,
                          GetUsers getUsers,
                          GetUser getUser,
                          UserDtoModelMapper userDtoModelMapper) {
        super(networkManager);
        this.authManager = authManager;
        this.getUsers = getUsers;
        this.getUser = getUser;
        this.userDtoModelMapper = userDtoModelMapper;
    }

    @Override
    protected void onViewAttached() {
        super.onViewAttached();
        refreshData();
    }

    @Override
    protected void onViewDetached() {
        super.onViewDetached();
        getUsers.unsubscribe();
        getUser.unsubscribe();

        if(signOutSubscriber != null){
            signOutSubscriber.unsubscribe();
            signOutSubscriber = null;
        }
    }

    @Override
    public void refreshData() {
        retrieveCurrentUser();
        retrieveUsers();
    }

    private void retrieveUsers(){
        view.showProgress();
        getUsers.execute(new DefaultSubscriber<List<UserDto>>(view){
            @Override
            public void onNext(List<UserDto> userDtos) {
                super.onNext(userDtos);
                view.renderUsers(excludeCurrent(userDtoModelMapper.map2(userDtos)));
                view.hideProgress();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showProgress(e.getMessage());
                view.hideProgress();
            }
        });
    }

    private void retrieveCurrentUser() {
        view.showProgress();

        Subscriber<UserDto> getUserSubscriber = new DefaultSubscriber<UserDto>(view) {

            @Override
            public void onNext(UserDto user) {
                super.onNext(user);
                currentUser = userDtoModelMapper.map2(user);
                view.renderCurrentUser(currentUser);
                view.hideProgress();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showMessage(e.getMessage());
                view.hideProgress();
            }
        };

        getUser.execute(authManager.getCurrentUserId(), getUserSubscriber);

        getUser.setOnUserChangedListener(userId -> {
            if (userId.equals(authManager.getCurrentUserId())) {
                getUser.execute(authManager.getCurrentUserId(), getUserSubscriber);
            }
        });
    }

    public void signOut() {
        view.showProgress(R.string.signing_out);

        signOutSubscriber = new DefaultSubscriber<String>(view) {
            @Override
            public void onNext(String userId) {
                super.onNext(userId);
                view.hideProgress();
                view.navigateToSplash();
                FirebaseMessaging.getInstance().unsubscribeFromTopic("user_" + userId);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showMessage(R.string.sign_out_error);
                view.hideProgress();
            }
        };
        authManager.signOut(signOutSubscriber);
    }

    private List<UserModel> excludeCurrent(List<UserModel> users){
        for (UserModel user : users) {
            if (user.getId().equals(authManager.getCurrentUserId())) {
                users.remove(user);
                break;
            }
        }
        return users;
    }
}
