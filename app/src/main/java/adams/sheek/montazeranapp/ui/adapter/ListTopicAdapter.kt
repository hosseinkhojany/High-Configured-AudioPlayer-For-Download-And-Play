package adams.sheek.montazeranapp.ui.adapter

import adams.sheek.montazeranapp.R
import adams.sheek.montazeranapp.data.datasource.ram.InRamDs
import adams.sheek.montazeranapp.data.model.Topic
import adams.sheek.montazeranapp.databinding.TopicItemBinding
import adams.sheek.montazeranapp.utils.Helper.loadImage
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.skydoves.bindables.binding

class ListTopicAdapter(private val list: List<Topic>,private val onItemClick: (Int) -> Unit)
    : RecyclerView.Adapter<ListTopicAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = parent.binding<TopicItemBinding>(R.layout.topic_item)
        return ViewHolder(binding).apply {
            binding.item.setOnClickListener {
                val position = bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }
                    ?: return@setOnClickListener
                list[position].let {
                    InRamDs.selectedTopicPositionInList = position
                    onItemClick.invoke(position)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            topicTitle.text = list[position].name
            topicTime.text = "${list[position].files.size-1} "+"جلسه | "+list[position].duration
            topicLogo.loadImage(list[position].image)
            executePendingBindings()
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: TopicItemBinding) : RecyclerView.ViewHolder(binding.root)

}