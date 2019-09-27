package cn.albert.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import cn.albert.autosystembar.SystemBarHelper

class NavigationBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_bar)

        SystemBarHelper.Builder()
                .navigationBarColor(ContextCompat.getColor(this, R.color.colorAccent))
                .into(this)
    }
}
