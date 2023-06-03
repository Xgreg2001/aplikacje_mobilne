package com.example.tic_tac_toe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



class MainActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance("https://tic-tac-toe-cb54d-default-rtdb.europe-west1.firebasedatabase.app")
        auth = FirebaseAuth.getInstance()

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        adapter = UserAdapter { user -> user.userId?.let { sendInvite(it) } }
        recyclerView.adapter = adapter

        registerActiveUser()
        checkIfCurrentlyInGame()
        listenForActiveUsers()
        listenForInvites()
    }

    private fun registerActiveUser() {
        val userId = auth.currentUser!!.uid
        val activeUsersRef = database.getReference("active_users")
        activeUsersRef.child(userId).setValue(true)
        activeUsersRef.child(userId).onDisconnect().removeValue()
    }

    private fun checkIfCurrentlyInGame() {
        val userId = auth.currentUser!!.uid
        val userRef = database.getReference("users").child(userId)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                if (user?.currentGame != null) {
                    val intent = Intent(this@MainActivity, GameActivity::class.java)
                    intent.putExtra("GAME_ID", user.currentGame)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
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
        val usersRef = database.getReference("users")
        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val users = dataSnapshot.children.mapNotNull { it.getValue(User::class.java) }
                var activeUsers = users.filter { activeUserIds.contains(it.userId) }

                // remove current user from list
                activeUsers = activeUsers.filter { it.userId != auth.currentUser!!.uid }

                // remove users that are already in a game
                activeUsers = activeUsers.filter { it.currentGame == null }

                adapter.users = activeUsers
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })
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
        // get sender username
        val usersRef = database.getReference("users")
        usersRef.child(invite.senderId!!).get().addOnSuccessListener {
            val senderUsername = it.child("username").value as String

            AlertDialog.Builder(this)
                .setTitle("Game Invite")
                .setMessage("You have received a game invite from ${senderUsername}. Do you want to accept?")
                .setPositiveButton("Accept") { _, _ ->
                    declineInvite(inviteId)
                    startGame(invite.senderId!!)
                }
                .setNegativeButton("Decline") { _, _ ->
                    declineInvite(inviteId)
                }
                .show()
        }


    }

    private fun startGame(opponentId: String) {
        val newGame = GameState(auth.currentUser!!.uid, opponentId)
        val newGameId = database.getReference("games").push().key!!
        database.getReference("games").child(newGameId).setValue(newGame)

        val userRef = database.getReference("users")
        userRef.child(auth.currentUser!!.uid).child("currentGame").setValue(newGameId)
        userRef.child(opponentId).child("currentGame").setValue(newGameId)

        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("GAME_ID", newGameId)
        startActivity(intent)
    }

    private fun declineInvite(inviteId: String) {
        val invitesRef = database.getReference("invites")
        invitesRef.child(inviteId).removeValue()
    }
}



