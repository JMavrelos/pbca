package gr.blackswamp.pbca.service;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import gr.blackswamp.pbca.model.Punch;

public class WorkoutRequest implements Parcelable {
    public static final Creator<WorkoutRequest> CREATOR = new Creator<WorkoutRequest>() {
        @Override
        public WorkoutRequest createFromParcel(Parcel in) {
            return new WorkoutRequest(in);
        }

        @Override
        public WorkoutRequest[] newArray(int size) {
            return new WorkoutRequest[size];
        }
    };
    private WorkoutAction _action = WorkoutAction.invalid;
    private boolean _alternate = false;
    private int _interval = 0;
    private int _repetitions = 0;
    private int _sets = 0;
    private List<Punch> _punches = new ArrayList<>();

    public WorkoutRequest() {
    }

    private WorkoutRequest(Parcel in) {
        _action = (WorkoutAction) in.readSerializable();
        _alternate = in.readByte() != 0;
        _interval = in.readInt();
        _repetitions = in.readInt();
        _sets = in.readInt();
        _punches = in.createTypedArrayList(Punch.CREATOR);
    }

    WorkoutAction get_action() {
        return _action;
    }

    public WorkoutRequest set_action(WorkoutAction _action) {
        this._action = _action;
        return this;
    }

    boolean is_alternate() {
        return _alternate;
    }

    public WorkoutRequest set_alternate(boolean _alternate) {
        this._alternate = _alternate;
        return this;
    }

    int get_interval() {
        return _interval;
    }

    public WorkoutRequest set_interval(int _interval) {
        this._interval = _interval;
        return this;
    }

    int get_repetitions() {
        return _repetitions;
    }

    public WorkoutRequest set_repetitions(int _repetitions) {
        this._repetitions = _repetitions;
        return this;
    }

    int get_sets() {
        return _sets;
    }

    public WorkoutRequest set_sets(int _sets) {
        this._sets = _sets;
        return this;
    }

    public List<Punch> punches() {
        return _punches;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(_action);
        dest.writeByte((byte) (_alternate ? 1 : 0));
        dest.writeInt(_interval);
        dest.writeInt(_repetitions);
        dest.writeInt(_sets);
        dest.writeTypedList(_punches);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
