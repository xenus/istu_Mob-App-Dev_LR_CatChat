package com.example.catchat

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var fab: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val navView = findViewById<NavigationView>(R.id.nav_view)
        NavigationUI.setupWithNavController(navView, navController)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val builder = AppBarConfiguration.Builder(navController.graph)
        builder.setOpenableLayout(drawer)
        val appBarConfiguration = builder.build()

        toolbar.setupWithNavController(navController, appBarConfiguration)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNavView.setupWithNavController(navController)

        // Настройка FloatingActionButton
        fab = findViewById(R.id.fab) // Инициализация FAB
        fab.setOnClickListener {
            openComposeEmailScreen()
        }
        // Динамическое расположение FAB над BottomNavigationView
        bottomNavView.viewTreeObserver.addOnGlobalLayoutListener {
            val layoutParams = fab.layoutParams as CoordinatorLayout.LayoutParams
            layoutParams.bottomMargin = bottomNavView.height + layoutParams.marginEnd
            fab.layoutParams = layoutParams
        }
        // Слушатель навигации для управления видимостью FAB
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.writeFragment -> fab.hide() // Скрыть FAB при открытии writeFragment
                else -> fab.show() // Показать FAB при закрытии writeFragment
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return item.onNavDestinationSelected(navController)
                || super.onOptionsItemSelected(item)
    }

    // Логика для открытия экрана написания письма
    private fun openComposeEmailScreen() {
        val navController = findNavController(R.id.nav_host_fragment)
        navController.navigate(R.id.writeFragment) // Укажите ID вашего фрагмента
    }
}