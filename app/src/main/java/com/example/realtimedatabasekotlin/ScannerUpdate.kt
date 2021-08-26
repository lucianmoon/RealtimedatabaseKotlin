package com.example.realtimedatabasekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.realtimedatabasekotlin.databinding.ActivityScannerUpdateBinding
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import org.json.JSONException
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class ScannerUpdate : AppCompatActivity(),EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks  {
    private lateinit var binding: ActivityScannerUpdateBinding
    private var hide: Animation? = null
    private var reveal: Animation? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner_update)
        binding = ActivityScannerUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hide = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
        reveal = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        binding.tvText.startAnimation(reveal)
        binding.cardView2.startAnimation(reveal)
        binding.tvText.text = "Scan QR Code Here"
        binding.cardView2.visibility = View.VISIBLE

        binding.btnScan.setOnClickListener {
            binding.tvText.startAnimation(reveal)
            binding.cardView1.startAnimation(hide)
            binding.cardView2.startAnimation(reveal)

            binding.cardView2.visibility = View.VISIBLE
            binding.cardView1.visibility = View.GONE
            binding.tvText.text = "Scan QR Code Here"
        }
        binding.cardView2.setOnClickListener {
            cameraTask()
        }

        binding.btnEnter.setOnClickListener {

            if (binding.edtCode.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter code", Toast.LENGTH_SHORT).show()
            } else {
                val value = binding.edtCode.text.toString()

                Toast.makeText(this, value, Toast.LENGTH_SHORT).show()

            }
        }
        binding.btnEnterCode.setOnClickListener {
            binding.tvText.startAnimation(reveal)
            binding.cardView1.startAnimation(reveal)
            binding.cardView2.startAnimation(hide)

            binding.cardView2.visibility = View.GONE
            binding.cardView1.visibility = View.VISIBLE
            binding.tvText.text = "Enter QR Code Here"
        }

    }

    private fun hasCameraAccess(): Boolean {
        return EasyPermissions.hasPermissions(this, android.Manifest.permission.CAMERA)
    }

    private fun cameraTask() {

        if (hasCameraAccess()) {

            val qrScanner = IntentIntegrator(this)
            qrScanner.setPrompt("scan a QR code")
            qrScanner.setCameraId(0)
            qrScanner.setOrientationLocked(true)
            qrScanner.setBeepEnabled(true)
            qrScanner.captureActivity = CaptureActivity::class.java
            qrScanner.initiateScan()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "This app needs access to your camera so you can take pictures.",
                123,
                android.Manifest.permission.CAMERA
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_SHORT).show()
                binding.edtCode.setText("")
            } else {
                try {

                    binding.cardView1.startAnimation(reveal)
                    binding.cardView2.startAnimation(hide)
                    binding.cardView1.visibility = View.VISIBLE
                    binding.cardView2.visibility = View.GONE
                    val username = result.contents.toString()
                    Intent(this,UpdateData::class.java).also{
                        it.putExtra("Username",username)
                        startActivity(it)
                    }
//                    binding.edtCode.setText(result.contents.toString())
                } catch (exception: JSONException) {
                    Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                    binding.edtCode.setText("")
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onRationaleDenied(requestCode: Int) {
    }

    override fun onRationaleAccepted(requestCode: Int) {
    }
}