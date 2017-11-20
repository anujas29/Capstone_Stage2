package anuja.project.finalproject.fragment;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import anuja.project.finalproject.MainActivity;
import anuja.project.finalproject.R;
import anuja.project.finalproject.data.ProductContract;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static anuja.project.finalproject.R.drawable.ic_favorite_black;
import static anuja.project.finalproject.R.drawable.ic_favorite_white;
import static anuja.project.finalproject.R.id.fab;

public class DetailActivity extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final int CURSOR_LOADER = 0;

    @BindView(R.id.main_Collapsing)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.main_backdrop)
    ImageView mBackdropImage;
    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.site_name)
    TextView mSite;
    @BindView(R.id.date)
    TextView mDate;
    @BindView(R.id.fab)
    FloatingActionButton mFabButton;
    @BindView(R.id.description)
    TextView mDescription;
    @BindView(R.id.price)
    TextView mPrice;
    @BindView(R.id.currency)
    TextView mCurrency;
    @BindView(R.id.currency_offer)
    TextView mOfferCurrency;
    @BindView(R.id.price_offer)
    TextView mPrice_offer;


    Uri mUrl;
    String UrlStr;
    private int mId = -1;
    private boolean favorite_flag;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "inside onActivityCreated.......");
        getLoaderManager().initLoader(CURSOR_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (mUrl != null) {
            Log.d(TAG, "inside onCreateLoader......");
            return new CursorLoader(getActivity(), mUrl,
                    null, null, null, null);
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {
            Log.d(TAG, "Inside onLoadFinished...... cursor data" + data);
            UrlStr = data.getString(data.getColumnIndex(ProductContract.ProductEntry.COLUMN_URL));
            mTitle.setText(data.getString(data.getColumnIndex(ProductContract.ProductEntry.COLUMN_TITLE)));
            mSite.setText(data.getString(data.getColumnIndex(ProductContract.ProductEntry.COLUMN_SITE)));
            String datestr = data.getString(data.getColumnIndex(ProductContract.ProductEntry.COLUMN_LASTCHANGE));
            String modifiedDate = datestr.substring(0, 10);
            mDate.setText(modifiedDate);
            mDescription.setText(data.getString(data.getColumnIndex(ProductContract.ProductEntry.COLUMN_DESCRIPTION)));
            mPrice.setText(data.getString(data.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRICE)));
            mCurrency.setText(data.getString(data.getColumnIndex(ProductContract.ProductEntry.COLUMN_CURRENCY)));
            mPrice_offer.setText(data.getString(data.getColumnIndex(ProductContract.ProductEntry.COLUMN_OFFER)));
            mOfferCurrency.setText(data.getString(data.getColumnIndex(ProductContract.ProductEntry.COLUMN_CURRENCY)));

            if (data.getString(data.getColumnIndex(ProductContract.ProductEntry.COLUMN_IMAGE)) != null) {
                Picasso.with(getActivity()).load(
                        data.getString(data.getColumnIndex(ProductContract.ProductEntry.COLUMN_IMAGE)))
                        .fit()
                        .into(mBackdropImage);
            } else {
                mBackdropImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.sale_default));
            }
            mCollapsingToolbarLayout.setTitle(data.getString(data.getColumnIndex(ProductContract.ProductEntry.COLUMN_TITLE)));
            favorite_flag = data.getInt(data.getColumnIndex(ProductContract.ProductEntry.COLUMN_FAV)) == 1;
            if (favorite_flag) {
                mFabButton.setImageResource(ic_favorite_black);
            }
            mId = data.getInt(data.getColumnIndex(ProductContract.ProductEntry._ID));

            getActivity().supportInvalidateOptionsMenu();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.e(TAG, "Inside onLoaderReset........");
        getLoaderManager().restartLoader(CURSOR_LOADER, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.detail_activity, container, false);
        ButterKnife.bind(this, rootView);

        if (getActivity().getClass().getSimpleName().equals("DetailFragment")) {
            mUrl = getActivity().getIntent().getData();
        }
        Log.d(TAG, "Inside onCreateView Url value = " + mUrl.toString());

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(!MainActivity.isTablet);
        return rootView;
    }


    @OnClick(fab)
    void onFabClicked() {
        ContentValues cv = new ContentValues();
        ContentResolver contentResolver = getActivity().getContentResolver();
        if (favorite_flag) {
            Log.d(TAG, "Remove from favourite....");
            cv.put(ProductContract.ProductEntry.COLUMN_FAV, 0);
            mFabButton.setImageResource(ic_favorite_white);
            Toast.makeText(getActivity(), getString(R.string.RemoveFromfab), Toast.LENGTH_LONG).show();
            favorite_flag = false;
        } else {
            Log.d(TAG, "Mark favourite....");
            cv.put(ProductContract.ProductEntry.COLUMN_FAV, 1);
            mFabButton.setImageResource(ic_favorite_black);
            Toast.makeText(getActivity(), getString(R.string.Addedasfab), Toast.LENGTH_LONG).show();
            favorite_flag = true;
        }

        contentResolver.update(ProductContract.ProductEntry.getUriWithId(mId),
                cv, null, null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.share, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "Inside onOptionsItemSelected....." + item.getItemId());
        int id = item.getItemId();
        switch (id) {
            case R.id.share_item: {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, UrlStr);
                intent.setType("text/plain");
                startActivity(intent);
                break;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
        return true;
    }
}
