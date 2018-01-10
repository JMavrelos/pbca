package gr.blackswamp.pbca.main;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import gr.blackswamp.core.app.DataActivity;
import gr.blackswamp.core.app.MessageLength;
import gr.blackswamp.pbca.App;
import gr.blackswamp.pbca.R;
import gr.blackswamp.pbca.model.Punch;

public class MainActivity extends DataActivity<MainData>
        implements MainSettingsFragment.SettingsInteraction
        , MainWorkFragment.WorkInteraction {

    public MainActivity() {
        super(R.layout.activity_main, R.id.main_content, MainData::new);
    }

    @Override
    protected String TAG() {
        return App.NAMESPACE + "main";
    }

    @Override
    protected Fragment starting_fragment() {
        return MainSettingsFragment.NewInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        data().ask_state();
    }


    //region settingsinteraction
    @Override
    public boolean get_alternate() {
        return App.Settings().is_alternate_hands();
    }

    @Override
    public List<Punch> get_punches() {
        return Punch.Build(this);
    }

    @Override
    public List<Integer> get_selected() {
        return App.Settings().get_punches();
    }

    @Override
    public int get_sets() {
        return App.Settings().get_sets();
    }

    @Override
    public int get_interval() {
        return App.Settings().get_interval();
    }

    @Override
    public int get_repetitions() {
        return App.Settings().get_repetitions();
    }

    //endregion
    //region intaraction with the service
    @Override
    @SuppressWarnings("ConstantConditions")
    public void start_working() {
        int error_id = 0;
        try {
            //region Retrieve data from view
            error_id = R.string.main_error_updating_from_view;
            boolean alternate = send(MainSettingsFragment.class, MainSettingsFragment::get_alternate);
            List<Punch> punches = send(MainSettingsFragment.class, MainSettingsFragment::get_punches);
            int interval = send(MainSettingsFragment.class, MainSettingsFragment::get_interval);
            int repetitions = send(MainSettingsFragment.class, MainSettingsFragment::get_repetitions);
            int sets = send(MainSettingsFragment.class, MainSettingsFragment::get_sets);
            //endregion
            //region validation
            error_id = R.string.main_error_validation;
            if (punches == null || punches.size() == 0)
                throw new Exception(getString(R.string.main_error_invalid_number_of_punches));
            //endregion
            //region save_data
            error_id = R.string.main_error_saving_parameters;
            App.Settings().set_alternate_hands(alternate);
            App.Settings().set_punches(punches);
            App.Settings().set_interval(interval);
            App.Settings().set_repetitions(repetitions);
            App.Settings().set_sets(sets);
            //endregion
            data().send_start(alternate, new ArrayList<>(punches), interval, repetitions, sets);//start service
        } catch (Exception e) {
            display_message(getString(error_id, e.getMessage()), MessageLength.Long);
        }
    }

    //endregion
    //region workinteraction
    public void stop_working() {
        data().send_stop();
    }
    public void ask_for_current() {
        data().request_latest();
    }
    //endregion
    //region interaction with the data fragment
    public void service_started() {
        Fragment fragment = shown_fragment();
        if (fragment != null && fragment.getClass() == MainSettingsFragment.class) {
            show_fragment(MainWorkFragment.NewInstance());
        }
    }

    public void service_finished() {
        Fragment fragment = shown_fragment();
        if (fragment != null && fragment.getClass() == MainWorkFragment.class)
            move_back();
    }

    public void set_data(final List<Integer> punches) {
        send(MainWorkFragment.class, w -> {
            StringBuilder text = new StringBuilder();
            for (int i : punches) {
                text.append(i).append(',');
            }
            w.set_text(text.delete(text.length() - 1, text.length()).toString());
        });
    }
    //endregion
}
