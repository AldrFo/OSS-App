package ru.mpei.feature_profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import kekmech.ru.common_android.viewbinding.viewBinding
import kekmech.ru.common_mvi.ui.BaseFragment
import kekmech.ru.common_navigation.PopUntil
import kekmech.ru.common_navigation.Router
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.koin.android.ext.android.inject
import ru.mpei.domain_profile.dto.ConfirmRefuseItem
import ru.mpei.domain_profile.dto.ReportItem
import ru.mpei.feature_profile.databinding.FragmentTaskReportBinding
import ru.mpei.feature_profile.items.URIPathHelper
import ru.mpei.feature_profile.mvi.*
import ru.mpei.feature_profile.mvi.ProfileEvent.Wish
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class EditReportFragment(private val taskId: String, private val taskName: String, private val type: ReportType) : BaseFragment<ProfileEvent, ProfileEffect, ProfileState, ProfileFeature>() {

    private lateinit var currentPhotoPath: String

    private var image: File? = null;

    private val binding by viewBinding(FragmentTaskReportBinding::bind)
    private val mSettings: SharedPreferences by inject()
    private val router: Router by inject()

    override var layoutId: Int = R.layout.fragment_task_report

    private val profileFeatureFactory: ProfileFeatureFactory by inject()
    override fun createFeature(): ProfileFeature = profileFeatureFactory.create()

    override val initEvent: ProfileEvent = Wish.System.InitReport

    @SuppressLint("QueryPermissionsNeeded")
    override fun onViewCreatedInternal(view: View, savedInstanceState: Bundle?) {
        when (type) {
            ReportType.NEW -> {
                with(binding) {
                    btnSendNoReport.visibility = View.VISIBLE
                    btnSendWithReport.visibility = View.VISIBLE
                    btnSendReport.visibility = View.GONE

                    btnSendNoReport.setOnClickListener {
                        val body = ConfirmRefuseItem(task_id = taskId, user_id = mSettings.getString(ProfileFragment.APP_PREFERENCES_ID, "0")!!)
                        feature.accept(Wish.ConfirmTask(body))
                    }
                    btnSendWithReport.setOnClickListener {
                        val body: ReportItem = if (image != null) {
                            ReportItem(comment = fragmentTaskReportComment.text.toString(), user_id = mSettings.getString(ProfileFragment.APP_PREFERENCES_ID, "0")!!, task_id = taskId, file_name = image!!.name)
                        } else {
                            ReportItem(comment = fragmentTaskReportComment.text.toString(), user_id = mSettings.getString(ProfileFragment.APP_PREFERENCES_ID, "0")!!, task_id = taskId)
                        }

                        var imageFileBody: MultipartBody.Part? = null

                        if (image != null) {
                            val requestBody: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), image!!)
                            imageFileBody = MultipartBody.Part.createFormData("image", image!!.name, requestBody)
                        }

                        feature.accept(Wish.SendReport(body, imageFileBody))
                    }
                }
            }

            ReportType.EDIT -> {
                with(binding) {
                    btnSendReport.visibility = View.VISIBLE
                    btnSendWithReport.visibility = View.GONE
                    btnSendNoReport.visibility = View.GONE

                    btnSendReport.setOnClickListener {
                        val body: ReportItem = if (image != null) {
                            ReportItem(comment = fragmentTaskReportComment.text.toString(), user_id = mSettings.getString(ProfileFragment.APP_PREFERENCES_ID, "0")!!, task_id = taskId, file_name = image!!.name)
                        } else {
                            ReportItem(comment = fragmentTaskReportComment.text.toString(), user_id = mSettings.getString(ProfileFragment.APP_PREFERENCES_ID, "0")!!, task_id = taskId)
                        }

                        var imageFileBody: MultipartBody.Part? = null

                        if (image != null) {
                            val requestBody: RequestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), image!!)
                            imageFileBody = MultipartBody.Part.createFormData("image", image!!.name, requestBody)
                        }

                        feature.accept(Wish.SendReport(body, imageFileBody))
                    }
                }
            }
        }

        binding.fragmentTaskReportToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        binding.fragmentTaskReportToolbar.setNavigationOnClickListener { router.executeCommand(PopUntil(TaskFragment::class)) }

        binding.fragmentTaskReportToolbarText.text = taskName

        binding.btnAddPhotoImage.setOnClickListener {

            feature.accept(Wish.AddPhoto)
        }
    }

    private fun createImageFile(): File {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.absolutePath
        return image
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun render(state: ProfileState) {
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            TAKE_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                binding.btnAddPhotoImage.visibility = View.GONE
                binding.reportImageCard.visibility = View.VISIBLE
                binding.reportImage.setImageURI(Uri.parse(currentPhotoPath))
                binding.removeImage.setOnClickListener {
                    binding.btnAddPhotoImage.visibility = View.VISIBLE
                    binding.reportImageCard.visibility = View.GONE
                }
                image = File(currentPhotoPath)
            }
            CHOOSE_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                // String picturePath contains the path of selected Image

                binding.btnAddPhotoImage.visibility = View.GONE
                binding.reportImageCard.visibility = View.VISIBLE
                binding.reportImage.setImageURI(data!!.data!!)
                binding.removeImage.setOnClickListener {
                    binding.btnAddPhotoImage.visibility = View.VISIBLE
                    binding.reportImageCard.visibility = View.GONE
                }
                val helper = URIPathHelper()
                image = File(helper.getPath(requireContext(), data.data!!)!!)
            }
        }
    }


    private fun getImagePath(data: Intent): String? {
        val path = data.data
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireContext().contentResolver.query(path!!,
            filePathColumn, null, null, null)
        cursor!!.moveToFirst()
        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val picturePath = cursor.getString(columnIndex)
        cursor.close()
        return picturePath
    }

    @SuppressLint("QueryPermissionsNeeded")
    override fun handleEffect(effect: ProfileEffect) {
        when (effect) {
            is ProfileEffect.ConfirmSuccess -> {
                router.executeCommand(PopUntil(TaskFragment::class))
            }
            is ProfileEffect.ReportSendSuccess -> {
                router.executeCommand(PopUntil(TaskFragment::class))
            }
            is ProfileEffect.ReportSendError -> {
                Toast.makeText(context, "Report send error - " + effect.throwable.message, Toast.LENGTH_LONG).show()
                Timber.e(effect.throwable.message.toString())
            }
            is ProfileEffect.ConfirmError -> {
                Toast.makeText(context, "Confirm error", Toast.LENGTH_SHORT).show()
            }
            is ProfileEffect.AddPhoto -> {

                val p = PopupMenu(context, binding.btnAddPhotoImage)
                p.menuInflater.inflate(R.menu.menu_popup_image_type, p.menu)
                p.setOnMenuItemClickListener { item: MenuItem ->
                    if (item.title == "Сделать фотографию") {

                        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) !=
                            PackageManager.PERMISSION_GRANTED  ){
                            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), 0)
                        }
                        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) ==
                            PackageManager.PERMISSION_GRANTED  ) {
                            //получение фото через камеру
                            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            // Ensure that there's a camera activity to handle the intent
                            if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
                                // Create the File where the photo should go
                                var photoFile: File? = null
                                try {
                                    photoFile = createImageFile()
                                } catch (ignored: IOException) {
                                }
                                // Continue only if the File was successfully created
                                if (photoFile != null) {
                                    val photoURI = FileProvider.getUriForFile(requireContext(),
                                        "ru.mpei.ossapp.myapplication.fileprovider", photoFile);
                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                                    startActivityForResult(takePictureIntent, TAKE_PHOTO)
                                }
                            }
                        }
                    } else {

                        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED  ){
                            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                        }

                        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_GRANTED  ) {
                            val pickPhoto = Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            startActivityForResult(pickPhoto, CHOOSE_PHOTO) //one can be replaced with any action code
                        }
                    }
                    true
                }
                p.show()
            }
            else -> {
            }
        }
    }

    companion object {
        private const val TAKE_PHOTO: Int = 0
        private const val CHOOSE_PHOTO: Int = 1
    }
}