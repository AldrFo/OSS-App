package ru.mpei.vmss.myapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.mpei.vmss.myapplication.R
import ru.mpei.vmss.myapplication.pojo.Article

class ArticleAdapter(private val context: Context, private var elements: List<Article>, private val onArticleClickListener: OnArticleClickListener) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {
    private val inflater: LayoutInflater
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.article_element, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = elements[position]
        Glide.with(context)
                .load(article.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail)
        holder.articleTitle.text = article.title
    }

    override fun getItemCount(): Int {
        return elements.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail: ImageView
        val articleTitle: TextView

        init {
            thumbnail = view.findViewById(R.id.articleElementImage)
            articleTitle = view.findViewById(R.id.articleElementText)
            view.setOnClickListener { v: View? ->
                val article = elements[layoutPosition]
                onArticleClickListener.onClickListener(article)
            }
        }
    }

    fun setArticles(articles: Collection<Article>) {
        elements = articles as List<Article>
        notifyDataSetChanged()
    }

    interface OnArticleClickListener {
        fun onClickListener(article: Article?)
    }

    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}