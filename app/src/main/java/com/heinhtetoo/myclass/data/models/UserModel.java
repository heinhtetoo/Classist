package com.heinhtetoo.myclass.data.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.heinhtetoo.myclass.R;
import com.heinhtetoo.myclass.data.vos.DayVO;
import com.heinhtetoo.myclass.data.vos.ModuleVO;
import com.heinhtetoo.myclass.data.vos.ScheduleVO;
import com.heinhtetoo.myclass.data.vos.UserVO;
import com.heinhtetoo.myclass.events.DataEvents;
import com.heinhtetoo.myclass.utils.PrefUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class UserModel {

    private static final String TAG = "UserProfileActivity";

    public static final String MyCLASS_USER = "users";
    public static final String ASSIGNMENT_NUMBER = "assignmentNo";
    public static final String TUTORIAL_NUMBER = "tutorialNo";
    public static final String STUDENT_NUMBER = "studentNo";

    private static UserModel objInstance;

    private DatabaseReference mDatabaseReference;
    private DatabaseReference mUserDr;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    private UserVO mUser;

    private UserModel() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mUserDr = mDatabaseReference.child(MyCLASS_USER);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }

    public static UserModel getInstance() {
        if (objInstance == null) {
            objInstance = new UserModel();
        }
        return objInstance;
    }

    public boolean isUserSignedIn() {
        return mUser != null;
    }

    public void firebaseAuthWithGoogle(final GoogleSignInAccount acct,
                                       final SignInWithGoogleAccountDelegate delegate) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        delegate.showProgressDialog("Authenticating...");

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            checkExistingUser(acct, delegate);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            delegate.onFailureSignIn(task.getException().getMessage());
                        }

                        delegate.hideProgressDialog();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        delegate.onFailureSignIn(e.getMessage());
                    }
                });
    }

    private void checkExistingUser(final GoogleSignInAccount signInAccount,
                                   final SignInWithGoogleAccountDelegate delegate) {
        //String formattedEmail = FirebaseUtils.replaceDotsWithCommas(signInAccount.getEmail());
        DatabaseReference singleAccountDR = mUserDr.child(signInAccount.getId());
        singleAccountDR.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserVO user = dataSnapshot.getValue(UserVO.class);
                if (user != null) {
                    delegate.onSuccessSignIn(user);
                    mUser = user;
                } else {
                    Uri photoUri = signInAccount.getPhotoUrl();
                    String photoUrl = (photoUri != null) ? ((Uri) photoUri).toString() : null;

                    ModuleVO emptyModule = new ModuleVO("No Data", "No Data");
                    ModuleVO lunchModule = new ModuleVO("Lunch Time", "Free Time");
                    ArrayList<ModuleVO> emptyModuleList = new ArrayList<>();
                    emptyModuleList.add(emptyModule);
                    emptyModuleList.add(emptyModule);
                    emptyModuleList.add(emptyModule);
                    emptyModuleList.add(lunchModule);
                    emptyModuleList.add(emptyModule);
                    emptyModuleList.add(emptyModule);
                    emptyModuleList.add(emptyModule);

                    DayVO emptyMondayVO = new DayVO(0, emptyModuleList);
                    DayVO emptyTuesdayVO = new DayVO(0, emptyModuleList);
                    DayVO emptyWednesdayVO = new DayVO(0, emptyModuleList);
                    DayVO emptyThursdayVO = new DayVO(0, emptyModuleList);
                    DayVO emptyFridayVO = new DayVO(0, emptyModuleList);

                    ScheduleVO scheduleVO = new ScheduleVO(emptyMondayVO, emptyTuesdayVO, emptyWednesdayVO, emptyThursdayVO, emptyFridayVO);

                    UserVO newUser = new UserVO(signInAccount.getId(), signInAccount.getDisplayName(),
                            signInAccount.getEmail(), photoUrl,
                            null, null, null, null, 0, 0, 0, false, scheduleVO);
                    registerNewUser(newUser);
                    delegate.onSuccessSignIn(newUser);
                    mUser = newUser;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                delegate.onFailureSignIn(databaseError.getMessage());
            }
        });
    }

    public void syncUserInfo(String accountId, final SyncUserInfoDelegate delegate) {
        DatabaseReference singleAccountDR = mUserDr.child(accountId);
        singleAccountDR.keepSynced(true);
        singleAccountDR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserVO user = dataSnapshot.getValue(UserVO.class);
                if (user != null) {
                    delegate.syncUserInfo(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void signOut(GoogleApiClient mGoogleApiClient, final LogoutDelegate delegate) {
        mFirebaseAuth.signOut();

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        mUser = null;
                        delegate.onLogoutSuccess();
                    }
                });
    }

    public void registerNewUser(UserVO user) {
        mUserDr.child(String.valueOf(user.getAccountId())).setValue(user);
    }

    public void saveSignInStateInPref(Context context, boolean flag) {
        SharedPreferences.Editor editor = PrefUtils.getSharedPrefs(context).edit();
        editor.putBoolean(context.getString(R.string.sign_in_state_key), flag);
        editor.apply();
    }

    public void saveAccountIdInPref(Context context, UserVO userVO) {
        SharedPreferences.Editor editor = PrefUtils.getSharedPrefs(context).edit();
        editor.putString(context.getString(R.string.account_id_key), userVO.getAccountId());
        editor.apply();
    }

    public boolean getSignInStateFromPref(Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPrefs(context);
        return sharedPreferences.getBoolean(context.getString(R.string.sign_in_state_key), false);
    }

    public String getAccountIdFromPref(Context context) {
        SharedPreferences sharedPreferences = PrefUtils.getSharedPrefs(context);
        return sharedPreferences.getString(context.getString(R.string.account_id_key), null);
    }

    public void removeAccountIdFromPref(Context context) {
        SharedPreferences.Editor editor = PrefUtils.getSharedPrefs(context).edit();
        editor.putString(context.getString(R.string.account_id_key), null);
        editor.apply();
    }

    public void updateClassroomData(int assignmentNo, int tutorialNo, int studentNo) {
        mUserDr.child(mUser.getAccountId()).child(ASSIGNMENT_NUMBER).setValue(assignmentNo);
        mUserDr.child(mUser.getAccountId()).child(TUTORIAL_NUMBER).setValue(tutorialNo);
        mUserDr.child(mUser.getAccountId()).child(STUDENT_NUMBER).setValue(studentNo);
    }

    public void loadUserFromPref(String id) {
        mUserDr.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserVO user = dataSnapshot.getValue(UserVO.class);
                if (user != null) {
                    mUser = user;
                }

                DataEvents.UserDataLoadedEvent event = new DataEvents.UserDataLoadedEvent(mUser);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public UserVO getUser() {
        return mUser;
    }

    public void updateSchedule(ScheduleVO scheduleVO) {
        if (scheduleVO != null) {
            mUserDr.child(mUser.getAccountId()).child("schedule").setValue(scheduleVO);
            mUserDr.child(mUser.getAccountId()).child("scheduleUpdated").setValue(true);
        }
    }

    public interface SignInWithGoogleAccountDelegate {
        void onSuccessSignIn(UserVO user);

        void onFailureSignIn(String msg);

        void showProgressDialog(String msg);

        void hideProgressDialog();
    }

    public interface LogoutDelegate {
        void onLogoutSuccess();
    }

    public interface SyncUserInfoDelegate {
        void syncUserInfo(UserVO user);
    }
}
