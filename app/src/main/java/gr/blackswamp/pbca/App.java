package gr.blackswamp.pbca;


import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import gr.blackswamp.core.functions.UniAction;
import gr.blackswamp.pbca.service.WorkoutReceiver;
import gr.blackswamp.pbca.service.WorkoutService;

public class App extends Application {
    public static final String NAMESPACE = "gr.blackswamp.pbca.";
    static final WorkoutReceiver RECEIVER = new WorkoutReceiver();
    static Settings _settings;

    public static Settings Settings() {
        return _settings;
    }


    public static void RegisterReceiver(UniAction<Intent> receiver) {
        RECEIVER.add_receiver(receiver);
    }

    public static void UregisterReceiver(UniAction<Intent> receiver){
        RECEIVER.remove_receiver(receiver);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        _settings = new Settings(getApplicationContext());
        getApplicationContext().registerReceiver(RECEIVER, new IntentFilter(WorkoutService.SERVICE_STATE_CHANGED));
    }
}
