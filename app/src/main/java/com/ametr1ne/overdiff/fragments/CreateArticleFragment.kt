package com.ametr1ne.overdiff.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ametr1ne.overdiff.MainActivity
import com.ametr1ne.overdiff.R
import com.ametr1ne.overdiff.UserFactory.Companion.getInstance
import com.ametr1ne.overdiff.encryption.JWT
import com.ametr1ne.overdiff.models.User
import com.ametr1ne.overdiff.utils.ArticleStatus
import com.ametr1ne.overdiff.utils.CreateArticleTask
import com.ametr1ne.overdiff.utils.ImageFilePath
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException

class CreateArticleFragment(private val source: MainActivity) : Fragment() {
    private var imagePath: String? = null
    private var bitmap: Bitmap? = null
    private var file: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_article, container, false)
        val user = getInstance().getCurrentUser()
        if (user.accessToken != null && !JWT.isAlive(user.accessToken)) {
            getInstance().refreshCurrentUser { user1: User? ->
                if (!user1!!.isAuthorization) {
                    source.openAuthorizationPage()
                }
            }
        }
        if (user.isAuthorization) {
            imageView = view.findViewById<View>(R.id.new_article_img) as ImageView
            view.findViewById<View>(R.id.new_article_find_img).setOnClickListener { v: View? ->
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                startActivityForResult(photoPickerIntent, Pick_image)
            }
            view.findViewById<View>(R.id.new_article_create).setOnClickListener { v: View? ->

                CoroutineScope(Dispatchers.IO).launch {

                    val result = CreateArticleTask(
                        file,
                        (view.findViewById<View>(R.id.new_article_name) as TextView).text.toString(),
                        (view.findViewById<View>(R.id.new_article_text) as TextView).text.toString()
                    ).createArticle()

                    if(result == ArticleStatus.SUCCESSFULLY){
                        withContext(Dispatchers.Main){
                            source.supportFragmentManager.beginTransaction().replace(
                                R.id.fragment_container,
                                TapesFragment(source)
                            ).commit()
                        }
                    }else{
                        //TODO если статья не создалась
                    }
                }
            }
        }
        return view
    }

    private var imageView: ImageView? = null
    private val Pick_image = 1
    override fun onActivityResult(requestCode: Int, resultCode: Int, imageReturnedIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent)
        if (requestCode == Pick_image) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    val imageUri = imageReturnedIntent!!.data
                    val imageStream = source.contentResolver.openInputStream(
                        imageUri!!
                    )
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    imageView!!.setImageBitmap(selectedImage)
                    val file = File(ImageFilePath.getPath(source, imageUri))
                    this.file = file
                    val path = ImageFilePath.getPath(source, imageUri)
                    bitmap = selectedImage
                    imagePath = path
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }
    }
}