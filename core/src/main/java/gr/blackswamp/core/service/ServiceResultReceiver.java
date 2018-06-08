package gr.blackswamp.core.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import java.util.ArrayList;
import java.util.List;

public class ServiceResultReceiver extends ResultReceiver {
    public static final int SUCCESS = 1;
    public static final int PROGRESS = 0;
    public static final int FAILURE = -1;

    private List<IServiceResult> _callbacks = new ArrayList<>();

    public ServiceResultReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        for (IServiceResult receiver : _callbacks) {
            ServiceReply reply = resultData.getParcelable(ServiceReply.TAG);
            receiver.got_result(resultCode, reply);
        }
    }

    public synchronized void register(IServiceResult callback) {
        _callbacks.add(callback);
    }

    public synchronized void unregister(IServiceResult callback) {
        _callbacks.remove(callback);
    }


}
