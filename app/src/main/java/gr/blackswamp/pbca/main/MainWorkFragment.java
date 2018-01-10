package gr.blackswamp.pbca.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.TextView;

import gr.blackswamp.core.app.AttachedFragment;
import gr.blackswamp.core.app.CoreInteraction;
import gr.blackswamp.pbca.App;
import gr.blackswamp.pbca.R;


public class MainWorkFragment extends AttachedFragment<MainWorkFragment.WorkInteraction> {

    public static MainWorkFragment NewInstance() {
        return new MainWorkFragment();
    }

    @Override
    protected String TAG() {
        return App.NAMESPACE + "main_work";
    }

    @Override
    protected void post_create_view(Bundle arguments, Bundle state) {
        parent().set_allow_back(false);
        if (state == null) //this is the first time it is run and not restored
            parent().ask_for_current();
    }

    protected WorkViewHolder holder() {
        return (WorkViewHolder) super.holder();
    }

    @Override
    protected int get_resource_layout_id() {
        return R.layout.fragment_main_work;
    }

    @Override
    protected void assign_views(boolean assign) {
        if (assign) {
            WorkViewHolder holder = new WorkViewHolder();
            holder.data = find(R.id.main_work_data);
            holder.fab = find(R.id.main_work_stop);
            set_holder(holder);
        } else {
            set_holder(null);
        }
    }

    @Override
    protected void attach_listeners(boolean attach) {
        holder().fab.setOnClickListener(attach ? this::stop_working : null);
    }

    private void stop_working(View v) {
        parent().stop_working();
    }

    public void set_text(String text) {
        holder().data.setText(text);
    }

    interface WorkInteraction extends CoreInteraction {
        void stop_working();

        void ask_for_current();
    }

    class WorkViewHolder {
        TextView data;
        FloatingActionButton fab;
    }
}
