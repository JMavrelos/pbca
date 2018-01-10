package gr.blackswamp.pbca.service;

import java.util.HashMap;
import java.util.Map;

public enum WorkoutAction {
    invalid(0),
    start_working(1),
    resend(2),
    state(3),
    stop_working(4);
    private static Map Map = new HashMap<>();

    static {
        for (WorkoutAction action : WorkoutAction.values()) {
            Map.put(action._val, action);
        }
    }

    private int _val;

    WorkoutAction(int value) {
        _val = value;
    }

    public static WorkoutAction valueOf(int action) {
        return (WorkoutAction) Map.get(action);
    }

    public int val() {
        return _val;
    }
}
