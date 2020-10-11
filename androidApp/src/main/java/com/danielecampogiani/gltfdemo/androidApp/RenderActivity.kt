package com.danielecampogiani.gltfdemo.androidApp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Choreographer
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.danielecampogiani.gltfdemo.shared.Api
import com.google.android.filament.utils.KtxLoader
import com.google.android.filament.utils.ModelViewer
import com.google.android.filament.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.nio.Buffer
import java.nio.ByteBuffer

/**
 * Mainly a clone of https://github.com/google/filament/tree/main/android/samples/sample-gltf-viewer
 * I've just added remote fetching
 */
class RenderActivity : AppCompatActivity() {

    companion object {
        init {
            Utils.init()
        }

        public const val URL = "URL"
    }

    private lateinit var surfaceView: SurfaceView
    private lateinit var choreographer: Choreographer
    private val frameScheduler = FrameCallback()
    private lateinit var modelViewer: ModelViewer

    private val api = Api.Implementation

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        surfaceView = SurfaceView(this).apply { setContentView(this) }
        choreographer = Choreographer.getInstance()


        modelViewer = ModelViewer(surfaceView)

        surfaceView.setOnTouchListener { _, event ->
            modelViewer.onTouchEvent(event)
            true
        }


        val fullUrl = intent.getStringExtra(URL)!!
        val baseUrlEnd = fullUrl.lastIndexOf("/") + 1
        val baseUrl = fullUrl.substring(startIndex = 0, endIndex = baseUrlEnd)
        val gltfFile = fullUrl.substring(startIndex = baseUrlEnd)

        createRenderablesRemote(baseUrl, gltfFile)
        createIndirectLight()

        val dynamicResolutionOptions = modelViewer.view.dynamicResolutionOptions
        dynamicResolutionOptions.enabled = true
        modelViewer.view.dynamicResolutionOptions = dynamicResolutionOptions

        val ssaoOptions = modelViewer.view.ambientOcclusionOptions
        ssaoOptions.enabled = true
        modelViewer.view.ambientOcclusionOptions = ssaoOptions

        val bloomOptions = modelViewer.view.bloomOptions
        bloomOptions.enabled = true
        modelViewer.view.bloomOptions = bloomOptions
    }


    private fun createRenderablesRemote(baseUrl: String, gltfFile: String) {

        lifecycle.coroutineScope.launchWhenResumed {
            val buffer = getBuffer(baseUrl + gltfFile)

            modelViewer.loadModelGltfAsync(buffer) { uri ->
                // loadModelGltfAsync will trigger this callback from a worker thread for each requested resource.
                runBlocking { getBuffer(baseUrl + uri) }
            }
            modelViewer.transformToUnitCube()
        }
    }

    private suspend fun getBuffer(uri: String): Buffer = withContext(Dispatchers.IO) {
        val model = api.download(uri)
        ByteBuffer.wrap(model)
    }

    private fun createIndirectLight() {
        val engine = modelViewer.engine
        val scene = modelViewer.scene
        val ibl = "default_env"
        readCompressedAsset("envs/$ibl/${ibl}_ibl.ktx").let {
            scene.indirectLight = KtxLoader.createIndirectLight(engine, it)
            scene.indirectLight!!.intensity = 30_000.0f
        }
        readCompressedAsset("envs/$ibl/${ibl}_skybox.ktx").let {
            scene.skybox = KtxLoader.createSkybox(engine, it)
        }
    }

    private fun readCompressedAsset(assetName: String): ByteBuffer {
        val input = assets.open(assetName)
        val bytes = ByteArray(input.available())
        input.read(bytes)
        return ByteBuffer.wrap(bytes)
    }

    override fun onResume() {
        super.onResume()
        choreographer.postFrameCallback(frameScheduler)
    }

    override fun onPause() {
        super.onPause()
        choreographer.removeFrameCallback(frameScheduler)
    }

    override fun onDestroy() {
        super.onDestroy()
        choreographer.removeFrameCallback(frameScheduler)
    }

    inner class FrameCallback : Choreographer.FrameCallback {
        private val startTime = System.nanoTime()
        override fun doFrame(frameTimeNanos: Long) {
            choreographer.postFrameCallback(this)

            modelViewer.animator?.apply {
                if (animationCount > 0) {
                    val elapsedTimeSeconds =
                        (frameTimeNanos - startTime).toDouble() / 1_000_000_000
                    applyAnimation(0, elapsedTimeSeconds.toFloat())
                }
                updateBoneMatrices()
            }

            modelViewer.render(frameTimeNanos)
        }
    }
}