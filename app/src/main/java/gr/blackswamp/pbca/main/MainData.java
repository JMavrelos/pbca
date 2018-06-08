package gr.blackswamp.pbca.main;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import gr.blackswamp.core.app.DataFragment;
import gr.blackswamp.core.functions.UniAction;
import gr.blackswamp.pbca.App;
import gr.blackswamp.pbca.model.Punch;
import gr.blackswamp.pbca.service.WorkoutAction;
import gr.blackswamp.pbca.service.WorkoutBroadcast;
import gr.blackswamp.pbca.service.WorkoutReceiver;
import gr.blackswamp.pbca.service.WorkoutRequest;
import gr.blackswamp.pbca.service.WorkoutService;


public class MainData extends DataFragment {
    UniAction<Intent> callback = this::got_broadcast;

    @Override
    protected String TAG() {
        return App.NAMESPACE + "main_data_fragment";
    }

    public void send_start(boolean alternate, ArrayList<Punch> punches, int interval, int repetitions, int sets) {
        WorkoutRequest request =
                new WorkoutRequest()
                        .set_action(WorkoutAction.start_working)
                        .set_alternate(alternate)
                        .set_interval(interval)
                        .set_repetitions(repetitions)
                        .set_sets(sets);
        request.punches().addAll(punches);
        send_service_message(request);
    }

    public void request_latest() {
        send_service_message(
                new WorkoutRequest().set_action(WorkoutAction.resend));
    }

    public void ask_state() {
        send_service_message(
                new WorkoutRequest().set_action(WorkoutAction.state)
        );
    }

    public void send_stop() {
        send_service_message(
                new WorkoutRequest().set_action(WorkoutAction.stop_working));
    }

    private void send_service_message(WorkoutRequest request) {
        Intent i = new Intent(getContext(), WorkoutService.class);
        i.putExtra(WorkoutService.REQUEST, request);
        getContext().startService(i);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.RegisterReceiver(callback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.UregisterReceiver(callback);
    }

    private void got_broadcast(Intent intent) {
        MainActivity activity = (MainActivity) getActivity();
        if (activity == null)
            return;
        WorkoutBroadcast response = intent.getParcelableExtra(WorkoutService.BROADCAST);

        if (response == null)
            return;
        switch (response.get_status()) {
            case started:
                activity.service_started();
                break;
            case finished:
                activity.service_finished();
                break;
            case failure:
                break;
            case working:
                activity.set_data(response.current_set());
                break;
        }
    }


}
