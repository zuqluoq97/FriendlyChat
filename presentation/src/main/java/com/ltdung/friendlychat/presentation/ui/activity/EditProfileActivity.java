package com.ltdung.friendlychat.presentation.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;


import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.ltdung.friendlychat.presentation.R;
import com.ltdung.friendlychat.presentation.databinding.ActivityEditProfileBinding;
import com.ltdung.friendlychat.presentation.di.component.ViewComponent;
import com.ltdung.friendlychat.presentation.mvp.model.UserModel;
import com.ltdung.friendlychat.presentation.mvp.presenter.EditProfilePresenter;
import com.ltdung.friendlychat.presentation.mvp.view.EditProfileView;
import com.ltdung.friendlychat.presentation.mvp.view.impl.EditProfileViewImpl;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.DecimalMax;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;


public class EditProfileActivity extends BaseDaggerActivity<EditProfileView, EditProfilePresenter, ActivityEditProfileBinding> {

    @Inject
    Lazy<EditProfilePresenter> editProfilePresenter;

    @NotEmpty
    EditText etFirstName;

    @NotEmpty
    EditText etLastName;

    @DecimalMax(100)
    EditText etAge;

    @InjectExtra
    UserModel user;

    private Validator validator;

    @Override
    public void onLoadFinished() {
        super.onLoadFinished();
        Dart.inject(this);
        validator = initValidator();
        initUi();
    }

    public static void start(Context context, UserModel userModel) {
        if (userModel == null) {
            return;
        }
        Intent intent = Henson.with(context)
                .gotoEditProfileActivity()
                .user(userModel)
                .build();
        context.startActivity(intent);
    }

    @Override
    protected void injectViewComponent(ViewComponent viewComponent) {
        viewComponent.inject(this);
    }

    @Override
    protected EditProfileView initView() {
        return new EditProfileViewImpl(this) {
        };
    }

    @Override
    protected Lazy<EditProfilePresenter> initPresenter() {
        return editProfilePresenter;
    }

    @Override
    protected ActivityEditProfileBinding initBinding() {
        return DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
    }

    private void initUi() {
        initViewsForSaripaar();
        initSaveButton();
        initEditTexts();
    }

    private void initSaveButton() {
        binding.btnSave.setOnClickListener(v -> validator.validate());
    }

    private void initViewsForSaripaar() {
        etFirstName = binding.etFirstName;
        etLastName = binding.etLastName;
        etAge = binding.etAge;
    }

    private void initEditTexts() {
        if (user == null) {
            return;
        }
        binding.etFirstName.setText(user.getFirstName());
        binding.etLastName.setText(user.getLastName());
        binding.etAge.setText(String.valueOf(user.getAge()));
    }

    private Validator initValidator() {
        Validator validator = new Validator(this);
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                view.hideKeyboard();
                user.setFirstName(binding.etFirstName.getText().toString());
                user.setLastName(binding.etLastName.getText().toString());
                user.setAge(Integer.valueOf(binding.etAge.getText().toString()));
                presenter.updateUser(user);
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                for (ValidationError error : errors) {
                    View view1 = error.getView();
                    String message = error.getCollatedErrorMessage(EditProfileActivity.this);

                    if (view1 instanceof EditText) {
                        ((EditText) view1).setError(message);
                    } else {
                        EditProfileActivity.this.view.showMessage(message);
                    }
                }
            }
        });
        return validator;
    }
}