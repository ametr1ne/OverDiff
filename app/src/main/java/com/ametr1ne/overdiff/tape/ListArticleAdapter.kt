package com.ametr1ne.overdiff.tape

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ametr1ne.overdiff.MainActivity
import com.ametr1ne.overdiff.R
import com.ametr1ne.overdiff.fragments.ArticleFragment
import com.ametr1ne.overdiff.models.Article
import com.ametr1ne.overdiff.utils.ImageLoadTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListArticleAdapter(private val source: MainActivity, private val articles: List<Article>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.preview_article, parent, false)
        return ListViewHolder(view, articles, source)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ListViewHolder).bindView(position)
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    private class ListViewHolder(
        itemView: View,
        private val articles: List<Article>,
        private val source: MainActivity
    ) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private var article: Article? = null
        private var mItemText: TextView = itemView.findViewById<View>(R.id.title_article) as TextView
        private var mItemImage: ImageView = itemView.findViewById<View>(R.id.image_preview) as ImageView

        init {
            itemView.setOnClickListener(this)
        }

        fun bindView(position: Int) {
            mItemText.text = articles[position].description

            CoroutineScope(Dispatchers.IO).launch {
                val result = ImageLoadTask(articles[position].icon).upload()
                result?.let {
                    withContext(Dispatchers.Main) { mItemImage.setImageBitmap(it) }
                }
            }
            article = articles[position]
        }

        override fun onClick(view: View) {
            source.supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                ArticleFragment(source, article!!.hash)
            ).addToBackStack("tag").commit()
        }


    }
}