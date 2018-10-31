package gr.blackswamp.pbca.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gr.blackswamp.pbca.App;
import gr.blackswamp.pbca.R;
import gr.blackswamp.pbca.main.MainActivity;
import gr.blackswamp.pbca.model.Punch;


public class WorkoutService extends Service {
    private static final String NAME = "workout_service";
    public static final String TAG = App.NAMESPACE + NAME;
    public static final String SERVICE_STATE_CHANGED = TAG + ".SERVICE_STATE_CHANGED";
    public static final String REQUEST = TAG + ".REQUEST";
    public static final String BROADCAST = TAG + ".BROADCAST";
    private static final int FOREGROUND_ID = 65;
    private static final String CHANNEL_ID = TAG;
    private final MediaPlayer _player = new MediaPlayer();
    private final Object _sync_token = new Object();


    //region tags to use to communicate with the outside world
    Random _rng;
    private int _interval;
    private int _repetitions;
    private int _sets;
    private PunchSets _punches;
    private Thread _main_thread;
    private boolean _working;
    private WorkoutBroadcast _latest;
    //endregion


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            stopSelf();
            return START_NOT_STICKY;
        }
        WorkoutRequest request = intent.getParcelableExtra(REQUEST);
        if (request == null) {
            stopSelf();
            return START_NOT_STICKY;
        }
        switch (request.get_action()) {
            case start_working:
                start_working(request);
                break;
            case state:
                if (_working)
                    send_broadcast(new WorkoutBroadcast(WorkoutStatus.started));
                else
                    send_broadcast(new WorkoutBroadcast(WorkoutStatus.finished));
                break;
            case resend:
                send_broadcast(_latest);
                break;
            case stop_working:
                stop_working();
                break;
        }
        return START_NOT_STICKY;
    }

    private synchronized void send_broadcast(final WorkoutBroadcast broadcast) {
        if (broadcast != null && !broadcast.equals(_latest) && broadcast.get_status() == WorkoutStatus.working)
            _latest = broadcast;
        Intent i = new Intent(SERVICE_STATE_CHANGED);
        i.putExtra(BROADCAST, broadcast);
        sendBroadcast(i);
    }

    private void start_working(final WorkoutRequest data) {
        if (data.get_sets() <= 0 || data.get_interval() < 0 || data.get_repetitions() < 0 || data.punches() == null || data.punches().size() == 0) {
            send_broadcast(new WorkoutBroadcast(getString(R.string.service_error_parsing_input_data)));
            return;
        }
        _interval = data.get_interval();
        _repetitions = data.get_repetitions();
        _sets = data.get_sets();
        _punches = new PunchSets(data.is_alternate(), data.punches());
        if (_main_thread == null || _main_thread.getState() == Thread.State.TERMINATED)
            _main_thread = new Thread(this::do_work);
        if (!_main_thread.isAlive())
            _main_thread.start();
    }

    private void do_work() {
        startForeground(FOREGROUND_ID, build_notification(""));
        try {
            _working = true;
            _rng = new Random(System.currentTimeMillis());
            int reps = _repetitions;
            send_broadcast(new WorkoutBroadcast(WorkoutStatus.started));
            do {
                Log.d(NAME, "do_work: repetition" + reps);
                final List<Punch> to_throw = new ArrayList<>();
                for (int cnt = 0; cnt < _sets; cnt++) {
                    final List<Punch> punches = _punches.next();
                    to_throw.add(punches.get(_rng.nextInt(punches.size())));
                }
                punch_selected(to_throw);

                try {
                    Thread.sleep(_interval);
                } catch (InterruptedException ignored) {
                }
                reps--;
            } while (_working && (reps > 0 || _repetitions == 0));
            send_broadcast(new WorkoutBroadcast(WorkoutStatus.finished));
        } finally {
            _working = false;
            stopForeground(true);
        }

    }

    private Notification build_notification(String punch) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        Intent stop_intent = new Intent(this, WorkoutService.class);
        stop_intent.putExtra(REQUEST, new WorkoutRequest().set_action(WorkoutAction.stop_working));
        PendingIntent pending_stop_intent = PendingIntent.getService(this, 0, stop_intent, PendingIntent.FLAG_UPDATE_CURRENT);

//        RemoteViews notification_view = new RemoteViews(getPackageName(), R.layout.notification);
//        notification_view.setOnClickPendingIntent(R.id.notification_stop, pending_stop_intent);
//        notification_view.setTextViewText(R.id.notification_title, punch);

        PendingIntent pendingActivityIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setOngoing(true)
                .setOnlyAlertOnce(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.punching))
                .setContentIntent(pendingActivityIntent)
                .addAction(R.drawable.ic_stop, getString(R.string.stop), pending_stop_intent);
        return builder.build();
    }

    private void punch_selected(final List<Punch> to_throw) {
        List<Integer> punch_numbers = new ArrayList<>();
        for (Punch punch : to_throw)
            punch_numbers.add(punch.get_number());
        send_broadcast(new WorkoutBroadcast(punch_numbers));

        StringBuilder text = new StringBuilder();
        for (Punch i : to_throw)
            text.append(i).append(',');
        startForeground(FOREGROUND_ID, build_notification(text.toString()));

        for (Punch punch : to_throw) {
            _player.setOnCompletionListener(this::playback_finished);

            try {
                AssetFileDescriptor file = getApplicationContext().getAssets().openFd(punch.get_file());
                _player.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                _player.prepare();
                _player.start();
                synchronized (_sync_token) {
                    try {
                        _sync_token.wait();
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                        return;
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        }
    }

    private void playback_finished(MediaPlayer mediaPlayer) {
        synchronized (_sync_token) {
            mediaPlayer.reset();
            _sync_token.notify();
        }
    }


    void send_error(final String message, final Exception e) {
        Log.e("", e.getMessage(), e);
        send_broadcast(new WorkoutBroadcast(message));
    }

    private void stop_working() {
        _working = false;
        if (_main_thread != null)
            _main_thread.interrupt();
        stopSelf();
    }


    static class PunchSets {
        int _current = 2; //this way it will always trigger the reset on the first next loop
        List<List<Punch>> _sets = new ArrayList<>();

        PunchSets(boolean alternate, List<Punch> all_punches) {
            if (!alternate) {
                _sets.add(all_punches);
            } else {
                List<Punch> rights = new ArrayList<>();
                List<Punch> lefts = new ArrayList<>();
                for (Punch punch : all_punches) {
                    if (punch.is_left()) {
                        lefts.add(punch);
                    } else {
                        rights.add(punch);
                    }
                }
                if (lefts.size() > 0)
                    _sets.add(lefts);
                if (rights.size() > 0)
                    _sets.add(rights);
            }
        }

        List<Punch> next() {
            _current++;
            if (_current >= _sets.size())
                _current = 0;
            return _sets.get(_current);
        }

    }

}
