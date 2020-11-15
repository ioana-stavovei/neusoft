package com.ioana.vahaapp

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.content.IntentFilter
import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var mAdminComponentName: ComponentName
    private lateinit var mDevicePolicyManager: DevicePolicyManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSettings()
        setContentView(R.layout.activity_main)
        setupAnimation()
        setupButtons()
    }

    private fun setupAnimation() {
        val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_animation)
        val nameTV = findViewById<TextView>(R.id.nameTV)
        nameTV.animation = animation
    }

    private fun setupButtons() {
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(false);
        getSupportActionBar()?.setHomeButtonEnabled(false);
    }

    override fun onBackPressed() {}
    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        val keyCode = event?.keyCode
        return when (keyCode) {
            KeyEvent.KEYCODE_VOLUME_UP -> {
                true
            }
            KeyEvent.KEYCODE_VOLUME_DOWN -> {
                true
            }
            else -> super.dispatchKeyEvent(event)
        }
    }

    private fun setupIntent() {
        val it = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        it.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, ComponentName(this, AdminReceiver::class.java))
        startActivityForResult(it, 0)
    }

    private fun setupSettings() {
        mAdminComponentName = AdminReceiver.getComponentName(this)
        mDevicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        if (mDevicePolicyManager.isDeviceOwnerApp(packageName)) {
            mDevicePolicyManager.setLockTaskPackages(mAdminComponentName, arrayOf(packageName))
            val intentFilter = IntentFilter(ACTION_MAIN)
            intentFilter.addCategory(CATEGORY_HOME)
            intentFilter.addCategory(CATEGORY_DEFAULT)
            mDevicePolicyManager.addPersistentPreferredActivity(mAdminComponentName,
                    intentFilter, ComponentName(packageName, MainActivity::class.java.name))
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            startLockTask()
        }
    }
}