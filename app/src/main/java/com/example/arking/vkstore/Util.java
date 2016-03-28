package com.example.arking.vkstore;

import android.content.Intent;
import android.util.Log;

import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONException;
import org.json.JSONObject;


public class Util {
    public static String setLike(String type, String owner_id, Long item_id) {
        final String[] res = {""};
        VKRequest request = new VKRequest("likes.add", VKParameters.from("type", type, "owner_id", owner_id, "item_id", item_id));
        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONObject jsonObject = response.json.getJSONObject("response");
                    res[0] = jsonObject.getString("likes");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return res[0];
    }

    public static String removeLike(String type, String owner_id, Long item_id) {
        final String[] res = {""};
        VKRequest request = new VKRequest("likes.delete", VKParameters.from("type", type, "owner_id", owner_id, "item_id", item_id));
        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                try {
                    JSONObject jsonObject = response.json.getJSONObject("response");
                    res[0] = jsonObject.getString("likes");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return res[0];
    }
}
