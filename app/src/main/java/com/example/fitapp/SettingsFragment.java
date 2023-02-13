package com.example.fitapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        view.findViewById(R.id.btnPasswordChange).setOnClickListener(mListener);
        view.findViewById(R.id.btnEmailChange).setOnClickListener(mListener);
        view.findViewById(R.id.btnPhoneChange).setOnClickListener(mListener);
        view.findViewById(R.id.btnLogout).setOnClickListener(mListener);
        return view;
    }

    @SuppressLint("NonConstantResourceId")
    private final View.OnClickListener mListener = new View.OnClickListener(){
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnPasswordChange:
                    startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                    break;
                case R.id.btnEmailChange:
                    startActivity(new Intent(getActivity(), ChangeEmailActivity.class));
                    break;
                case R.id.btnPhoneChange:
                    startActivity(new Intent(getActivity(), ChangePhoneActivity.class));
                    break;
                case R.id.btnLogout:
                    getActivity().finish();
                default:
                    break;
            }
        }
    };
}