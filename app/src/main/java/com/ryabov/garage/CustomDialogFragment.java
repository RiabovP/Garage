package com.ryabov.garage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class CustomDialogFragment extends DialogFragment{

    static String temp_data, titleDiag;
    //private Button btn1, btn2, btn3;

    final int[] ifd = new int[1];

    static CustomDialogFragment newInstance (String temp, String title)
    {
        CustomDialogFragment dialogFragment=new CustomDialogFragment();
        temp_data=temp;
        titleDiag=title;
        return dialogFragment;
    }

    @NonNull
    public Dialog onCreateDialog (Bundle savedInstanceState){

        final String[] choiseArray = {"График за месяц", "График за неделю", "График за 24 часа"};

        //LayoutInflater inflater = getActivity().getLayoutInflater();

        //View view = inflater.inflate(R.layout.dialog_popup, null);

        AlertDialog.Builder bld = new AlertDialog.Builder(getActivity());
        return bld.setTitle(titleDiag + temp_data + " °C")
                .setSingleChoiceItems(choiseArray, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ifd[0] = which;
                    }
                })
                .setCancelable(true)
                .setPositiveButton("OK", onClickListener)
                .setNegativeButton("Отмена", null)
                .create();
    }


    DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (ifd[0]) {
                case 0:
                    Intent intent1 = new Intent(getActivity(), tempGraph.class);
                    startActivity(intent1);
                    dialog.dismiss();
                    break;
                case 1:
                    Toast.makeText(getActivity(),"Данных пока нет, попробуйте позже",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getActivity(),"Данных пока нет, попробуйте позже",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
