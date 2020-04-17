package com.passitwiki.passit.dialogfragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.hbisoft.pickit.PickiT
import com.hbisoft.pickit.PickiTCallbacks
import com.passitwiki.passit.R
import com.passitwiki.passit.activity.accessToken
import com.passitwiki.passit.networking.Resource
import com.passitwiki.passit.networking.Status
import com.passitwiki.passit.repository.Repository
import com.passitwiki.passit.utilities.Utilities
import kotlinx.android.synthetic.main.fragment_add_news_dialog.view.*
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.android.ext.android.inject
import java.io.File


/**
 * Pop-up that enables you to add new news.
 */
class AddNewsDialogFragment(private val key: String, private val fieldAgeGroup: Int) :
    DialogFragment(),
    PickiTCallbacks {
    private val repository: Repository by inject()
    private var selectedUri: Uri? = null
    private var selectedPickiTFile: String = ""
    private var pickiT: PickiT? = null

    /**
     * Changes the layout's size to below.
     */
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window!!.setLayout(width, height)
        }
    }

    /**
     * Prepares the view and listeners.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater
            .inflate(R.layout.fragment_add_news_dialog, container, false)

        val titleEditText = view.editTextNewsTitle
        val contentEditText = view.editTextNewsContent
        val check = view.buttonCheck
        val cross = view.buttonCross
        val buttonAttach = view.buttonAttach

        cross.setOnClickListener {
            dismiss()
        }

        pickiT = PickiT(activity!!.applicationContext, this)
        val permission =
            ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity!!, Array(1) {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, 111
            )
        }

        view.buttonAttachCross.setOnClickListener {
            selectedUri = null
            selectedPickiTFile = ""
            view.buttonAttach.visibility = View.VISIBLE
            view.textViewUrl.text = ""
            view.textViewUrl.visibility = View.GONE
            view.buttonAttachCross.visibility = View.GONE
        }

        buttonAttach.setOnClickListener {
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(
                Intent.createChooser(intent, "Select a file"), 111
            )
        }

        check.setOnClickListener {

            val title = titleEditText.text.toString().trim()
            Log.d("MyTag", title)
            val content = contentEditText.text.toString().trim()
            val fileRequest: RequestBody?
            var fileBody: MultipartBody.Part? = null

            if (title.isEmpty()) {
                view.editTextNewsTitle.error = "Title required"
                view.editTextNewsTitle.requestFocus()
                return@setOnClickListener
            }
            if (content.isEmpty()) {
                view.editTextNewsContent.error = "Content required"
                view.editTextNewsContent.requestFocus()
                return@setOnClickListener
            }

            if (selectedPickiTFile != "" && selectedUri != null) {
                val file = File(selectedPickiTFile)
                fileRequest = file.asRequestBody(
                    activity!!.contentResolver.getType(selectedUri!!)!!.toMediaTypeOrNull()
                )
                fileBody =
                    MultipartBody.Part.createFormData("attachment", file.name, fileRequest)
            }


            //TODO CHANGE THE SUBJECT GROUP ACCORDINGLY
            postInputNews(title, content, 2, fieldAgeGroup, fileBody)

        }

        return view
    }

    /**
     * Takes the input values and posts them to the API. Closes the dialog.
     */
    private fun postInputNews(
        title: String,
        content: String,
        subjectGroup: Int,
        fag: Int,
        requestBody: MultipartBody.Part?
    ) {
        Log.d("MyTag", "before the postInstance $title")
        val titlePart =
            MultipartBody.Part.createFormData("title", title)
        val contentPart =
            MultipartBody.Part.createFormData("content", content)

        var resource: Resource<Unit>? = null
        runBlocking {
            resource = repository.handlePostNews(
                accessToken,
                titlePart,
                contentPart,
                subjectGroup,
                fag,
                requestBody
            )
        }
        when (resource!!.status) {
//                Status.LOADING -> null //TODO loading
            Status.ERROR -> {
                if (resource!!.message == "Unauthorised. Please refresh.") {
                    runBlocking {
                        Utilities(repository).justRefresh(key)
                        postInputNews(title, content, subjectGroup, fag, requestBody)
                    }
                } else {
                    Toast.makeText(
                        activity!!.applicationContext,
                        resource!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            Status.SUCCESS -> {
                Log.d(
                    "MyTagExplicitNetworking",
                    "AddNewsDialogFragment response ${resource.toString()}"
                )
                dismiss()
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            selectedUri = data?.data
            pickiT!!.getPath(
                data?.data,
                Build.VERSION.SDK_INT
            ) //The uri with the location of the file
            view!!.buttonAttach.visibility = View.GONE
            view!!.textViewUrl.text = selectedPickiTFile.split("/").last()
            view!!.textViewUrl.visibility = View.VISIBLE
            view!!.buttonAttachCross.visibility = View.VISIBLE
        }
    }

    override fun PickiTonProgressUpdate(progress: Int) {

    }

    override fun PickiTonStartListener() {

    }

    override fun PickiTonCompleteListener(
        path: String?,
        wasDriveFile: Boolean,
        wasUnknownProvider: Boolean,
        wasSuccessful: Boolean,
        Reason: String?
    ) {
        Log.d("MyTagUri", path)
        if (path != null) {
            selectedPickiTFile = path
        }

    }

}
