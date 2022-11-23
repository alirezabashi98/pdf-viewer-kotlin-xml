package com.alirezabashi98.pdfviewer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.alirezabashi98.pdfviewer.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity(), LoadPdf {

    private lateinit var binding: ActivityMainBinding
    private var statePage: Int = 0

    // on below line we are creating a variable for our pdf view url.
    var pdfUrl = "https://nemodaan.com/dl/13991113pq82537755.pdf"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // user cannot screenshot
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        );

        onlinePDF()

        binding.turnDarkModeOn.setOnClickListener {
            onlinePDF()
        }

    }

    // on below line we are calling our async
    // task to load our pdf file from url.
    // we are also passing our pdf view to
    // it along with pdf view url.
    private fun onlinePDF() {
        RetrievePDFFromURL(
            binding.pdfView,
            statePage,
            binding.turnDarkModeOn.isChecked,
            this
        ).execute(pdfUrl)
    }

    private fun offlinePDF() {
        val file = File("/sdcard/Android/data/com.alirezabashi98.pdfviewer/files/x.pdf")
        val file2 = getExternalFilesDir("x.pdf")

        binding.pdfView.fromFile(file2)
            .enableAnnotationRendering(true)
            .nightMode(binding.turnDarkModeOn.isChecked)
            .defaultPage(statePage)
            .onPageChange { page, pageCount ->
                this.onPage(page + 1, pageCount)
            }
            .load()
    }

    @SuppressLint("SetTextI18n")
    override fun onPage(page: Int, pageCount: Int) {
        binding.numberPage.text = "$page / $pageCount"
        statePage = page - 1
    }


}