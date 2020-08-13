package ru.mpei.vmss.myapplication.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import ru.mpei.vmss.myapplication.R
import ru.mpei.vmss.myapplication.activities.ArticleActivity
import ru.mpei.vmss.myapplication.adapters.ArticleAdapter
import ru.mpei.vmss.myapplication.adapters.ArticleAdapter.OnArticleClickListener
import ru.mpei.vmss.myapplication.pojo.Article
import kotlinx.android.synthetic.main.fragment_articles.*
import java.util.*

class Articles : Fragment {
    private var adapter: ArticleAdapter? = null
    private val dataList: MutableList<Article> = ArrayList()
    private var url: String? = null
    private var header: String? = null
    private var prefix: String? = null
    private var type = 0

    constructor()
    constructor(type: Int) {
        this.type = type
    }

    @SuppressLint("NewApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (type == 0) {
            url = requireContext().getString(R.string.dashboardUrl)
            header = requireContext().getString(R.string.dashboard)
            prefix = requireContext().getString(R.string.imagesDashboardUrl)
        } else if (type == 1) {
            url = requireContext().getString(R.string.newsUrl)
            header = requireContext().getString(R.string.news)
            prefix = requireContext().getString(R.string.imagesNewsUrl)
        }
        articlesHeader.text = header
        val layoutManager = LinearLayoutManager(context)
        articlesList.layoutManager = layoutManager
        adapter = ArticleAdapter(activity!!.applicationContext, dataList, object : OnArticleClickListener {
            override fun onClickListener(article: Article?) {
                val intent = Intent(context, ArticleActivity::class.java)
                if (article != null) {
                    intent.putExtra("head", article.title)
                    intent.putExtra("date", article.date)
                    intent.putExtra("content", article.content)
                    intent.putExtra("imageUrl", article.imageUrl)
                }
                startActivity(intent)
            }
        })
        articlesList.adapter = adapter
        updateList()
        articlesRefresher.setColorSchemeColors(requireContext().getColor(R.color.bgBottomNavigation))
        articlesRefresher.setOnRefreshListener {
            updateList()
            articlesRefresher.isRefreshing = false
        }
        return inflater.inflate(R.layout.fragment_articles, container, false)
    }

    private fun updateList() {
        dataList.clear()
        val request = JsonArrayRequest(Request.Method.GET,
                url, Response.Listener { response: JSONArray ->
            for (i in 0 until response.length()) {
                var obj: JSONObject
                try {
                    obj = response.getJSONObject(i)
                    dataList.add(Article(obj.getString("name"),
                            obj.getString("content"),
                            obj.getString("chislo") + " " + obj.getString("month") + " в " + obj.getString("hour"),
                            null,
                            prefix + obj.getString("image_src")))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            adapter!!.setArticles(dataList)
        },
                Response.ErrorListener { _: VolleyError? -> Toast.makeText(context, "Connection error", Toast.LENGTH_LONG).show() })
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)
    }
}