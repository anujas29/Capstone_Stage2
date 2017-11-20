package anuja.project.finalproject.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import anuja.project.finalproject.R;
import anuja.project.finalproject.adapters.SaleAdapter;
import anuja.project.finalproject.data.ProductContract;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by USER on 17-10-2017.
 */

public class SaleFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @BindView(R.id.sale_recycler_view)
    RecyclerView recyclerView;

    private SaleAdapter mSaleAdapter;
    private LinearLayoutManager mLayout;
    private static final int CURSOR_LOADER = 0;

    public SaleFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.sale_recycler_view, container, false);
        ButterKnife.bind(this, rootView);

        mSaleAdapter = new SaleAdapter(getActivity(), null);
        recyclerView.setAdapter(mSaleAdapter);

        mLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayout);
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.e(TAG, " Inside OnCreateLoader");
        Uri uri = ProductContract.ProductEntry.CONTENT_URI;
        return new CursorLoader(getActivity()
                , uri
                , ProductContract.ProductEntry.SALE_COLUMNS
                , ProductContract.ProductEntry.COLUMN_GLOBAL_SALE + " = ?"
                , new String[]{String.valueOf(1)}
                , ProductContract.ProductEntry._ID + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.e(TAG, " Inside onLoadFinished datasize = " + data.getCount());
        mSaleAdapter.swap(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.e(TAG, " Inside onLoaderReset");
        mSaleAdapter.swap(null);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }


}
