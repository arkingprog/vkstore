package com.example.arking.vkstore;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;

import com.example.arking.vkstore.adapter.ListProductAdapter;
import com.example.arking.vkstore.model.Market.Market;
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

public class ProductListActivity extends AppCompatActivity {
    Market market;
    ListView listProduct;
    SimpleDraweeView productImage;
    Long groupId;
    Long contactId;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        final Intent intent = getIntent();
        groupId = intent.getLongExtra("groupId", 0);
        contactId = intent.getLongExtra("contactId", 0);
        final String title = intent.getStringExtra("title");

        init();
        initHeader();
        getSupportActionBar().setTitle(title);
        VKRequest request = new VKRequest("market.get", VKParameters.from(VKApiConst.OWNER_ID, "-" + groupId, VKApiConst.EXTENDED, 1));
        request.setPreferredLang("ru");
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    market = new Gson().fromJson(String.valueOf(response.json.getJSONObject("response")), Market.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                listProduct = (ListView) findViewById(R.id.listProduct);
                listProduct.setAdapter(new ListProductAdapter(getApplicationContext(), market));
                listProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intentDetails = new Intent(getApplicationContext(), ProductDetailsActivity.class);
                        intentDetails.putExtra("productId", id);
                        intentDetails.putExtra("groupId", groupId);
                        intentDetails.putExtra("contactId", contactId);

                        startActivity(intentDetails);
                    }
                });
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor all = db.query("favorite_store", null, "group_id = ? and user_id =?", new String[]{String.valueOf(groupId), String.valueOf(userId())}, null, null, null);
        Log.v("test", String.valueOf(all.getCount()));
        if (all.getCount() <= 0) {
            getMenuInflater().inflate(R.menu.menu_add_fav, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_del_fav, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addFavotire) {
            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("group_id", groupId);
            cv.put("user_id", userId());
            db.insert("favorite_store", null, cv);
            //Cursor all = db.query("favorite_store", null, "group_id = ? and user_id =?", new String[]{String.valueOf(userId()), String.valueOf(groupId)}, null, null, null);
            //all.moveToFirst();
            return true;
        }
        if (id == R.id.delFavotire) {
            DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("favorite_store", "group_id= ?", new String[]{String.valueOf(groupId)});
            return true;
        }
        if (id == R.id.sendMessage) {
            Intent intentMessages = new Intent(getApplicationContext(), MessageActivity.class);
            intentMessages.putExtra("contactId", contactId);
            startActivity(intentMessages);
        }
        return super.onOptionsItemSelected(item);
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
                Intent intent;
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
}
