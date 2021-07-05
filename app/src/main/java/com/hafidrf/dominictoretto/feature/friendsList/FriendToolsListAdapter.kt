package com.hafidrf.dominictoretto.feature.friendsList

import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hafidrf.dominictoretto.R
import com.hafidrf.dominictoretto.feature.ItemClickListener
import com.hafidrf.dominictoretto.model.Tool
import com.hafidrf.dominictoretto.util.inflate
import com.hafidrf.dominictoretto.util.setAvatarImage
import kotlinx.android.synthetic.main.friend_tools_list_item.view.*

class FriendToolsListAdapter : RecyclerView.Adapter<FriendToolsListAdapter.ViewHolder>() {

    private var toolsList: List<Tool> = listOf()

    private lateinit var itemClickListener: ItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.friend_tools_list_item))

    override fun getItemCount() = toolsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(toolsList[position])
        holder.bindClickListener(position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(tool: Tool) {
            with(itemView) {
                tool_name_text_view.text = tool.name
                borrowed_tool_count_text_view.text = tool.borrowed_count.toString()
                setAvatarImage(context, tool.image_name, avatar_image_view)

                if (tool.is_selected) {
                    tool_view.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.colorPrimary
                        )
                    )
                    tool_name_text_view.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white_color
                        )
                    )
                    borrowed_tool_count_title_text_view.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white_color
                        )
                    )
                    borrowed_tool_count_text_view.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white_color
                        )
                    )
                } else {
                    tool_view.setCardBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white_color
                        )
                    )
                    tool_name_text_view.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primary_text_color
                        )
                    )
                    borrowed_tool_count_title_text_view.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.secondary_text_color
                        )
                    )
                    borrowed_tool_count_text_view.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.primary_text_color
                        )
                    )
                }
            }
        }

        fun bindClickListener(position: Int) {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(position)
            }
        }
    }

    fun setItems(toolsList: List<Tool>) {
        this.toolsList = toolsList
        notifyDataSetChanged()
    }

    fun getItem(position: Int): Tool {
        return toolsList[position]
    }

    fun setToolClickListener(itemClickListener: ItemClickListener) {
        this.itemClickListener = itemClickListener
    }
}