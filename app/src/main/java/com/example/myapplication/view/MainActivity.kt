package com.example.myapplication.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.DataItem
import com.example.myapplication.util.Constants
import com.example.myapplication.util.Utility
import com.example.myapplication.view.adapter.UserAdapter
import com.example.myapplication.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), UserAdapter.onItemClick {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initViewModel()
        initView()
    }

    /**
     * initialize views
     */
    private fun initView() {
        adapter = UserAdapter(this)
        binding.rvUser.adapter = adapter
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchUserDataInDb()
            binding.swipeRefresh.isRefreshing=false
        }
    }

    /**
     * initialize viewmodel
     */
    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel._data.observe(this, Observer {
            when (it.first) {
                Constants.ERROR -> Toast.makeText(
                    this@MainActivity,
                    it.second.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                Constants.DATA_LOADED -> {
                    var dataList=ArrayList<DataItem>()
                    dataList.addAll(it.second as ArrayList<DataItem>)
                    adapter.addData(dataList)
                }
            }
        })
        if (Utility.isOnline(this)) {
            viewModel.getDataApi()
        } else {
            Toast.makeText(
                this@MainActivity,
                getString(R.string.noInternetAvailable),
                Toast.LENGTH_SHORT
            ).show()
            viewModel.fetchUserDataInDb()
        }
    }

    override fun onResume() {
        super.onResume()
        if(viewModel.isBackFromDetail.get()) {
            viewModel.fetchUserDataInDb()
        }
    }

    override fun itemClick(data: DataItem) {
        viewModel.isBackFromDetail.set(true)
        startActivity(
            Intent(
                this@MainActivity,
                UserDetailActivity::class.java
            ).putExtra(Constants.DATA, data)
        )
    }
}