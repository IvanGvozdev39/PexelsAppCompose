package com.test.pexelsapp.presentation

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.test.pexelsapp.R
import com.test.pexelsapp.presentation.adapters.ImageRVAdapterBookmarks
import com.test.pexelsapp.presentation.viewmodelfactory.BookmarksViewModel
import com.test.pexelsapp.presentation.viewmodelfactory.BookmarksViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class BookmarksFragment : Fragment() {

    @Inject
    lateinit var vmFactory: BookmarksViewModelFactory
    lateinit var viewModel: BookmarksViewModel
    private val imageRVAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ImageRVAdapterBookmarks(findNavController())
    }
    private lateinit var noResultsLayout: LinearLayout
    private lateinit var exploreBtn: Button
    private lateinit var imageRV: RecyclerView
    private var layoutManagerState: Parcelable? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        (activity?.applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmarks, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, vmFactory)[BookmarksViewModel::class.java]
        noResultsLayout = view.findViewById(R.id.nothing_in_bookmarks_layout)
        exploreBtn = view.findViewById(R.id.explore_button)

        imageRV = view.findViewById<RecyclerView>(R.id.idRVPhotos)

        imageRV.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = imageRVAdapter
            layoutManager?.onRestoreInstanceState(layoutManagerState)
        }

        viewModel.imageList.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    imageRVAdapter.setImageData(response, context!!)
//                    imageRV.scrollToPosition(0)

                    if (response.size == 0) {
                        noResultsLayout.visibility = View.VISIBLE
                        imageRV.visibility = View.GONE
                    } else {
                        noResultsLayout.visibility = View.GONE
                        imageRV.visibility = View.VISIBLE
                    }
                }
        }

        exploreBtn.setOnClickListener {
            onBackPressed()
        }


        val backBtn = view.findViewById<ImageButton>(R.id.back_button)
        backBtn.setOnClickListener {
            onBackPressed()
        }

        val homeTab = view.findViewById<ImageView>(R.id.home_tab)
        homeTab.setOnClickListener {
            onBackPressed()
        }
    }


    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.onBackPressed()
    }


    override fun onResume() {
        super.onResume()
        // Save scroll position when fragment is paused
        layoutManagerState = imageRV.layoutManager?.onSaveInstanceState()
        lifecycleScope.launch { viewModel.getAllImagesFromBookmarks() }
    }

    override fun onPause() {
        super.onPause()
        // Save scroll position when fragment is paused
        layoutManagerState = imageRV.layoutManager?.onSaveInstanceState()
    }
}