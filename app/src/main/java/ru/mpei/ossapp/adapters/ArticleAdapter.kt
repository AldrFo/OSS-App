package ru.mpei.ossapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ru.mpei.ossapp.R
import ru.mpei.ossapp.pojo.Article

class ArticleAdapter(private val context: Context, private var elements: MutableList<Article>, private val onArticleClickListener: OnArticleClickListener, private val prefix: String) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.article_element, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = elements[position]
        Glide.with(context)
                .load(prefix + article.imageSrc)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.thumbnail)
        holder.articleTitle.text = article.name
    }

    override fun getItemCount(): Int {
        return elements.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val thumbnail: ImageView = view.findViewById(R.id.articleElementImage)
        val articleTitle: TextView = view.findViewById(R.id.articleElementText)

        init {
            view.setOnClickListener {
                val article = elements[layoutPosition]
                onArticleClickListener.onClickListener(article)
            }
        }
    }

    interface OnArticleClickListener {
        fun onClickListener(article: Article?)
    }

    fun updateList(list: MutableList<Article>){
        elements.clear()
        elements = list
        notifyDataSetChanged()
    }
}