package com.example.arking.vkstore;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.arking.vkstore.adapter.UserGroupAdapter;
import com.example.arking.vkstore.model.userGroup;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    String[] scopes = new String[]{VKScope.FRIENDS, VKScope.WALL, VKScope.MESSAGES, VKScope.OFFLINE, "market", VKScope.GROUPS};
    private ListView drawerListView;
    JSONArray res = null;
    JSONArray items = null;
    List<userGroup> userGroups;
    ListView catalogList;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<Long> array = new ArrayList<>();
        array.add((long) 14890753);
        DatabaseHelper dbHelper1 = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db1 = dbHelper1.getWritableDatabase();
        for (Long i : array) {
            ContentValues cv = new ContentValues();
            cv.put("group_id", i);
            db1.insert("catalog_store", null, cv);
        }
        if (!VKSdk.isLoggedIn()) {

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }

        init();
        initHeader();
/*
        String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        Log.v("p", Arrays.toString(fingerprints));
  */

        String query = "";


        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor all = db.query("catalog_store", null, null, null, null, null, null);
        all.moveToFirst();
        for (int i = 0; i < all.getCount(); i++) {
            query += all.getString(1) + ",";
            all.moveToNext();
        }
        Log.v("test", query);
        userGroups = new ArrayList<>();
        VKRequest request = new VKRequest("groups.getById", VKParameters.from("group_ids", query, "fields", "market"));
        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    res = response.json.getJSONArray("response");

                    for (int i = 0; i < res.length(); i++) {
                        JSONObject item = res.getJSONObject(i);
                        //userGroups.add(new userGroup(item.getString("name"), item.getInt("id"), item.getString("photo_100")));

                        JSONObject market = item.getJSONObject("market");
                        Integer enabled = market.getInt("enabled");
                        if (enabled.equals(1)) {
                            userGroups.add(new userGroup(item.getString("name"), item.getLong("id"), item.getString("photo_100"), market.getLong("contact_id")));
                        }
                    }
                    Log.v("test", String.valueOf(userGroups.size()));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        catalogList = (ListView) findViewById(R.id.listCatalogStore);
        catalogList.setAdapter(new UserGroupAdapter(this, userGroups));
        catalogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ProductListActivity.class);
                intent.putExtra("groupId", id);
                for (userGroup group : userGroups) {
                    if (group.getId().equals(Long.valueOf(id))) {
                        intent.putExtra("contactId", group.getContactId());
                        intent.putExtra("title", group.getName());
                    }
                }
                startActivity(intent);
            }
        });

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
