package anuja.project.finalproject.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import anuja.project.finalproject.MainActivity;
import anuja.project.finalproject.R;
import anuja.project.finalproject.sync.SyncAdapter;

/**
 * Created by USER on 08-11-2017.
 */

public class WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context con, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remote_views = new RemoteViews(con.getPackageName(), R.layout.widget_layout);

            Intent intent = new Intent(con, MainActivity.class);
            PendingIntent Pend_intent = PendingIntent.getActivity(con, 0, intent, 0);
            remote_views.setOnClickPendingIntent(R.id.widget_header, Pend_intent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                remote_views.setRemoteAdapter(R.id.widget_list, new Intent(con, WidgetService.class));
            } else {
                remote_views.setRemoteAdapter(0, R.id.widget_list, new Intent(con, WidgetService.class));
            }
            appWidgetManager.updateAppWidget(appWidgetId, remote_views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (SyncAdapter.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
        }
    }
}
