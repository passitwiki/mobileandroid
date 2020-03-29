package com.passitwiki.passit.dialogfragments

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
import com.passitwiki.passit.activities.accessToken
import com.passitwiki.passit.api.RetrofitClient
import com.passitwiki.passit.tools.justRefresh
import kotlinx.android.synthetic.main.fragment_add_news_dialog.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


/**
 * Pop-up that enables you to add new news.
 */
class AddNewsDialogFragment : DialogFragment(), PickiTCallbacks {

    var selectedUri: Uri? = null
    var selectedPickiTFile: String = ""
    var pickiT: PickiT? = null

    companion object {
        const val KEY = "AddNewsDialogFragment"
        const val FIELD_AGE_GROUP = "FieldAgeGroup"
        fun newInstance(key: String, fag: Int): DialogFragment {
            val dFragment =
                AddNewsDialogFragment()
            val argument = Bundle()
            argument.putString(KEY, key)
            argument.putInt(FIELD_AGE_GROUP, fag)
            dFragment.arguments = argument
            return dFragment
        }
    }

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
        val view = inflater.inflate(R.layout.fragment_add_news_dialog, container, false)

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
                activity!!, Array<String>(1) { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 111
            )
        }

        buttonAttach.setOnClickListener {
            val intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)

        }

        check.setOnClickListener {

            val title = titleEditText.text.toString().trim()
            val content = contentEditText.text.toString().trim()
            var filerequest: RequestBody? = null
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
                filerequest = file.asRequestBody(
                    activity!!.contentResolver.getType(selectedUri!!)!!.toMediaTypeOrNull()
                )
                fileBody =
                    MultipartBody.Part.createFormData("attachment", file.name, filerequest)
            }

            arguments.let {
                val fag: Int = it!!.getInt(FIELD_AGE_GROUP)

                //TODO CHANGE THE SUBJECT GROUP ACCORDINGLY
                postInputNews(accessToken, title, content, 2, fag, fileBody)
            }
        }

        return view
    }

    /**
     * Takes the input values and posts them to the API. Closes the dialog.
     */
    fun postInputNews(
        access: String,
        title: String,
        content: String,
        subjectGroup: Int,
        fag: Int,
        requestBody: MultipartBody.Part?
    ) {
        RetrofitClient.instance.postNews(access, title, content, subjectGroup, fag, requestBody)
            .enqueue(object : Callback<Unit> {
                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Toast.makeText(
                        activity!!.applicationContext,
                        t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<Unit>,
                    response: Response<Unit>
                ) {
                    Log.d(
                        "MyTagExplicitNetworking",
                        "AddNewsDialogFragment response ${response.body()}"
                    )
                    Log.d(
                        "MyTagExplicitNetworking",
                        "AddNewsDialogFragment code ${response.code()}"
                    )

                    if (response.code() == 401) {
                        justRefresh("addNewsDialogFragment")
                        postInputNews(accessToken, title, content, subjectGroup, fag, requestBody)
                    }
                    dismiss()
                }

            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 111 && resultCode == RESULT_OK) {
            selectedUri = data?.data
            pickiT!!.getPath(
                data?.data,
                Build.VERSION.SDK_INT
            ) //The uri with the location of the file

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
