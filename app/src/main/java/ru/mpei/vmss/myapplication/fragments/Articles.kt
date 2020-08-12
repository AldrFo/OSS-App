package ru.mpei.vmss.myapplication.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
import java.util.*

class Articles : Fragment {
    private var adapter: ArticleAdapter? = null
    private val dataList: MutableList<Article> = ArrayList()
    private var url: String? = null
    private var header: String? = null
    private var prefix: String? = null
    private var type = 0

    constructor() {}
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
        val rootView = inflater.inflate(R.layout.fragment_articles, container, false)
        val title = rootView.findViewById<TextView>(R.id.articlesHeader)
        title.text = header
        val list: RecyclerView = rootView.findViewById(R.id.articlesList)
        val layoutManager = LinearLayoutManager(context)
        list.layoutManager = layoutManager
        adapter = ArticleAdapter(activity!!.applicationContext, dataList, object : OnArticleClickListener {
            override fun onClickListener(article: Article?) {
                val intent = Intent(context, ArticleActivity::class.java)
                if (article != null) {
                    intent.putExtra("head", article.title)
                }
                if (article != null) {
                    intent.putExtra("date", article.date)
                }
                if (article != null) {
                    intent.putExtra("content", article.content)
                }
                if (article != null) {
                    intent.putExtra("imageUrl", article.imageUrl)
                }
                startActivity(intent)
            }
        })
        list.adapter = adapter
        updateList()
        val refresher: SwipeRefreshLayout = rootView.findViewById(R.id.articlesRefresher)
        refresher.setColorSchemeColors(requireContext().getColor(R.color.bgBottomNavigation))
        refresher.setOnRefreshListener {
            updateList()
            refresher.isRefreshing = false
        }
        return rootView
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
                Response.ErrorListener { error: VolleyError? -> Toast.makeText(context, "Connection error", Toast.LENGTH_LONG).show() })
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)
    }
}