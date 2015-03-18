package id.zelory.hipwee.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import java.util.ArrayList;

import id.zelory.hipwee.R;
import id.zelory.hipwee.activity.ReadActivity;
import id.zelory.hipwee.model.Post;
import id.zelory.hipwee.utils.Scraper;
import id.zelory.hipwee.utils.Utils;

public class NotificationReceiver extends BroadcastReceiver {
    public NotificationReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("zet", "Memulai");
        new DownloadData(context).execute();
    }

    private class DownloadData extends AsyncTask<Void, Void, Void> {
        Context context;
        ArrayList<Post> posts;

        public DownloadData(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            while (posts == null) {
                posts = Scraper.random();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (posts.size() > 1) {
                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Post post = posts.get(Utils.randInt(0, posts.size() - 1));
                Intent intent = new Intent(context, ReadActivity.class);
                intent.putExtra("url", post.getAlamat());
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                Notification mNotification = new NotificationCompat.Builder(context)
                        .setContentTitle("hipwee")
                        .setContentText(Html.fromHtml(post.getJudul()))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                        .setContentIntent(pIntent)
                        .setSound(soundUri)
                        .setVibrate(new long[]{1000, 1000, 1000})
                        .setLights(Color.argb(100, 170, 56, 58), 3000, 3000)
                        .build();

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                mNotification.flags |= Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(0, mNotification);
            }
        }
    }
}
