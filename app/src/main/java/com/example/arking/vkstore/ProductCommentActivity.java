package com.example.arking.vkstore;

import android.content.Context;
import android.content.Intent;
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

import com.example.arking.vkstore.adapter.ListCommentAdapter;
import com.example.arking.vkstore.adapter.ListProductAdapter;
import com.example.arking.vkstore.model.Comment.Comment;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import org.json.JSONException;

public class ProductCommentActivity extends AppCompatActivity {
    Comment comment;
    ListView listComment;
    Long productId;
    Long groupId;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_comment);
        init();
        initHeader();
        Intent intent = getIntent();
        productId = intent.getLongExtra("productId", 0);
        groupId = intent.getLongExtra("groupId", 0);
        getComment();
        Button btnSendComment = (Button) findViewById(R.id.btnSendComment);
        final EditText textComment = (EditText) findViewById(R.id.textComment);

        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKRequest request = new VKRequest("market.createComment", VKParameters.from("owner_id", "-" + groupId,
                        "item_id", productId,
                        "message", textComment.getText().toString()));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        super.onComplete(response);
                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                        textComment.setText(" ");
                        getComment();
                    }
                });
            }
        });
    }

    public void getComment() {
        VKRequest request = new VKRequest("market.getComments", VKParameters.from("owner_id", "-" + groupId,
                "item_id", productId,
                "need_likes", 1,
                "extended", 1,
                "fields", "photo_50",
                "sort", "desc"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.v("test", String.valueOf(response.json));
                try {
                    comment = new Gson().fromJson(String.valueOf(response.json.getJSONObject("response")), Comment.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listComment = (ListView) findViewById(R.id.listComment);
                listComment.setAdapter(new ListCommentAdapter(getApplicationContext(), comment, groupId));

            }

            @Override
            public void onError(VKError error) {
                super.onError(error);

            }
        });
        return;
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


}
