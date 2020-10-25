package com.guet.xiaohongsong_guetonly

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import com.guet.xiaohongsong_guetonly.databinding.ActivityMainBinding
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class MainActivity : BaseActivity() {
    companion object {
        const val REQUEST_IMAGE_GET = 1
    }

    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding?.root)
    }

    override fun onStart() {
        super.onStart()

        binding?.fabAddPost?.setOnClickListener {
            selectImageWithPermissionCheck()
        }

        binding?.bnvHome?.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.i_home -> {
                    binding?.navHostFragment?.findNavController()?.popBackStack()
                    binding?.navHostFragment?.findNavController()?.navigate(R.id.action_global_homeFragment)
                    true
                }
                R.id.i_me -> {
                    binding?.navHostFragment?.findNavController()?.popBackStack()
                    binding?.navHostFragment?.findNavController()?.navigate(R.id.action_global_userFragment)
                    true
                }
                else -> {
                    false
                }
            }
        }

        binding?.bnvHome?.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.i_home -> {
                    true
                }
                R.id.i_me -> {
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_GET) {
            val bundle = bundleOf("imgUri" to data?.data.toString())
            binding?.navHostFragment?.findNavController()?.navigate(R.id.action_global_postFragment, bundle)
        }
    }

    fun showBottomNavigationBar() {
        binding?.bnvHome?.visibility = View.VISIBLE
        binding?.fabAddPost?.visibility = View.VISIBLE
    }

    fun hideBottomNavigationBar() {
        binding?.bnvHome?.visibility = View.GONE
        binding?.fabAddPost?.visibility = View.GONE
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onStateChange(event: StateChangeEvent) {
        if (event.isMainActivity) {
            showBottomNavigationBar()
        } else {
            hideBottomNavigationBar()
        }
    }
}