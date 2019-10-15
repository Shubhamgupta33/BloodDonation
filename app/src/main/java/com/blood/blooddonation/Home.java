package com.blood.blooddonation;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class Home extends Fragment {
    private FrameLayout mMainFrame;
    private Button btnSearch;
    private EditText city;
    private Spinner blood_group, state;
    public String states1, citys, blood;
    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.activity_home, container, false);

        btnSearch = v.findViewById(R.id.btnSearch);
        city = v.findViewById(R.id.etCity);
        state = v.findViewById(R.id.etState);
        blood_group = v.findViewById(R.id.spinner);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.Blood_group, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blood_group.setAdapter(adapter);


        ArrayAdapter<CharSequence> states = ArrayAdapter.createFromResource(getContext(),
                R.array.State, android.R.layout.simple_spinner_item);
        states.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(states);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vel();
            }
        });

        return v;
    }

    private void vel() {
        String blood = blood_group.getSelectedItem().toString();
        String sta = state.getSelectedItem().toString();
        String citys = city.getText().toString();
        if (blood.equals("Select Blood Group") || sta.equals("Select State") || citys.equals("")) {
            Toast.makeText(getContext(), "Fill All Requirements", Toast.LENGTH_SHORT).show();
        } else {
            states1 = state.getSelectedItem().toString();
            citys = city.getText().toString();
            citys = citys.toLowerCase();
            blood = blood_group.getSelectedItem().toString();
            Intent intent = new Intent(getContext(), Searched_People.class);
            intent.putExtra("states", states1); // getText() SHOULD NOT be static!!!
            intent.putExtra("Citys", citys);
            intent.putExtra("blood", blood);
            startActivity(intent);
            startActivity(new Intent(getContext(), Searched_People.class));
        }
    }
}
