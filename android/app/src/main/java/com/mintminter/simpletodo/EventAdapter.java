package com.mintminter.simpletodo;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintminter.simpletodo.common.Event;
import com.mintminter.simpletodo.common.EventCallback;
import com.mintminter.simpletodo.common.Util;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Irene on 8/21/17.
 */

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private ArrayList<Event> mEvents;
    private int mShowType;
    private EventCallback mEventCallback;

    public EventAdapter(Context context, ArrayList<Event> events, int showType, EventCallback eventCallback){
        mContext = context;
        mEvents = events;
        mEventCallback = eventCallback;
        mShowType = showType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mEvents.size() > 0) {
            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_event, parent, false);
            return new EventViewHolder(itemView);
        }else{
            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_blank, parent, false);
            return new BlankViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(mEvents.size() > 0) {
            ((EventViewHolder) holder).bind(position);
        }else{
            ((BlankViewHolder) holder).bind();
        }
    }

    @Override
    public int getItemCount() {
        if(mEvents.size() == 0){
            return 1;
        }

        return mEvents.size();
    }

    class BlankViewHolder extends RecyclerView.ViewHolder{
        private ImageView mBlankIcon;
        private TextView mBlankDes;

        public BlankViewHolder(View itemView){
            super(itemView);
            mBlankIcon = (ImageView) itemView.findViewById(R.id.item_blank_icon);
            mBlankDes = (TextView) itemView.findViewById(R.id.item_blank_des);
        }

        public void bind(){
            if(mEventCallback.countAllEvents() == 0){
                mBlankIcon.setImageResource(R.mipmap.ic_todo);
                mBlankDes.setText(Util.getString(mContext, R.string.app_slogn));
            }else {
                switch (mShowType) {
                    case MainActivity.SHOW_TYPE_ALL:
                        mBlankIcon.setImageResource(R.mipmap.ic_todo);
                        mBlankDes.setText(Util.getString(mContext, R.string.app_slogn));
                        break;
                    case MainActivity.SHOW_TYPE_TODO:
                        mBlankIcon.setImageResource(R.mipmap.ic_cafe);
                        mBlankDes.setText(Util.getString(mContext, R.string.app_slogn_blanktodo));
                        break;
                    case MainActivity.SHOW_TYPE_DONE:
                        mBlankIcon.setImageResource(R.mipmap.ic_neutral);
                        mBlankDes.setText(Util.getString(mContext, R.string.app_slogn_blankdone));
                        break;
                }
            }
        }
    }

    class EventViewHolder extends RecyclerView.ViewHolder{
        private View mItemView;
        private TextView mTitle;
        private ImageView mDoneIcon;
        private LinearLayout mBg;
        private TextView mCreate;
        private TextView mFinish;


        public EventViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            mTitle = (TextView) itemView.findViewById(R.id.item_event_title);
            mDoneIcon = (ImageView) itemView.findViewById(R.id.item_event_done_icon);
            mBg = (LinearLayout) itemView.findViewById(R.id.item_event_bg);
            mCreate = (TextView) itemView.findViewById(R.id.item_event_create);
            mFinish = (TextView) itemView.findViewById(R.id.item_event_finish);
        }

        public void bind(int position){
            final Event oldEvent = mEvents.get(position);
            //final int realPosition = mOriginalEvents.indexOf(event);
            mTitle.setText(oldEvent.mTitle);

            switch (oldEvent.mPriority){
                case Event.PRIORITY_NORMAL:
                    mBg.setBackgroundColor(Util.getNormalPriorityColor(mContext));
                    break;
                case Event.PRIORITY_HIGH:
                    mBg.setBackgroundColor(Util.getHighPriorityColor(mContext));
                    break;
                case Event.PRIORITY_LOW:
                    mBg.setBackgroundColor(Util.getLowPriorityColor(mContext));
                    break;
            }

            mItemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mEventCallback.deleteEvent(oldEvent);
                    return false;
                }
            });

            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fm = ((Activity)mContext).getFragmentManager();
                    EditEventDialogFragment editDialogFragment = EditEventDialogFragment.newInstance(oldEvent, mEventCallback);
                    editDialogFragment.show(fm, mContext.getString(R.string.edit_fragmenttitle));
                }
            });

            if(oldEvent.mDoneTime > 0){
                mDoneIcon.setVisibility(View.GONE);

            }else{
                mDoneIcon.setVisibility(View.VISIBLE);
            }
            mDoneIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Event newEvent = new Event();
                    newEvent.fromJson(oldEvent.toJson());
                    newEvent.mDoneTime = Calendar.getInstance().getTimeInMillis();
                    mEventCallback.updateEvent(oldEvent, newEvent);
                }
            });

            mCreate.setText(String.format(Util.getString(mContext, R.string.item_event_create), Util.formatTime(oldEvent.mCreateTime)));
            if(oldEvent.mDoneTime > -1){
                mFinish.setText(String.format(Util.getString(mContext, R.string.item_event_finish), Util.formatTime(oldEvent.mDoneTime)));
            }else{
                mFinish.setText(Util.getString(mContext, R.string.item_event_todo));
            }
        }

    }

}
