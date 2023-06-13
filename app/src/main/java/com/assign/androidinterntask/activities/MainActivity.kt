package com.assign.androidinterntask.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.assign.androidinterntask.R
import com.assign.androidinterntask.databinding.ActivityMainBinding
import com.assign.androidinterntask.fragments.HomeFragment
import com.assign.androidinterntask.utils.AppDelegate
import com.assign.androidinterntask.utils.Constants


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppDelegate.getInstance().saveBearerToken(Constants.Token)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(binding.container.id, HomeFragment())
            .commitNow()

    }
}