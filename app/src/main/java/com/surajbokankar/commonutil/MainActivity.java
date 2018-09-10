package com.surajbokankar.commonutil;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.surajbokankar.commonutil.databinding.ActivityMainBinding;
import com.surajbokankar.commonutil.model.User;

/**
 * Created by suraj.bokankar on 05/07/18.
 */

public class MainActivity extends AppCompatActivity {

    private User user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        user = new User();
        user.setName("Suraj Bokankar");
        user.setEmail("suraj@gamil.com");
        binding.setUser(user);
    }


}
