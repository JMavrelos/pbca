package gr.blackswamp.pbca.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import gr.blackswamp.core.functions.UniAction;

public class WorkoutReceiver extends BroadcastReceiver {
    List<UniAction<Intent>> callbacks = new ArrayList<>();


    @Override
    public void onReceive(Context context, Intent intent) {
        for (UniAction<Intent> receiver : callbacks) {
            receiver.call(intent);
        }
    }

    public void add_receiver(UniAction<Intent> function) {
        callbacks.add(function);
    }

    public void remove_receiver(UniAction<Intent> function) {
        callbacks.remove(function);
    }
}
