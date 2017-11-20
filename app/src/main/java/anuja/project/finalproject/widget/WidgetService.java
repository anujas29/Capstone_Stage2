package anuja.project.finalproject.widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import anuja.project.finalproject.R;
import anuja.project.finalproject.data.ProductContract;

/**
 * Created by USER on 08-11-2017.
 */

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor CursorData = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                System.out.println("----------------------- T1-------------------------------------------");
                if (CursorData != null) {
                    CursorData.close();
                }

                final long token = Binder.clearCallingIdentity();
                CursorData = getContentResolver().query(
                        ProductContract.ProductEntry.CONTENT_URI,
                        ProductContract.ProductEntry.WIDGET_COLUMNS,
                        null,
                        null,
                        null
                );
                Binder.restoreCallingIdentity(token);
            }

            @Override
            public void onDestroy() {
                System.out.println("--------------------------- T2---------------------------------------");
                if (CursorData != null) {
                    CursorData.close();
                    CursorData = null;
                }
            }

            @Override
            public int getCount() {
                System.out.println("------------------------ t3------------------------------------------" + CursorData.getCount());
                return CursorData == null ? 0 : CursorData.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                System.out.println("-------------------------- t4----------------------------------------");
                if (position == AdapterView.INVALID_POSITION || CursorData == null || !CursorData.moveToPosition(position)) {
                    return null;
                }
                RemoteViews remote_views = new RemoteViews(getPackageName(),
                        R.layout.widget_item);
                remote_views.setTextViewText(R.id.product, CursorData.getString(CursorData.getColumnIndex(ProductContract.ProductEntry.COLUMN_TITLE)));
                final Intent view_intent = new Intent();
                view_intent.setData(ProductContract.ProductEntry.CONTENT_URI);
                remote_views.setOnClickFillInIntent(R.id.product_list, view_intent);

                return remote_views;
            }

            @Override
            public int getViewTypeCount() {
                System.out.println("------------------------ t6------------------------------------------");

                return 1;
            }

            @Override
            public RemoteViews getLoadingView() {
                System.out.println("------------------------- t5-----------------------------------------");
                return new RemoteViews(getPackageName(), R.layout.widget_item);
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public long getItemId(int position) {
                System.out.println("------------------------- t7-----------------------------------------");
                if (CursorData.moveToPosition(position))
                    return CursorData.getInt(CursorData.getColumnIndex(ProductContract.ProductEntry._ID));
                return position;
            }


        };
    }
}