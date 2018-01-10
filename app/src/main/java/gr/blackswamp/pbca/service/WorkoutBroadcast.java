package gr.blackswamp.pbca.service;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import gr.blackswamp.pbca.App;

public class WorkoutBroadcast implements Parcelable {
    public static final Creator<WorkoutBroadcast> CREATOR = new Creator<WorkoutBroadcast>() {
        @Override
        public WorkoutBroadcast createFromParcel(Parcel in) {
            return new WorkoutBroadcast(in);
        }

        @Override
        public WorkoutBroadcast[] newArray(int size) {
            return new WorkoutBroadcast[size];
        }
    };
    private static final String TAG = App.NAMESPACE + "workout_response";
    private WorkoutStatus _status = WorkoutStatus.invalid;
    private String _message;
    private List<Integer> _current_set = new ArrayList<>();

    WorkoutBroadcast(WorkoutStatus status) {
        this._status = status;
    }

    WorkoutBroadcast(List<Integer> punch_set) {
        this._status = WorkoutStatus.working;
        this._current_set = punch_set;
    }

    WorkoutBroadcast(String _message) {
        this._status = WorkoutStatus.failure;
        this._message = _message;
    }


    private WorkoutBroadcast(Parcel in) {
        _message = in.readString();
        _status = (WorkoutStatus) in.readSerializable();
        in.readList(_current_set, Integer.class.getClassLoader());
    }

    public static WorkoutBroadcast From(Bundle data) {
        return data.getParcelable(TAG);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_message);
        dest.writeSerializable(_status);
        dest.writeList(_current_set);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public WorkoutStatus get_status() {
        return _status;
    }

    public void set_status(WorkoutStatus _status) {
        this._status = _status;
    }

    public String get_message() {
        return _message;
    }

    public void set_message(String _message) {
        this._message = _message;
    }

    public List<Integer> current_set() {
        return _current_set;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkoutBroadcast that = (WorkoutBroadcast) o;

        return _status == that._status && (_message != null ? _message.equals(that._message) : that._message == null) && _current_set.equals(that._current_set);
    }

    @Override
    public int hashCode() {
        int result = _status.hashCode();
        result = 31 * result + (_message != null ? _message.hashCode() : 0);
        result = 31 * result + _current_set.hashCode();
        return result;
    }

}
