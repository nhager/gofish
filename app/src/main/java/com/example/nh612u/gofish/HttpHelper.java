package com.example.nh612u.gofish;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;

import com.loopj.android.http.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InterfaceAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import android.os.Handler.Callback;
import android.telecom.Call;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class HttpHelper {
    public enum TABLE {
        USER,
        EVENT,
        EVENTS,
        EMERGENCY_CONTACT,
        LOCATION,
        EVENT_SIGNUP,
        MAP_MARKER,
        USERS,
        ITEM,
        ITEM_ASSIGN,
        ITEM_TYPE,
        ITEMS;
}

    private static final String SERVER_URL = "http://go-fish-api.herokuapp.com/";

    private AsyncHttpClient client = new AsyncHttpClient();
    private Callback callback = null;

    public HttpHelper(Callback callback) {
        this.callback = callback;
    }

    public void GET(final TABLE tableEnum, final JSONObject jsonObject) {
        final String urlString = buildURLString_GET(tableEnum, jsonObject);
        client.get(urlString, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = "Error contacting server.";
                    if (statusCode == 200) {
                        response = new String(responseBody, "UTF-8");
                    }
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", response);
                    message.setData(bundle);
                    callback.handleMessage(message);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                try {
                    final String response = new String(responseBody, "UTF-8");
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", response);
                    message.setData(bundle);
                    callback.handleMessage(message);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void POST(final Context context, final TABLE tableEnum, final JSONObject jsonObject)
            throws UnsupportedEncodingException {
        final String urlString = buildURLString_POST(tableEnum);
        StringEntity entity = new StringEntity(jsonObject.toString());
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        client.post(context, urlString, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = "Error contacting server.";
                    if (statusCode == 200) {
                        response = new String(responseBody, "UTF-8");
                    }
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", response);
                    message.setData(bundle);
                    callback.handleMessage(message);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    
    public void DELETE(final Context context, final TABLE tableEnum, final JSONObject jsonObject)
            throws UnsupportedEncodingException {
        final String urlString = buildURLString_POST(tableEnum);
        StringEntity entity = new StringEntity(jsonObject.toString());
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        System.out.println("________DELETE___________");
        client.delete(context, urlString, entity, "application/json", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String response = "Error contacting server.";
                    if (statusCode == 200) {
                        response = new String(responseBody, "UTF-8");
                    }
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("response", response);
                    message.setData(bundle);
                    callback.handleMessage(message);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private String buildURLString_GET(final  TABLE tableEnum, final JSONObject jsonObject) {
        final String table = enumToString(tableEnum);
        String urlString = SERVER_URL + table + "?";
        try {
            Iterator<?> keys = jsonObject.keys();
            if(jsonObject.length() == 1 && jsonObject.has("user_id")){
                urlString += "user_id" + "=" + jsonObject.getString("user_id") + "&";
            } else {
                while (keys.hasNext()) {
                    final String key = keys.next().toString().trim();
                    final String val = jsonObject.get(key).toString().trim();
                    urlString += key + "=" + val + "&";
                }
            }
        } catch (JSONException e) {
            Log.wtf("Error:", "JSON Exception thrown");
        }
        return urlString.substring(0, urlString.length() - 1);
    }

    private String buildURLString_POST(final TABLE tableEnum) {
        return SERVER_URL + enumToString(tableEnum);
    }

    private String enumToString(TABLE table) {
        switch (table) {
            case USER:
                return "user";
            case EVENT:
                return "event";
            case EVENTS:
                return "events";
            case EMERGENCY_CONTACT:
                return "emergency_contact";
            case EVENT_SIGNUP:
                return "event_signup";
            case MAP_MARKER:
                return "map_marker";
            case LOCATION:
                return "location";
            case USERS:
                return "users";
            case ITEM:
                return "item";
            case ITEM_ASSIGN:
                return "assign_item";
            case ITEM_TYPE:
                return "item_types";
            case ITEMS:
                return "items";
            default:
                return null;
        }
    }
}
