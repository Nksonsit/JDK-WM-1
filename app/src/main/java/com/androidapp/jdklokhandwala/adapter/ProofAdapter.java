package com.androidapp.jdklokhandwala.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.androidapp.jdklokhandwala.R;
import com.androidapp.jdklokhandwala.api.model.UserIdentityType;
import com.androidapp.jdklokhandwala.custom.TfTextView;

import java.util.List;

/**
 * Created by sagartahelyani on 17-01-2017.
 */

public class ProofAdapter extends ArrayAdapter<UserIdentityType> {

    private List<UserIdentityType> models;

    private Context context;
    private int textViewResourceId;
    private LayoutInflater inflater;

    public ProofAdapter(Context context, int textViewResourceId, List<UserIdentityType> models) {
        super(context, textViewResourceId);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        this.models = models;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public UserIdentityType getItem(int position) {
        return models.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    private View getCustomView(int position, ViewGroup parent) {

        View convertView = inflater.inflate(textViewResourceId, parent, false);

        TfTextView txtItem = (TfTextView) convertView.findViewById(R.id.spinnerItem);
        txtItem.setText(models.get(position).getCodeValue());

        return convertView;
    }
}
