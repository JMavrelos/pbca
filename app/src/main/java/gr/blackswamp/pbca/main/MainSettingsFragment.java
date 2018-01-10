package gr.blackswamp.pbca.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import java.util.List;

import gr.blackswamp.core.app.AttachedFragment;
import gr.blackswamp.core.app.CoreInteraction;
import gr.blackswamp.core.widgets.NumberPicker;
import gr.blackswamp.pbca.App;
import gr.blackswamp.pbca.R;
import gr.blackswamp.pbca.adapters.PunchAdapter;
import gr.blackswamp.pbca.model.Punch;


public class MainSettingsFragment extends AttachedFragment<MainSettingsFragment.SettingsInteraction> {

    public static MainSettingsFragment NewInstance() {
        return new MainSettingsFragment();
    }

    protected SettingsViewHolder holder() {
        return (SettingsViewHolder) super.holder();
    }

    @Override
    protected String TAG() {
        return App.NAMESPACE + "main_settings";
    }

    //region set up methods
    @Override
    protected void post_create_view(Bundle arguments, Bundle state) {
        parent().set_allow_back(true);
        if (state == null)
            update_view();
    }

    @Override
    protected int get_resource_layout_id() {
        return R.layout.fragment_main_settings;
    }

    @Override
    protected void assign_views(boolean assign) {
        if (assign) {
            SettingsViewHolder holder = new SettingsViewHolder();
            holder.alternate = find(R.id.main_settings_alternate);
            holder.punches = find(R.id.main_settings_punch);
            holder.start = find(R.id.main_settings_start);
            holder.sets = find(R.id.main_settings_sets);
            holder.interval = find(R.id.main_settings_interval);
            holder.repetitions = find(R.id.main_settings_repetitions);
            holder.adapter = new PunchAdapter(parent().get_punches());
            set_holder(holder);
        } else {
            set_holder(null);
        }
    }

    @Override
    protected void attach_listeners(boolean attach) {
        holder().punches.setAdapter(attach ? holder().adapter : null);
        holder().start.setOnClickListener(attach ? this::start_working : null);
    }

    private void update_view() {
        holder().alternate.setChecked(parent().get_alternate());
        holder().adapter.set_selected(parent().get_selected());
        holder().sets.set_value(parent().get_sets());
        holder().interval.set_value(parent().get_interval());
        holder().repetitions.set_value(parent().get_repetitions());
    }
    //endregion


    //region events
    @SuppressWarnings("unused")
    private void start_working(View view) {
        parent().start_working();
    }
    //endregion

    //region methods to communicate with the outside world
    public boolean get_alternate() {
        return holder().alternate.isChecked();
    }

    public List<Punch> get_punches() {
        return holder().adapter.get_selected();
    }

    public int get_interval() {
        return holder().interval.get_value();
    }

    public int get_repetitions() {
        return holder().repetitions.get_value();
    }

    public int get_sets() {
        return holder().sets.get_value();
    }
    //endregion

    interface SettingsInteraction extends CoreInteraction {

        boolean get_alternate();

        List<Punch> get_punches();

        List<Integer> get_selected();

        int get_sets();

        int get_interval();

        int get_repetitions();

        void start_working();
    }

    static class SettingsViewHolder {
        CheckBox alternate;
        RecyclerView punches;
        NumberPicker sets;
        NumberPicker interval;
        NumberPicker repetitions;
        PunchAdapter adapter;
        FloatingActionButton start;
    }

}
