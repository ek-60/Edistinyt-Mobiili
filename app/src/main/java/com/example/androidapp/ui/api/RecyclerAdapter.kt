package com.example.androidapp.ui.api

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.androidapp.data.comment.Comment
import com.example.androidapp.databinding.RecyclerviewItemRowBinding

class RecyclerAdapter(private val comment: List<Comment>) :
    RecyclerView.Adapter<RecyclerAdapter.CommentHolder>() {

    //Alustetaan bindig layout -> recyclerview_item_row.xml
    private var _binding: RecyclerviewItemRowBinding? = null
    private val binding get() = _binding!!

    //RecyclerAdapter vaatti että luokassa on toteutettuna:
    //getItemCount, onCreateView, OnBindViewHolder

    //Jotta RecyclerView tietää kuinka monta itemiä listassa on
    override fun getItemCount() = comment.size

    //Tämä funktio kytkee jokaisen yksittäisen
    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        val itemComment = comment[position]
        holder.bindComment(itemComment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentHolder {
        _binding = RecyclerviewItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentHolder(binding)
    }

    class CommentHolder(v: RecyclerviewItemRowBinding) : RecyclerView.ViewHolder(v.root), View.OnClickListener {

        private var view: RecyclerviewItemRowBinding = v
        private var comment: Comment? = null

        init {
            v.root.setOnClickListener(this)
        }

        fun bindComment(comment: Comment) {
            //Laitetaan Comment olio talteen jotta pääsemme siihen helpommin käsiksi esim onClickissä
            this.comment = comment

            //Jos kommentin nimi on liian pitkä, lyhennetään
            var commentName = comment.name as String

            if (commentName.length > 20) {
                commentName = commentName.substring(0, 20) + "..."
            }

            view.textViewCommentTitle.text = comment.name
            view.textViewCommentContent.text = comment.body
            view.textViewCommentEmail.text = comment.email
        }

        override fun onClick(v: View) {
            Log.d("RecyclerAdapter: ", "ID: " + comment?.id.toString() + " Name: " +  comment?.name.toString())
            //HUOM: comment.id on int?, minkä vuoksi meidän pitää muuntaa se vielä Int- muotoon
            val action = ApiFragmentDirections.actionApiFragmentToApiDetailFragment(comment?.id as Int)
            v.findNavController().navigate(action)
        }

        companion object {

            //private val PHOTO_KEY = "PHOTO"
        }
    }

}