package com.example.e_ticaret_projesi.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter


import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_ticaret_projesi.R
import com.example.e_ticaret_projesi.adapter.FeedRecyclerAdapter
import com.example.e_ticaret_projesi.databinding.ActivityFeedBinding
import com.example.e_ticaret_projesi.model.post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FeedActivity : AppCompatActivity() {

    private lateinit var db:FirebaseFirestore
    private lateinit var binding :ActivityFeedBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var feedadapter: FeedRecyclerAdapter
    private lateinit var postArrayList:ArrayList<post>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
setContentView(binding.root)
        val user = arrayOf("comment","Erkekgym","KadinGym","Montkaban","Pc","Saat","downloadUrl")
        val userAdapter:ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1)





        binding= ActivityFeedBinding.inflate(layoutInflater)
val view=binding.root


        setContentView(view)
        auth=Firebase.auth
        db=Firebase.firestore
        postArrayList=ArrayList<post>()
getData()
        binding.recyclerview1.layoutManager=LinearLayoutManager(this)
        feedadapter= FeedRecyclerAdapter(postArrayList)
        binding.recyclerview1.adapter=feedadapter
    }
@SuppressLint("NotifyDataSetChanged")
private fun getData(){
    db.collection("Posts").addSnapshotListener { value, error ->
        if (error!=null){
            Toast.makeText( this,error.localizedMessage,Toast.LENGTH_LONG).show()
        }
else{
    if (value!=null){
        if (!value.isEmpty)//boş değer mi kontrolu
        {
         val documents=value.documents
            for (documents in documents){
                //casting
                val comment=documents.get("Elektronik") as String
                 val Erkekgym=documents.get("Erkek Giyim") as String
                val KadinGym=documents.get("Kadın Giyim") as String
                val Montkaban=documents.get("Mont Kaban") as String
                val Pc=documents.get("Acer") as String
                val Saat=documents.get("casio") as String
                val downloadUrl=documents.get("downloadUrl")as String
val Post=post(comment,Erkekgym,KadinGym, Montkaban ,Pc,Saat, downloadUrl)

                postArrayList.add(Post)
            }
            feedadapter.notifyDataSetChanged()
        }
    }
        }
    }
}







    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater=getMenuInflater()
        menuInflater.inflate(R.menu.eticaretmenu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       if (item.itemId == R.id.add_post){
           val intent=Intent(this, UploadActivity::class.java)
           startActivity(intent)//BUrada finish yapmıyoruz belki geri dönüş istenebilir.!
       }else if (item.itemId == R.id.signout){

auth.signOut()//Direk Çıkış yapar sunucuya Sormaya bile gerek yok

           val intent=Intent(this, MainActivity::class.java)
           startActivity(intent)
           finish()
       }

        return super.onOptionsItemSelected(item)
    }
}