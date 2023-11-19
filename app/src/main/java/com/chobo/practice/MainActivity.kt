package com.chobo.practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import java.lang.Math.abs
import java.util.*

class MainActivity : AppCompatActivity() {
    var p_num = 0
    var k = 1
    val point_list = mutableListOf<Float>()
    var isBlind = false

    fun start(){
        setContentView(R.layout.activity_start)

        MobileAds.initialize(this) {}
        val adView: AdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        val tv_pnum: TextView = findViewById(R.id.tv_pnum)
        val btn_minus: Button = findViewById(R.id.btn_minus)
        val btn_plus: Button = findViewById(R.id.btn_plus)
        val btn_start: Button = findViewById(R.id.btn_start)
        val btn_blind: Button = findViewById(R.id.btn_blind)

        btn_blind.setOnClickListener {
            isBlind = !isBlind
            if (isBlind == true) {
                btn_blind.text = "Blind 모드 ON"
            }else {
                btn_blind.text = "Blind 모드 OFF"
            }
        }

        tv_pnum.text = p_num.toString()
        btn_minus.setOnClickListener {
            p_num--
            if (p_num == 0) {
                p_num = 1
            }
            tv_pnum.text = p_num.toString()
        }
        btn_plus.setOnClickListener {
            p_num++
            tv_pnum.text = p_num.toString()
        }
        btn_start.setOnClickListener {
            main()
        }
    }


    fun main() {
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}
        val adView4: AdView = findViewById(R.id.adView4)
        val adRequest = AdRequest.Builder().build()
        adView4.loadAd(adRequest)

        var timerTask: Timer? = null
        var stage = 1
        var sec: Int = 0
        val tv: TextView = findViewById(R.id.tv_ramdom)
        val tv_t: TextView = findViewById(R.id.tv_pnum)
        val tv_p: TextView = findViewById(R.id.tv_point)
        val tv_people: TextView = findViewById(R.id.tv_people)
        val btn: Button = findViewById(R.id.btn_start)
        val btn_i: Button = findViewById(R.id.btn_i)
        val random_box = Random()
        val num = random_box.nextInt(1001)

        tv.text = ((num.toFloat()) / 100).toString()
        btn.text = "시작"
        tv_people.text = "참가자 $k"

        btn_i.setOnClickListener {
            point_list.clear()
            k = 1
            start()
        }

        btn.setOnClickListener {
            stage++
            if (stage == 2) {
                timerTask = kotlin.concurrent.timer(period = 10) {
                    sec++
                    runOnUiThread {
                        if(isBlind == false) {
                            tv_t.text = (sec.toFloat() / 100).toString()
                        } else if(isBlind == true && stage == 2) {
                            tv_t.text = "?????"
                        }
                    }
                }
                btn.text = "정지"
            } else if (stage == 3) {
                tv_t.text = (sec.toFloat() / 100).toString()
                timerTask?.cancel()
                val point = abs(sec - num).toFloat() / 100
                point_list.add(point)
                tv_p.text = point.toString()
                btn.text = "다음"
                stage = 0
            } else if (stage == 1) {
                if (k < p_num) {
                    k++
                    main()
                } else {
                    end()
                }
            }
        }
    }

    fun end() {
        setContentView(R.layout.activity_end)

        MobileAds.initialize(this) {}
        val adView2: AdView = findViewById(R.id.adView2)
        val adRequest = AdRequest.Builder().build()
        adView2.loadAd(adRequest)


        val tv_last: TextView = findViewById(R.id.tv_last)
        val tv_lpoint: TextView = findViewById(R.id.tv_lpoint)
        val btn_init: Button = findViewById(R.id.btn_init)

        tv_lpoint.text = (point_list.maxOrNull()).toString()
        var index_last = point_list.indexOf(point_list.maxOrNull())
        tv_last.text = "참가자 "+(index_last+1).toString()

        btn_init.setOnClickListener {
            point_list.clear()
            k = 1
            start()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        start()
    }
}
