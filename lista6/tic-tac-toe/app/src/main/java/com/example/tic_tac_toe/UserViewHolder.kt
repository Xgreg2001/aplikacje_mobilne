package com.example.tic_tac_toe

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val nameView: TextView = view.findViewById(R.id.name)
    private val inviteButton: Button = view.findViewById(R.id.invite_button)

    fun bind(user: User, onInviteClicked: (User) -> Unit) {
        nameView.text = user.username
        inviteButton.setOnClickListener { onInviteClicked(user) }
    }
}