package anuja.project.finalproject.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import anuja.project.finalproject.R;

/**
 * Created by USER on 20-10-2017.
 */

public class DetailFragment extends AppCompatActivity {

    String TAG = DetailFragment.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Inside onCreate......");
        setContentView(R.layout.detail_fragment);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.detailFragment, new DetailActivity()).commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
