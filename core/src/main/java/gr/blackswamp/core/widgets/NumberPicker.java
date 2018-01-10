package gr.blackswamp.core.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gr.blackswamp.core.R;

@SuppressWarnings("unused")
public class NumberPicker extends ConstraintLayout {
    private TextView _val;
    private OnValueChangedListener _listener;
    private int _value;
    private int _max;
    private int _min;
    private int _text_size = 20;
    private int _step;
    private int _step_big;
    private @LayoutRes
    int _resource;

    public NumberPicker(Context context) {
        super(context);
        init(context);
    }


    public NumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        load_data(context, attrs);
        init(context);
    }

    public NumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        load_data(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(_resource, this);
    }

    private void load_data(Context context, AttributeSet attrs) {
        TypedArray typed = context.obtainStyledAttributes(attrs, R.styleable.NumberPicker);
        _min = typed.getInt(R.styleable.NumberPicker_min, 0);
        _max = typed.getInt(R.styleable.NumberPicker_max, 99);
        set_value(typed.getInt(R.styleable.NumberPicker_val, 0));
        _text_size = typed.getInt(R.styleable.NumberPicker_textSize, 20);
        _step = Math.max(1, Math.abs(typed.getInt(R.styleable.NumberPicker_step, 1)));
        _step_big = Math.abs(typed.getInt(R.styleable.NumberPicker_step_big, 0));
        _resource = get_resource_id(typed.getInt(R.styleable.NumberPicker_view_orientation, 0), typed.getInt(R.styleable.NumberPicker_button_orientation, 0));
        if (_min > _max) {
            int temp = _max;
            _max = _min;
            _min = temp;
        }
        if (_step_big <= _step)
            _step_big = 0;
        typed.recycle();
    }

    private @LayoutRes
    int get_resource_id(int view_orientation, int button_orientation) {
        if (view_orientation == 0 && button_orientation == 0) {
            return R.layout.n_p_h_hb;
        } else if (view_orientation == 0) {
            return R.layout.n_p_h_vb;
        } else if (button_orientation == 0) {
            return R.layout.n_p_v_hb;
        } else {
            return R.layout.n_p_v_vb;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        _val = findViewById(R.id.number_picker_value);
        ImageView increase = findViewById(R.id.number_picker_increase);
        ImageView increase_big = findViewById(R.id.number_picker_increase_big);
        ImageView decrease = findViewById(R.id.number_picker_decrease);
        ImageView decrease_big = findViewById(R.id.number_picker_decrease_big);

        _val.setTextSize(_text_size);
        increase.setMinimumHeight(_val.getHeight());
        increase.setMinimumWidth(_val.getWidth());
        if (_step_big == 0) {
            increase_big.setVisibility(GONE);
            decrease_big.setVisibility(GONE);
        } else {
            increase_big.setVisibility(VISIBLE);
            decrease_big.setVisibility(VISIBLE);
        }
        increase_big.setOnClickListener(v -> change_value(_step_big));
        increase.setOnClickListener(v -> change_value(_step));
        decrease_big.setOnClickListener(v -> change_value(-_step_big));
        decrease.setOnClickListener(v -> change_value(-_step));
        update_value();
    }



    public void change_value(int val) {
        set_value(_value + val);
    }

    public void set_value(int val) {
        _value = Math.max(_min, Math.min(_max, val));
        update_value();
    }

    public void setOnValueChangedListener(OnValueChangedListener listener) {
        _listener = listener;
    }

    private void update_value() {
        if (_listener != null)
            _listener.onValueChanged(this, _value);
        if (_val != null)
            _val.setText(String.valueOf(_value));
    }

    public int get_value() {
        return _value;
    }

    public interface OnValueChangedListener {
        void onValueChanged(View me, int new_value);
    }
}


