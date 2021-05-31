package com.ametr1ne.overdiff.listener

import android.view.View
import com.ametr1ne.overdiff.models.Article
import com.ametr1ne.overdiff.models.User
import com.ametr1ne.overdiff.utils.EvaluationArticleTask
import com.ametr1ne.overdiff.utils.NConsumer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class EvaluationClickListener(
    private val like: Boolean,
    private val article: Article,
    private val user: User,
    private val action: NConsumer<JSONObject>
) : View.OnClickListener {
    override fun onClick(v: View) {
        if (user.isAuthorization) {

            CoroutineScope(Dispatchers.Main).launch {
                val result = EvaluationArticleTask(user.accessToken, article, like).evaluation()
                if(result!=null){
                    action.accept(result)
                }
            }
        }
    }
}