package com.ltdung.friendlychat.presentation.mvp.view;

import com.ltdung.friendlychat.library.presentation.mvp.view.View;
import com.ltdung.friendlychat.presentation.mvp.model.UserModel;

import java.util.List;

public interface UsersView extends View {
    void renderCurrentUser(UserModel user);

    void renderUsers(List<UserModel> users);

    void navigateToDialog(String peerId);

    void navigateToEditProfile();

    void navigateToSplash();
}
