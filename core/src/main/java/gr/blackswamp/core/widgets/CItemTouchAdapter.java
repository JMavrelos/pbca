package gr.blackswamp.core.widgets;

public interface CItemTouchAdapter {
    void on_item_move(int from_position, int to_position);

    void on_item_move_finished(int from_position, int to_position);

    void on_item_dismissed(int position);
}
