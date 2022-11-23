package com.alirezabashi98.pdfviewer

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.alirezabashi98.pdfviewer.databinding.ActivityMainBinding
import com.github.barteksc.pdfviewer.PDFView
import java.io.BufferedInputStream
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity(), LoadPdf {

    private lateinit var binding: ActivityMainBinding

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

        this.onLoad(binding.turnDarkModeOn.isChecked)

        binding.turnDarkModeOn.setOnClickListener {
            this.onReload(binding.turnDarkModeOn.isChecked)
        }

        // on below line we are calling our async
        // task to load our pdf file from url.
        // we are also passing our pdf view to
        // it along with pdf view url.
//        RetrievePDFFromURL(binding.pdfView,this,binding.turnDarkModeOn.isChecked).execute(pdfUrl)

//        val file = File("/sdcard/Android/data/com.alirezabashi98.pdfviewer/files/x.pdf")
//        val file2 = getExternalFilesDir("x.pdf")
//
//        binding.pdfView.fromFile(file2)
//            .enableAnnotationRendering(true)
//            .nightMode(binding.turnDarkModeOn.isChecked)
//            .onPageChange { page, pageCount ->
//                binding.numberPage.text = "${page+1} / $pageCount"
//            }
//            .load()


    }

    // on below line we are creating a class for
    // our pdf view and passing our pdf view
    // to it as a parameter.
    class RetrievePDFFromURL(pdfView: PDFView, val context: Context, val darkModel: Boolean,val viewPage:TextView) :
        AsyncTask<String, Void, InputStream>() {

        // on below line we are creating a variable for our pdf view.
        val mypdfView: PDFView = pdfView

        // on below line we are calling our do in background method.
        override fun doInBackground(vararg params: String?): InputStream? {
            // on below line we are creating a variable for our input stream.
            var inputStream: InputStream? = null
            try {
                // on below line we are creating an url
                // for our url which we are passing as a string.
                val url = URL(params.get(0))

                // on below line we are creating our http url connection.
                val urlConnection: HttpURLConnection = url.openConnection() as HttpsURLConnection

                // on below line we are checking if the response
                // is successful with the help of response code
                // 200 response code means response is successful
                if (urlConnection.responseCode == 200) {
                    // on below line we are initializing our input stream
                    // if the response is successful.
                    inputStream = BufferedInputStream(urlConnection.inputStream)
                }
            }
            // on below line we are adding catch block to handle exception
            catch (e: Exception) {
                // on below line we are simply printing
                // our exception and returning null
                e.printStackTrace()
                return null;
            }
            // on below line we are returning input stream.
            return inputStream;
        }

        // on below line we are calling on post execute
        // method to load the url in our pdf view.
        override fun onPostExecute(result: InputStream?) {
            // on below line we are loading url within our
            // pdf view on below line using input stream.
            mypdfView.fromStream(result)
                .enableAnnotationRendering(true)
                .nightMode(darkModel)
                .onPageChange { page, pageCount ->
                    viewPage.text = "${page+1} / $pageCount"
                }
                .load()

        }
    }

    override fun onLoad(darkMode: Boolean) {
        RetrievePDFFromURL(binding.pdfView, this, darkMode,binding.numberPage).execute(pdfUrl)
    }

    override fun onReload(darkMode: Boolean) {
        RetrievePDFFromURL(binding.pdfView, this, darkMode,binding.numberPage).execute(pdfUrl)
    }
}