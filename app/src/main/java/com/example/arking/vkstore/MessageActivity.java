package com.example.arking.vkstore;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.arking.vkstore.adapter.ListMessagesAdapter;
import com.example.arking.vkstore.model.Message.Message;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import org.json.JSONException;

public class MessageActivity extends AppCompatActivity {
    Message messages;
    ListView listMessages;
    Button sendMessages;
    EditText textMessages;
    Long contactId;
    String productDetails;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        init();
        initHeader();

        Intent intent = getIntent();
        contactId = intent.getLongExtra("contactId", 0);
        productDetails = intent.getStringExtra("productDetails");
        textMessages = (EditText) findViewById(R.id.textMessage);
        textMessages.setText(productDetails);
        sendMes();
        sendMessages = (Button) findViewById(R.id.sendMessage);
        sendMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKRequest request = new VKRequest("messages.send", VKParameters.from("user_id", contactId, "message", textMessages.getText().toString()));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                        textMessages.setText(" ");

                        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        ContentValues cv = new ContentValues();
                        cv.put("contact_id", contactId);
                        cv.put("user_id", userId());
                        db.insert("user_contact", null, cv);
                        sendMes();
                    }
                });
            }
        });

    }

    public void sendMes() {
        VKRequest request = new VKRequest("messages.getHistory", VKParameters.from("user_id", contactId, "count", 100));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    messages = new Gson().fromJson(String.valueOf(response.json.getJSONObject("response")), Message.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listMessages = (ListView) findViewById(R.id.listMessages);
                listMessages.setAdapter(new ListMessagesAdapter(getApplicationContext(), messages));
                listMessages.setSelection(messages.getItems().size() - 1);


            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.v("test", String.valueOf(error.errorMessage));

            }
        });
    }

    public void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                drawerLayout.closeDrawers();
                Intent intent = null;
                switch (item.getItemId()) {
                    case R.id.logout:
                        VKSdk.logout();
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.catalog:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.favoriteStore:
                        intent = new Intent(getApplicationContext(), FavotiteGroupActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_messages:
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        return true;
                }
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);


        actionBarDrawerToggle.syncState();
    }

    public void initHeader() {
        View headerLayout = navigationView.getHeaderView(0); // 0-index header
        final TextView username = (TextView) headerLayout.findViewById(R.id.username);
        final SimpleDraweeView profileImage = (SimpleDraweeView) headerLayout.findViewById(R.id.profile_image);
        VKRequest request = VKApi.users().get(VKParameters.from("fields", "photo_50"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKList<VKApiUserFull> user = (VKList<VKApiUserFull>) response.parsedModel;
                username.setText(user.get(0).first_name + " " + user.get(0).last_name);
                profileImage.setImageURI(Uri.parse(user.get(0).photo_50));
                RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                roundingParams.setBorder(R.color.red, (float) 1.0);
                roundingParams.setRoundAsCircle(true);
                profileImage.getHierarchy().setRoundingParams(roundingParams);

            }
        });
    }

    public Long userId() {
        final Long[] id = new Long[1];
        VKRequest request = VKApi.users().get(VKParameters.from());
        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKList<VKApiUserFull> user = (VKList<VKApiUserFull>) response.parsedModel;
                id[0] = Long.valueOf(user.get(0).id);
            }
        });
        return id[0];
    }
}
