package com.example.myapplication.view

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityUerDetailBinding
import com.example.myapplication.model.DataItem
import com.example.myapplication.util.Constants
import com.example.myapplication.util.Utility
import com.example.myapplication.viewmodel.UserDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executors


@AndroidEntryPoint
class UserDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: UserDetailViewModel
    private lateinit var binding: ActivityUerDetailBinding
    private lateinit var data: DataItem

    // Declaring a Bitmap local
    lateinit var mImage: Bitmap

    // Declaring and initializing an Executor and a Handler
    val myExecutor = Executors.newSingleThreadExecutor()
    val myHandler = Handler(Looper.getMainLooper())

    val EXTERNAL_STORAGE_PERMISSION_CODE=300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_uer_detail)
        initViewModel()
        initView()
    }

    /**
     * initialize views
     */
    private fun initView() {
        if (intent.getSerializableExtra(Constants.DATA) != null) {
            data = intent.getSerializableExtra(Constants.DATA) as DataItem
            viewModel.name.set(data.firstName)
            viewModel.email.set(data.email)
            Glide.with(this).load(data.avatar).placeholder(R.mipmap.ic_launcher)
                .into(binding.ivUser)
        }

        binding.btnDelete.setOnClickListener {
            if (Utility.isOnline(this)) {
                viewModel.removeUser(data)
            } else {
                Toast.makeText(
                    this@UserDetailActivity,
                    getString(R.string.noInternetAvailable),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.removeData(data) //remove from local
            }
        }

        binding.btnUpdate.setOnClickListener {
            var updatedData = DataItem(
                data.lastName,
                data.id,
                data.avatar,
                binding.tvName.text.toString(),
                binding.tvEmail.text.toString()
            )
            viewModel.editUser(updatedData)
            if (Utility.isOnline(this)) {
                viewModel.editUser(updatedData)
            } else {
                Toast.makeText(
                    this@UserDetailActivity,
                    getString(R.string.noInternetAvailable),
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.updateData(updatedData) //update to local
            }
        }

        binding.btnDownloadImage.setOnClickListener {
            checkPermission()
        }
    }

    /**
     * check permision
     */
    private fun checkPermission() {
        val result = ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)
        if (result == PackageManager.PERMISSION_GRANTED){
            downloadImage(data.avatar.toString())
        }else{
            ActivityCompat.requestPermissions(
                this, arrayOf(WRITE_EXTERNAL_STORAGE),
                EXTERNAL_STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==EXTERNAL_STORAGE_PERMISSION_CODE && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            downloadImage(data.avatar.toString())
        }
    }

    /**
     * dwnload image
     */
    fun downloadImage(url: String) {
        if(Utility.isOnline(this)) {
            myExecutor.execute {
                mLoad(url)?.let {
                    mImage = it
                    myHandler.post {
                        if (mImage != null) {
                            mSaveMediaToStorage(mImage)
                        }
                    }
                }
            }
        }else{
            Toast.makeText(this@UserDetailActivity,getString(R.string.noInternetAvailable),Toast.LENGTH_SHORT).show()
        }
    }

    // Function to establish connection and load image
    private fun mLoad(string: String): Bitmap? {
        val url: URL = mStringToURL(string)!!
        val connection: HttpURLConnection?
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            return BitmapFactory.decodeStream(bufferedInputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            runOnUiThread {
                Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show() }
        }
        return null
    }


    // Function to convert string to URL
    private fun mStringToURL(string: String): URL? {
        try {
            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }

    private fun mSaveMediaToStorage(bitmap: Bitmap?) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this, "Saved to Gallery", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * initialize viewmodel
     */
    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(UserDetailViewModel::class.java)
        viewModel._data.observe(this, Observer {
            when (it.first) {
                Constants.ERROR -> Toast.makeText(
                    this@UserDetailActivity,
                    it.second.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                Constants.UPDATED_DATA -> {
                    finish()
                }
            }
        })
        binding.viewModel = viewModel
    }
}