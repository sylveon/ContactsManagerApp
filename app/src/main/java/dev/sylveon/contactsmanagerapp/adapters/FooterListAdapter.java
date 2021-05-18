package dev.sylveon.contactsmanagerapp.adapters;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * A custom implementation of ListAdapter that allows you to add a footer to RecyclerView
 * @param <T> The type of the items in the list
 * @param <VH> The type of the view holder
 */
public abstract class FooterListAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    private final AsyncListDiffer<T> differ;

    public FooterListAdapter(DiffUtil.ItemCallback<T> itemCallback) {
        differ = new AsyncListDiffer<>(
                this.new OffsetListUpdateCallback(),
                new AsyncDifferConfig.Builder<>(itemCallback).build());
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size() + 1;
    }

    public T getItem(int position) {
        return differ.getCurrentList().get(position);
    }

    public void submitList(@Nullable List<T> list) {
        differ.submitList(list);
    }

    private class OffsetListUpdateCallback implements ListUpdateCallback {

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count, @Nullable Object payload) {
            notifyItemRangeChanged(position, count, payload);
        }
    }
}
