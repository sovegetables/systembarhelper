package cn.albert.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import cn.albert.autosystembar.SystemBarHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by albert on 2017/9/26.
 *
 */
class MainActivity : AppCompatActivity() {

    object Static{

        val DATAS = listOf(Data(AutoStatusBarSettingsActivity::class.java,"自动配置状态栏颜色"),
                Data(ImmersedActivity::class.java, "沉浸式状态栏"),
                Data(NavigationBarActivity::class.java, "改变导航栏颜色"),
                Data(DrawerActivity::class.java, "抽屉布局"))
    }

    data class Data(val clazz: Class<out Activity>, val title: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemBarHelper.Builder()
                .statusBarColor(ContextCompat.getColor(this, R.color.colorAccent))
                .navigationBarColor(ContextCompat.getColor(this, R.color.colorAccent))
                .statusBarFontStyle(SystemBarHelper.STATUS_BAR_DARK_FONT_STYLE)
                .navigationBarStyle(SystemBarHelper.NAVIGATION_BAR_LIGHT_ICON_STYLE)
                .enableImmersedNavigationBar(true)
                .enableImmersedStatusBar(true)
                .into(this)
        setContentView(R.layout.activity_main)

        recycler_view.layoutManager = LinearLayoutManager(this)
        val adapter = object : BaseQuickAdapter<Data, BaseViewHolder>(R.layout.item, Static.DATAS) {

            override fun convert(helper: BaseViewHolder?, item: Data?) {
                helper?.setText(R.id.btn, item?.title)
                        ?.addOnClickListener(R.id.btn)
            }
        }

        recycler_view.adapter = adapter
        adapter.setOnItemChildClickListener {
            _, _, position ->
            startActivity(Intent(applicationContext, adapter.data[position].clazz))
        }

        Log.d("0----098880", getSystemProperty("ro.build.product", "defaultValue"))
    }

    private fun getSystemProperty(key: String, defaultValue: String): String {
        try {
            val clz = Class.forName("android.os.SystemProperties")
            val get = clz.getMethod("get", String::class.java, String::class.java)
            return get.invoke(clz, key, defaultValue) as String
        } catch (ignored: Exception) {
        }

        return defaultValue
    }
}