package me.maxct.changer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class MainActivity : AppCompatActivity() {

    companion object {
        val requestWriteStorage = 0x00000010
        val filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "Android/data/jp.co.hit_point.tabikaeru/files/Tabikaeru.sav"
        var writable = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        btnChangeClover.setOnClickListener {
            if (inputClover.text.isNullOrBlank()) {
                showToast("未输入数量")
                return@setOnClickListener
            }
            val cloverNumber: Int

            try {
                cloverNumber = Integer.parseInt(inputClover.text.toString())
            } catch (e: NumberFormatException) {
                showToast("请输入数字")
                return@setOnClickListener
            }

            if (cloverNumber < 0 || cloverNumber > 0xffffffff) {
                showToast("三叶草数值超过限制")
                return@setOnClickListener
            } else {
                changeClover(cloverNumber)
            }
        }

        btnChangeTicket.setOnClickListener {
            if (inputTicket.text.isNullOrBlank()) {
                showToast("未输入数量")
                return@setOnClickListener
            }
            val ticketNumber: Int
            try {
                ticketNumber = Integer.parseInt(inputTicket.text.toString())
            } catch (e: NumberFormatException) {
                showToast("请输入数字")
                return@setOnClickListener
            }
            if (ticketNumber < 0 || ticketNumber > 0xff) {
                showToast("奖券数值超过限制")
                return@setOnClickListener
            } else {
                changeTicket(ticketNumber)
            }
        }
    }

    private fun changeClover(number: Int) {
        if (!writable) {
            requestPermission()
            return
        }

        /*val tmp = numberToByteArray(number)

        val ss = tmp.joinToString(separator = ",")

        Log.d("changer", ss)*/

        val f: File
        try {
            f = File(filePath)
            Log.d("changer", f.absolutePath)
        } catch (e: RuntimeException) {
            showToast("存档文件不存在")
            return
        }
        val fis = FileInputStream(f)

        val arr = ByteArray(fis.available())
        try {
            fis.read(arr)
        } catch (e: IOException) {
            showToast("读取存档文件错误")
            return
        } finally {
            fis.close()
        }

        /*
        修改数目
        val fos = FileOutputStream(f)
        try {
            arr[25] = 0xff.toByte()
            fos.write(arr)
        } catch (e : IOException) {
            showToast("写入文件错误")
            return
        } finally {
            fos.close()
        }
        */


        var s = ""
        arr.map { t -> s += String.format("%02x", t) }
        Log.d("changer", s)
        Log.d("changer", arr.joinToString(","))

    }

    private fun changeTicket(number: Number) {
        if (!writable) {
            requestPermission()
            return
        }
    }

    private fun showToast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    private fun requestPermission() {
        val hasPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        if (!hasPermission) {
            // ask the permission
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    requestWriteStorage
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestWriteStorage) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                writable = true
            }
        }
    }

    private fun numberToByteArray(number: Int): ByteArray {
        val arr = ByteArray(4)
        for (i in 0..3) {
            arr[i] = (0xff and number.shr(i * 8)).toByte()
        }
        return arr
    }
}
