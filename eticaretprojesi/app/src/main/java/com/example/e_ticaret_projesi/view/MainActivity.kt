package com.example.e_ticaret_projesi.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.e_ticaret_projesi.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view=binding.root

        setContentView(view)
        auth=Firebase.auth
        val currentUser=auth.currentUser//Giriş Yapan Kullanıcıyı dinlememizi sağlıyor.
        if(currentUser!=null){
            val intent=Intent(this, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }


    }
   fun girisInClicked (view:View){
 val email=binding.emailtext.text.toString()
       val password=binding.passwordText.text.toString()
       if (email.equals("")||password.equals("")){
           Toast.makeText(this,"Lütfen email ve Şifre Giriniz!",Toast.LENGTH_LONG).show()
       }else
       {
           auth.signInWithEmailAndPassword(email,password)
               .addOnSuccessListener {
               val intent=Intent(this@MainActivity, FeedActivity::class.java)
               startActivity(intent)
               finish()//Kullanıcının Tekrar Bu kısma Dönmesine gerek kalmayacak.
           }

               .addOnFailureListener {
               Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
           }
       }

   }
    fun kydlInClıcked(view: View){
 val email=binding.emailtext.text.toString()
        val password=binding.passwordText.text.toString()
        if (email.equals("") || password.equals("")){
            Toast.makeText(this,"Hatalı Veya Eksik Giriş",Toast.LENGTH_LONG).show()

            }else{
                auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {
                    //Sadece Success olduğunda çağırıyoruz.
val intent=Intent(this@MainActivity, FeedActivity::class.java)
                    startActivity(intent)
                    finish()//Kullanıcının Tekrar Bu kısma Dönmesine gerek kalmayacak.
                }.addOnFailureListener{
                    Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                }
        }
       //if (email.isNotEmpty()&&password.isNotEmpty()){



    }
}