package anuja.project.finalproject;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import anuja.project.finalproject.adapters.MyPagerAdapter;
import anuja.project.finalproject.adapters.SaleAdapter;
import anuja.project.finalproject.data.ProductContract;
import anuja.project.finalproject.fragment.DetailFragment;
import anuja.project.finalproject.fragment.FavouriteFragment;
import anuja.project.finalproject.fragment.LocalSaleFragment;
import anuja.project.finalproject.fragment.SaleFragment;
import anuja.project.finalproject.sync.SyncAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SaleAdapter.CallBack {

    private static final String TAG = "MainActivity";
    public static boolean isTablet = false;

    @BindView(R.id.tabs)
    TabLayout mTabs;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    private MyPagerAdapter mMyPagerAdapter;

    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitleList = new ArrayList<>();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        setupMyPager(mViewPager);
        mTabs.setupWithViewPager(mViewPager);
        Cursor countCursor = this.getContentResolver().query(ProductContract.ProductEntry.CONTENT_URI,
                new String[]{"count(*) AS count"},
                null,
                null,
                null);
        countCursor.moveToFirst();
        int count = countCursor.getInt(0);
        Log.e(TAG, "No of data present... = " + count);

        if (count == 0) {
            Log.e(TAG, "Calling Sync.....");
            SyncAdapter.Sync(this);
        } else {
            Log.e(TAG, "Calling initializeAdapter.....");
            SyncAdapter.initializeAdapter(this);
        }

        //  Analytics tracking started
        ((AnalyticsApplication) getApplication()).startTracking();

    }

    private void setupMyPager(ViewPager viewPager) {

        mFragmentList.add(new SaleFragment());
        mFragmentList.add(new LocalSaleFragment());
        mFragmentList.add(new FavouriteFragment());
        mFragmentTitleList.add("Sale");
        mFragmentTitleList.add("Local Sale");
        mFragmentTitleList.add("Favourites");
        mMyPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), mFragmentList, mFragmentTitleList);
        viewPager.setAdapter(mMyPagerAdapter);
    }

    @Override
    public void ItemSelected(Uri selectedUri) {
        startActivity(new Intent(MainActivity.this, DetailFragment.class).
                setData(selectedUri));
    }

}
