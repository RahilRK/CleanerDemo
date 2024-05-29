package com.base.hilt

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.base.hilt.base.LocaleManager
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.ActivityMainBinding
import com.base.hilt.extensions.hide
import com.base.hilt.extensions.show
import com.base.hilt.ui.composable.MyNavController
import com.base.hilt.ui.theme.CleanerAppTheme
import com.base.hilt.utils.Constants
import com.base.hilt.utils.DataStoreUtil
import com.base.hilt.utils.DebugLog
import com.base.hilt.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var dialog: Dialog? = null
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var localeManager: LocaleManager


    @Inject
    lateinit var dataStoreUtil: DataStoreUtil
    var isUserLoggedIn = false
    private lateinit var navController: NavController

    companion object {
        fun newIntent(
            context: Context,
            destination: Int,
            data: Long? = null,
            graphId: Int? = -1,
            i: Intent? = null
        ): Intent {
            var intent = i
            if (intent == null) {
                intent = Intent(context, MainActivity::class.java)
            }
            val bundle = Bundle()
            bundle.putInt(Constants.KEY_START_DESTINATION, destination)
            data?.let {
                bundle.putLong(Constants.KEY_DATA, it)
            }
            graphId?.let {
                bundle.putInt(Constants.KEY_GRAPH_ID, it)
            }
            intent.putExtra(Constants.KEY_BUNDLE, bundle)
            return intent
        }
    }

/*
    private fun handleGraphNavigation() {
        val navView: BottomNavigationView = binding.navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
         navController = navHostFragment.navController
        navController.let {
            val navInflater = navController.navInflater
            val bundle = intent.getBundleExtra(Constants.KEY_BUNDLE)
            if (getIntValue<Int>(Constants.KEY_GRAPH_ID) != -1) {
                val graph = navInflater.inflate(getIntValue(Constants.KEY_GRAPH_ID))

                graph.setStartDestination(getIntValue<Int>(Constants.KEY_START_DESTINATION))

                graph.let { navController.setGraph(graph, bundle) }
            } else {
                val graph = navInflater.inflate(R.navigation.mobile_navigation)
                graph.setStartDestination(R.id.navigation_home)
                graph.let { navController.setGraph(graph, bundle) }
            }
        }


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home
            )
        )
//            setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

    }
*/

    override fun onCreate(savedInstanceState: Bundle?) {
        DebugLog.e("onCreate")
        super.onCreate(savedInstanceState)

        /**
         * setKeepOnScreenCondition pass true
         *
         */

        /*  val splashScreen = installSplashScreen()
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
              Log.d("MainActivity", "onCreate: I AM RUNNING ON API 12 or higher")
              lifecycleScope.launch {
                  dataStoreUtil.retrieveValue(PrefKey.IS_LOGGED_IN, Boolean::class)
                      .collect { value ->
                          isUserLoggedIn = value  ?: false
                      }
              }

          }*/


        /*  // custom exit on splashScreen
          splashScreen.setOnExitAnimationListener { splashScreenView ->
              // custom animation.
              ObjectAnimator.ofFloat(
                  splashScreenView.view,
                  View.TRANSLATION_X,
                  0f,
                  splashScreenView.view.width.toFloat()
              ).apply {
                  duration = 1000
                  // Call SplashScreenView.remove at the end of your custom animation.
                  doOnEnd {
                      splashScreenView.remove()
                  }
              }.also {
                  // Run your animation.
                  it.start()
              }
          }*/

//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        setContent {
            CleanerAppTheme {
                MyNavController()
            }
        }

//        handleGraphNavigation()
    }

    /**
     * Show Progress dialog
     * @param t: true show progress bar
     *
     *  */
    fun displayProgress(t: Boolean) {
        // binding.loading = t
        if (t) {
            if (dialog == null) {
                dialog = Utils.progressDialog(this)
            }
            dialog?.show()
        } else {
            dialog?.dismiss()
        }
    }

    /**
     * This method is used to hide the keyboard.
     */
    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * Toolbar manages items and visibility according to
     */
    fun toolBarManagement(toolbarModel: ToolbarModel?) {
        if (toolbarModel != null) {
            when {
                toolbarModel.isVisible -> {
                    binding.toolbarWithBackToolbar.hide()
                    binding.layToolbar.appBar.show()
                    binding.layToolbar.toolbarTitle.text = toolbarModel.title
                }
                //Custom Toolbar with Back Button
                (toolbarModel.isToolbarWithBackVisible) -> {
                    binding.layToolbar.appBar.hide()
                    binding.layWithBackToolbar.show()
                    setSupportActionBar(binding.toolbarWithBackToolbar)
                    if (toolbarModel.isBackBtnVisible) {
                        binding.toolbarWithBackToolbar.setNavigationIcon(R.drawable.ic_back_white)
                    } else {
                        binding.toolbarWithBackToolbar.navigationIcon = null
                    }
                    binding.toolbarWithBackToolbar.title = toolbarModel.title
                    binding.toolbarWithBackToolbar.setNavigationOnClickListener {
                        hideKeyboard()
                        navController.navigateUp()

                    }
                }
                else -> {
                    binding.layToolbar.appBar.visibility = View.GONE
                }
            }
        }
    }


    /**
     * Override method for fragments attach
     */
    override fun attachBaseContext(base: Context) {
        DebugLog.e("attachBaseContext")
        super.attachBaseContext(base);
        /*val languageCode = MyApp.applicationContext().mPref.getValueString(
            PrefKey.SELECTED_LANGUAGE,
            PrefKey.EN_CODE
        )
        useCustomConfig(base, languageCode)?.let { super.attachBaseContext(it) }*/
    }


    /**
     * Method called when language changes done
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        localeManager.setLocale(this)
    }

    /**
     * After applied locale changes override method called
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
//    val languageCode = mPref.getValueString(PrefKey.SELECTED_LANGUAGE, PrefKey.EN_CODE)
        /*  window.decorView.layoutDirection =
              if (languageCode == PrefKey.AR_CODE) View.LAYOUT_DIRECTION_RTL else View.LAYOUT_DIRECTION_LTR*/
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun useCustomConfig(context: Context, languageCode: String?): Context? {
        Locale.setDefault(Locale(languageCode!!))
        return if (Build.VERSION.SDK_INT >= 17) {
            val config = Configuration()
            config.setLocale(Locale(languageCode))
            context.createConfigurationContext(config)
        } else {
            val res: Resources = context.resources
            val config =
                Configuration(res.configuration)
            config.locale = Locale(languageCode)
            res.updateConfiguration(config, res.displayMetrics)
            context
        }
    }

    /**
     *
     */
    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onDestroy() {
        lifecycleScope.launch { dataStoreUtil.clearDataStore()  }
        hideKeyboard()
        super.onDestroy()


    }

    override fun onSupportNavigateUp(): Boolean {
        return/* NavigationUI.navigateUp(navController, drawerLayout) ||*/ super.onSupportNavigateUp()
    }
}