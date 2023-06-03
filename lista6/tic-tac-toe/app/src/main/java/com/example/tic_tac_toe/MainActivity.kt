package com.example.tic_tac_toe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        val listView = findViewById<ListView>(R.id.listView)
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        listView.adapter = adapter

        registerActiveUser()
        listenForActiveUsers()
        listenForInvites()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterActiveUser()
    }

    private fun registerActiveUser() {
        val userId = auth.currentUser!!.uid
        val activeUsersRef = database.getReference("active_users")
        activeUsersRef.child(userId).setValue(true)
        activeUsersRef.child(userId).onDisconnect().removeValue()
    }

    private fun unregisterActiveUser() {
        val userId = auth.currentUser!!.uid
        val activeUsersRef = database.getReference("active_users")
        activeUsersRef.child(userId).removeValue()
    }

    private fun listenForActiveUsers() {
        val activeUsersRef = database.getReference("active_users")
        activeUsersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val activeUserIds = dataSnapshot.children.map { it.key!! }
                updateActiveUserList(activeUserIds)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    private fun updateActiveUserList(activeUserIds: List<String>) {
        adapter.clear()
        for (userId in activeUserIds) {
            if (userId != auth.currentUser!!.uid) {
                adapter.add(userId)
            }
        }
    }

    private fun sendInvite(receiverId: String) {
        val invitesRef = database.getReference("invites")
        val senderId = auth.currentUser!!.uid
        invitesRef.push().setValue(Invite(senderId, receiverId))
    }

    private fun listenForInvites() {
        val invitesRef = database.getReference("invites")
        invitesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (inviteSnapshot in dataSnapshot.children) {
                    val invite = inviteSnapshot.getValue(Invite::class.java)!!
                    if (invite.receiverId == auth.currentUser!!.uid) {
                        // You've received an invite!
                        showInviteDialog(invite, inviteSnapshot.key!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
    }

    private fun showInviteDialog(invite: Invite, inviteId: String) {
        AlertDialog.Builder(this)
            .setTitle("Game Invite")
            .setMessage("You have received a game invite from ${invite.senderId}. Do you want to accept?")
            .setPositiveButton("Accept") { _, _ ->
                startGame(invite.senderId!!)
                declineInvite(inviteId)
            }
            .setNegativeButton("Decline") { _, _ ->
                declineInvite(inviteId)
            }
            .show()
    }

    private fun startGame(opponentId: String) {
        val newGame = GameState(auth.currentUser!!.uid, opponentId)
        val newGameId = database.getReference("games").push().key!!
        database.getReference("games").child(newGameId).setValue(newGame)

        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("GAME_ID", newGameId)
        startActivity(intent)
    }

    private fun declineInvite(inviteId: String) {
        val invitesRef = database.getReference("invites")
        invitesRef.child(inviteId).removeValue()
    }
}

