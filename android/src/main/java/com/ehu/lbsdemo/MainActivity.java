package com.ehu.lbsdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ehu.lbsdemo.content.VenueListLoader;
import com.ehu.lbsdemo.model.Venue;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new VenueListFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class VenueListFragment extends ListFragment
            implements LoaderManager.LoaderCallbacks<List<Venue>> {

        private String category = "4bf58dd8d48988d1e0931735"; // coffee id
        private double lat = -33.884095, lng = 151.20625; // default to central station
        private VenueAdapter mAdapter;

        /**
         * This method will be called when an item in the list is selected.
         * Subclasses should override. Subclasses can call
         * getListView().getItemAtPosition(position) if they need to access the
         * data associated with the selected item.
         *
         * @param l        The ListView where the click happened
         * @param v        The view that was clicked within the ListView
         * @param position The position of the view in the list
         * @param id       The row id of the item that was clicked
         */
        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);
        }

        /**
         * Called when the fragment's activity has been created and this
         * fragment's view hierarchy instantiated.  It can be used to do final
         * initialization once these pieces are in place, such as retrieving
         * views or restoring state.  It is also useful for fragments that use
         * {@link #setRetainInstance(boolean)} to retain their instance,
         * as this callback tells the fragment when it is fully associated with
         * the new activity instance.  This is called after {@link #onCreateView}
         * and before {@link #onViewStateRestored(android.os.Bundle)}.
         *
         * @param savedInstanceState If the fragment is being re-created from
         *                           a previous saved state, this is the state.
         */
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Give some text to display if there is no data.  In a real
            // application this would come from a resource.
            setEmptyText("No Venues");

            // We have a menu item to show in action bar.
            setHasOptionsMenu(true);

            mAdapter = new VenueAdapter(getActivity());
            setListAdapter(mAdapter);

            // Start out with a progress indicator.
            setListShown(false);

            // Prepare the loader.  Either re-connect with an existing one,
            // or start a new one.
            getLoaderManager().initLoader(0, null, this);
        }

        /**
         * Instantiate and return a new Loader for the given ID.
         *
         * @param id   The ID whose loader is to be created.
         * @param args Any arguments supplied by the caller.
         * @return Return a new Loader instance that is ready to start loading.
         */
        @Override
        public Loader<List<Venue>> onCreateLoader(int id, Bundle args) {
            return new VenueListLoader(getActivity(), lat, lng, category);
        }

        @Override
        public void onLoadFinished(Loader<List<Venue>> loader, List<Venue> data) {
            // set the new data.
            mAdapter.clear();
            if (data != null) {
                mAdapter.addAll(data);
            }
            // The list should now be shown.
            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }

        /**
         * Called when a previously created loader is being reset, and thus
         * making its data unavailable.  The application should at this point
         * remove any references it has to the Loader's data.
         *
         * @param loader The Loader that is being reset.
         */
        @Override
        public void onLoaderReset(Loader<List<Venue>> loader) {
            mAdapter.clear();
        }
    }

    public static class VenueAdapter extends ArrayAdapter<Venue> {
        /**
         * Constructor
         *
         * @param context The current context.
         */
        public VenueAdapter(Context context) {
            super(context, R.layout.venue);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView;
            if (convertView == null) {
                rowView = inflater.inflate(R.layout.venue, parent, false);
            } else {
                rowView = convertView;
            }
            TextView nameView = (TextView) rowView.findViewById(R.id.name);
            TextView distanceView = (TextView) rowView.findViewById(R.id.distance);
            TextView addressView = (TextView) rowView.findViewById(R.id.address);

            Venue v = getItem(position);
            nameView.setText(v.name);
            distanceView.setText(v.location.distance + "m");
            addressView.setText(v.location.address);
            return rowView;
        }
    }
}
