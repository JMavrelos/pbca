package gr.blackswamp.core.app;

import android.support.annotation.StringRes;

public interface CoreInteraction {
    void set_allow_back(boolean allow);

    void restart();

    void move_back();

    void display_message(@StringRes int res_id);

    void display_message(String text);

    void display_message(@StringRes int res_id, MessageLength length);

    void display_message(String text, MessageLength length);


}
