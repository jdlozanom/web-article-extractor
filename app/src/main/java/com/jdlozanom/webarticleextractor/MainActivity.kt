package com.jdlozanom.webarticleextractor

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.URLUtil
import androidx.appcompat.app.AppCompatActivity
import com.bitly.Bitly
import com.bitly.Response
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup


class MainActivity : AppCompatActivity() {
    private val BITLY_TOKEN = ""

    private var loadingCount = 0

    private var headline = ""
    private var subtitle = ""
    private var articleBody = ""
    private var link = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Bitly.initialize(this, BITLY_TOKEN)

        setContentView(R.layout.activity_main)
        extractButton.setOnClickListener { parseURL() }
        copyButton.setOnClickListener { copyToClipboard() }


    }

    private fun parseURL() {
        val urlString = urlEditText.text.toString()
        if (!URLUtil.isValidUrl(urlString)) return


        class GetArticleTask : AsyncTask<Void, Void, Void>() {

            override fun onPreExecute() {
                super.onPreExecute()
                addLoadingIndicator()
            }

            override fun doInBackground(vararg arg0: Void): Void? {
                val doc = Jsoup.connect(urlString).get()

                val headlineDoc = doc.select("")
                val subtitleDoc = doc.select("")
                val articleBodyDoc = doc.select("")

                if (headlineDoc.size > 0) headline = headlineDoc[0].wholeText()
                if (subtitleDoc.size > 0) subtitle = subtitleDoc[0].wholeText()
                if (articleBodyDoc.size > 0) articleBody = articleBodyDoc[0].wholeText()

                return null
            }

            override fun onPostExecute(result: Void?) {
                headlineTextView.text = headline
                subheadlineTextView.text = subtitle
                articleTextView.text = articleBody
                removeLoadingIndicator()
            }

        }

        GetArticleTask().execute()

        if (shortenerSwitch.isChecked) {
            addLoadingIndicator()
            Bitly.shorten(urlString, object : Bitly.Callback {
                override fun onResponse(response: Response) {
                    link = response.bitlink
                    linkTextView.text = response.bitlink

                    removeLoadingIndicator()
                }

                override fun onError(p0: com.bitly.Error?) {
                    if (p0 != null) {
                        Log.d(
                            "Bitly shorten error",
                            p0.errorMessage
                        )
                    }
                }
            })
        }
    }

    private fun copyToClipboard() {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip: ClipData =
            ClipData.newPlainText("extract", headline + subtitle + articleBody + link)
        clipboard.setPrimaryClip(clip)

    }

    private fun addLoadingIndicator() {
        loadingCount++
        progressBar.visibility = View.VISIBLE
    }

    private fun removeLoadingIndicator() {
        loadingCount--
        if (loadingCount == 0) progressBar.visibility = View.GONE
    }
}

