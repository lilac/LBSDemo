package com.ehu.lbsdemo.io;

/**
 * Copyright James Deng 2014.
 */

import android.net.Uri;
import android.text.TextUtils;

import com.ehu.lbsdemo.model.Venue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

public class FourSquareService {
    private static String CLIENT_ID = "ACAO2JPKM1MXHQJCK45IIFKRFR2ZVL0QASMCBCG5NPJQWF2G";
    private static String CLIENT_SECRET = "YZCKUYJ1WHUV2QICBXUBEILZI1DMPUIDP5SHV043O04FKBHL";

    public static ArrayList<Venue> searchNearbyVenues(double latitude, double longitude, String category) throws IOException {
        Uri.Builder builder = new Uri.Builder().scheme("https")
                .authority("api.foursquare.com")
                .path("v2/venues/search")
                .appendQueryParameter("ll", String.valueOf(latitude) + ',' + String.valueOf(longitude))
                .appendQueryParameter("client_id", CLIENT_ID)
                .appendQueryParameter("client_secret", CLIENT_SECRET)
                .appendQueryParameter("v", "20140803");
        if (!TextUtils.isEmpty(category)) {
            builder.appendQueryParameter("categoryId", category);
        }
        ArrayList<Venue> res = new ArrayList<Venue>();
        Uri uri = builder.build();
        String str = null;
        if (uri != null) {
            str = uri.toString();
        }
        URL url = new URL(str);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

        SSLContext context;
        try {
            context = SSLContext.getDefault();
            con.setSSLSocketFactory(context.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        InputStream stream = con.getInputStream();

        JsonReader jsonReader = new JsonReader(new InputStreamReader(stream));
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            if (name.equals("response")) {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String s = jsonReader.nextName();
                    if (s.equals("venues")) {
                        Type venueList = new TypeToken<ArrayList<Venue>>() {
                        }.getType();
                        return new Gson().fromJson(jsonReader, venueList);
                    } else {
                        jsonReader.skipValue();
                    }
                }
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        jsonReader.close();
        stream.close();
        con.disconnect();
        return res;
    }
}
