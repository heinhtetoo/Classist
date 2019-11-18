package com.heinhtetoo.myclass.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.heinhtetoo.myclass.BuildConfig;
import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.data.models.UserModel;
import com.heinhtetoo.myclass.data.vos.UserVO;
import com.heinhtetoo.myclass.utils.MMFontUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "UserProfileActivity";
    private static final int RC_SIGN_IN = 9001;
    private static final String IS_FIRST_TIME = "isFirstTime";

    private boolean isFirstTime = false;

    private FirebaseAuth mAuth;

    private GoogleApiClient mGoogleApiClient;

    public ProgressDialog mProgressDialog;

    private UserModel mUserModel;

    boolean isAttached = false;

    @Bind(R.id.ic_back_user_profile)
    ImageView ivBack;

    @Bind(R.id.ll_welcome)
    LinearLayout llWelcome;

    @Bind(R.id.ll_contact_info_holder)
    LinearLayout llContactInfoHolder;

    @Bind(R.id.ll_button)
    LinearLayout llButton;

    @Bind(R.id.cv_profile_holder)
    CardView cvProfileHolder;

    @Bind(R.id.iv_profile)
    ImageView ivProfile;

    @Bind(R.id.tv_username)
    TextView tvUsername;

    /*@Bind(R.id.tv_user_rank)
    TextView tvUserRank;*/

    @Bind(R.id.tv_contact_holder)
    TextView tvContactHolder;

    @Bind(R.id.tv_email)
    TextView tvEmail;

    @Bind(R.id.tv_phone)
    TextView tvPhone;

    @Bind(R.id.cv_schedule)
    MaterialCardView cvSchedule;

    @Bind(R.id.btn_input_schedule)
    MaterialButton btnInputSchedule;

    @Bind(R.id.btn_google_login)
    MaterialButton btnGoogleLogin;

    @Bind(R.id.btn_logout)
    MaterialButton btnLogout;

    public static Intent newIntent(Context context, boolean isFirstTime) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(IS_FIRST_TIME, isFirstTime);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.getWindow().setStatusBarColor(Color.parseColor("#F15F79"));
        }
        ButterKnife.bind(this, this);

        isFirstTime = this.getIntent().getBooleanExtra(IS_FIRST_TIME, true);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.Google_Sign_In_Id_Token)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        mAuth = FirebaseAuth.getInstance();

        mUserModel = UserModel.getInstance();
    }

    @OnClick(R.id.ic_back_user_profile)
    public void onClickBack() {
        onBackPressed();
    }

    @OnClick(R.id.btn_input_schedule)
    public void onClickInputSchedule(View view) {
        Intent intent = CreateScheduleActivity.newIntent(view.getContext());
        startActivity(intent);
    }

    @OnClick(R.id.btn_google_login)
    public void onClickGoogleLogin() {
        signIn();
    }

    @OnClick(R.id.btn_logout)
    public void onClickLogout() {
        showConfirmLogoutDialog();
    }

    private void showConfirmLogoutDialog() {

        String confirmMsg = getResources().getString(R.string.confirm_logout);

        new AlertDialog.Builder(this)
                .setTitle("Logout?")
                .setMessage(confirmMsg)
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ivBack.setVisibility(View.GONE);
                        signOut();
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onStart() {
        super.onStart();

        String userId = UserModel.getInstance().getAccountIdFromPref(this);
        if (userId != null) {
            UserModel.getInstance().loadUserFromPref(userId);
        }

        if (mUserModel.isUserSignedIn()) {
            showProgress(getString(R.string.file_loading));
            mUserModel.syncUserInfo(mUserModel.getAccountIdFromPref(this), new UserModel.SyncUserInfoDelegate() {
                @Override
                public void syncUserInfo(UserVO user) {
                    updateUser(user);
                    ivBack.setVisibility(View.VISIBLE);
                    if (isFirstTime) {
                        onBackPressed();
                    }
                }
            });
        }
    }

    @Override
    public void onAttachedToWindow() {
        isAttached = true;
    }

    @Override
    public void onDetachedFromWindow() {
        isAttached = false;
    }

    @Override
    public void onBackPressed() {
        if (!mUserModel.getSignInStateFromPref(this)) {
            finish();
            moveTaskToBack(true);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            processGoogleSignInResult(result);
        }
    }

    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void processGoogleSignInResult(GoogleSignInResult signInResult) {
        if (signInResult.isSuccess()) {
            // Google Sign-In was successful, authenticate with Firebase
            final GoogleSignInAccount account = signInResult.getSignInAccount();
            UserModel.getInstance().firebaseAuthWithGoogle(account, new UserModel.SignInWithGoogleAccountDelegate() {
                @Override
                public void onSuccessSignIn(UserVO user) {
                    updateUser(user);
                    UserModel.getInstance().saveSignInStateInPref(UserProfileActivity.this, true);
                    UserModel.getInstance().saveAccountIdInPref(UserProfileActivity.this, user);
                    onBackPressed();
                }

                @Override
                public void onFailureSignIn(String msg) {
                    updateUser(null);
                }

                @Override
                public void showProgressDialog(String msg) {
                    showProgress(msg);
                }

                @Override
                public void hideProgressDialog() {
                    hideProgress();
                }
            });
        } else {
            Log.e(TAG, "Google Sign-In failed. Caused by " + signInResult.getStatus().getStatusMessage());
            Snackbar.make(tvUsername, "Your Google sign-in failed.", Snackbar.LENGTH_LONG).show();
        }
    }

    private void signOut() {
        mUserModel.signOut(mGoogleApiClient, new UserModel.LogoutDelegate() {
            @Override
            public void onLogoutSuccess() {
                mUserModel.saveSignInStateInPref(UserProfileActivity.this, false);
                mUserModel.removeAccountIdFromPref(UserProfileActivity.this);
                updateUser(null);
            }
        });
    }

    private void updateUser(UserVO user) {
        if (user != null) {
            if (isAttached) {
                Glide.with(UserProfileActivity.this)
                        .load(user.getPhotoUrl())
                        .thumbnail(
                                Glide.with(UserProfileActivity.this)
                                        .load(user.getPhotoUrl()))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivProfile);
            }
            tvUsername.setText(MMFontUtils.mmTextUnicodeOrigin(user.getDisplayName()));
            tvEmail.setText(user.getEmail());
            tvPhone.setText(user.getPhone());

            //cvProfileHolder.setVisibility(View.VISIBLE);
            tvUsername.setVisibility(View.VISIBLE);
            //tvUserRank.setVisibility(View.VISIBLE);
            tvContactHolder.setVisibility(View.VISIBLE);
            llContactInfoHolder.setVisibility(View.VISIBLE);
            cvSchedule.setVisibility(View.VISIBLE);
            llButton.setVisibility(View.VISIBLE);

            if (user.isScheduleUpdated()) {
                btnInputSchedule.setText(R.string.update_schedule);
            } else {
                btnInputSchedule.setText(R.string.input_schedule);
            }

            llWelcome.setVisibility(View.GONE);
            btnGoogleLogin.setVisibility(View.GONE);

            hideProgress();
        } else {
            if (isAttached) {
                Glide.with(UserProfileActivity.this)
                        .load(R.mipmap.ic_launcher_foreground)
                        .thumbnail(
                                Glide.with(UserProfileActivity.this)
                                        .load(R.mipmap.ic_launcher_foreground))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivProfile);
            }

            llWelcome.setVisibility(View.VISIBLE);
            btnGoogleLogin.setVisibility(View.VISIBLE);

            //cvProfileHolder.setVisibility(View.GONE);
            tvUsername.setVisibility(View.GONE);
            //tvUserRank.setVisibility(View.GONE);
            tvContactHolder.setVisibility(View.GONE);
            llContactInfoHolder.setVisibility(View.GONE);
            cvSchedule.setVisibility(View.GONE);
            llButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(UserProfileActivity.this);
            mProgressDialog.setMessage(msg);
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
