package com.nytarticlesearch.bhaskarjaiswal.khabar.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.nytarticlesearch.bhaskarjaiswal.khabar.R;
import com.nytarticlesearch.bhaskarjaiswal.khabar.constants.Config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bhaskarjaiswal on 7/31/16.
 */
public class SearchCriteriaDialogFragment extends DialogFragment implements DatePickerFragment.DatePickerDialogListener {

    private SharedPreferences sharedpreferences;
    private SharedPreferences.Editor editor;
    private boolean isPreferenceChanged = false;
    private DatePickerFragment datePickerFragment;

    private CallSearchArticles callSearchArticles;

    @BindView(R.id.tvDate)
    TextView tvDate;

    @BindView(R.id.spinner)
    Spinner spinner;

    @BindView(R.id.chkbxArts)
    CheckBox chkbxArts;

    @BindView(R.id.chkbxFashion)
    CheckBox chkbxFashion;

    @BindView(R.id.chkbxSports)
    CheckBox chkbxSports;

    public SearchCriteriaDialogFragment() {
    }

    public interface CallSearchArticles {
        public void onChangedSearchCriteria();
    }

    public void setCallSearchArticles(CallSearchArticles callSearchArticles) {
        this.callSearchArticles = callSearchArticles;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.article_search_criteria, container);
        ButterKnife.bind(this, view);
        sharedpreferences = this.getActivity().getSharedPreferences(Config.PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(Config.DATE_FORMAT);

        datePickerFragment = new DatePickerFragment();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_search_criteria);

        Log.d("SCDF", sharedpreferences.getString("date", "NoDate"));
        String date = sharedpreferences.getString("date", "NoDate");

        if (!date.equalsIgnoreCase("NoDate")) {
            tvDate.setText(date);
            Log.d("dateset to ", tvDate.getText().toString());
        } else {
            tvDate.setText(sdf.format(today));
        }

        spinner.setSelection(sharedpreferences.getString("sortedBy", "newest").equalsIgnoreCase("newest") ? 0 : 1);

        chkbxArts.setSelected(sharedpreferences.getBoolean("arts", false) ? true : false);
        chkbxFashion.setSelected(sharedpreferences.getBoolean("fashion", false) ? true : false);
        chkbxSports.setSelected(sharedpreferences.getBoolean("sports", false) ? true : false);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("date", tvDate.getText().toString());
                datePickerFragment.setArguments(args);
                datePickerFragment.setTargetFragment(SearchCriteriaDialogFragment.this, 300);
                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.miSave:

                        editor.putString("date", tvDate.getText().toString());
                        editor.putString("sortedBy", spinner.getSelectedItem().toString().toLowerCase());
                        editor.putBoolean("arts", chkbxArts.isChecked() ? true : false);
                        editor.putBoolean("fashion", chkbxFashion.isChecked() ? true : false);
                        editor.putBoolean("sports", chkbxSports.isChecked() ? true : false);
                        editor.commit();

                        Log.d("saved sharedpreferences", sharedpreferences.getString("date", "NoDate") + " " +
                                sharedpreferences.getString("sortedBy", "NoSorting") + " " +
                                sharedpreferences.getBoolean("arts", false) + " " +
                                sharedpreferences.getBoolean("fashion", false) + " " +
                                sharedpreferences.getBoolean("sports", false));

                        callSearchArticles.onChangedSearchCriteria();


                        getDialog().dismiss();
                        return true;
                    case R.id.miCancel:
                        getDialog().dismiss();
                        return true;
                }
                return true;
            }
        });

    }

    @Override
    public void onFinishDatePickerDialog(String date) {
        tvDate.setText(date);
        editor.putString("date", tvDate.getText().toString());
        editor.commit();

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
