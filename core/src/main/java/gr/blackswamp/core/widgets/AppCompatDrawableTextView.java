package gr.blackswamp.core.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import gr.blackswamp.core.R;

public class AppCompatDrawableTextView extends AppCompatTextView {
    int _tint;

    public AppCompatDrawableTextView(Context context) {
        super(context);
    }

    public AppCompatDrawableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AppCompatDrawableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public void set_drawable_start(int resource_id) {
        set_drawable(0, resource_id);
    }

    public void set_drawable_end(int resource_id) {
        set_drawable(2, resource_id);
    }

    public void set_drawable_top(int resource_id) {
        set_drawable(1, resource_id);
    }

    public void set_drawable_bottom(int resource_id) {
        set_drawable(3, resource_id);
    }

    public void set_drawable(int location, int resource_id) {
        Drawable drawable = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getContext().getDrawable(resource_id);
        } else {
            drawable = AppCompatResources.getDrawable(getContext(), resource_id);
        }

        if (_tint != Color.TRANSPARENT && drawable != null)
            drawable.setColorFilter(_tint, PorterDuff.Mode.SRC_ATOP);

        if (drawable != null) {
            Drawable[] current = getCompoundDrawables();
            current[location] = drawable;
            setCompoundDrawablesWithIntrinsicBounds(current[0], current[1], current[2], current[3]);
        }
    }


    private void init(AttributeSet attrs) {
        if (attrs == null)
            return;
        TypedArray attributeArray = getContext().obtainStyledAttributes(attrs, R.styleable.AppCompatDrawableTextView);
        Drawable drawable_start = null;
        Drawable drawable_end = null;
        Drawable drawable_bottom = null;
        Drawable drawable_top = null;
        _tint = attributeArray.getColor(R.styleable.AppCompatDrawableTextView_drawableTintCompat, Color.TRANSPARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable_start = attributeArray.getDrawable(R.styleable.AppCompatDrawableTextView_drawableStartCompat);
            drawable_end = attributeArray.getDrawable(R.styleable.AppCompatDrawableTextView_drawableEndCompat);
            drawable_bottom = attributeArray.getDrawable(R.styleable.AppCompatDrawableTextView_drawableBottomCompat);
            drawable_top = attributeArray.getDrawable(R.styleable.AppCompatDrawableTextView_drawableTopCompat);
        } else {
            int drawable_start_id = attributeArray.getResourceId(R.styleable.AppCompatDrawableTextView_drawableStartCompat, -1);
            int drawable_end_id = attributeArray.getResourceId(R.styleable.AppCompatDrawableTextView_drawableEndCompat, -1);
            int drawable_bottom_id = attributeArray.getResourceId(R.styleable.AppCompatDrawableTextView_drawableBottomCompat, -1);
            int drawable_top_id = attributeArray.getResourceId(R.styleable.AppCompatDrawableTextView_drawableTopCompat, -1);

            if (drawable_start_id != -1)
                drawable_start = AppCompatResources.getDrawable(getContext(), drawable_start_id);
            if (drawable_end_id != -1)
                drawable_end = AppCompatResources.getDrawable(getContext(), drawable_end_id);
            if (drawable_bottom_id != -1)
                drawable_bottom = AppCompatResources.getDrawable(getContext(), drawable_bottom_id);
            if (drawable_top_id != -1)
                drawable_top = AppCompatResources.getDrawable(getContext(), drawable_top_id);
        }

        if (_tint != Color.TRANSPARENT) {
            if (drawable_top != null)
                drawable_top.setColorFilter(_tint, PorterDuff.Mode.SRC_ATOP);
            if (drawable_bottom != null)
                drawable_bottom.setColorFilter(_tint, PorterDuff.Mode.SRC_ATOP);
            if (drawable_start != null)
                drawable_start.setColorFilter(_tint, PorterDuff.Mode.SRC_ATOP);
            if (drawable_end != null)
                drawable_end.setColorFilter(_tint, PorterDuff.Mode.SRC_ATOP);
        }
        setCompoundDrawablesWithIntrinsicBounds(drawable_start, drawable_top, drawable_end, drawable_bottom);
        attributeArray.recycle();
    }
}
