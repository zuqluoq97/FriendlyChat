package com.ltdung.friendlychat.presentation.mvp.presenter;

import com.ltdung.friendlychat.data.manager.AuthManager;
import com.ltdung.friendlychat.data.util.StringUtil;
import com.ltdung.friendlychat.domain.dto.MessageDto;
import com.ltdung.friendlychat.domain.dto.UserDto;
import com.ltdung.friendlychat.domain.interactor.message.DeleteMessage;
import com.ltdung.friendlychat.domain.interactor.message.EditMessage;
import com.ltdung.friendlychat.domain.interactor.message.GetMessages;
import com.ltdung.friendlychat.domain.interactor.message.PostMessage;
import com.ltdung.friendlychat.domain.interactor.user.GetUser;
import com.ltdung.friendlychat.library.data.manager.NetworkManager;
import com.ltdung.friendlychat.library.presentation.DefaultSubscriber;
import com.ltdung.friendlychat.library.presentation.mvp.presenter.BasePresenter;
import com.ltdung.friendlychat.presentation.R;
import com.ltdung.friendlychat.presentation.di.scope.ViewScope;
import com.ltdung.friendlychat.presentation.mapper.MessageDtoModelMapper;
import com.ltdung.friendlychat.presentation.mvp.model.MessageModel;
import com.ltdung.friendlychat.presentation.mvp.view.DialogView;

import java.util.List;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

@ViewScope
public class DialogPresenter extends BasePresenter<DialogView> {

    @Getter
    private static String currentPeerId;

    private GetMessages getMessages;

    private PostMessage postMessage;

    private DeleteMessage deleteMessage;

    private EditMessage editMessage;

    private GetUser getUser;

    @Getter
    @Setter
    private String peerId;

    private MessageDtoModelMapper messageDtoModelMapper;

    private AuthManager authManager;

    @Inject
    public DialogPresenter(NetworkManager networkManager,
                           AuthManager authManager,
                           GetMessages getMessages,
                           PostMessage postMessage,
                           DeleteMessage deleteMessage,
                           EditMessage editMessage,
                           GetUser getUser,
                           MessageDtoModelMapper messageDtoModelMapper) {
        super(networkManager);
        this.authManager = authManager;
        this.getMessages = getMessages;
        this.postMessage = postMessage;
        this.deleteMessage = deleteMessage;
        this.editMessage = editMessage;
        this.getUser = getUser;
        this.messageDtoModelMapper = messageDtoModelMapper;
    }

    public void sendMessage(String message) {
        view.showProgress(R.string.sending);
        MessageModel messageModel = new MessageModel();
        messageModel.setSenderId(authManager.getCurrentUserId());
        messageModel.setReceiverId(peerId);
        messageModel.setText(message);
        messageModel.setTimestamp(System.currentTimeMillis());
        postMessage.execute(messageDtoModelMapper.map1(messageModel),
                new DefaultSubscriber<Void>(null) {
                    @Override
                    public void onNext(Void aVoid) {
                        super.onNext(aVoid);
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

    public void deleteMessage(MessageModel messageModel) {
        view.showProgress(R.string.deleting);
        deleteMessage.execute(messageDtoModelMapper.map1(messageModel),
                new DefaultSubscriber<Void>(null) {
                    @Override
                    public void onNext(Void aVoid) {
                        super.onNext(aVoid);
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

    public void editMessage(MessageModel messageModel) {
        view.showProgress(R.string.editing);
        editMessage.execute(messageDtoModelMapper.map1(messageModel),
                new DefaultSubscriber<Void>(view) {
                    @Override
                    public void onNext(Void aVoid) {
                        super.onNext(aVoid);
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

    @Override
    public void refreshData() {

    }

    @Override
    protected void onViewAttached() {
        super.onViewAttached();
        refreshData();
    }

    @Override
    public void resume() {
        super.resume();
        currentPeerId = peerId;
    }

    @Override
    public void pause() {
        super.pause();
        currentPeerId = null;
    }

    @Override
    protected void onViewDetached() {
        super.onViewDetached();
        getMessages.unsubscribe();
        postMessage.unsubscribe();
        deleteMessage.unsubscribe();
        editMessage.unsubscribe();
        getUser.unsubscribe();
    }

    private void getMessages() {
        view.showProgress();
        getMessages.execute(peerId, new DefaultSubscriber<List<MessageDto>>(view) {
            @Override
            public void onNext(List<MessageDto> messages) {
                super.onNext(messages);
                view.renderMessages(messageDtoModelMapper.map2(messages));
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

    private void getPeerUser() {
        getUser.execute(peerId, new DefaultSubscriber<UserDto>(view) {
            @Override
            public void onNext(UserDto userDto) {
                super.onNext(userDto);
                view.setTitle(StringUtil.concatLinearly(" ", userDto.getFirstName(), userDto.getLastName()));
            }
        });
    }

    public AuthManager getAuthManager() {
        return authManager;
    }
}
