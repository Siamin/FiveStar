package com.siamin.fivestart.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.siamin.fivestart.R;
import com.siamin.fivestart.activitys.SettingActivity;
import com.siamin.fivestart.helpers.ValidationHelper;
import com.siamin.fivestart.models.SystemModel;
import com.siamin.fivestart.models.TextMessageModel;

import java.util.List;

public class TextMessageAdapter extends RecyclerView.Adapter<TextMessageAdapter.cvh> {


    private Context context;
    public List<TextMessageModel> model;
    public String prifixCode = "B";
    public int indexSystem = 0;
    public SystemModel systemModel;
    private String[] Chars = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private ValidationHelper validationHelper;


    public TextMessageAdapter(Context context, List<TextMessageModel> model) {
        this.context = context;
        this.model = model;
        this.validationHelper = new ValidationHelper();

    }

    @Override
    public cvh onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_textmessage, parent, false);
        return new cvh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final cvh holder, final int position) {

        holder.bind(model.get(position));
        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textMessage = holder.body.getText().toString();

                if (!textMessage.isEmpty()) {
                    Log.i("TAG_","validation => "+validationHelper.validateEnglish(textMessage));
                    if (!validationHelper.validateEnglish(textMessage) && textMessage.length() < 15) {
                        sendMessage(textMessage, position);
                    } else if (validationHelper.validateEnglish(textMessage) && textMessage.length() < 20) {
                        sendMessage(textMessage, position);
                    } else {
                        holder.body.setError(context.getResources().getString(R.string.ErrorLenghtText));
                    }
                } else {
                    //Error number is empty
                    ((SettingActivity) context).message.ErrorMessage(context.getString(R.string.EnterMessage));
                }


            }
        });
    }

    private void sendMessage(String textMessage, int index) {
        if (indexSystem >= 0) {
            String CodeMessage = prifixCode + Chars[index] + systemModel.pinCode + textMessage;
            ((SettingActivity) context).sendMessage(CodeMessage, indexSystem + 1);
        } else {
            //Error set Model
            ((SettingActivity) context).message.ErrorMessage(context.getString(R.string.selectSystem));

        }

    }

    @Override
    public int getItemCount() {
        return model.size();
    }


    public class cvh extends RecyclerView.ViewHolder {

        TextView title;
        EditText body;

        CardView submit;


        public cvh(View iv) {
            super(iv);

            title = iv.findViewById(R.id.adapterTextMessageTitle);
            body = iv.findViewById(R.id.adapterTextMessageBody);
            submit = iv.findViewById(R.id.adapterTextMessageSubmit);


        }

        void bind(TextMessageModel model) {

            title.setText(model.Title);
            body.setText(model.Body);

        }


    }


}