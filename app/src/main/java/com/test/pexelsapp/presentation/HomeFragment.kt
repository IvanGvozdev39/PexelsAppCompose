package com.test.pexelsapp.presentation

import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.test.domain.models.images.Photo
import com.test.pexelsapp.R
import com.test.pexelsapp.app.App
import com.test.pexelsapp.presentation.adapters.FeaturedCollectionsAdapter
import com.test.pexelsapp.presentation.adapters.ImageRVAdapter
import com.test.pexelsapp.presentation.viewmodelfactory.HomeViewModel
import com.test.pexelsapp.presentation.viewmodelfactory.HomeViewModelFactory
import javax.inject.Inject


class HomeFragment : Fragment() {

    @Inject
    lateinit var vmFactory: HomeViewModelFactory
    private lateinit var progressBar: ProgressBar
    private lateinit var searchBarET: EditText
    private lateinit var searchBarCloseIcon: ImageView
    private lateinit var parentConstraintLayout: ConstraintLayout
    private lateinit var noResultsLayout: LinearLayout
    private lateinit var exploreBtn: Button
    private lateinit var imageRV: RecyclerView
    private var layoutManagerState: Parcelable? = null
    private var connectionRecoveredRecreate = false
    lateinit var viewModel: HomeViewModel
    private val imageRVAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ImageRVAdapter(findNavController())
    }
    private val featuredCollectionsAdapter by lazy(LazyThreadSafetyMode.NONE) {
        FeaturedCollectionsAdapter(
            requireContext(),
            ArrayList(),
            imageRVAdapter
        )
    }
    private var lastSearchQuery = ""


//    companion object {
//        fun newInstance(): HomeFragment {
//            return HomeFragment()
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        (activity?.applicationContext as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, vmFactory)[HomeViewModel::class.java]
        progressBar = view.findViewById(R.id.progressBarHorizontal)
        searchBarET = view.findViewById(R.id.search_bar_edit_text)
        searchBarCloseIcon = view.findViewById(R.id.search_bar_close_icon)
        parentConstraintLayout = view.findViewById(R.id.parent_constraint_layout)
        noResultsLayout = view.findViewById(R.id.no_results_layout)
        exploreBtn = view.findViewById(R.id.explore_button)


        viewModel.featuredCollectionNames.observe(viewLifecycleOwner) { collections ->
            if (collections != null) {
                featuredCollectionsAdapter.setData(collections)
            }
        }


        val featuredCollectionRV =
            view.findViewById<RecyclerView>(R.id.featured_collections_recycler_view)
        featuredCollectionRV.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        featuredCollectionRV.adapter = featuredCollectionsAdapter

        imageRV = view.findViewById<RecyclerView>(R.id.idRVPhotos)


        imageRV.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = imageRVAdapter
            Log.d("awfawfa", layoutManagerState.toString())
            layoutManager?.onRestoreInstanceState(layoutManagerState)
        }



        viewModel.featuredCollectionNames.observe(viewLifecycleOwner) { collections ->
            if (collections != null) {
                featuredCollectionsAdapter.setData(collections)
            }
        }

        featuredCollectionsAdapter.onCollectionSelected = { collection ->
//            viewModel.getImagesFromCollection(collection.id)
            lastSearchQuery = collection.title
            searchBarET.clearFocus()
            if (collection.title.contains("Curated Picks"))
                viewModel.getCuratedPhotos()
            else
                viewModel.getImages(collection.title)
        }

        viewModel.imageList.observe(viewLifecycleOwner) {
            if (it?.isSuccessful == true) {
                val response = it.body()
                if (response != null) {
                    imageRVAdapter.setImageData(response.photos as ArrayList<Photo>, context!!)
//                    imageRV.scrollToPosition(0)

                    if (response.photos.size == 0) {
                        noResultsLayout.visibility = View.VISIBLE
                        imageRV.visibility = View.GONE
                    } else {
                        noResultsLayout.visibility = View.GONE
                        imageRV.visibility = View.VISIBLE
                    }
                }
            }
        }

        exploreBtn.setOnClickListener {
            Log.d("afajkff", "afawl,")
            noResultsLayout.visibility = View.GONE
            searchBarET.text.clear()
            searchBarET.clearFocus()
            viewModel.getPopularImages()
        }


//        viewModel.cleanImageRV.observe(viewLifecycleOwner) {
//            imageRVAdapter.setImageData(ArrayList<Photo>(), context!!)
//        }


        val searchViewTextWatcher: TextWatcher = object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().trim { it <= ' ' }.length == 0) {
                    searchBarCloseIcon.visibility = View.GONE
                } else {
                    searchBarCloseIcon.visibility = View.VISIBLE
                    featuredCollectionsAdapter.resetSelection()
                    context?.let { imageRVAdapter.setImageData(ArrayList<Photo>(), it) }
                    viewModel.getImages(searchBarET.text.toString())
                    lastSearchQuery = searchBarET.text.toString()
                    imageRV.scrollToPosition(0)
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        }

        searchBarCloseIcon.setOnClickListener {
            searchBarET.text.clear()
            searchBarET.clearFocus()
        }

        parentConstraintLayout.setOnClickListener {
            searchBarET.clearFocus()
        }

        searchBarET.addTextChangedListener(searchViewTextWatcher)

        val bookmarksTab = view.findViewById<ImageView>(R.id.bookmarks_tab)
        bookmarksTab.setOnClickListener {
            findNavController().navigate(R.id.bookmarksFragment)
        }

//        viewModel.imageListFeaturedCollection.observe(viewLifecycleOwner) {
//            if (it.isSuccessful) {
//                val response = it.body()
//                if (response != null) {
//                    imageRVAdapter.setImageData(response.media as ArrayList<Photo>, context!!)
//                }
//            }
//        }


        viewModel.noNetwork.observe(viewLifecycleOwner) {
            val noInternetLinearLayout = view.findViewById<LinearLayout>(R.id.no_internet_layout)
            if (it) {
                imageRV.visibility = View.GONE
                noInternetLinearLayout.visibility = View.VISIBLE
                Toast.makeText(context, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show()
            } else {
                imageRV.visibility = View.VISIBLE
                noInternetLinearLayout.visibility = View.GONE
            }

            val tryAgainBtn = view.findViewById<Button>(R.id.reconnect_button)
            tryAgainBtn.setOnClickListener {
                viewModel.retryLastOperation(lastSearchQuery)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        // Save scroll position when fragment is paused
        layoutManagerState = imageRV.layoutManager?.onSaveInstanceState()
    }

    override fun onPause() {
        super.onPause()
    }

}