/*
 * Created by Muhamad Syafii
 * , 22/9/2023
 * Copyright (c) 2023.
 * All Rights Reserved
 */

package id.syafii.ataskcalculator.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import id.syafii.ataskcalculator.BuildConfig
import id.syafii.ataskcalculator.R
import id.syafii.ataskcalculator.databinding.ActivityMainBinding
import id.syafii.ataskcalculator.utils.ALL_IMAGES
import id.syafii.ataskcalculator.utils.gone
import id.syafii.ataskcalculator.utils.visible

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val viewModel: MainViewModel by viewModels()
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { mapResults ->
            if (!mapResults.getOrDefault(Manifest.permission.CAMERA, false)) {
                Toast.makeText(
                    this@MainActivity,
                    "Please allow permission to use camera feature",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private val isFilesystem by lazy { BuildConfig.isFilesystemEnabled }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        setupViewModel()
        binding.run {

            /** this is check condition if button file system*/
            if (isFilesystem) {
                btnCamera.gone()
                btnGallery.visible()
            } else {
                btnCamera.visible()
                btnGallery.gone()
            }

            btnGallery.setOnClickListener {
                galleryLauncherResult.launch(ALL_IMAGES)
            }

            btnCamera.setOnClickListener {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    permissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
                } else {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraLauncherResult.launch(intent)
                }
            }
        }
    }

    private fun setupViewModel() {
        viewModel.calculateQuestion.observe(this) {
            binding.tvScan.text = getString(R.string.label_question, it)
        }
        viewModel.calculateAnswer.observe(this) {
            binding.tvResult.text = getString(R.string.label_answer, it)
        }
    }

    //region permission on pick from gallery or camera
    private val cameraLauncherResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val image = it.data?.extras?.get("data") as Bitmap
                val image1 = InputImage.fromBitmap(image, 0)
                viewModel.calculateFromImage(image1)
            }
        }

    private val galleryLauncherResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let {
                val resolver = applicationContext.contentResolver
                val bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(resolver, it)
                } else {
                    val source = ImageDecoder.createSource(resolver, it)
                    ImageDecoder.decodeBitmap(source)
                }
                val image = InputImage.fromBitmap(bitmap, 0)
                viewModel.calculateFromImage(image)
            }
        }

    //endregion
}