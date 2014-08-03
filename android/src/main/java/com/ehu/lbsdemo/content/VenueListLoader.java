package com.ehu.lbsdemo.content;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.ehu.lbsdemo.io.FourSquareService;
import com.ehu.lbsdemo.model.Venue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Copyright James Deng 2014.
 */
public class VenueListLoader extends AsyncTaskLoader<List<Venue>> {
    private double lat, lng;
    private String cat;

    public VenueListLoader(Context context, double lat, double lng, String cat) {
        super(context);
        this.lat = lat;
        this.lng = lng;
        this.cat = cat;
        onContentChanged();
    }

    /**
     */
    @Override
    public List<Venue> loadInBackground() {
        List<Venue> res = null;
        try {
             /*
            InputStream stream = getResources().openRawResource(R.raw.venues);
            Reader reader = new InputStreamReader(stream);

            Type venueList = new TypeToken<ArrayList<Venue>>() {}.getType();
            mVenues = (ArrayList<Venue>) new Gson().fromJson(reader, venueList);
            */
            res = FourSquareService.searchNearbyVenues(lat, lng, cat);
        } catch (IOException e) {
            e.printStackTrace();
            res = new ArrayList<Venue>();
        }
        Collections.sort(res, DISTANCE_COMPARATOR);
        return res;
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
    }

    @Override
    protected void onStopLoading() {
        // Attempts to cancel the current load task if possible
        cancelLoad();
    }

    private final static Comparator<Venue> DISTANCE_COMPARATOR = new Comparator<Venue>() {
        @Override
        public int compare(Venue lhs, Venue rhs) {
            return (int) (lhs.location.distance - rhs.location.distance);
        }
    };
}
