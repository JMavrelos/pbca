package gr.blackswamp.pbca;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gr.blackswamp.pbca.model.Punch;

import static gr.blackswamp.pbca.App.NAMESPACE;

public class Settings {
    private static final String PUNCHES = NAMESPACE + "punches";
    private static final String SETS = NAMESPACE + "sets";
    private static final String INTERVAL = NAMESPACE + "interval";
    private static final String ALTERNATE_HANDS = NAMESPACE + "alternate_hands";
    private static final String REPETITIONS = NAMESPACE + "repetitions";

    //region cached values
    private Boolean _alternate_hands = null;
    private List<Integer> _punches = null;
    private Integer _sets = null;
    private Integer _interval = null;
    private Integer _repetitions = null;
    //endregion
    private SharedPreferences _prefs;

    Settings(Context ctx) {
        _prefs = ctx.getSharedPreferences("base_preferences", Context.MODE_PRIVATE);
    }


    public synchronized boolean is_alternate_hands() {
        if (_alternate_hands == null)
            _alternate_hands = _prefs.getBoolean(ALTERNATE_HANDS, false);
        return _alternate_hands;
    }

    public synchronized void set_alternate_hands(boolean alternate_hands) {
        _alternate_hands = alternate_hands;
        _prefs.edit().putBoolean(ALTERNATE_HANDS, _alternate_hands).apply();
    }

    public synchronized List<Integer> get_punches() {
        if (_punches == null) {
            _punches = new ArrayList<>();
            Set<String> data = _prefs.getStringSet(PUNCHES, new HashSet<>(Arrays.asList("1", "2", "3", "4", "5", "6")));
            for (String num : data) {
                try {
                    _punches.add(Integer.parseInt(num));
                } catch (Exception ignored) {
                }
            }
        }
        return _punches;
    }

    public synchronized void set_punches(List<Punch> punches) {
        _punches = new ArrayList<>();
        Set<String> str = new HashSet<>();
        for (Punch punch : punches) {
            str.add(String.valueOf(punch.get_number()));
            _punches.add(punch.get_number());
        }
        _prefs.edit().putStringSet(PUNCHES, str).apply();
    }

    public synchronized int get_sets() {
        if (_sets == null)
            _sets = _prefs.getInt(SETS, 1);
        return _sets;
    }

    public synchronized void set_sets(int sets) {
        _sets = sets;
        _prefs.edit().putInt(SETS, _sets).apply();
    }

    public synchronized int get_interval() {
        if (_interval == null)
            _interval = _prefs.getInt(INTERVAL, 500);
        return _interval;
    }

    public synchronized void set_interval(int interval) {
        _interval = interval;
        _prefs.edit().putInt(INTERVAL, _interval).apply();
    }

    public synchronized int get_repetitions() {
        if (_repetitions == null)
            _repetitions = _prefs.getInt(REPETITIONS, 0);
        return _repetitions;
    }

    public synchronized void set_repetitions(int repetitions) {
        _repetitions = repetitions;
        _prefs.edit().putInt(REPETITIONS, _repetitions).apply();
    }
}

