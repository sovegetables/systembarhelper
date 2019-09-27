package cn.albert.demo

import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceActivity
import androidx.core.content.ContextCompat
import net.margaritov.preference.colorpicker.ColorPickerPreference

class AutoStatusBarSettingsActivity : PreferenceActivity() {

    var pickedColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)

        val color = findPreference("s_start_action_bar_color")

        pickedColor = ContextCompat.getColor(this, R.color.colorPrimary)

        if(color is ColorPickerPreference){
            color.onPreferenceChangeListener = object : Preference.OnPreferenceChangeListener {
                override fun onPreferenceChange(p0: Preference?, p1: Any?): Boolean {
                    pickedColor = p1 as Int
                    return true
                }

            }
        }
        val start = findPreference("pref_key_start")
        start.setOnPreferenceClickListener {
            val intent = Intent(this@AutoStatusBarSettingsActivity, AutoStatusBarActivity::class.java)
            intent.putExtra(AutoStatusBarActivity.Static.KEY_DATA, pickedColor)
            startActivity(intent)
            true
        }

    }
}
