package adams.sheek.montazeranapp.ui.adapter

import adams.sheek.montazeranapp.R
import adams.sheek.montazeranapp.data.datasource.ram.InRamDs
import adams.sheek.montazeranapp.data.model.Topic
import adams.sheek.montazeranapp.data.model.TopicFile
import adams.sheek.montazeranapp.databinding.ClassItemBinding
import adams.sheek.montazeranapp.databinding.RecyclerViewHeaderBinding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mikhaellopez.hfrecyclerview.HFRecyclerView


class ListClassesAdapter(private val topic: Topic,
                         private val onItemClick: (TopicFile, Int) -> Unit,
                         private val onDownloadClick: (TopicFile, Int) -> Unit,
                         private val onBackClick: () -> Unit):
HFRecyclerView<TopicFile>(true, false) {

    override fun getItemView(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder.ItemViewHolder(ClassItemBinding.inflate(inflater, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder.ItemViewHolder -> {
                holder.bind(topic, topic.files[position],{
                    InRamDs.selectedTopicFilePositionInList = position
                    onItemClick(it, position)}, {
                    InRamDs.selectedTopicFilePositionInList = position
                    onDownloadClick(it, position)
                })
            }
            is ViewHolder.HeaderViewHolder -> {
                holder.bind(topic){onBackClick()}
            }
        }
    }
    override fun getItemCount(): Int {
        return topic.files.size
    }

    override fun getHeaderView(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder =
        ViewHolder.HeaderViewHolder(RecyclerViewHeaderBinding.inflate(inflater, parent, false))


    override fun getFooterView(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder? = null

    sealed class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        class ItemViewHolder(private val view: ClassItemBinding) : ViewHolder(view.root) {

            fun bind(topic: Topic, item: TopicFile,
                     onItemClick: (TopicFile) -> Unit,
                     onDownloadClick: (TopicFile) -> Unit) {
                item.apply {
                    view.topicTime.text = item.duration
                    view.topicTitle.text = item.title
                    if (item.isDownloading){
                        view.imageView.visibility = View.INVISIBLE
                        view.progressBar.visibility = View.VISIBLE
                        view.cancellDownload.visibility = View.VISIBLE
                        view.progressBar.setOnClickListener { onDownloadClick(item) }
                        view.cancellDownload.setOnClickListener { onDownloadClick(item) }
                    }else{
                        view.imageView.visibility = View.VISIBLE
                        view.progressBar.visibility = View.INVISIBLE
                        view.cancellDownload.visibility = View.INVISIBLE
                    }
                    if (item.isDownloaded){
                        view.imageView.setImageResource(R.drawable.audio_icon)
                        view.imageView.setOnClickListener { onItemClick(item) }
                    }else{
                        view.imageView.setImageResource(R.drawable.download_icon)
                        view.imageView.setOnClickListener { onDownloadClick(item) }
                    }
                }
                view.cardView.text = bindingAdapterPosition.toString()
                view.item.setOnClickListener { onItemClick(item) }
            }

        }

        class HeaderViewHolder(private val view: RecyclerViewHeaderBinding) : ViewHolder(view.root){
            fun bind(item: Topic,
                     onBackClick: () -> Unit) {
                item.apply {
                    Glide.with(view.logo).load(item.image).into(view.logo)
                    view.headerTitle.text = item.name
                    view.allTime.text = "${item.files.size-1} "+"جلسه | "+item.duration
                    view.back.setOnClickListener{ onBackClick() }
                }
            }
        }

    }

}