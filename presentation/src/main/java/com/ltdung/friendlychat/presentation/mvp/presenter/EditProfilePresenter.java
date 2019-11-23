package com.ltdung.friendlychat.presentation.mvp.presenter;

import com.ltdung.friendlychat.domain.interactor.user.EditUser;
import com.ltdung.friendlychat.library.data.manager.NetworkManager;
import com.ltdung.friendlychat.library.presentation.DefaultSubscriber;
import com.ltdung.friendlychat.library.presentation.mvp.presenter.BasePresenter;
import com.ltdung.friendlychat.presentation.R;
import com.ltdung.friendlychat.presentation.di.scope.ViewScope;
import com.ltdung.friendlychat.presentation.mapper.UserDtoModelMapper;
import com.ltdung.friendlychat.presentation.mvp.model.UserModel;
import com.ltdung.friendlychat.presentation.mvp.view.EditProfileView;

import javax.inject.Inject;

@ViewScope
public class EditProfilePresenter extends BasePresenter<EditProfileView> {

    private EditUser editUser;

    private UserDtoModelMapper userDtoModelMapper;

    @Inject
    public EditProfilePresenter(NetworkManager networkManager,
                                EditUser editUser,
                                UserDtoModelMapper userDtoModelMapper) {
        super(networkManager);
        this.editUser = editUser;
        this.userDtoModelMapper = userDtoModelMapper;
    }

    @Override
    public void refreshData() {

    }

    public void updateUser(UserModel userModel){
        editUser.execute(userDtoModelMapper.map1(userModel), new DefaultSubscriber<String>(view){
            @Override
            public void onNext(String s) {
                super.onNext(s);
                view.showMessage(R.string.user_was_successfully_edited);
                view.hideProgress();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                view.showMessage(e.getMessage());
                view.hideProgress();
            }
        });
    }
}
