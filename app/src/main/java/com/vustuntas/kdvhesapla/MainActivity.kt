package com.vustuntas.kdvhesapla

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.core.widget.addTextChangedListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.vustuntas.kdvhesapla.databinding.ActivityMainBinding
import java.lang.Exception
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var tutar : Double? = null
    private var kdv : Double? = null

    private var kdvDahil : Boolean? = null

    private lateinit var editTutarDegisiklikler : TextWatcher
    private lateinit var editKdvDegisiklikler : TextWatcher

    private var colorsRadioArray = arrayOf("#38FF00","#FF0000")

    private lateinit var asagidanYukariAnim : Animation
    private lateinit var yukaridanAsagiAnim : Animation

    private lateinit var banner1 : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        tutar = binding.tutarEditText.text.toString().toDoubleOrNull()
        kdv = binding.kdvEditText.text.toString().toDoubleOrNull()

        editKdvDegisiklikler = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    kdv = p0.toString().toDoubleOrNull()
                }
                catch (E:Exception){
                    println(E.localizedMessage)
                    kdv = 0.0
                }            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    kdv = p0.toString().toDoubleOrNull()
                }
                catch (E:Exception){
                    println(E.localizedMessage)
                    kdv = 0.0
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                try {
                    kdv = p0.toString().toDoubleOrNull()
                }
                catch (E:Exception){
                    println(E.localizedMessage)
                    kdv = 0.0
                }
            }

        }

        editTutarDegisiklikler = object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    tutar = p0.toString().toDoubleOrNull()
                }
                catch (E:Exception){
                    println(E.localizedMessage)
                    tutar = 0.0
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    tutar = p0.toString().toDoubleOrNull()
                }
                catch (E:Exception){
                    println(E.localizedMessage)
                    tutar = 0.0
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                try {
                    tutar = p0.toString().toDoubleOrNull()
                }
                catch (E:Exception){
                    println(E.localizedMessage)
                    tutar = 0.0
                }
            }

        }


        binding.tutarEditText.addTextChangedListener(editTutarDegisiklikler)
        binding.kdvEditText.addTextChangedListener(editKdvDegisiklikler)
        binding.kdvDurumRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            if(i == R.id.kdvDahil_radioButton){
                kdvDahil = true
                binding.kdvDurumuTextView.text = "KDV Dahil"
                binding.kdvDurumuTextView.setTextColor(Color.parseColor(colorsRadioArray.get(0)))
            }


            else if(i == R.id.kdvHaric_radioButton){
                kdvDahil = false
                binding.kdvDurumuTextView.text = "KDV Hariç"
                binding.kdvDurumuTextView.setTextColor(Color.parseColor(colorsRadioArray.get(1)))
            }

        }

        asagidanYukariAnim = AnimationUtils.loadAnimation(this@MainActivity,R.anim.asagidan_yukari_buton)
        yukaridanAsagiAnim = AnimationUtils.loadAnimation(this@MainActivity,R.anim.yukaridan_asagi_baslik)
        binding.textView.animation = asagidanYukariAnim
        binding.hesaplaButton.animation = yukaridanAsagiAnim


        MobileAds.initialize(this) {}

        banner1 = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        banner1.loadAd(adRequest)
    }

    fun kdvHesapla(view: View){
        //Değer girildikten sonra klavyeyi kapatmak için
        var inputMethodManager: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken,0)

        var decimalFormetter = DecimalFormat("###,###.##")

        if(tutar != null && kdv != null){
            if(kdvDahil != null){
                if(kdvDahil!!){
                    var kdvDahilIslemTutari = tutar!!.div(1 + kdv!!.div(100))
                    var kdvDahilKdvTutari = tutar!! - kdvDahilIslemTutari

                    binding.toplamTutarEditText.setText(decimalFormetter.format(tutar))
                    binding.kdvTutariEditText.setText(decimalFormetter.format(kdvDahilKdvTutari))
                    binding.islemTutariEditText.setText(decimalFormetter.format(kdvDahilIslemTutari))

                }
                else{
                    var kdvHaricKdvTutari = tutar!! * (kdv!!.div(100))
                    var kdvHaricToplamTutar = tutar!! + kdvHaricKdvTutari

                    binding.islemTutariEditText.setText(decimalFormetter.format(tutar))
                    binding.kdvTutariEditText.setText(decimalFormetter.format(kdvHaricKdvTutari))
                    binding.toplamTutarEditText.setText(decimalFormetter.format(kdvHaricToplamTutar))

                }
            }
        }

    }

    fun yuzdeYirmi(view:View){
        binding.kdvEditText.setText(20.toString())

    }
    fun yuzdeOn(view:View){
        binding.kdvEditText.setText(10.toString())

    }
    fun yuzdeBir(view:View){
        binding.kdvEditText.setText(1.toString())

    }
}