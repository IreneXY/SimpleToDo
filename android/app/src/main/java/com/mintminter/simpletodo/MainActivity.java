package com.mintminter.simpletodo;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mintminter.simpletodo.common.Event;
import com.mintminter.simpletodo.common.EventCallback;
import com.mintminter.simpletodo.common.EventDepot;
import com.mintminter.simpletodo.common.Util;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements EventCallback {

    protected static final int SORT_TYPE_CREATETIME = 0;
    protected static final int SORT_TYPE_PRIORITY = 1;
    protected static final int SORT_TYPE_DONETIME = 2;

    protected static final int SHOW_TYPE_ALL = 0;
    protected static final int SHOW_TYPE_TODO = 1;
    protected static final int SHOW_TYPE_DONE = 2;

    private LinearLayout mAreaDes;
    private ImageView mIconDes;
    private TextView mTxtDes;
    private TextView mBtnAdd;
    private EditText mEditTitle;
    private RecyclerView mListEvents;

    private ArrayList<Event> mEvents = new ArrayList<>();
    private int mSortType = SORT_TYPE_CREATETIME;
    private int mShowType = SHOW_TYPE_TODO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniUI();
        runGetEventTask();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        MenuItem itemSort = menu.findItem(R.id.menu_sort);
        SpannableString sort = new SpannableString(Util.getString(this,R.string.menu_group_sort));
        sort.setSpan(new ForegroundColorSpan(Util.getColor(this, R.color.colorPrimary)), 0, sort.length(), 0);
        sort.setSpan(new StyleSpan(Typeface.BOLD), 0, sort.length(), 0);
        itemSort.setTitle(sort);

        MenuItem itemShow = menu.findItem(R.id.menu_show);
        SpannableString show = new SpannableString(Util.getString(this,R.string.menu_group_show));
        show.setSpan(new ForegroundColorSpan(Util.getColor(this, R.color.colorPrimary)), 0, show.length(), 0);
        show.setSpan(new StyleSpan(Typeface.BOLD), 0, show.length(), 0);
        itemShow.setTitle(show);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        switch (mSortType){
            case SORT_TYPE_CREATETIME:
                menu.findItem(R.id.menu_sort_createtime).setChecked(true);
                break;
            case SORT_TYPE_PRIORITY:
                menu.findItem(R.id.menu_sort_priority).setChecked(true);
                break;
            case SORT_TYPE_DONETIME:
                menu.findItem(R.id.menu_sort_donetime).setChecked(true);
                break;
        }

        switch (mShowType){
            case SHOW_TYPE_ALL:
                menu.findItem(R.id.menu_show_all).setChecked(true);
                break;
            case SHOW_TYPE_TODO:
                menu.findItem(R.id.menu_show_todo).setChecked(true);
                break;
            case SHOW_TYPE_DONE:
                menu.findItem(R.id.menu_show_done).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_sort_createtime:
                mSortType = SORT_TYPE_CREATETIME;
                break;
            case R.id.menu_sort_priority:
                mSortType = SORT_TYPE_PRIORITY;
                break;
            case R.id.menu_sort_donetime:
                mSortType = SORT_TYPE_DONETIME;
                break;
            case R.id.menu_show_all:
                mShowType = SHOW_TYPE_ALL;
                break;
            case R.id.menu_show_todo:
                mShowType = SHOW_TYPE_TODO;
                break;
            case R.id.menu_show_done:
                mShowType = SHOW_TYPE_DONE;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        setEventList();
        return true;
    }

    @Override
    public void addEvent(Event event){
        new AddEventTask(new EventDepot(this), event).execute();
    }

    @Override
    public void deleteEvent(Event event){
        new DeleteEventTask(new EventDepot(this), event).execute();
    }

    @Override
    public void updateEvent(Event oldEvent, Event newEvent){
        new UpdateEventTask(new EventDepot(this), oldEvent, newEvent).execute();
    }

    @Override
    public int countAllEvents() {
        return mEvents.size();
    }

    private void iniUI(){
        mAreaDes = (LinearLayout) findViewById(R.id.des_area);
        mIconDes = (ImageView) findViewById(R.id.des_icon);
        mTxtDes = (TextView) findViewById(R.id.des_text);
        mBtnAdd = (TextView) findViewById(R.id.btn_add);
        mEditTitle = (EditText) findViewById(R.id.edit_title);
        mListEvents = (RecyclerView) findViewById(R.id.list_events);

        mListEvents.setLayoutManager(new LinearLayoutManager(this));
        setEventList();

        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickAddBtn();
            }
        });

        mEditTitle.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null &&
                        (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    clickAddBtn();
                }
                return false;
            }
        });

        mListEvents.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                scrollToLast();
            }
        });
    }

    private void setEventList(){
        ArrayList<Event> events = filter();
        if(events.size() > 0){
            String des = "";
            switch (mShowType){
                case MainActivity.SHOW_TYPE_ALL:
                    mIconDes.setImageResource(R.mipmap.ic_todo);
                    des = String.format(Util.getString(this, R.string.des_all), events.size());
                    break;
                case MainActivity.SHOW_TYPE_TODO:
                    mIconDes.setImageResource(R.mipmap.ic_neutral);
                    des = String.format(Util.getString(this, R.string.des_todo), events.size());
                    break;
                case MainActivity.SHOW_TYPE_DONE:
                    mIconDes.setImageResource(R.mipmap.ic_cafe);
                    des = String.format(Util.getString(this, R.string.des_done), events.size());
                    break;
            }
            mAreaDes.setVisibility(View.VISIBLE);
            mTxtDes.setText(des);
        }else{
            mAreaDes.setVisibility(View.GONE);
        }
        mListEvents.setAdapter(new EventAdapter(this, events, mShowType, this));
        scrollToLast();
    }

    private void sort(){
        switch (mSortType){
            case MainActivity.SORT_TYPE_CREATETIME:
                Collections.sort(mEvents, new Util.AscendingCreateTimeComparator());
                break;
            case MainActivity.SORT_TYPE_PRIORITY:
                Collections.sort(mEvents, new Util.DecendingPriorityComparator());
                break;
            case MainActivity.SORT_TYPE_DONETIME:
                Collections.sort(mEvents, new Util.AscendingDoneTimeComparator());
                break;
            default:
                Collections.sort(mEvents, new Util.AscendingCreateTimeComparator());
                break;
        }
    }

    private ArrayList<Event> filter(){
        sort();
        ArrayList<Event> res = new ArrayList<>();
        switch (mShowType){
            case MainActivity.SHOW_TYPE_ALL:
                res = mEvents;
                break;
            case MainActivity.SHOW_TYPE_TODO:
                for(Event event : mEvents){
                    if(event.mDoneTime < 0){
                        res.add(event);
                    }
                }
                break;
            case MainActivity.SHOW_TYPE_DONE:
                for(Event event : mEvents){
                    if(event.mDoneTime > -1){
                        res.add(event);
                    }
                }
                break;
            default:
                res = mEvents;
                break;
        }
        return res;
    }

    private void clickAddBtn(){
        String title = mEditTitle.getText().toString();
        Event event = new Event(title);
        addEvent(event);
    }

    private void scrollToLast(){
        mListEvents.scrollToPosition(mListEvents.getAdapter().getItemCount()-1);
    }

    private void runGetEventTask(){
        new GetEventsTask(new EventDepot(this)).execute();
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null && inputManager != null) {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            inputManager.hideSoftInputFromInputMethod(getCurrentFocus().getWindowToken(), 0);
        }
    }

    class GetEventsTask extends AsyncTask<Void, Void, Boolean>{

        EventDepot mEventDepot;

        public GetEventsTask(EventDepot eventDepot){
            mEventDepot = eventDepot;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean res = false;
            try {
                mEventDepot.open();
                mEvents = mEventDepot.getAllEvents();
                mEventDepot.close();
                res = true;
            }catch (Exception e){
                e.printStackTrace();
            }
            return res;
        }

        @Override
        protected void onPostExecute(Boolean res){
            if(res){
                setEventList();
            }
        }
    }


    class AddEventTask extends AsyncTask<Void, Void, Boolean>{

        EventDepot mEventDepot;
        Event mEvent;

        public AddEventTask(EventDepot eventDepot, Event event){
            mEventDepot = eventDepot;
            mEvent = event;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean res = false;
            try {
                mEventDepot.open();
                mEventDepot.add(mEvent);
                mEventDepot.close();
                res = true;
            } catch (Exception e){
                e.printStackTrace();
            }

            return res;
        }

        @Override
        protected void onPostExecute(Boolean res){
            if(res){
                mEvents.add(mEvent);
                mShowType = MainActivity.SHOW_TYPE_TODO;
                setEventList();
                mEditTitle.setText("");
                mEditTitle.clearFocus();
                hideSoftKeyboard();
            }
        }
    }

    class UpdateEventTask extends AsyncTask<Void, Void, Boolean> {

        EventDepot mEventDepot;
        Event mEvent;
        Event mOldEvent;
        int mPosition;

        public UpdateEventTask(EventDepot eventDepot, Event oldEvent, Event event){
            mEventDepot = eventDepot;
            mEvent = event;
            mOldEvent = oldEvent;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean res = false;
            try {
                mEventDepot.open();
                mEventDepot.update(mEvent);
                mEventDepot.close();
                res = true;
            } catch (Exception e){
                e.printStackTrace();
            }

            return res;
        }

        @Override
        protected void onPostExecute(Boolean res){
            if(res){
                int position = mEvents.indexOf(mOldEvent);
                if(position > -1) {
                    mEvents.set(position, mEvent);
                    setEventList();
                }
            }
        }
    }

    class DeleteEventTask extends AsyncTask<Void, Void, Boolean> {

        EventDepot mEventDepot;
        Event mEvent;

        public DeleteEventTask(EventDepot eventDepot, Event event){
            mEventDepot = eventDepot;
            mEvent = event;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            boolean res = false;
            try {
                mEventDepot.open();
                mEventDepot.delete(mEvent);
                mEventDepot.close();
                res = true;
            } catch (Exception e){
                e.printStackTrace();
            }

            return res;
        }

        @Override
        protected void onPostExecute(Boolean res){
            if(res){
                mEvents.remove(mEvent);
                setEventList();
            }
        }
    }
}
