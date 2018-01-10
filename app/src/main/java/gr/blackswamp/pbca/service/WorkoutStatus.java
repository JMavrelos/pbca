package gr.blackswamp.pbca.service;

import java.util.HashMap;
import java.util.Map;

public enum WorkoutStatus {
    invalid(0),
    started(1),
    working(2),
    finished(3),
    failure(4);

    private static Map EnumMap = new HashMap();

    static {
        for (WorkoutStatus workoutStatus : WorkoutStatus.values()) {
            EnumMap.put(workoutStatus._value, workoutStatus);
        }
    }

    private int _value;

    WorkoutStatus(int value) {
        _value = value;
    }

    public static WorkoutStatus valueOf(int value) {
        return (WorkoutStatus) EnumMap.get(value);
    }

    public int value() {
        return _value;
    }

}
