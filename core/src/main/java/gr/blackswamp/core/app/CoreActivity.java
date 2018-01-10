package gr.blackswamp.core.app;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import gr.blackswamp.core.R;
import gr.blackswamp.core.functions.UniAction;
import gr.blackswamp.core.functions.UniFunction;

@SuppressWarnings("unused,SameParameterValue")
public abstract class CoreActivity extends FragmentActivity implements CoreInteraction {
    private static final String STARTING_FRAGMENT = "gr.blackswamp.core.starting_fragment";
    boolean _allow_back = true;
    boolean _only_toasts;
    private @LayoutRes
    int _activity_id;
    private @IdRes
    int _content_id;

    public CoreActivity(@LayoutRes int activity_resource_id, @IdRes int content_frame_resource_id) {
//        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        _activity_id = activity_resource_id;
        _content_id = content_frame_resource_id;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(_activity_id);
        check_for_only_toasts();
        post_create(savedInstanceState);
    }

    private void check_for_only_toasts() {
        _only_toasts = true;
        View view = findViewById(_content_id);
        do {
            if (view instanceof CoordinatorLayout) {
                _only_toasts = false;
                return;
            } else {
                ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);
    }
    //region abstract methods

    /**
     * Activity's tag, just to force people to use tag
     */
    protected abstract String TAG();

    /**
     * The fragment that will be shown the first time the activity is loaded
     */
    protected abstract Fragment starting_fragment();
    //endregion

    @Override
    public final void set_allow_back(final boolean allow) {
        _allow_back = allow;
    }

    //region virtual methods

    /**
     * This will be called after creation has been finished
     *
     * @param state the state the activity started with
     */
    protected void post_create(final Bundle state) {
        if (state == null)
            show_fragment(starting_fragment(), STARTING_FRAGMENT);
    }
    //endregion
    //region movement between fragments

    /**
     * returns the currently shown fragment
     * @return the currently shown fragment
     */
    protected final Fragment shown_fragment() {
        return getSupportFragmentManager().findFragmentById(_content_id);
    }

    /**
     * Shows a fragment
     *
     * @param fragment the fragment to show
     * @param id       the identifier that will be given to the transaction
     */
    protected final void show_fragment(final Fragment fragment, final String id) {
        show_fragment(fragment, true, id, true);
    }

    /**
     * Shows a fragment
     *
     * @param fragment the fragment to show
     */
    protected final void show_fragment(final Fragment fragment) {
        show_fragment(fragment, true, null, true);
    }

    /**
     * Shows a fragment
     *
     * @param fragment          the fragment to show
     * @param add_to_back_stack If set to <c>true</c> add the transaction to the back stack.
     */
    protected final void show_fragment(final Fragment fragment, final boolean add_to_back_stack) {
        show_fragment(fragment, add_to_back_stack, null, true);
    }

    /**
     * Shows a fragment
     *
     * @param fragment          the fragment to show
     * @param id                the identifier that will be given to the transaction
     * @param add_to_back_stack If set to <c>true</c> add the transaction to the back stack.
     */
    protected final void show_fragment(final Fragment fragment, final boolean add_to_back_stack, final String id) {
        show_fragment(fragment, add_to_back_stack, id, true);
    }

    /**
     * Shows a fragment
     *
     * @param fragment          the fragment to show
     * @param add_to_back_stack If set to <c>true</c> add the transaction to the back stack.
     * @param with_animation    if the transition will be animated, will be ignored if this is the first fragment
     */
    protected final void show_fragment(final Fragment fragment, final boolean add_to_back_stack, final boolean with_animation) {
        show_fragment(fragment, add_to_back_stack, null, with_animation);
    }

    /**
     * Shows a fragment
     *
     * @param fragment          the fragment to show
     * @param id                the identifier that will be given to the transaction
     * @param add_to_back_stack If set to <c>true</c> add the transaction to the back stack.
     * @param with_animation    if the transition will be animated, will be ignored if this is the first fragment
     */
    final void show_fragment(final Fragment fragment, final boolean add_to_back_stack, final String id, final boolean with_animation) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (with_animation && !STARTING_FRAGMENT.equals(id)) {
            update_animation(transaction);
        }
        transaction.replace(_content_id, fragment);
        if (add_to_back_stack)
            transaction.addToBackStack(id);
        transaction.commit();
    }

