package com.ametr1ne.overdiff.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ametr1ne.overdiff.MainActivity
import com.ametr1ne.overdiff.R
import com.ametr1ne.overdiff.UserFactory.Companion.getInstance
import com.ametr1ne.overdiff.models.Article
import com.ametr1ne.overdiff.models.User
import com.ametr1ne.overdiff.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException

class ArticleFragment(private val source: MainActivity, private val articleHash: String) :
    Fragment() {
    private val likeButton: ImageButton? = null
    private val dislikeButton: ImageButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_article, container, false)
        val currentUser = getInstance().getCurrentUser()

        CoroutineScope(Dispatchers.Main).launch {
            val article = ArticleActionTask(articleHash).getArticle()
            if (article != null) {
                (source.findViewById<View>(R.id.articleText) as TextView).text = article.text
                (source.findViewById<View>(R.id.articleName) as TextView).text = article.description

                val imageView = source.findViewById(R.id.articleIcon) as ImageView

                ImageLoadTask(article.icon).upload()?.let {
                    imageView.setImageBitmap(it)
                }


                source.findViewById<View>(R.id.article_newcomment).visibility =
                    if (currentUser.isAuthorization) View.VISIBLE else View.INVISIBLE
                val button = source.findViewById<ImageButton>(R.id.new_comment_button)
                button.setOnClickListener { v: View? ->
                    val textView = source.findViewById<View>(R.id.new_comment_textview) as TextView

                    CoroutineScope(Dispatchers.Main).launch {

                        val resoult = AddCommentTask(
                            currentUser.accessToken,
                            article.id,
                            textView.text.toString(),
                        ).addComment()

                        if (resoult != null) {
                            source.supportFragmentManager.beginTransaction().replace(
                                R.id.fragment_container,
                                ArticleFragment(source, article.hash)
                            ).commit()
                        } else {
                            //TODO оповестить, что что-то сломалось
                        }

                    }
                }
                (source.findViewById<View>(R.id.article_like_view) as TextView).setText(
                    Integer.valueOf(
                        article.likes
                    ).toString()
                )
                (source.findViewById<View>(R.id.article_dislikes_view) as TextView).setText(
                    Integer.valueOf(
                        article.dislikes
                    ).toString()
                )
                val layout = source.findViewById<LinearLayout>(R.id.comment_layout)
                for (comment in article.comment) {
                    val view =
                        LayoutInflater.from(source).inflate(R.layout.preview_comment, layout, false)
                    val text = view.findViewById<TextView>(R.id.comment_title)
                    text.text = comment.comment
                    layout.addView(view)
                }

                source.findViewById<View>(R.id.article_like_button)
                    .setOnClickListener { v: View? ->
                        evaluationArticle(
                            currentUser,
                            article,
                            true
                        )
                    }
                source.findViewById<View>(R.id.article_dislike_button)
                    .setOnClickListener { v: View? ->
                        evaluationArticle(
                            currentUser,
                            article,
                            false
                        )
                    }
            } else {
                //TODO оповестить, что произошла ошибка при загрзке статьи
            }
        }



        return inflate
    }

    private fun evaluationArticle(currentUser: User, article: Article?, like: Boolean) {
        if (currentUser.isAuthorization) {

            CoroutineScope(Dispatchers.IO).launch {

                val jsonObject =
                    EvaluationArticleTask(currentUser.accessToken, article!!, like).evaluation()


                if(jsonObject!=null) {
                    try {
                        val status: Int = jsonObject.getInt("status")
                        if (status == ArticleStatus.SUCCESSFULLY) {
                            val likes: Int = jsonObject.getInt("likes")
                            val dislikes: Int = jsonObject.getInt("dislikes")
                            withContext(Dispatchers.Main){
                                (source.findViewById<View>(R.id.article_like_view) as TextView).text =
                                    likes.toString()
                                (source.findViewById<View>(R.id.article_dislikes_view) as TextView).text =
                                    dislikes.toString()
                            }
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

        }
    }
}