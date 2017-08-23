package com.mintminter.simpletodo;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mintminter.simpletodo.common.EventCallback;
import com.mintminter.simpletodo.common.Event;

/**
 * Created by Irene on 8/21/17.
 */

public class EditEventDialogFragment extends DialogFragment {

    private static final String KEY_EVENT = "event";

    private EditText mEditTitle;
    private TextView mBtnCancel;
    private TextView mBtnSave;
    private LinearLayout mAreaLow;
    private RadioButton mRadioLow;
    private LinearLayout mAreaNormal;
    private RadioButton mRadioNormal;
    private LinearLayout mAreaHigh;
    private RadioButton mRadioHigh;

    private EventCallback mEditCallback;
    private Event mOldEvent;

    public EditEventDialogFragment(){

    }

    public static EditEventDialogFragment newInstance(Event oldEvent, EventCallback callback){
        EditEventDialogFragment frag = new EditEventDialogFragment();
        Bundle args = new Bundle();
        args.putString(KEY_EVENT, oldEvent.toString());
        frag.setArguments(args);
        frag.mEditCallback = callback;
        frag.mOldEvent = oldEvent;
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_edit, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        String sEvent = getArguments().getString(KEY_EVENT);
        final Event event = new Event();
        event.fromString(sEvent);

        mEditTitle = (EditText) view.findViewById(R.id.edit_title);
        mBtnCancel = (TextView) view.findViewById(R.id.edit_cancel);
        mBtnSave = (TextView) view.findViewById(R.id.edit_save);
        mAreaLow = (LinearLayout) view.findViewById(R.id.edit_low_area);
        mRadioLow = (RadioButton) view.findViewById(R.id.edit_low_radio);
        mAreaNormal = (LinearLayout) view.findViewById(R.id.edit_normal_area);
        mRadioNormal = (RadioButton) view.findViewById(R.id.edit_normal_radio);
        mAreaHigh = (LinearLayout) view.findViewById(R.id.edit_high_area);
        mRadioHigh = (RadioButton) view.findViewById(R.id.edit_high_radio);

        mEditTitle.setText(event.mTitle);

        setPriority(event);

        mAreaLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.mPriority = Event.PRIORITY_LOW;
                setPriority(event);
            }
        });

        mAreaNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.mPriority = Event.PRIORITY_NORMAL;
                setPriority(event);
            }
        });

        mAreaHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.mPriority = Event.PRIORITY_HIGH;
                setPriority(event);
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKeyboard(view);
                dismiss();
            }
        });

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.mTitle = mEditTitle.getText().toString();
                mEditCallback.updateEvent(mOldEvent, event);
                hideSoftKeyboard(view);
                dismiss();
            }
        });

    }

    private void setPriority(Event event){
        switch (event.mPriority){
            case Event.PRIORITY_LOW:
                mRadioLow.setChecked(true);
                mRadioNormal.setChecked(false);
                mRadioHigh.setChecked(false);
                break;
            case Event.PRIORITY_NORMAL:
                mRadioLow.setChecked(false);
                mRadioNormal.setChecked(true);
                mRadioHigh.setChecked(false);
                break;
            case Event.PRIORITY_HIGH:
                mRadioLow.setChecked(false);
                mRadioNormal.setChecked(false);
                mRadioHigh.setChecked(true);
                break;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onResume() {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.9), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        super.onResume();

    }

}

