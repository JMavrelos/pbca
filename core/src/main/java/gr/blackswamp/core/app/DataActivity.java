package gr.blackswamp.core.app;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import gr.blackswamp.core.functions.Supplier;

public abstract class DataActivity<T extends DataFragment> extends CoreActivity {
    private final Supplier<T> _constructor;
    private T _data;

    public DataActivity(int activity_resource_id, int content_frame_resource_id, Supplier<T> fragment_constructor) {
        super(activity_resource_id, content_frame_resource_id);
        _constructor = fragment_constructor;
    }
    protected final T data() {
        return _data;
    }

    @Override
    protected void post_create(Bundle savedInstanceState) {
        init_data_fragment();
        super.post_create(savedInstanceState);
    }


    private void init_data_fragment() {
        if (_data == null) {
            Fragment frag = getSupportFragmentManager().findFragmentByTag(TAG() + "_data");
            if (frag == null) {
                _data = _constructor.get();
                getSupportFragmentManager().beginTransaction().add(_data, TAG() + "_data").commit();
            } else {
                //noinspection unchecked
                _data = (T) frag;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()){
            getSupportFragmentManager().beginTransaction().remove(_data).commit();
            _data = null;
        }
    }
}
