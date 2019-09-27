package cn.albert.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.albert.autosystembar.SystemBarHelper
import kotlinx.android.synthetic.main.activity_auto_status_bar.*


class AutoStatusBarActivity : AppCompatActivity() {

    object Static{
        val KEY_DATA = "data.AutoStatusBarActivity"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_status_bar)
        toolbar.setBackgroundColor(intent.extras.getInt(Static.KEY_DATA))
        val helper = SystemBarHelper.Builder()
                .into(this)
    }
}
