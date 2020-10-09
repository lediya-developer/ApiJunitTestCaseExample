package com.lediya.apitest.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.lediya.apitest.R
import com.lediya.apitest.databinding.ListScreenBinding
import com.lediya.apitest.model.Posts
import com.lediya.apitest.utility.ResultType
import com.lediya.apitest.utility.Utils
import com.lediya.apitest.view.adapter.PostAdapter
import com.lediya.apitest.viewmodel.ListScreenViewModel

class ListScreen : AppCompatActivity(){
    private lateinit var binding:ListScreenBinding
    private lateinit var viewModel: ListScreenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.list_screen)
        viewModel =
            ViewModelProviders.of(this).get(ListScreenViewModel::class.java)
        observer()
    }
  /**
   * Fetch the cache data if the connectivity is off*/
    override fun onStart() {
        super.onStart()
        if(!Utils.isConnectedToNetwork(this)){
            viewModel.fetchPostDataFromDatabase()
            binding.button.isEnabled = false
        }
    }
/**
 * Observer to observe the livedata and shown in the UI*/
    private fun observer(){
        viewModel.fetchResult.observe(
            this, Observer { event ->
                event.getContentIfNotHandled()?.let { result ->
                    when (result.resultType) {
                        ResultType.PENDING -> {
                            showProgressBar()
                        }
                        ResultType.SUCCESS -> {
                            viewModel.fetchPostDataFromDatabase()
                        }
                        ResultType.FAILURE -> {
                            showErrorAlert()
                        }
                    }
                }
            }
        )

        viewModel.postList.observe(
            this, Observer { event ->
                event.getContentIfNotHandled()?.let { posts ->
                    if(posts.isNullOrEmpty()){
                        showErrorAlert()

                    }else{
                        showData(posts)
                    }

                }
            }
        )
        viewModel.totalTimeResult.observe(this, Observer {
                event ->
            event.getContentIfNotHandled()?.let {
                val textStr = "$it Seconds"
                binding.timeTaken.text=textStr
            }
        })
        viewModel.totalCharacterLength.observe(this, Observer {
            event->
            event.getContentIfNotHandled()?.let{
                if(it.contentLength!=null){
                    val textStr = it.contentLength+" Characters"
                    binding.totalCharacter.text = textStr
                }
            }
        })
    }
    /**
     * Updating the post data in the recycler view to shown in the UI screen*/
    private fun showData(postList: List<Posts>) {
        binding.spinKit.visibility = View.GONE
        binding.button.isEnabled = false
        binding.noInternetAvailable.visibility= View.GONE
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = PostAdapter()
        adapter.setItems(postList)
        binding. recyclerView.addItemDecoration(
            DividerItemDecoration(
                binding.recyclerView.context,
                (binding.recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        binding.recyclerView.adapter = adapter

    }
    /**
     * Load button click listener. Once user clicks button it will check connectivity and download data
     * */
    fun downloadData(view: View) {
        if(Utils.isConnectedToNetwork(this)){
            showProgressBar()
            viewModel.getPostDataFromServer()
            viewModel.getUserDataFromServer()
        }
    }
    /**
     * Shows progress bar while data is in downloading
     * */
    private fun showProgressBar(){
        binding.spinKit.visibility = View.VISIBLE
        binding.button.isEnabled = false
        binding.noInternetAvailable.visibility= View.GONE
    }
    /**
     * Hide the progress bar and shows the alert warning in the UI Screen*/
    private fun showErrorAlert(){
        binding.spinKit.visibility = View.GONE
        binding.button.isEnabled = false
        binding.noInternetAvailable.visibility= View.VISIBLE
    }
}