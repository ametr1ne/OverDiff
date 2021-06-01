package com.ametr1ne.overdiff.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ametr1ne.overdiff.MainActivity
import com.ametr1ne.overdiff.R
import com.ametr1ne.overdiff.UserFactory.Companion.getInstance
import com.ametr1ne.overdiff.tape.ListArticleAdapter
import com.ametr1ne.overdiff.utils.ArticlesActionTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TapesFragment(private val mainActivity: MainActivity) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.recycler_view, container, false)
        val recyclerView = view.findViewById<View>(R.id.recycler_view) as RecyclerView

       CoroutineScope(Dispatchers.IO).launch {

           val articles = ArticlesActionTask().getArticles()
           withContext(Dispatchers.Main){
               val listAdapter = ListArticleAdapter(
                   mainActivity, articles
               )
               recyclerView.adapter = listAdapter
               val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
               recyclerView.layoutManager = layoutManager
           }

       }

        view.findViewById<View>(R.id.new_article).visibility =
            if (getInstance().getCurrentUser().isAuthorization) View.VISIBLE else View.INVISIBLE
        view.findViewById<View>(R.id.new_article).setOnClickListener {
            mainActivity.runOnUiThread {
                mainActivity.supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    CreateArticleFragment(mainActivity)
                ).commit()
            }
        }
        return view
    }
}