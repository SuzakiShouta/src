package com.k18054.myroster

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.k18054.myroster.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(findViewById(R.id.toolbar))

        val naviController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(naviController)

        //fab(右下の丸いボタン)が押されたらEdit画面へ
        binding.fab.setOnClickListener { view ->
            naviController.navigate(R.id.action_to_rosterEditFragment)
        }
    }

    override fun onSupportNavigateUp()
            = findNavController(R.id.nav_host_fragment).navigateUp()

    //Edit画面ではfabはいらない。戻る時(FirstFragmentのViewCreated)で見えるようにする。
    fun setFabVisible(visibility: Int) {
        binding.fab.visibility = visibility
    }
}