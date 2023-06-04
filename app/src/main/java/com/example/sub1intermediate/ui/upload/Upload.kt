package com.example.sub1intermediate.ui.upload

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import com.example.sub1intermediate.Utils.createTempFile
import com.example.sub1intermediate.Utils.reduceFileImage
import com.example.sub1intermediate.Utils.uriToFile
import com.example.sub1intermediate.ViewModelFactory
import com.example.sub1intermediate.data.repository.Result
import com.example.sub1intermediate.databinding.ActivityUploadBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class Upload : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding

    private val factory = ViewModelFactory.getInstance(this)
    private val uploadViewModel: UploadViewModel by viewModels {
        factory
    }

    private lateinit var currentPhotoPath: String
    private var getFile: File? = null

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            val result = BitmapFactory.decodeFile(myFile.path)
            getFile = myFile
            binding.imageView.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@Upload)
            getFile = myFile
            binding.imageView.setImageURI(selectedImg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cameraBtn.setOnClickListener { startTakePhoto() }
        binding.galleryBtn.setOnClickListener { startGallery() }
        uploadViewModel.getToken().observe(this) { token ->
            binding.uploadBtn.setOnClickListener { uploadImage(token) }
        }
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@Upload,
                "com.example.sub1intermediate.camera",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage(token: String?) {
        showLoading(true)
        val description = binding.descEt.text.toString()

        if (getFile == null) {
            Toast.makeText(this@Upload, "Add your Image", Toast.LENGTH_SHORT).show()
            showLoading(false)
        } else if (description.isBlank()) {
            Toast.makeText(this@Upload, "Add your Description", Toast.LENGTH_SHORT).show()
            showLoading(false)
        } else if (token != null) {
            val file = reduceFileImage(getFile as File)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            uploadViewModel.uploadStory(token, description, imageMultipart).observe(this) {
                when (it) {

                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this@Upload, "Failed", Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        Toast.makeText(this@Upload, "Success", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressbar.visibility = View.VISIBLE
        } else {
            binding.progressbar.visibility = View.GONE
        }
    }
}