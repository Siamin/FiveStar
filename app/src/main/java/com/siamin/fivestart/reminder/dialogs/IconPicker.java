package com.siamin.fivestart.reminder.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.siamin.fivestart.R;
import com.siamin.fivestart.reminder.adapters.IconsAdapter;
import com.siamin.fivestart.reminder.database.DatabaseHelper;

public class IconPicker extends DialogFragment {

    public interface IconSelectionListener {
        void onIconSelection(DialogFragment dialog, String iconName, String iconType, int iconResId);
    }

    IconSelectionListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (IconSelectionListener) context;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.view_dialog_icons, null);

        RecyclerView recyclerView = (RecyclerView) dialogView.findViewById(R.id.icons_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), getResources().getInteger(R.integer.grid_columns)));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);

        DatabaseHelper database = DatabaseHelper.getInstance(getContext());
        recyclerView.setAdapter(new IconsAdapter(IconPicker.this, database.getIconList()));
        database.close();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Dialog);
        builder.setTitle(R.string.select_icon);
        builder.setView(dialogView);
        return builder.create();
    }

    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }
}
