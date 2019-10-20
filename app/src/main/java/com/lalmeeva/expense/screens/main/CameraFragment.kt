package com.lalmeeva.expense.screens.main

import android.Manifest
import android.graphics.Matrix
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.DisplayMetrics
import android.util.Rational
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.lalmeeva.expense.R
import com.lalmeeva.expense.base.BaseApp
import com.lalmeeva.expense.base.view.BaseFragment
import com.lalmeeva.expense.utils.QrCodeAnalyzer
import kotlinx.android.synthetic.main.fragment_camera.*
import javax.inject.Inject

class CameraFragment : BaseFragment<MainView, CameraView, CameraPresenter>(), CameraView, LifecycleOwner {

    override var permissions: Array<String> = arrayOf(Manifest.permission.CAMERA)

    @Inject
    override lateinit var presenter: CameraPresenter

    private var preview: Preview? = null

    private var detectedCount: Int = 0

    override fun inject() {
        BaseApp.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonSend.setOnClickListener {
            buttonSend.isEnabled = false
            presenter.parseBills()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.reset_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.reset -> {
                presenter.clearBarcodes()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private var dialog: AlertDialog? = null

    override fun onPermissionDenied() {
        context?.let {
            val alertDialogBuilder = AlertDialog.Builder(it)
            alertDialogBuilder.setCancelable(false)
            alertDialogBuilder.setMessage(R.string.permission_error)
            //set empty OnClickListener to prevent auto dismissing the dialog by clicking a button
            alertDialogBuilder.setPositiveButton(R.string.permission_check_again_message) { _, _ ->
                requestPermissions(
                    permissions
                )
            }
            dialog = alertDialogBuilder.show()
        }
    }

    override fun onPermissionGranted() {
        cameraView.post { startCamera() }
    }

    private fun startCamera() {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        val screenAspectRatio = Rational(width, height)

        val previewConfig = PreviewConfig.Builder().apply {
            setTargetAspectRatio(screenAspectRatio)

        }.build()

        preview = Preview(previewConfig)

        preview?.setOnPreviewOutputUpdateListener { previewOutput ->

            // To update the SurfaceTexture, we have to remove it and re-add it
            val parent = cameraView.parent as ViewGroup
            parent.removeView(cameraView)
            parent.addView(cameraView, 0)

            cameraView.surfaceTexture = previewOutput.surfaceTexture
            updateTransform()
        }

        val imageAnalysisConfig = ImageAnalysisConfig.Builder().apply {
            // Use a worker thread for image analysis to prevent glitches
            val analyzerThread = HandlerThread(
                "ImageAnalysis"
            ).apply { start() }
            setCallbackHandler(Handler(analyzerThread.looper))
            // In our analysis, we care more about the latest image than
            // analyzing *every* image
            setImageReaderMode(
                ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE
            )
        }.build()

        val imageAnalysis = ImageAnalysis(imageAnalysisConfig)

        val qrAnalyzer = QrCodeAnalyzer { qrCodes ->
            presenter.receiveDetections(qrCodes)
        }
        imageAnalysis.analyzer = qrAnalyzer

        CameraX.bindToLifecycle( this as LifecycleOwner, preview, imageAnalysis)
    }

    private fun updateTransform() {
        if (cameraView.display == null) return
        val matrix = Matrix()

        // Compute the center of the view finder
        val centerX = cameraView.width / 2f
        val centerY = cameraView.height / 2f

        // Correct preview output to account for display rotation
        val rotationDegrees = when (cameraView.display.rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        matrix.postRotate(-rotationDegrees.toFloat(), centerX, centerY)

        // Finally, apply transformations to our TextureView
        cameraView.setTransform(matrix)
    }

    /**
     * Make a View Blink for a desired duration
     *
     * @param view     view that will be animated
     * @return returns the same view with animation properties
     */
    private fun makeMeBlink(view: View) {
        context?.let {
            qrFrame.setColorFilter(ContextCompat.getColor(it, R.color.dove_green), PorterDuff.Mode.SRC_ATOP)
            Handler().postDelayed({ qrFrame.setColorFilter(ContextCompat.getColor(it, R.color.dove_gray), PorterDuff.Mode.SRC_ATOP) }, 500)
        }
    }

    override fun setBarcodesCount(count: Int) {
        detectedCount = count
        activity?.runOnUiThread {
            if (count > 0) {
                makeMeBlink(viewMain)
            }
            barcodeCount.text = count.toString()
        }
    }

    override fun showProgress() {
        contextView?.showProgress()
    }

    override fun hideProgress() {
        contextView?.hideProgress()
    }

    override fun showResult(result: String?) {
        setBarcodesCount(0)
        buttonSend.isEnabled = true
        context?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.results))
            builder.setMessage(result)
            builder.setCancelable(false)

            builder.setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.cancel() }
            builder.create().show()
        }
    }

    override fun showError(message: String?) {
        setBarcodesCount(0)
        buttonSend.isEnabled = true
        message?.let { messageNotNull ->
            context?.let {
                val builder = AlertDialog.Builder(it)
                builder.setTitle(getString(R.string.error_occurred))
                builder.setMessage(messageNotNull)
                builder.setCancelable(false)

                builder.setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.cancel() }
                val alert = builder.create()
                alert.show()
            }
        }
    }
}