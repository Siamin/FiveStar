package com.siamin.fivestart.reminder.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.provider.CalendarContract;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.siamin.fivestart.R;
import com.siamin.fivestart.helpers.LanguageHelper;
import com.siamin.fivestart.helpers.PersianCalendarHelper;
import com.siamin.fivestart.reminder.activities.ViewActivity;
import com.siamin.fivestart.reminder.models.Reminder;
import com.siamin.fivestart.reminder.utils.DateAndTimeUtil;

import java.util.Calendar;
import java.util.List;


public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {

    private Context context;
    private List<Reminder> reminderList;
    private LanguageHelper languageHelper;
    private PersianCalendarHelper persianCalendarHelper;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title,time,content,textSeparator;
        ImageView icon,circle;
        private View view;

        public ViewHolder(final View view) {
            super(view);
            this.view = view;
            title =view.findViewById(R.id.notification_title);
            time =view.findViewById(R.id.notification_time);
            content =view.findViewById(R.id.notification_content);
            textSeparator =view.findViewById(R.id.header_separator);
            icon =view.findViewById(R.id.notification_icon);
            circle =view.findViewById(R.id.notification_circle);

        }
    }

    public interface RecyclerListener {
        void hideFab();
    }

    public ReminderAdapter(Context context, List<Reminder> reminderList) {
        this.context = context;
        this.reminderList = reminderList;
        this.languageHelper = new LanguageHelper(context);
        this.persianCalendarHelper =new PersianCalendarHelper();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification_list, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        Calendar calendar = DateAndTimeUtil.parseDateAndTime(reminderList.get(position).getDateAndTime());
        Reminder reminder = reminderList.get(position);

        // Show header for item if it is the first in date group
        if (position > 0 && reminder.getDate().equals(reminderList.get(position - 1).getDate()) ) {
            viewHolder.textSeparator.setVisibility(View.GONE);
        } else {
            String appropriateDate ;
            if (languageHelper.getLanguage().equals(languageHelper.KeyEn)){
                appropriateDate = DateAndTimeUtil.getAppropriateDateFormat(context, calendar);
            }else{
                appropriateDate = persianCalendarHelper.ConvertDateMiladiToJalaliByMonthNames(reminder.getDateAndTime());
            }

            viewHolder.textSeparator.setText(appropriateDate);
            viewHolder.textSeparator.setVisibility(View.VISIBLE);
        }

        viewHolder.title.setText(reminder.getTitle().split("=>")[0]);
        Log.i("TAG_Reminder","Title : "+reminder.getTitle());
        viewHolder.content.setText(reminder.getContent().split("=>")[0]);
        viewHolder.time.setText(DateAndTimeUtil.toStringReadableTime(calendar, context));
        int iconResId = context.getResources().getIdentifier(reminder.getIcon(), "drawable", context.getPackageName());
        viewHolder.icon.setImageResource(iconResId);
        GradientDrawable bgShape = (GradientDrawable) viewHolder.circle.getDrawable();
        bgShape.setColor(Color.parseColor(reminder.getColour()));

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewActivity.class);
                intent.putExtra("NOTIFICATION_ID", reminderList.get(viewHolder.getAdapterPosition()).getId());

                // Add shared element transition animation if on Lollipop or later
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    CardView cardView = (CardView) view.findViewById(R.id.notification_card);

                    TransitionSet setExit = new TransitionSet();
                    Transition transition = new Fade();
                    transition.excludeTarget(android.R.id.statusBarBackground, true);
                    transition.excludeTarget(android.R.id.navigationBarBackground, true);
                    transition.excludeTarget(R.id.fab_button, true);
                    transition.excludeTarget(R.id.recycler_view, true);
                    transition.setDuration(400);
                    setExit.addTransition(transition);

                    ((Activity) context).getWindow().setSharedElementsUseOverlay(false);
                    ((Activity) context).getWindow().setReenterTransition(null);

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(((Activity) context), cardView, "cardTransition");
                    ActivityCompat.startActivity(((Activity) context), intent, options.toBundle());

                    ((RecyclerListener) context).hideFab();
                } else {
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reminderList.size();
    }
}