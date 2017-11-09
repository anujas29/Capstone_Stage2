package anuja.project.finalproject.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import anuja.project.finalproject.R;
import anuja.project.finalproject.sync.SyncAdapter;

/**
 * Created by USER on 08-11-2017.
 */

public class SettingsFragment  extends PreferenceFragment implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener
        ,Preference.OnPreferenceChangeListener,SharedPreferences.OnSharedPreferenceChangeListener{

    String TAG = SettingsFragment.class.getSimpleName();
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocation;
    public static final int REQUEST_SETTINGS = 0x1;

    Context mContext;
    Preference Location;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.location_pref);
        mContext = getActivity();

        //set up google api
        Log.e(TAG, "setting the api onCreate");
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        Location= findPreference(getString(R.string.location));
        Location.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(!mGoogleApiClient.isConnected()) {
                    Log.e(TAG, "locatio preference is clicked .. connecting the api");
                    mGoogleApiClient.connect();// connect api
                }else{
                    Log.e(TAG, "locatio preference is clicked .. api is connected already");
                    SettingRequest();
                }
                return true;
            }
        });
        locatioPreferenceToValue(Location);

    }

    @Override
    public void onStart() {//connects our google api
        super.onStart();
        // mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        sp.registerOnSharedPreferenceChangeListener(this);
        super.onResume();
    }

    @Override
    public void onStop() {// disconnects api
        if(mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onPause() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        sp.unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.location))){
            Log.e(TAG, " syncImmediately from onSharedPreferenceChanged ");
            locatioPreferenceToValue(Location);// update the location preference summary with the new value added
            SyncAdapter.Sync(getActivity());
        }
    }

    private void locatioPreferenceToValue(Preference preference){
        //set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);
        String key = preference.getKey();
        String value= PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getString(key,getString(R.string.default_location));
        if(key.equals(getString(R.string.location))){
            preference.setSummary(value);
        }

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Log.e(TAG, " onPreferenceChanged is called ");
        String stringValue = newValue.toString();
        if(preference.getKey().equals(getString(R.string.location))){
            preference.setSummary(stringValue);
        }


        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG,"OnActivityResult");
        switch (requestCode) {
// Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG,"Activity Result is ok .. requestLocationUpdates is fired");
                        UpdatesLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG,"Activity Result is canceled ..call settingRequest to fire a dialog again!");

                        //settingsRequest();
                        break;
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.e(TAG, "inside onConnected ");
        mLocation = LocationRequest.create();
        mLocation.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        mLocation.setInterval(1000);
        SettingRequest();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void SettingRequest(){
        Log.e(TAG,"settingRequest");
        LocationSettingsRequest.Builder Locationbuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest( mLocation);
        Locationbuilder.setAlwaysShow(true);//Whether or not location is required by the calling app in order to continue.This changes the wording/appearance of the dialog accordingly.

        PendingResult<LocationSettingsResult> pending_result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,Locationbuilder.build());
        pending_result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status location_status = locationSettingsResult.getStatus();

                switch (location_status.getStatusCode()){
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.e(TAG,"Location Success ..");
                        UpdatesLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.e(TAG,"Location Required! .. ");
                        try {
                            location_status.startResolutionForResult(getActivity(),REQUEST_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });

    }

    private void UpdatesLocation(){
        Log.e(TAG,"RequestLocationUpdates is called");
        if (PermissionChecker.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            Log.e(TAG,"Check Permissions");
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocation, this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "inside onConnectionFailed");

    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        Log.e(TAG ,"OnLOcationChanged called");
        Log.e(TAG ,"Location : " + location.toString());

        //-- getting the country name form the location object
        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0)
        {
            String currentName=addresses.get(0).getCountryCode();

            Log.e(TAG,"Update the sharedPreferences with the location "+currentName);
            PreferenceManager.getDefaultSharedPreferences(mContext).edit()
                    .putString(getString(R.string.location),currentName).apply();
        }

    }
}
