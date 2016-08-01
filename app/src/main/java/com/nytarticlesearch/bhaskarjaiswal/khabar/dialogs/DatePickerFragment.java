package com.nytarticlesearch.bhaskarjaiswal.khabar.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

/**
 * Created by bhaskarjaiswal on 6/18/16.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private DatePickerDialog.OnDateSetListener onDateSetListener;

    public interface DatePickerDialogListener {
        void onFinishDatePickerDialog(String inputText);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof DatePickerDialog.OnDateSetListener) {
            onDateSetListener = (DatePickerDialog.OnDateSetListener) activity;
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){

        String date = getArguments().get("date").toString();
        int month = Integer.parseInt(date.substring(0,date.indexOf("-")))-1;
        int day = Integer.parseInt(date.substring(date.indexOf("-")+1,date.lastIndexOf("-")));
        int year = Integer.parseInt(date.substring(date.lastIndexOf("-")+1));
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.d("DPF", ""+(monthOfYear+1)+"-"+dayOfMonth+"-"+year);
        monthOfYear++;
        String month =monthOfYear+"", day=dayOfMonth+"";

        if(((monthOfYear)+"").length() < 2 ){
            month="0"+(monthOfYear);
        }
        if((dayOfMonth+"").length()<2){
            day="0"+dayOfMonth;
        }
        sendBackResults(month + "-" + day + "-" + year);
    }

    private void sendBackResults(String date){
        Log.d("datepickerfragment",date);
        DatePickerDialogListener listener = (DatePickerDialogListener) getTargetFragment();
        listener.onFinishDatePickerDialog(date);
    }
}
