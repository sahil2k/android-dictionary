package com.example.dictionary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dictionary.models.DisctionaryMainModel
import com.google.gson.Gson
import kotlinx.coroutines.*
import org.json.JSONArray
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    val mCoroutineScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun findWord(view: View) {
        var textbox: TextView = findViewById(R.id.input_text)
        Log.d("mainactivity", "button pressed")
        val searchUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + textbox.text.toString()
        Log.d("mainactivity", "searchUrl is " + searchUrl)
//        val asynctask = async()
//        asynctask.execute(searchUrl)
        mCoroutineScope.launch {
            val httpResponse = getHttpResponse(createUrl(searchUrl))
            val dictionaryMainModel = extractFeatureFromJson(httpResponse)
            withContext(Dispatchers.Main) {
                dictionaryMainModel?.meanings?.let {
                    showDefinition(it[0].definitions?.get(0)?.definition)
                }
            }
        }
//        GlobalScope.launch(Dispatchers.IO) {
//            val httpResponse = getHttpResponse(createUrl(searchUrl))
//            val dictionaryMainModel = extractFeatureFromJson(httpResponse)
//            withContext(Dispatchers.Main){
//                dictionaryMainModel?.meanings?.let {
//                    showDefinition(it[0].definitions?.get(0)?.definition)
//                }
//            }
//        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mCoroutineScope.cancel()
    }
//    inner class async : AsyncTask<String, Void, DisctionaryMainModel>() {
//        override fun doInBackground(vararg p0: String?): DisctionaryMainModel? {
//            var url: URL? = null
//            url = createUrl(p0[0])
//            var jsonResponse: String? = null
//            //jsonResponse = getHttpResponse(url)
//            return extractFeatureFromJson(jsonResponse)
//        }
//
//        override fun onPostExecute(mainModel: DisctionaryMainModel?) {
//            super.onPostExecute(mainModel)
//            if (mainModel == null) {
//                return
//            }
//
//            mainModel.meanings?.let {
//                showDefinition(it[0].definitions?.get(0)?.definition)
//            }
//
//        }
//
//
//
//    }

    private fun showDefinition(definition: String?) {
        val intent = Intent(applicationContext, DefinitionActivity::class.java)
        intent.putExtra("my definition", definition)
        startActivity(intent)

    }

    fun getHttpResponse(url: URL?): String? {
        var jsonResponse: String = ""
        var urlConnection: HttpURLConnection? = null
        urlConnection = url?.openConnection() as HttpURLConnection
        var inputStream: InputStream? = null
        try {
            urlConnection.requestMethod = "GET"
            urlConnection.setRequestProperty("Accept", "Application/json")
            urlConnection.readTimeout = 10000
            urlConnection.connectTimeout = 15000

            urlConnection.connect()
            if (urlConnection.responseCode == 200) {
                Log.d("mainactivity", "connection succesful")
                inputStream = urlConnection.inputStream
                jsonResponse = readFromStream(inputStream)
                if (inputStream == null) {
                    Log.d("mainactivity", "inputstream is null")
                }
            } else {
                Log.d("mainactivity", "connection failed")
            }
        } catch (e: IOException) {
            Log.e("mainactivity", "conneciton failed")
        }
        urlConnection.disconnect()
        inputStream?.close()
        return jsonResponse

    }

    private fun extractFeatureFromJson(jsonResponse: String?): DisctionaryMainModel? {
        val baseJson = JSONArray(jsonResponse)
        val jsonObject = baseJson.getJSONObject(0)
        return Gson().fromJson(jsonObject.toString(), DisctionaryMainModel::class.java)
    }

//    fun extractFeatureFromJson(jsonResponse: String?): Data? {
//        var data:Data?=Data("nothing here")
//        try{
//            val baseJson=JSONArray(jsonResponse)
//            val resultArray=baseJson.getJSONObject(0)
//            val meaningArray=resultArray.getJSONArray("meanings")
//            //Log.d("mainactivity","meaningarray is "+meaningArray.toString())
//            val defArray=meaningArray.getJSONObject(0)
//            //Log.d("mainactivity","defArrayarray is "+defArray.toString())
//            val innerDefArray=defArray.getJSONArray("definitions")
//            //Log.d("mainactivity","innerdefArrayarray is "+innerDefArray.toString())
//            val finArray=innerDefArray.getJSONObject(0)
//            //Log.d("mainactivity","finArrayarray is "+finArray.toString())
//            val str1:String=finArray.optString("definition")
//            //Log.d("mainactivity", "string is" +str1)
//            data?.definition=str1
//
//
//        }catch(e:JSONException){
//            Log.d("mainactivity","error in parsing json"+e.toString())
//
//        }
//        Log.d("mainactivity","data is "+data?.definition)
//        return data
//
//    }

    fun readFromStream(inputStream: InputStream?): String {
        var output = StringBuilder()
        //val str:String="string"
        val charset: Charset = Charset.forName("UTF-8")
        if (inputStream != null) {
            var inputReader = InputStreamReader(inputStream, charset)
            val reader = BufferedReader(inputReader)
            var line = reader.readLine()
            Log.d("mainactivity", "line is " + line)
            output.append(line)
            while (reader.readLine() != null) {
                Log.d("mainactivity", "line is " + line)
                output.append(line)
                line = reader.readLine()
            }

        }
        return output.toString()

    }

    fun createUrl(stringUrl: String?): URL? {
        var url: URL? = null
        try {
            url = URL(stringUrl)
        } catch (exception: MalformedURLException) {
            Log.d("MainActivity", "ERROR: COULD NOT CREATE URL")
        }

        return url
    }
}