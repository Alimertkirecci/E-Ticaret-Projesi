package com.example.e_ticaret_projesi.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.e_ticaret_projesi.databinding.ActivityUploadBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class UploadActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage:FirebaseStorage
    private lateinit var binding: ActivityUploadBinding
    private lateinit var activityResultLauncher:ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedPicture : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityUploadBinding.inflate(layoutInflater)
        val view=binding.root

auth=Firebase.auth
        firestore=Firebase.firestore
        storage=Firebase.storage
        setContentView(view)
        registerLauncher()

    }//Child bir alta geçerken Parent bir üste geçerken
    fun YuklemeOnClckBtn(view : View){

        //universel unique ID
    val uuid=UUID.randomUUID()
        val imageName="$uuid.jpg" // BU kısım DA Javadan Kotline Geçen bir özellik Rastgele Atama yapar ki
        //Firebase de tek olarak kayıt yapan ve her kayıtta artma yapmayıp son kaydı alan özelliği yok etmiş olur
        val postMap = hashMapOf<String,Any>()//BUrda En kök  değeri çağırıyoruz.
//anahtar kalimelerin hepsi string Değerleri any yapıyıtuz
 val reference=storage.reference
      //  reference.putFile(selectedPicture)//Bu şekilde yarsak da olur ama koulandırma klasor içerinde olsun düzgün olsun dersek
  //Bu kısımda child , kapatıp da tekrar .child()olarak da açabiliriz.Ama Örnek olduğundan dolayı tek Child içersinde yapıyoruz.
  //şimdi putfile olarak istediğmiz dosyaları içersine bırakabaliriz.Seçili olanları
   val imageReference=reference.child("images").child(imageName)
if(selectedPicture!=null){
    imageReference.putFile(selectedPicture!!).addOnCanceledListener {
//Download Url - > FireStore
        val uploadPictureReference=storage.reference.child("images").child(imageName)
        uploadPictureReference.downloadUrl.addOnSuccessListener {


          val downloadUrl=it.toString()
            if (auth.currentUser!=null){
                val postMap= hashMapOf<String,Any>()
                postMap.put("downloadUrl",downloadUrl)
                postMap.put("userEmail",auth.currentUser!!.email!!)
                postMap.put("comment",binding.yorumtxt.text.toString())
                postMap.put("date",Timestamp.now())
                firestore.collection("E-Ticaret").add(postMap).addOnSuccessListener  {
                finish()


                }.addOnFailureListener{
                    Toast.makeText(this@UploadActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                }
            }


        }

    }.addOnFailureListener{
        Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
    }

}

    }
    fun SelectImage(view: View){
if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
    if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
        Snackbar.make(view,"Ürünlere Ulaşmak İçin İzin Gereklidir!!",Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver"){
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        //izin talep etmek
        }.show()

    }else
    {
        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        //izin talebi
    }
}else{
val intentToGalery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)//git ve bir veriyi oradan al
    activityResultLauncher.launch(intentToGalery)
    //Intenti başlat!!
}
    }
    private fun registerLauncher(){
        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if (result.resultCode== RESULT_OK){
                val intentFromResult=result.data
               if ( intentFromResult!=null){
                   selectedPicture=   intentFromResult!!.data
                   selectedPicture?.let {
                       binding.imageView.setImageURI(it)

                   }
               }
            }
       }
        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){result->
            if (result!==null){
                val intentToGalery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI!!)//git ve bir veriyi oradan al
                activityResultLauncher.launch(intentToGalery)
                //permission  granted

            }else{
                //permission defult
                Toast.makeText(this@UploadActivity,"İzin Gerekli!",Toast.LENGTH_LONG).show()
            }

        }
    }
}