    final void update_animation(final FragmentTransaction transaction) {
        update_animation(transaction, false);
    }

    final void update_animation(final FragmentTransaction transaction, final boolean reverse) {
        if (reverse) {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }

    //endregion
    //region back movement methods
    @Override
    public void onBackPressed() {
        back_pressed(false);
    }

    /**
     * used to force back movement
     */
    public final void move_back() {
        back_pressed(true);
    }

    /**
     * Used to restart the activity from the first fragment
     */
    public final void restart() {
        getSupportFragmentManager().popBackStack(STARTING_FRAGMENT, 0);
    }

    /**
     * Moves back one fragment
     * If the fragment is the first one it moves to the previous activity
     * If the current activity is the first one it moves it to the background
     *
     * @param force If set to <c>true</c> ignore the allow back value.
     */
    final void back_pressed(final boolean force) {
        if (_allow_back || force) {
            if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
                if (this.isTaskRoot()) {
                    this.moveTaskToBack(true);
                } else {
                    finish();
                }
            } else {
                super.onBackPressed();
            }
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(_content_id);
            if (fragment != null && fragment instanceof CoreFragment)
                ((CoreFragment) fragment).back_pressed();
        }
    }
    //endregion
    //region methods used to display a message (snackbar or textbox according to the container

    /**
     * Shows a message in the form of a toast or snackbar according to what the base activity content is, it is shown for the maximum possible time
     *
     * @param res_id the resource_id pointing to the text that will be shown
     */
    @Override
    public final void display_message(final @StringRes int res_id) {
        display_message(getString(res_id), MessageLength.Indefinate);
    }

    /**
     * Shows a message in the form of a toast or snackbar according to what the base activity content is, it is shown for the maximum possible time
     *
     * @param text The text to display
     */
    @Override
    public final void display_message(String text) {
        display_message(text, MessageLength.Indefinate);
    }

    /**
     * Shows a message in the form of a toast or snackbar according to what the base activity content is
     *
     * @param res_id the resource_id pointing to the text that will be shown
     * @param length how long the display will show for
     */
    @Override
    public final void display_message(final @StringRes int res_id, final MessageLength length) {
        display_message(getString(res_id), length);
    }

    /**
     * Shows a message in the form of a toast or snackbar according to what the base activity content is
     *
     * @param text   The text to display
     * @param length how long the display will show for
     */
    @Override
    public final void display_message(final String text, final MessageLength length) {
        if (_only_toasts) {
            switch (length) {
                case Short:
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                    break;
            }
        } else {
            final Snackbar snackbar;
            switch (length) {
                case Short:
                    snackbar = Snackbar.make(findViewById(_content_id), text, Snackbar.LENGTH_SHORT);
                    break;
                case Long:
                    snackbar = Snackbar.make(findViewById(_content_id), text, Snackbar.LENGTH_LONG);
                    break;
                default:
                    snackbar = Snackbar.make(findViewById(_content_id), text, Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction(R.string.dismiss, v -> snackbar.dismiss());
                    break;
            }
            TextView tv = snackbar.getView().findViewById(R.id.snackbar_text);
            tv.setTextColor(Color.WHITE);
            snackbar.show();
        }
    }

    //endregion
    //region Methdos to communicate with the child fragments
    protected final <T extends Fragment> List<T> fragments(final Class<T> clazz) {
        List<T> children = new ArrayList<>();
        if (getSupportFragmentManager().getFragments() == null)
            return children;
        for (Fragment child : getSupportFragmentManager().getFragments()) {
            if (clazz.isInstance(child)) {
                children.add(clazz.cast(child));
            }
            if (child instanceof CoreFragment) {
                List<T> sub_children = ((CoreFragment) child).fragments(clazz);
                children.addAll(sub_children);
            }
        }
        return children;
    }

    protected final <T extends Fragment> void send(final Class<T> clazz, UniAction<T> action) {
        List<T> frags = fragments(clazz);
        for (T frag : frags) {
            action.call(frag);
        }
    }

    protected final <T extends Fragment, R> R send(final Class<T> clazz, UniFunction<T, R> function) {
        List<T> frags = fragments(clazz);
        if (frags.size() > 0)
            return function.call(frags.get(0));
        return null;
    }

//endregion
}

