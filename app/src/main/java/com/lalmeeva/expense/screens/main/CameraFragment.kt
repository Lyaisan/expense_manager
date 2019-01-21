package com.lalmeeva.expense.screens.main

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.*
import android.view.animation.AnimationUtils
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.lalmeeva.expense.R
import com.lalmeeva.expense.base.BaseApp
import com.lalmeeva.expense.base.view.BaseFragment
import kotlinx.android.synthetic.main.fragment_camera.*
import javax.inject.Inject

class CameraFragment : BaseFragment<MainView, CameraView, CameraPresenter>(), CameraView {

    override var permissions: Array<String> = arrayOf(Manifest.permission.CAMERA)

    @Inject
    override lateinit var presenter: CameraPresenter

    private var detectedCount: Int = 0
    private var barcodeDetector: BarcodeDetector? = null
    private var cameraSource: CameraSource? = null

    override fun inject() {
        BaseApp.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonSend.setOnClickListener {
            buttonSend.isEnabled = false
            presenter.parseBills()
        }
    }

    override fun onPause() {
        super.onPause()
        cameraSource?.stop()
        cameraSource?.release()
        barcodeDetector?.release()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
        inflater?.inflate(R.menu.reset_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
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
            alertDialogBuilder.setPositiveButton(R.string.permission_check_again_message) { _, _ -> requestPermissions(permissions) }
            dialog = alertDialogBuilder.show()
        }
    }

    override fun onPermissionGranted() {
        startCameraSource()
    }

    @SuppressLint("MissingPermission")
    private fun startCameraSource() {
        barcodeDetector = BarcodeDetector.Builder(context).setBarcodeFormats(Barcode.QR_CODE).build()
        cameraSource = CameraSource.Builder(context, barcodeDetector).setAutoFocusEnabled(true).build()

        if (cameraView.holder.surface.isValid) {
            cameraSource?.start(cameraView.holder)
        }

        barcodeDetector?.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {

            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
                presenter.receiveDetections(detections?.detectedItems)
            }
        })

        cameraView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                cameraSource?.stop()
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
                cameraSource?.start(cameraView.holder)
            }
        })
    }

    /**
     * Make a View Blink for a desired duration
     *
     * @param view     view that will be animated
     * @return returns the same view with animation properties
     */
    private fun makeMeBlink(view: View) {

        val startAnimation = AnimationUtils.loadAnimation(context, R.anim.blinking_animation)
        view.startAnimation(startAnimation)
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