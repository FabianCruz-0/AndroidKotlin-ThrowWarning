package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.item_user.*

class UsersActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    var usuarios = ArrayList<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        title = "Chats"
        auth = Firebase.auth


        db.collection("usuarios").get().addOnSuccessListener { result ->
            for (user in result)
            {
                var nombre = user.get("Nombre").toString()
                var email = user.get("Email").toString()

                if(!email.equals(auth.currentUser?.email) && !email.equals("void"))
                {
                    var user = User(nombre, email)
                    usuarios.add(user)
                }
            }
            initRecycler()
        }
    }

    fun initRecycler(){
        usersRV.layoutManager = LinearLayoutManager(this)
        val adapter = UserAdapter(usuarios)
        usersRV.adapter = adapter
        adapter.setOnItemClickListener(object:UserAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(this@UsersActivity,ChatActivity::class.java).apply{
                    putExtra("email",usuarios[position].email)
                }
                startActivity(intent)
            }
        })
    }
}