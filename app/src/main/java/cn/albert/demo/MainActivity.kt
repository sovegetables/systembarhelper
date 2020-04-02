package cn.albert.demo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import android.widget.Toast
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

    companion object Static{

        val DATAS = listOf(Data(AutoStatusBarSettingsActivity::class.java,"自动配置状态栏颜色"),
                Data(ImmersedActivity::class.java, "沉浸式状态栏"),
                Data(NavigationBarActivity::class.java, "改变导航栏颜色"),
                Data(DrawerActivity::class.java, "抽屉布局"))
    }

    data class Data(val clazz: Class<out Activity>, val title: String)

    private fun isShowNavigationBar(context: Context): Boolean {
        var hasNavigationBar = false
        val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        if (!hasMenuKey && !hasBackKey) {
            hasNavigationBar = true
        }
        val pSize =  Point()
        window.windowManager.defaultDisplay.getSize(pSize)
        val pRealSize =  Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            window.windowManager.defaultDisplay.getRealSize(pRealSize)
        }

        val statusBarHeight = getStatusBarHeight(context.resources)
        val navigationBarHeight = getNavigationBarHeight(context.resources)
        val j = pRealSize.y - pSize.y
        return !(hasNavigationBar && j < navigationBarHeight)
    }

    private fun getStatusBarHeight(resources: Resources): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun getNavigationBarHeight(resources: Resources): Int {
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        SystemBarHelper.Builder()
//                .statusBarColor(Color.BLUE)
//                .navigationBarColor(ContextCompat.getColor(this, R.color.colorAccent))
//                .statusBarFontStyle(SystemBarHelper.STATUS_BAR_DARK_FONT_STYLE)
//                .navigationBarStyle(SystemBarHelper.NAVIGATION_BAR_LIGHT_ICON_STYLE)
//                .enableImmersedNavigationBar(true)
//                .enableImmersedStatusBar(false)
//                .into(this)
        setContentView(R.layout.activity_main)
        val heightPixels = resources.displayMetrics.heightPixels
        val p =  Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            window.windowManager.defaultDisplay.getRealSize(p)
        }

        val pSize =  Point()
        window.windowManager.defaultDisplay.getSize(pSize)

        val pRealSize =  Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            window.windowManager.defaultDisplay.getRealSize(pRealSize)
        }

        val realMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            window.windowManager.defaultDisplay.getRealMetrics(realMetrics)
        }

        val metrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            window.windowManager.defaultDisplay.getMetrics(metrics)
        }

        val pStart =  Point()
        val pEnd =  Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            window.windowManager.defaultDisplay.getCurrentSizeRange(pStart, pEnd);
        }

        val rect = Rect()
        window.windowManager.defaultDisplay.getRectSize(rect)

        val isNavigatorShow = isShowNavigationBar(this)
        Toast.makeText(this, isNavigatorShow.toString(), Toast.LENGTH_LONG).show()

        val hasNavBar = hasNavBar(this)


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

    fun hasNavBar(context: Context): Boolean { // Kitkat and less shows container above nav bar
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            return false
        }
        // Emulator
        if (Build.FINGERPRINT.startsWith("generic")) {
            return true
        }
        val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        val hasNoCapacitiveKeys = !hasMenuKey && !hasBackKey
        val resources = context.resources
        val id = resources.getIdentifier("config_showNavigationBar", "bool", "android")
        val hasOnScreenNavBar = id > 0 && resources.getBoolean(id)
        return hasOnScreenNavBar || hasNoCapacitiveKeys || getNavigationBarHeight(context, true) > 0
    }

    fun getNavigationBarHeight(context: Context, skipRequirement: Boolean): Int {
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0 && (skipRequirement || hasNavBar(context))) {
            context.resources.getDimensionPixelSize(resourceId)
        } else 0
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