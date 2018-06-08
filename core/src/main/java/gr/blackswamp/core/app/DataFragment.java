package gr.blackswamp.core.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


public abstract class DataFragment extends Fragment {
    protected abstract String TAG();
    DataActivity _attached;

    protected DataActivity attached() {
        return _attached;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }


}
