package com.example.timesincebirth

import android.app.DatePickerDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import com.example.timesincebirth.databinding.ActivityMainBinding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.concurrent.timer

/**
 * 選択した日付と現在時刻の差分を計算するアプリ
 * 閏年・生年月日の出産時刻は考慮していないので厳密ではない
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val handler = Handler()
    private val dateFormat = DateTimeFormatter.ofPattern("HH時間mm分ss秒")
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectBirthDay.setOnClickListener {
            selectDate()
        }

        // 1秒ごとに選択した生年月日と現在時刻の差分を計算して
        // リアルタイム表示
        timer(name = "timeSinceBirth", period = 1000) {
            val selectDateTimeMillis: Long = calendar.timeInMillis
            val now = System.currentTimeMillis()

            // 画面に関する処理はメインスレッドで行う必要がある
            // Handlerを使ってメインスレッドに処理した内容を送ることができる
            handler.post {
                val diff = now - selectDateTimeMillis
                val date = diff / 1000 / 60 / 60 / 24
                val hour = diff / 1000 / 60
                val second = diff / 1000
                binding.timeSinceBirth.text = "${date}日${hour}時間${second}秒"
            }
        }
    }

    /**
     * 生年月日を選択
     */
    private fun selectDate() {
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener() { view, year, month, day ->
                binding.selectBirthDay.text = "${year}/${month + 1}/${day}"
                calendar.set(year, month, day)
            },
            2021,
            7,
            1
        )
        datePickerDialog.show()
    }
}