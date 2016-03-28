package com.example.arking.vkstore;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.drawee.generic.RoundingParams;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

public class LoginActivity extends AppCompatActivity {
    String[] scopes = new String[]{VKScope.FRIENDS,VKScope.WALL,VKScope.MESSAGES, VKScope.OFFLINE,"market",VKScope.GROUPS};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


    }

    public void login(View view) {
        VKSdk.login(this, scopes);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {

                VKRequest request= VKApi.users().get(VKParameters.from("fields", "photo_50"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        //getApplicationContext().deleteDatabase("vkstore");
                        VKList<VKApiUserFull> user= (VKList<VKApiUserFull>) response.parsedModel;
                        DatabaseHelper dbHelper=new DatabaseHelper(getApplicationContext());
                        SQLiteDatabase db=dbHelper.getWritableDatabase();
                        ContentValues cv = new ContentValues();
                        cv.put("user_id", user.get(0).id);
                        try{
                            long c= db.insert("user",null,cv);
                            Log.v("test",String.valueOf(c));
                        }
                        catch (SQLiteConstraintException ex)
                        {

                        }



                    }
                });
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

            }
            @Override
            public void onError(VKError error) {
// Произошла ошибка авторизации (например, пользователь запретил авторизацию)
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
