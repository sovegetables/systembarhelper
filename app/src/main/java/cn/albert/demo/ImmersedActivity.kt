package cn.albert.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.albert.autosystembar.SystemBarHelper

class ImmersedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expanded_to_status_bar)
        SystemBarHelper.Builder()
                .enableImmersedStatusBar(true)
                .enableImmersedNavigationBar(true)
                .into(this)
    }
}
