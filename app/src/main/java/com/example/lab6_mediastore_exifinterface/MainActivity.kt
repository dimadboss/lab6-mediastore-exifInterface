package com.example.lab6_mediastore_exifinterface

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Debug
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.lab6_mediastore_exifinterface.data.ExifData
import com.example.lab6_mediastore_exifinterface.data.toStringPretty
import com.example.lab6_mediastore_exifinterface.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    lateinit var imageView: ImageView
    lateinit var button: Button
    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


        imageView = findViewById(R.id.imageView)
        button = findViewById(R.id.buttonUploadImg)
        button.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
            loadExifInfo()
        }
    }

    private fun loadExifInfo() {
        val tag = "loadExifInfo"
        if (imageUri == null) {
            Log.e(tag, "imageUri was null")
            return
        }
        try {
            this.contentResolver.openInputStream(imageUri!!).use { inputStream ->
                val exif = inputStream?.let { ExifInterface(it) }
                if (exif == null) {
                    Log.e(tag, "exif was null")
                    return
                }
                val data = ExifData(
                    exif.getAttribute(ExifInterface.TAG_DATETIME),
                    exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE),
                    exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF),
                    exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE),
                    exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF),
                    exif.getAttribute(ExifInterface.TAG_DEVICE_SETTING_DESCRIPTION),
                    exif.getAttribute(ExifInterface.TAG_MODEL),
                )
                findViewById<TextView>(R.id.exifTagsLabel).text = data.toStringPretty()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}