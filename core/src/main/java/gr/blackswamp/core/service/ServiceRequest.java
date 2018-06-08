package gr.blackswamp.core.service;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.util.Log;

import gr.blackswamp.core.os.Bundles;

public class ServiceRequest implements Parcelable {
    public static final String TAG = "ServiceRequest";
    public static final Creator<ServiceRequest> CREATOR = new Creator<ServiceRequest>() {
        @Override
        public ServiceRequest createFromParcel(Parcel in) {
            return new ServiceRequest(in);
        }

        @Override
        public ServiceRequest[] newArray(int size) {
            return new ServiceRequest[size];
        }
    };
    private final String _command;
    private final int _request_id;
    private final ResultReceiver _receiver;
    private Bundle _data;

    public ServiceRequest(int request_id, String command, ServiceResultReceiver receiver, Bundle data) {
        _command = command;
        _request_id = request_id;
        _receiver = receiver;
        _data = data;
    }

    //region parcelable
    private ServiceRequest(Parcel in) {
        _command = in.readString();
        _request_id = in.readInt();
        _receiver = in.readParcelable(ServiceResultReceiver.class.getClassLoader());
        _data = in.readBundle(getClass().getClassLoader());//in.readParcelable(Bundle.class.getClassLoader());
    }

    public String get_command() {
        return _command;
    }
    public Bundle get_data() {
        return _data;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_command);
        dest.writeInt(_request_id);
        dest.writeParcelable(_receiver, flags);
        dest.writeBundle(_data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //endregion

    public void send_success() {
        Log.d(TAG, "success for " + String.valueOf(_request_id));
        if (_receiver != null)
            _receiver.send(ServiceResultReceiver.SUCCESS, Bundles.Create().put_parcelable(ServiceReply.TAG, new ServiceReply(_request_id)).get());
    }

    public void send_success(Bundle data) {
        Log.d(TAG, "success for " + String.valueOf(_request_id));
        if (_receiver != null)
            _receiver.send(ServiceResultReceiver.SUCCESS, Bundles.Create().put_parcelable(ServiceReply.TAG, new ServiceReply(_request_id, data)).get());
    }

    public void send_progress(Bundle data) {
        Log.d(TAG, "progress for " + String.valueOf(_request_id));
        if (_receiver != null)
            _receiver.send(ServiceResultReceiver.PROGRESS, Bundles.Create().put_parcelable(ServiceReply.TAG, new ServiceReply(_request_id, data)).get());
    }

    public void send_error(Exception e) {
        Log.e(TAG, e.getMessage(), e);
        if (_receiver != null)
            _receiver.send(ServiceResultReceiver.FAILURE, Bundles.Create().put_parcelable(ServiceReply.TAG, new ServiceReply(_request_id, e.getMessage(), _data)).get());
    }

}
