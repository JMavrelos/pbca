package gr.blackswamp.core.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import gr.blackswamp.core.R;
import gr.blackswamp.core.functions.Action;
import gr.blackswamp.core.functions.UniAction;
import gr.blackswamp.core.functions.UniFunction;

@SuppressWarnings("unused,SameParameterValue")
public abstract class CoreActivity extends AppCompatActivity implements CoreInteraction {
    boolean _allow_back = true;
    boolean _block_user = false;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(_activity_id);
        check_for_only_toasts();
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

    //endregion

    @Override
    public final void set_allow_back(final boolean allow) {
        _allow_back = allow;
    }

    //region movement between fragments

    /**
     * returns the currently shown fragment
     *
     * @return the currently shown fragment
     */
    protected final Fragment shown_fragment() {
        return getSupportFragmentManager().findFragmentById(_content_id);
    }
    protected final Class<? extends Fragment> shown_fragment_class() {
        return shown_fragment().getClass();
    }

    /**
     * Shows a fragment always adds it to backstack
     *
     * @param fragment the fragment to show
     * @param id       the identifier that will be given to the transaction
     */
    protected final void show_fragment(final Fragment fragment, final String id) {
        show_fragment(fragment, true, id, FragmentAnimation.None);
    }

    /**
     * Shows a fragment , does not add it to backstack
     *
     * @param fragment the fragment to show
     */
    protected final void show_fragment(final Fragment fragment) {
        show_fragment(fragment, true, null, FragmentAnimation.None);
    }

    /**
     * Shows a fragment, does not add it to backstack
     *
     * @param fragment  the fragment to show
     * @param animation the animation to use to make the transition
     */
    protected final void show_fragment(final Fragment fragment, final FragmentAnimation animation) {
        show_fragment(fragment, true, null, animation);
    }
    /**
     * Shows a fragment
     *
     * @param fragment          the fragment to show
     * @param add_to_back_stack If set to <c>true</c> add the transaction to the back stack.
     */
    protected final void show_fragment(final Fragment fragment, final boolean add_to_back_stack) {
        show_fragment(fragment, add_to_back_stack, null, FragmentAnimation.None);
    }

    /**
     * Shows a fragment
     *
     * @param fragment          the fragment to show
     * @param id                the identifier that will be given to the transaction
     * @param add_to_back_stack If set to <c>true</c> add the transaction to the back stack.
     */
    protected final void show_fragment(final Fragment fragment, final boolean add_to_back_stack, final String id) {
        show_fragment(fragment, add_to_back_stack, id, FragmentAnimation.None);
    }

    /**
     * Shows a fragment
     *
     * @param fragment          the fragment to show
     * @param add_to_back_stack If set to <c>true</c> add the transaction to the back stack.
     * @param animation         the animation to use to make the transition
     */
    protected final void show_fragment(final Fragment fragment, final boolean add_to_back_stack, final FragmentAnimation animation) {
        show_fragment(fragment, add_to_back_stack, null, animation);
    }

    /**
     * Shows a fragment
     *
     * @param fragment          the fragment to show
     * @param id                the identifier that will be given to the transaction
     * @param add_to_back_stack If set to <c>true</c> add the transaction to the back stack.
     * @param animation         the animation to use to make the transition
     */
    final void show_fragment(final Fragment fragment, final boolean add_to_back_stack, final String id, final FragmentAnimation animation) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        set_animation(transaction, animation);
        transaction.replace(_content_id, fragment);
        if (add_to_back_stack)
            transaction.addToBackStack(id);
        transaction.commit();
    }

    @Override
    public final void set_animation(final FragmentTransaction transaction, final FragmentAnimation animation) {
        switch (animation) {
            case ForwardFade:
                transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.fade_out, android.R.anim.fade_in);
                break;
            case BackwardsFade:
                transaction.setCustomAnimations(android.R.anim.fade_out, android.R.anim.fade_in, android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case BackwardsSlide:
                transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case ForwardSlide:
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
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
    //region user interaction methods

    /**
     * Used to signify that the activity does not want to receive inputs from the views
     *
     * @return if the parent wants to block user input
     */
    @Override
    public boolean is_user_blocked() {
        return _block_user;
    }

    /**
     * Used to signify to the child fragments that this activity does not need any more inputs
     * This has no functionality attached, functionality should be added by the child fragments
     *
     * @param allow if user input should be allowed
     */
    protected void allow_user_input(boolean allow) {
        _block_user = !allow;
    }
    //endregion
    //region methods to communicate with the child fragments
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

    protected final <T extends Fragment> void send(final Class<T> clazz, final UniAction<T> action) {
        List<T> frags = fragments(clazz);
        for (T frag : frags) {
            action.call(frag);
        }
    }

    protected final <T extends Fragment, S> S retrieve(final Class<T> clazz, UniFunction<T, S> function) {
        List<T> frags = fragments(clazz);
        if (frags.size() > 0)
            return function.call(frags.get(0));
        return null;
    }
    //endregion
    protected final void check_working(Action action) {
        if (_block_user)
            return;
        allow_user_input(false);
        if (action != null)
            action.call();
        allow_user_input(true);
    }
}

