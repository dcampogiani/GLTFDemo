package com.danielecampogiani.gltfdemo.androidApp

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = Adapter {
            val intent = Intent(this, RenderActivity::class.java)
            intent.apply { putExtra(RenderActivity.URL, it.url) }
            startActivity(intent)
        }

        findViewById<RecyclerView>(R.id.list).apply { setAdapter(adapter) }

        viewModel.state.observe(this) {
            adapter.submitList(it)
        }
    }
}
