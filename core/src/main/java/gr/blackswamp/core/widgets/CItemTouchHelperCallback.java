package gr.blackswamp.core.widgets;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class CItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private static final float ALPHA_FULL = 1f;
    private final boolean _allow_swipe;
    private final boolean _allow_drag;
    private CItemTouchAdapter _adapter;
    private int _drag_from = -1;
    private int _drag_to = -1;

    public CItemTouchHelperCallback(CItemTouchAdapter adapter) {
        this(adapter, true, true);
    }

    public CItemTouchHelperCallback(CItemTouchAdapter adapter, boolean allow_swipe, boolean allow_drag) {
        _allow_swipe = allow_swipe;
        _allow_drag = allow_drag;
        _adapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false; //declares that we can use a long press drag
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return _allow_swipe; //declares that we can use a view swipe
    }

    @Override
    public int getMovementFlags(RecyclerView recycler, RecyclerView.ViewHolder viewHolder) {
        int drag_flags = 0;
        int swipe_flags = 0;


        if (recycler.getLayoutManager() instanceof GridLayoutManager) {
            //if we use a grid layout manager, then we only allow movement, not swiping
            drag_flags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END;
        } else if (recycler.getLayoutManager() instanceof LinearLayoutManager) {
            //if we use a linearlayout manager, then directions are different
            LinearLayoutManager manager = (LinearLayoutManager) recycler.getLayoutManager();
            if (manager.getOrientation() == LinearLayoutManager.VERTICAL) {
                drag_flags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                swipe_flags = ItemTouchHelper.END;
            } else if (manager.getOrientation() == LinearLayoutManager.HORIZONTAL) {
                drag_flags = ItemTouchHelper.START | ItemTouchHelper.END;
                swipe_flags = ItemTouchHelper.UP;
            }
        }
        if (!_allow_drag)
            drag_flags = 0;
        if (!_allow_swipe)
            swipe_flags = 0;
        return makeMovementFlags(drag_flags, swipe_flags);
        //enable drag up and down and swipe left and right
    }

    @Override
    public boolean onMove(RecyclerView recycler, RecyclerView.ViewHolder from, RecyclerView.ViewHolder to) {
        int from_position = from.getAdapterPosition();
        if (_drag_from == -1) {
            _drag_from = from_position;
        }
        _drag_to = to.getAdapterPosition();
        _adapter.on_item_move(from_position, _drag_to);
        return true;
    }


    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        //if we are swiping remove the alpha of the view
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Fade out the view as it is swiped out of the parent's bounds
            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha); //change the alpha
            viewHolder.itemView.setTranslationX(dX); //draw it with a translation (i.e. make 0,0 different for the view);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        _adapter.on_item_dismissed(viewHolder.getAdapterPosition());
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setAlpha(ALPHA_FULL);
        if (viewHolder instanceof CItemTouchViewHolder) {
            ((CItemTouchViewHolder) viewHolder).on_item_end_drag();
        }
        if (_drag_from != -1 && _drag_to != -1)
            _adapter.on_item_move_finished(_drag_from, _drag_to);
        _drag_to = -1;
        _drag_from = -1;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE && viewHolder instanceof CItemTouchViewHolder) {
            ((CItemTouchViewHolder) viewHolder).on_item_start_drag();
        }
        super.onSelectedChanged(viewHolder, actionState);
    }
}
