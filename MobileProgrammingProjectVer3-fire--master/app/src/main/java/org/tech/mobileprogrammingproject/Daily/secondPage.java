package org.tech.mobileprogrammingproject.Daily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import org.tech.mobileprogrammingproject.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class secondPage extends Fragment {
    public static String dateTime = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        return (ViewGroup) inflater.inflate(R.layout.secondpage, container, false);
    }
}
