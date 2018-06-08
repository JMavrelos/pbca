package gr.blackswamp.core.collections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Query<T> {
    private final List<T> _items;
    private final Class<T> _class;
    private List<Condition> _conditions = new ArrayList<>();
    private HashMap<String, Field> _fields = new HashMap<>();
    private HashMap<String, Method> _methods = new HashMap<>();

    public Query(Class<T> cl, List<T> items) {
        _items = items;
        _class = cl;
    }

    public Query<T> and() {
        _conditions.add(new Condition(ConditionType.and));
        return this;
    }


    public Query<T> or() {
        _conditions.add(new Condition(ConditionType.or));
        return this;
    }

    public Query<T> equals(String field, String value) {
        _conditions.add(new Condition(ConditionType.eq, field, value));
        return this;
    }

    public List<T> to_list() {
        List<T> out = new ArrayList<T>();
        for (T item : _items) {
            if (allowed(item)) {
                out.add(item);
            }
        }
        return out;
    }

    public T first() {
        for (T item : _items) {
            if (allowed(item))
                return item;
        }
        return null;
    }

    private boolean allowed(T item) {
        boolean allowed = true;
        Condition[] conditions = (Condition[]) _conditions.toArray();

        for (int idx = 0; idx < conditions.length; idx++) {
            Condition condition = conditions[idx];
            String val = extract_value(item, condition.field);


        }
        return allowed;
    }

    private String extract_value(T item, String field) {
        if (field.endsWith("()")) { //search in methods;
            Method m = null;
            if (_methods.containsKey(field))
                m = _methods.get(field);
            else {
                try {
                    m = _class.getDeclaredMethod(field);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                    return null;
                }
                _methods.put(field, m);
            }
            try {
                return (m.invoke(item)).toString();
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            Field f = null;
            if (_fields.containsKey(field))
                f = _fields.get(field);
            else {
                try {
                    f = _class.getDeclaredField(field);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                    return null;
                }
                _fields.put(field, f);
            }
            try {
                return (f.get(item)).toString();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    enum ConditionType {
        eq, dt, lt, lte, gt, gte, and, or //,group, end_group
    }

    static class Condition {
        ConditionType type;
        String field;
        String value;

        Condition(ConditionType type) {
            this.type = type;
        }

        Condition(ConditionType type, String first, String second) {
            this(type);
            field = first;
            value = second;
        }
    }

}
