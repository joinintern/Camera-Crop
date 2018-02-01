package com.cameracrop

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import com.cameracrop.Util.UtilityofActivity
import com.crop.Croperino
import com.crop.CroperinoConfig
import com.crop.CroperinoFileUtil
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity(),View.OnClickListener {

/*Created By Prince Rastogi*/

    var mContext: Context? = null
    var appCompatActivity: AppCompatActivity? = null
    var utilityofActivity: UtilityofActivity? = null
    val TAG = "meme"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appCompatActivity=this
        mContext=this
        utilityofActivity= UtilityofActivity(appCompatActivity!!)


        chane_picture.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when(v?.id){

            R.id.chane_picture ->{

                sendEnquiry_DialogBox()

            }
        }

    }

    fun sendEnquiry_DialogBox() {

        val dialog = AlertDialog.Builder(mContext!!)
        val dialogView = layoutInflater.inflate(R.layout.camera_dialog, null)
        val mLayoutcamera = dialogView.findViewById<RelativeLayout>(R.id.layoutcamera)
        val mLayoutgallery = dialogView.findViewById<RelativeLayout>(R.id.layoutgallery)
        dialog.setView(dialogView)
        //   dialog.setCancelable(false)

        val customDialog = dialog.create()
        customDialog.show()

        mLayoutcamera.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View): Unit {
                takePicture()
                customDialog.dismiss()

            }
        })

        mLayoutgallery.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View): Unit {
                openGallery()
                customDialog.dismiss()
            }
        })
    }

    fun takePicture() {

        CroperinoConfig("IMG_" + System.currentTimeMillis() + ".jpg", "/TyroSolution/Pictures", "/sdcard/TyroSolution/Pictures")
        CroperinoFileUtil.setupDirectory(appCompatActivity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                Croperino.prepareCamera(appCompatActivity)
            } catch (e: ActivityNotFoundException) {

                Log.d(TAG, "cannot take picture", e)
            }
        } else {

            try {
                Croperino.prepareCamera(appCompatActivity)
            } catch (e: ActivityNotFoundException) {

                Log.d(TAG, "cannot take picture", e)
            }

        }
    }

    fun openGallery() {

        CroperinoConfig("IMG_" + System.currentTimeMillis() + ".jpg", "/TyroSolution/Pictures", "/sdcard/TyroSolution/Pictures")
        CroperinoFileUtil.setupDirectory(appCompatActivity)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Croperino.prepareGallery21(this)
        } else {
            Croperino.prepareGallery21(this)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        when(requestCode) {

            CroperinoConfig.REQUEST_TAKE_PHOTO ->{

                Croperino.runCropImage(CroperinoFileUtil.getmFileTemp(), appCompatActivity, true, 1, 1, 0, 0)
            }
            CroperinoConfig.REQUEST_PICK_FILE ->{
                CroperinoFileUtil.newGalleryFile(data,appCompatActivity)
                Croperino.runCropImage(CroperinoFileUtil.getmFileTemp(), appCompatActivity, true, 1, 1, 0, 0)
            }

            CroperinoConfig.REQUEST_CROP_PHOTO -> {

                val uri = Uri.fromFile(com.crop.CroperinoFileUtil.getmFileTemp())
                val imgurl = utilityofActivity?.getPath(appCompatActivity!!.applicationContext, uri)
                val myBitmap = utilityofActivity?.decodeScaledBitmapFromSdCard(imgurl!!)

                profile_image.setImageBitmap(myBitmap)
                var imageInByte: ByteArray? = null
                val stream = ByteArrayOutputStream()
                myBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, stream)//80 before 100
                imageInByte = stream.toByteArray()
                //  String encodedImage = Base64.encodeToString(imageInByte, Base64.DEFAULT);
                val filename = imgurl?.substring(imgurl.lastIndexOf("/") + 1)
            }


        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
