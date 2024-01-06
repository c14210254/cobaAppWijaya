package paba.uas.cobaappwijaya

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import paba.uas.cobaappwijaya.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentManager : FragmentManager
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        goToFragment(fragment_home())

        binding.btnHomeNavBar.setOnClickListener {
            goToFragment(fragment_home())
        }

        binding.btnOrderNavBar.setOnClickListener {
            goToFragment(fragment_order())
        }


    }

    private fun goToFragment(fragment: Fragment) {
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.fragmentContainerView, fragment).commit()

        val color = "#28D5F3"
        val colorInt = Color.parseColor(color)
        val colorStateList = ColorStateList.valueOf(colorInt)

        when (fragment) {
            is fragment_home -> {
                Log.d("HOME", "masuk")
                binding.btnHomeNavBar.backgroundTintList = colorStateList
                binding.btnOrderNavBar.backgroundTintList =
                    ContextCompat.getColorStateList(this, android.R.color.transparent)
            }
            is fragment_order -> {
                Log.d("ORDER", "masuk")
                binding.btnOrderNavBar.backgroundTintList = colorStateList
                binding.btnHomeNavBar.backgroundTintList =
                    ContextCompat.getColorStateList(this, android.R.color.transparent)
            }
        }
    }
}