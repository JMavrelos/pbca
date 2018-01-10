package gr.blackswamp.pbca.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import gr.blackswamp.core.functions.UniAction;
import gr.blackswamp.pbca.R;
import gr.blackswamp.pbca.model.Punch;


@SuppressWarnings("WeakerAccess")
public class PunchAdapter extends RecyclerView.Adapter<PunchAdapter.PunchViewHolder> {
    private ArrayMap<Punch, Boolean> _punches = new ArrayMap<>();

    public PunchAdapter(List<Punch> punches) {
        super();
        _punches.clear();
        for (Punch punch : punches) {
            _punches.put(punch, true);
        }
    }

    public boolean is_selected(int position) {
        return _punches.get(get_item(position));
    }

    public Punch get_item(int position) {
        return _punches.keyAt(position);
    }

    public int get_position(Punch punch) {
        return _punches.indexOfKey(punch);
    }

    void item_clicked(Punch punch) {
        if (_punches.keySet().contains(punch)) {
            _punches.put(punch, !_punches.get(punch));
            notifyItemChanged(get_position(punch));
        }
    }

    @Override
    public void onBindViewHolder(PunchViewHolder holder, int position) {
        holder.set_data(get_item(position), is_selected(position), this::item_clicked);
    }

    @Override
    public PunchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PunchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_punch, parent, false));
    }

    @Override
    public int getItemCount() {
        return _punches.size();
    }


    public List<Punch> get_selected() {
        List<Punch> selected = new ArrayList<>();
        for (int idx = 0; idx < getItemCount(); idx++) {
            if (is_selected(idx))
                selected.add(get_item(idx));
        }
        return selected;
    }

    public void set_selected(List<Integer> selected) {
        for (Punch punch : _punches.keySet()) {
            _punches.put(punch, selected.contains(punch.get_number()));
        }
        notifyDataSetChanged();
    }

    static class PunchViewHolder extends RecyclerView.ViewHolder {
        TextView _description;

        PunchViewHolder(View itemView) {
            super(itemView);
            _description = (TextView) itemView;
        }

        void set_data(final Punch punch, final boolean selected, final UniAction<Punch> listener) {
            _description.setText(String.format(Locale.getDefault(), "%1$s(%2$d)", punch.get_name(), punch.get_number()));
            _description.setActivated(selected);
            itemView.setOnClickListener(v -> listener.call(punch));
        }
    }

}
