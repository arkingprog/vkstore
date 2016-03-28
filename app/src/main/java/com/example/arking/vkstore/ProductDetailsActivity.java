package com.example.arking.vkstore;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.arking.vkstore.model.Market.Market;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
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

import java.util.ArrayList;
import java.util.jar.Attributes;

public class ProductDetailsActivity extends AppCompatActivity {
    Market product;
    SimpleDraweeView productImage;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        final Intent intent=getIntent();
        final Long productId=intent.getLongExtra("productId", 0);
        final Long groupId=intent.getLongExtra("groupId", 0);
        final Long contactId=intent.getLongExtra("contactId",0);
        init();
        initHeader();
        VKRequest request= new VKRequest("market.getById", VKParameters.from("item_ids", "-" + groupId + "_" + productId, "extended", 1));
        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    product = new Gson().fromJson(String.valueOf(response.json.getJSONObject("response")), Market.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(VKError error) {
                super.onError(error);
                Log.v("error", String.valueOf(error.request));

            }
        });
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setProgressBarImage(new ProgressBarDrawable())
                .build();
        productImage=(SimpleDraweeView)findViewById(R.id.productImage);
        productImage.setHierarchy(hierarchy);
        getSupportActionBar().setTitle(product.getItems().get(0).getTitle());
        productImage.setImageURI(Uri.parse(product.getItems().get(0).getPhotos().get(0).getPhoto604()));
        LinearLayout arrayPhoto=(LinearLayout)findViewById(R.id.arrayPhoto);
        Integer photoSize=product.getItems().get(0).getPhotos().size();
        ((TextView)findViewById(R.id.productDescription)).setText(product.getItems().get(0).getDescription());
        if(product.getItems().get(0).getLikes().getCount()!=0)
            ((TextView)findViewById(R.id.countLike)).setText(String.valueOf(product.getItems().get(0).getLikes().getCount()));
        final ToggleButton btnLike=(ToggleButton)findViewById(R.id.btbProductLike);
        final ToggleButton btnComment=(ToggleButton)findViewById(R.id.btbProductComment);
        if(product.getItems().get(0).getLikes().getUserLikes()==1)
            btnLike.setChecked(true);
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnLike.isChecked()) {
                    ((TextView)findViewById(R.id.countLike)).setText(Util.removeLike("market", "-" + groupId, productId));
                } else {
                    ((TextView)findViewById(R.id.countLike)).setText(Util.setLike("market", "-" + groupId, productId));
                }
            }
        });
        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentComment=new Intent(getApplicationContext(),ProductCommentActivity.class);
                intentComment.putExtra("productId", productId);
                intentComment.putExtra("groupId", groupId);
                startActivity(intentComment);
            }
        });

        ((TextView)findViewById(R.id.price)).setText(String.valueOf(product.getItems().get(0).getPrice().getText()));

        Button btnBuy=(Button)findViewById(R.id.btnBuy);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMessages = new Intent(getApplicationContext(), MessageActivity.class);
                intentMessages.putExtra("contactId", contactId);
                intentMessages.putExtra("productDetails", "Здравствуйте!\n" +
                        "Меня заинтересовал данный товар.\n\n" +
                        product.getItems().get(0).getTitle());
                startActivity(intentMessages);

            }
        });

        if(photoSize<2) {
            arrayPhoto.setVisibility(LinearLayout.GONE);
        }
        else {
            ArrayList<SimpleDraweeView> viewArrayList = new ArrayList<SimpleDraweeView>();
            viewArrayList.add((SimpleDraweeView)findViewById(R.id.productPhoto1));
            viewArrayList.add((SimpleDraweeView)findViewById(R.id.productPhoto2));
            viewArrayList.add((SimpleDraweeView)findViewById(R.id.productPhoto3));
            viewArrayList.add((SimpleDraweeView)findViewById(R.id.productPhoto4));
            viewArrayList.add((SimpleDraweeView)findViewById(R.id.productPhoto5));
            for (int i=0;i<5;i++)
            {
                if(i<photoSize){
                    viewArrayList.get(i).setImageURI(Uri.parse(product.getItems().get(0).getPhotos().get(i).getPhoto130()));
                    final int finalI = i;
                    viewArrayList.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            productImage.setImageURI(Uri.parse(product.getItems().get(0).getPhotos().get(finalI).getPhoto604()));
                        }
                    });
                    continue;
                }
                viewArrayList.get(i).setVisibility(View.GONE);

            }
        }

    }
    public void initHeader(){
        View headerLayout = navigationView.getHeaderView(0); // 0-index header
        final TextView username=(TextView)headerLayout.findViewById(R.id.username);
        final SimpleDraweeView profileImage=(SimpleDraweeView)headerLayout.findViewById(R.id.profile_image);
        VKRequest request= VKApi.users().get(VKParameters.from("fields", "photo_50"));
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
    public void  init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                drawerLayout.closeDrawers();
                Intent intent=null;
                switch (item.getItemId()) {
                    case R.id.logout:
                        VKSdk.logout();
                        intent=new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.catalog:
                        intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.favoriteStore:
                        intent=new Intent(getApplicationContext(),FavotiteGroupActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_messages:
                        intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        return true;
                    default:
                        return true;
                }
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close){
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
