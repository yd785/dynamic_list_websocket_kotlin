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
import androidx.recyclerview.widget.LinearLayoutManager
import com.interview.dynamiclist.DynamicListApp
import com.interview.dynamiclist.R
import com.interview.dynamiclist.ui.adapter.FeedAdapter
import com.interview.dynamiclist.viewmodel.FeedViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
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

    lateinit var startTopBtn : MenuItem
    lateinit var stopTopBtn : MenuItem


    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach: ")
        super.onAttach(context)
        (context.applicationContext as DynamicListApp).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
        subscribeObservers()
        setListeners()
    }

    private fun initViews() {
        //start_btn.isEnabled = false

        linearLayoutManager = LinearLayoutManager(context)
        recycler_view.layoutManager = linearLayoutManager
        feedAdapter = FeedAdapter()
        recycler_view.adapter = feedAdapter
    }

    private fun subscribeObservers() {
        feedViewModel.getFeedItem().observe(viewLifecycleOwner, Observer { feed ->
            feedAdapter?.addFeedItemToList(feed, 0)
            linearLayoutManager?.scrollToPosition(0)
            (requireActivity() as MainActivity).toolbar.setTitle(feed.name)
        })

        feedViewModel.getFeedActionState().observe(viewLifecycleOwner, Observer { state -> setButtonsState(state) })
    }

    private fun setListeners() {
      /*  start_btn.setOnClickListener(View.OnClickListener {
            it.isEnabled = false
            stop_btn.isEnabled = true
            feedViewModel.startRunFeeds()
        })
        stop_btn.setOnClickListener(View.OnClickListener {
            it.isEnabled = false
            start_btn.isEnabled = true
            feedViewModel.stopRunFeeds()
        })*/
        /*text_input_filter.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
                feedAdapter.getFilter().filter(text_input_filter.getText().toString())
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                mFeedAdapter.getFilter().filter(mInputFilterTxt.getText().toString())
            }
        })*/

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
        inflater.inflate(R.menu.menu_main, menu)
        startTopBtn = menu.findItem(R.id.start_top_button)
        stopTopBtn = menu.findItem(R.id.stop_top_button)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsItemSelected: ")
        return when (item.itemId) {
            R.id.start_top_button -> {
                Toast.makeText(context, "start", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "onOptionsItemSelected: start")
                //feedViewModel.feedActionState.value = true
                feedViewModel.startRunFeeds()
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
        if (requestingAction) {
            startTopBtn.isVisible = false
            stopTopBtn.isVisible = true
        } else {
            startTopBtn.isVisible = true
            stopTopBtn.isVisible = false
        }
    }

    override fun onStart() {
        super.onStart()
        //if (start_btn.isEnabled)
        //feedViewModel.startRunFeeds()
    }

    override fun onStop() {
        super.onStop()
        feedViewModel.stopRunFeeds()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}