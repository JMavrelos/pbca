package gr.blackswamp.core.app;

import android.support.annotation.StringRes;
import android.support.v4.app.FragmentTransaction;

public interface CoreInteraction {
    /**
     * If set to true (default) then the activity will handle back being pressed normally
     * If set to false then the back_pressed() method will be called on the fragment instead
     */
    void set_allow_back(boolean allow);

    /**
     * Used to signal the parent activity that a back event needs to be issued
     * This ignores the allow back flag
     */
    void move_back();

    /**
     * Displays a message to the user either in the form of a snackbar or a toast (whichever is available)
     *
     * @param res_id the resource id of the string to be displayed
     */
    void display_message(@StringRes int res_id);

    /**
     * Displays a message to the user either in the form of a snackbar or a toast (whichever is available)
     *
     * @param text the text to display
     */
    void display_message(String text);

    /**
     * Displays a message to the user either in the form of a snackbar or a toast (whichever is available)
     *
     * @param res_id the resource id of the string to be displayed
     * @param length how long will the notification persist (if it is a toast it will not be able to handle indefinite and istead act as long
     */
    void display_message(@StringRes int res_id, MessageLength length);

    /**
     * Displays a message to the user either in the form of a snackbar or a toast (whichever is available)
     *
     * @param text   the text to display
     * @param length how long will the notification persist (if it is a toast it will not be able to handle indefinite and istead act as long
     */
    void display_message(String text, MessageLength length);

    /**
     * Helper method to apply an predefined animation (as defined by the Fragment Animation enum) to a transaction
     *
     * @param transaction the transaction to change
     * @param animation   an enum showing which animation to apply
     */
    void set_animation(FragmentTransaction transaction, FragmentAnimation animation);

    /**
     * method used to retrieve from the parent whether it wishes for the fragment to not be sending it messages
     *
     * @return if the parent wishes to block all input
     */
    boolean is_user_blocked();

}
