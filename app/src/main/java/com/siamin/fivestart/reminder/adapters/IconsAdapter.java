package com.siamin.fivestart.reminder.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.siamin.fivestart.R;
import com.siamin.fivestart.reminder.database.DatabaseHelper;
import com.siamin.fivestart.reminder.dialogs.IconPicker;
import com.siamin.fivestart.reminder.models.Icon;

import java.util.List;

public class IconsAdapter extends RecyclerView.Adapter<IconsAdapter.ViewHolder>{

    private IconPicker iconPicker;
    private List<Icon> iconList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        private View view;

        public ViewHolder(final View view) {
            super(view);
            this.view = view;
            imageView = view.findViewById(R.id.icon);
        }
    }

    public IconsAdapter(IconPicker iconPicker, List<Icon> iconList) {
        this.iconPicker = iconPicker;
        this.iconList = iconList;
    }

    @Override
    public int getItemCount() {
        return iconList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_icon_grid, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        final String iconName = iconList.get(position).getName();
        final int iconResId = viewHolder.view.getContext().getResources().getIdentifier(iconName, "drawable", viewHolder.view.getContext().getPackageName());
        viewHolder.imageView.setImageResource(iconResId);
        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper database = DatabaseHelper.getInstance(viewHolder.view.getContext());
                iconList.get(viewHolder.getAdapterPosition()).setUseFrequency(iconList.get(viewHolder.getAdapterPosition()).getUseFrequency() + 1);
                database.updateIcon(iconList.get(viewHolder.getAdapterPosition()));
                database.close();

                String name;
                if (!iconName.equals(viewHolder.view.getContext().getString(R.string.default_icon_value))) {
                    name = viewHolder.view.getContext().getString(R.string.custom_icon);
                } else {
                    name = viewHolder.view.getContext().getResources().getString(R.string.default_icon);
                }

                ((IconPicker.IconSelectionListener) viewHolder.view.getContext()).onIconSelection(iconPicker, iconName, name, iconResId);
            }
        });
    }
}