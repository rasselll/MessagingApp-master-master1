package com.myproj.blogapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Guillermo on 09/04/2016.
 */
public class BottomNavigationFragment extends Fragment {

    private final static String TAG = BottomNavigationFragment.class.getName();

    private Button button1;
    private Button button2;
    private Button button3;

    private Button[] buttons;

    int selectedPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_navigation, container, false);

        // Buttons
        button1 = (Button) view.findViewById(R.id.button1);
        button2 = (Button) view.findViewById(R.id.button2);
        button3 = (Button) view.findViewById(R.id.button3);
        buttons = new Button[]{button1,button2,button3};

        // Get tag position
        try {
            Log.d(TAG,getTag());
            selectedPosition = Integer.valueOf(getTag());
        }catch (Exception e){
            Log.d(TAG,"No se puede recuperar tag...");
            selectedPosition = -1;
        }

        //Set buttons color depending on the active one
        setButtonsColor(selectedPosition);

        // Setting onclick listernet to buttons
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton1();

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton2();
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButton3();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void onClickButton1(){
        Intent intent = new Intent(getActivity(),timetable.class);
        startActivity(intent);
    }

    public void onClickButton2(){
        Intent intent = new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
    }

    public void onClickButton3(){
        Intent intent = new Intent(getActivity(),SetupActivity.class);
        startActivity(intent);
    }

    /**
     * Da un color destacado al boton seleccionado (el primary color para seguir las indicaciones de material design)
     * @param activePosition
     */
    private void setButtonsColor(int activePosition){
        if(activePosition>=0 && activePosition<buttons.length) {
            Button selectedButton = buttons[activePosition];
            selectedButton.setTextColor(getActivity().getResources().getColor(R.color.blackt));

            if(selectedButton == button1) {
                Drawable top = getResources().getDrawable(R.drawable.pray);
                selectedButton.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
            }

            if(selectedButton == button2) {
                Drawable top = getResources().getDrawable(R.drawable.homenew);
                selectedButton.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
            }

            if(selectedButton == button3) {
                Drawable top = getResources().getDrawable(R.drawable.email);
                selectedButton.setCompoundDrawablesWithIntrinsicBounds(null, top , null, null);
            }
        }
    }

}
