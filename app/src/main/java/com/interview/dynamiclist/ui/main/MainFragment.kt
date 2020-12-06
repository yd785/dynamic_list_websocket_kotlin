package com.interview.dynamiclist.ui.main

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.interview.dynamiclist.DynamicListApp
import com.interview.dynamiclist.R
import com.interview.dynamiclist.data.model.FeedEventModel
import com.interview.dynamiclist.ui.adapter.FeedAdapter
import com.interview.dynamiclist.util.Constants
import com.interview.dynamiclist.util.Status
import com.interview.dynamiclist.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.ArrayList
import javax.inject.Inject

class MainFragment : Fragment() {

    private val TAG = "MainFragment"

    private var linearLayoutManager: LinearLayoutManager? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val feedViewModel: FeedViewModel by viewModels {
        viewModelFactory
    }

    private var feedAdapter: FeedAdapter? = null

    var startTopBtn: MenuItem? = null
    var stopTopBtn: MenuItem? = null
    var stateBtn:Boolean = false

    //val animFadeIn = AnimationUtils.loadAnimation(requireActivity().applicationContext,
    //     R.anim.fade_in)


    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach: ")
        super.onAttach(context)
        (context.applicationContext as DynamicListApp).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: frgment")
        setHasOptionsMenu(true)
        if (savedInstanceState != null)
            stateBtn = savedInstanceState.getBoolean(Constants.BUTTON_STATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        initViews()
        if (savedInstanceState != null) {
            val feedEventModelList : ArrayList<FeedEventModel>? = savedInstanceState.getParcelableArrayList(Constants.FEEDS)
            if (feedEventModelList != null) {
                feedAdapter?.setFeeds(feedEventModelList)
                Log.d(TAG, "onViewCreated: saved ${feedEventModelList}")
            }
        }
        subscribeObservers()
        setListeners()
    }

    private fun initViews() {
        //start_btn.isEnabled = false

        linearLayoutManager = LinearLayoutManager(context)
        recycler_view.layoutManager = linearLayoutManager
        recycler_view.itemAnimator = DefaultItemAnimator()
        recycler_view.addItemDecoration(
            DividerItemDecoration(
                recycler_view.context,
                (recycler_view.layoutManager as LinearLayoutManager).orientation
            )
        )
        feedAdapter = FeedAdapter(requireActivity())
        recycler_view.adapter = feedAdapter
        recycler_view.scheduleLayoutAnimation()
    }

    private fun subscribeObservers() {

        feedViewModel.getFeedItem().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { feed -> updateList(feed) }
                    progressbar.visibility = View.GONE
                    main_txt.visibility = View.GONE
                    text_input_filter.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    progressbar.visibility = View.GONE
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        feedViewModel.getFeedActionState()
            .observe(viewLifecycleOwner, Observer { state -> setButtonsState(state) })
    }

    private fun updateList(feed: FeedEventModel) {
        feedAdapter?.addFeedItemToList(feed, 0)
        linearLayoutManager?.scrollToPosition(0)
        (requireActivity() as MainActivity).toolbar.setTitle(feed.name)
        //recycler_view.scheduleLayoutAnimation()
    }

    private fun setListeners() {

        text_input_filter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                Log.d(TAG, "afterTextChanged: ${text_input_filter.getText().toString()}")
                feedViewModel.updateFilter(text_input_filter.getText().toString())
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //super.onCreateOptionsMenu(menu, inflater)
        Log.d(TAG, "frgment onCreateOptionsMenu: ")
        inflater.inflate(R.menu.menu_main, menu)
        startTopBtn = menu.findItem(R.id.start_top_button)
        stopTopBtn = menu.findItem(R.id.stop_top_button)

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        setButtonsState(stateBtn)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected: ")
        return when (item.itemId) {
            R.id.start_top_button -> {
                Toast.makeText(context, "start", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onOptionsItemSelected: start")
                //feedViewModel.feedActionState.value = true
                feedViewModel.startRunFeeds()
                progressbar.visibility = View.VISIBLE
                true
            }

            R.id.stop_top_button -> {
                Toast.makeText(context, "stop", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onOptionsItemSelected: stop")
                //feedViewModel.feedActionState.value = false
                feedViewModel.stopRunFeeds()
                true
            }

            else -> false
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setButtonsState(requestingAction: Boolean) {
        Log.d(TAG, "setButtonsState: ")
        if (requestingAction) {
            stateBtn = true
            startTopBtn?.isVisible = false
            stopTopBtn?.isVisible = true
        } else {
            startTopBtn?.isVisible = true
            stopTopBtn?.isVisible = false
            stateBtn = false
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        // if (!startTopBtn?.isVisible) feedViewModel.startRunFeeds()
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
        //feedViewModel.stopRunFeeds()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
        // if (!startTopBtn?.isVisible) feedViewModel.startRunFeeds()
        //feedViewModel.feedActionState.value = true
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
        // feedViewModel.stopRunFeeds()
        //feedViewModel.feedActionState.value = false
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState: ${feedAdapter?.getFeeds()}" )
        outState.putBoolean(Constants.BUTTON_STATE, feedViewModel.feedActionState.value!!)
        outState.putParcelableArrayList(Constants.FEEDS, feedAdapter?.getFeeds())
    }

}