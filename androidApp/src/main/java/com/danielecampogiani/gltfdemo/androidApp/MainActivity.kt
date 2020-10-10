package com.danielecampogiani.gltfdemo.androidApp

import android.content.Intent
import android.net.Uri
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

            val sceneViewerIntent = Intent(Intent.ACTION_VIEW)
            sceneViewerIntent.data =
                Uri.parse("https://arvr.google.com/scene-viewer/1.0?file=${it.url}")
            sceneViewerIntent.setPackage("com.google.android.googlequicksearchbox")
            startActivity(sceneViewerIntent)
        }

        val list = findViewById<RecyclerView>(R.id.list).apply { setAdapter(adapter) }

        viewModel.state.observe(this) {
            adapter.submitList(it)
        }
    }
}
