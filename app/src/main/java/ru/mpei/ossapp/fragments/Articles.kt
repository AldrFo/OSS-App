package ru.mpei.ossapp.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_articles.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.mpei.ossapp.R
import ru.mpei.ossapp.activities.ArticleActivity
import ru.mpei.ossapp.adapters.ArticleAdapter
import ru.mpei.ossapp.adapters.ArticleAdapter.OnArticleClickListener
import ru.mpei.ossapp.http.HttpRequests
import ru.mpei.ossapp.pojo.Article
import java.util.*

class Articles : Fragment {
    private lateinit var adapter: ArticleAdapter
    private var dataList: MutableList<Article> = ArrayList()
    private var url: String? = null
    private var header: String? = null
    private lateinit var prefix: String
    private var type = 0

    constructor()
    constructor(type: Int) {
        this.type = type
    }

    @SuppressLint("NewApi")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val inf = inflater.inflate(R.layout.fragment_articles, container, false)
        if (type == 0) {
            url = requireContext().getString(R.string.dashboardUrl)
            header = requireContext().getString(R.string.dashboard)
            prefix = requireContext().getString(R.string.imagesDashboardUrl)
        } else if (type == 1) {
            url = requireContext().getString(R.string.newsUrl)
            header = requireContext().getString(R.string.news)
            prefix = requireContext().getString(R.string.imagesNewsUrl)
        }
        return inf
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articlesHeader.text = header
        val layoutManager = LinearLayoutManager(context)
        articlesList.layoutManager = layoutManager
        adapter = ArticleAdapter(activity!!.applicationContext, dataList, object : OnArticleClickListener {
            override fun onClickListener(article: Article?) {
                val intent = Intent(context, ArticleActivity::class.java)
                if (article != null) {
                    intent.putExtra("head", article.name)
                    intent.putExtra("date", article.chislo + " " + article.month + " " + article.hour)
                    intent.putExtra("content", article.content)
                    intent.putExtra("imageUrl", prefix + article.imageSrc)
                    Toast.makeText(context, prefix + article.imageSrc, Toast.LENGTH_SHORT).show()
                }
                startActivity(intent)
            }
        }, prefix)

        articlesList.adapter = adapter

        updateList()
        articlesRefresher.setColorSchemeColors(requireContext().getColor(R.color.bgBottomNavigation))
        articlesRefresher.setOnRefreshListener {
            updateList()
            articlesRefresher.isRefreshing = false
        }
    }

    private fun updateList() {

        val retrofit = Retrofit.Builder()
                .baseUrl("http://cy37212.tmweb.ru")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val httpRequests = retrofit.create(HttpRequests::class.java)

        val request = if (type == 0) httpRequests.getEvents() else httpRequests.getNews()

        request.enqueue(object : Callback<MutableList<Article>> {
            override fun onResponse(call: Call<MutableList<Article>>, response: Response<MutableList<Article>>) {
                dataList = response.body()!!
                adapter.updateList(dataList)
            }

            override fun onFailure(call: Call<MutableList<Article>>, t: Throwable) {
                Toast.makeText(context, "Unable to get data from server", Toast.LENGTH_SHORT).show()
            }
        })

    }
}