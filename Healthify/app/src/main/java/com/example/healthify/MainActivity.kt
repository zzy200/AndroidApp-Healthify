package com.example.healthify

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.RadioGroup
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    lateinit var drawer_layout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.main)

        //获取各控件引用
        drawer_layout = findViewById(R.id.main)
        //DrawerLayout与ToolBar相互配合
        setSupportActionBar(findViewById(R.id.toolbar))

        //使DrawerLayout与Toolbar同步的关键对象
        val toggle = ActionBarDrawerToggle(
            this,//承载Drawer的activity
            findViewById(R.id.main),//引用布局文件中最顶层的drawerlayout布局控件
            findViewById(R.id.toolbar),//引用toolbar，点击左上角图标可现实drawer
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        //设置toggle对Drawer相关事件进行响应
        drawer_layout.addDrawerListener(toggle)
        //同步toolbar与drawerlayout的状态
        toggle.syncState()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()


        //在onCreate()后引用navController，确保所有fragment都已装载完毕
        val nav_view: NavigationView = findViewById(R.id.nav_view)
        val navController = this.findNavController(R.id.fragmentContainerView)
        NavigationUI.setupWithNavController(nav_view, navController)


        //设置在第一个fragment不显示actionbar
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.first_fragment) {
                supportActionBar?.hide()
            } else {
                supportActionBar?.show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }



}