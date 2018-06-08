package gr.blackswamp.core.service;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class ServiceReply implements Parcelable {
    public static final String TAG = "ServiceReply";

    public static final Creator<ServiceReply> CREATOR = new Creator<ServiceReply>() {
        @Override
        public ServiceReply createFromParcel(Parcel in) {
            return new ServiceReply(in);
        }

        @Override
        public ServiceReply[] newArray(int size) {
            return new ServiceReply[size];
        }
    };
    private final int _id;
    private final String _error;
    private final Bundle _data;

    private ServiceReply(Parcel in) {
        _id = in.readInt();
        _error = in.readString();
        _data = in.readBundle(getClass().getClassLoader());
    }

    ServiceReply(int id, String message, Bundle data) {
        _id = id;
        _error = message;
        _data = data;
    }

    ServiceReply(int id, Bundle data) {
        this(id, null, data);
    }

    ServiceReply(int id) {
        this(id, null, null);
    }

    public int get_id() {
        return _id;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(_id);
        dest.writeString(_error);
        dest.writeBundle(_data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Bundle get_data() {
        return  _data;
    }

    public String get_error() {
        return _error;
    }
}
