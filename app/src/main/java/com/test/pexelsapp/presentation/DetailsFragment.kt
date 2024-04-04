package com.test.pexelsapp.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.test.domain.models.images.Photo
import com.test.pexelsapp.R
import com.test.pexelsapp.app.App
import com.test.pexelsapp.presentation.viewmodelfactory.DetailsViewModel
import com.test.pexelsapp.presentation.viewmodelfactory.DetailsViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject


class DetailsFragment : Fragment() {

    @Inject
    lateinit var vmFactory: DetailsViewModelFactory
    private lateinit var imageView: ImageView
    private lateinit var viewModel: DetailsViewModel
    private lateinit var src: String
    private var photo: Photo? = null
    private var inBookmarks = false
    private lateinit var bookmarksBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, vmFactory)[DetailsViewModel::class.java]
        val imageView = view.findViewById<ImageView>(R.id.image)
        photo = arguments?.getParcelable("photo")

        val placeholderDrawable = ColorDrawable(Color.parseColor(photo?.avg_color)).apply {
            if (photo != null) {
                setBounds(0, 0, photo!!.width, photo!!.height)
            }
        }

        Log.d("awdwaf", photo!!.url)
        if (photo != null)
            src = photo!!.src.original ?: ""


        activity?.let {
            Log.d("awdwaf", "loading image")
            Glide.with(it)
                .load(src)
                .placeholder(placeholderDrawable)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }

        val backBtn = view.findViewById<ImageButton>(R.id.back_button)
        backBtn.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

        val photographerTV = view.findViewById<AppCompatButton>(R.id.photographer_name)
        photographerTV.isEnabled = false
        photographerTV.text = photo?.photographer

        val downloadBtnText = view.findViewById<TextView>(R.id.download_button_text)
        val downloadBtnImage = view.findViewById<ImageView>(R.id.download_button_image)
        val downloadBtn = view.findViewById<FrameLayout>(R.id.download_button)
        downloadBtn.setOnClickListener {
            if (isWriteStoragePermissionGranted()) {
                downloadBtn.isEnabled = false
                downloadBtnText.text = getString(R.string.downloading)
                downloadBtnImage.setImageDrawable(context?.let { it1 -> getDrawable(it1, R.drawable.ic_clock) })
                viewModel.downloadImage(src)
            } else {
                // Request WRITE_EXTERNAL_STORAGE permission
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE
                )
            }
        }

        viewModel.imageDownloaded.observe(viewLifecycleOwner) {
            var message = ""
            if (it)
                message = getString(R.string.downloaded_successfully)
            else
                message = getString(R.string.download_failed)
            downloadBtn.isEnabled = true
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            downloadBtnText.text = getString(R.string.download)
            downloadBtnImage.setImageDrawable(context?.let { it1 -> getDrawable(it1, R.drawable.ic_download) })
        }

        bookmarksBtn = view.findViewById(R.id.bookmarks_button)
        bookmarksBtn.setOnClickListener {
            photo?.let { it1 -> addOrDeleteBookmark(it1) }
        }

        if (photo != null)
            checkBookmars(photo!!)
    }


    private fun checkBookmars(image: Photo) {
        var dbPhotos = ArrayList<Photo>()
        lifecycleScope.launch {
            dbPhotos = viewModel.getAllImagesFromBookmarks() as ArrayList<Photo>
            for (dbPhoto in dbPhotos) {
                Log.d("awksaflw", photo?.src?.original + "\n" + dbPhoto.src.original)
                if (photo?.src?.original?.equals(dbPhoto.src.original) == true) {
                    inBookmarks = true
                    bookmarksBtn.setImageDrawable(context?.let { getDrawable(it, R.drawable.ic_bookmark_added) })
                }
            }
        }
    }


    private fun addOrDeleteBookmark(image: Photo) {
        if (!inBookmarks) {
            lifecycleScope.launch { viewModel.addImageToBookmarks(image) }
            bookmarksBtn.setImageDrawable(context?.let { getDrawable(it, R.drawable.ic_bookmark_added) })
            inBookmarks = true
        } else {
            lifecycleScope.launch { viewModel.deleteImageFromBookmarks(image) }
            bookmarksBtn.setImageDrawable(context?.let { getDrawable(it, R.drawable.ic_bookmark) })
            inBookmarks = false
        }
    }


    // Add this function to check if permission is granted
    private fun isWriteStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissionCheck = checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            permissionCheck == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }


    // Handle permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the download
                viewModel.downloadImage(src)
            } else {
                // Permission denied, show a message or handle accordingly
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 100
    }

}