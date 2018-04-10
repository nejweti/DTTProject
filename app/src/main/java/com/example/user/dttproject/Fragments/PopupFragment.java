package com.example.user.dttproject.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.user.dttproject.R;

public class PopupFragment extends DialogFragment implements View.OnClickListener {

    private LinearLayout mCallButton, mCancelDialog,mPopupLinear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setCancelable(false);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        View view = inflater.inflate(R.layout.fragment_popup, container, false);

        mCallButton = view.findViewById(R.id.callbutton);
        mCancelDialog = view.findViewById(R.id.cancelDialog);
        mPopupLinear = view.findViewById(R.id.popupLinear);

//        mPopupLinear.getBackground().setAlpha(0);
        mCallButton.setOnClickListener(this);
        mCancelDialog.setOnClickListener(this);
        getCallPermission();

        return view;
    }


    @Override
    public void onClick(View view) {
        if (view == mCallButton) {

            dialPhoneNumber("22222");

        }
        if (view == mCancelDialog) {
            dismiss();

        }
    }



    public void dialPhoneNumber(String phonenumber) {

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phonenumber));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }

    }
    public void getCallPermission() {
        String[] permission = {Manifest.permission.CALL_PHONE};

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

        } else ActivityCompat.requestPermissions(getActivity(), permission, 1);
    }

}
