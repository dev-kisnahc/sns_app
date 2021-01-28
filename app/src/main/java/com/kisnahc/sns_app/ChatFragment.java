package com.kisnahc.sns_app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatFragment extends Fragment {

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_message;
        ImageView iv_message;
        CircleImageView iv_profile;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_message = itemView.findViewById(R.id.tv_message);
            iv_message = itemView.findViewById(R.id.iv_message);
            iv_profile = itemView.findViewById(R.id.iv_profile);
        }
    }

    private RecyclerView mRecyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mRecyclerView = view.findViewById(R.id.message_recycler_view);
        return view;


    }
}