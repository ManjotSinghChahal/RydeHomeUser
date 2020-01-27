package com.example.rydehomeuser.utils

import android.content.Context
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder
import java.util.ArrayList



class PlaceAutoCompleteAdapter(context: Context, textViewResourceId: Int) :
    ArrayAdapter<Any>(context, textViewResourceId),
    Filterable {
    private val mAdapterCallBack: PlaceList
    private var resultList: ArrayList<Array<String>>? = null

    var CURRENT_LAT = "30.7046"
    var CURRENT_LONG = "76.7179"

    init {
        this.mAdapterCallBack = context as PlaceList
    }

    override fun getCount(): Int {
        return if (resultList != null)
            resultList!!.size
        else
            0
    }

    override fun getItem(index: Int): String? {
        return resultList!![index][0]
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    resultList = autocomplete(constraint.toString())
                    mAdapterCallBack.getPlaceList(resultList)
                    // Assign the data to the FilterResults
                    filterResults.values = resultList
                    filterResults.count = resultList!!.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }

    fun autocomplete(input: String): ArrayList<Array<String>>? {
        var resultList: ArrayList<Array<String>> = ArrayList()

        var conn: HttpURLConnection? = null
        val jsonResults = StringBuilder()
        try {
            val sb = StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON)
            sb.append("?key=AIzaSyBfffgfmuX2uumW09fqJZKbEHOnPqSPzaE")
            sb.append("&input=" + URLEncoder.encode(input, "utf8"))
            sb.append(
                "&location=" + CURRENT_LAT + "," + CURRENT_LONG)

            val url = URL(sb.toString())

            println("URL: $url")
            conn = url.openConnection() as HttpURLConnection
            val `in` = InputStreamReader(conn.inputStream)


            var read: Int
            val buff = CharArray(1024)



         //   while ((read = `in`.read(buff)) != -1) {
            while ( `in`.read(buff).also { read = it } != -1) {
                jsonResults.append(buff, 0, read)
            }
        } catch (e: MalformedURLException) {
            Log.e("Error", "Error processing Places API URL", e)
            return resultList
        } catch (e: IOException) {
            Log.e("Error", "Error connecting to Places API", e)
            return resultList
        } finally {
            conn?.disconnect()
        }

        try {
            // Create a JSON object hierarchy from the results
            Log.e("predictionsssssss", jsonResults.toString())
            val jsonObj = JSONObject(jsonResults.toString())
            val predsJsonArray = jsonObj.getJSONArray("predictions")

            // Extract the Place descriptions from the results
            resultList = ArrayList(predsJsonArray.length())

            for (i in 0 until predsJsonArray.length()) {
                val placeId = arrayOf<String>()
                Log.e(
                    "results",
                    "results " + predsJsonArray.getJSONObject(i).getString("description") + "  " + predsJsonArray.getJSONObject(
                        i
                    ).getString("place_id")
                )
                placeId[0] = predsJsonArray.getJSONObject(i).getString("description")
                placeId[1] = predsJsonArray.getJSONObject(i).getString("place_id")
                resultList.add(placeId)
            }
        } catch (e: JSONException) {
            Log.e("Error", "Cannot process JSON results", e)
        }

        return resultList
    }

    interface PlaceList {
        fun getPlaceList(resultList: ArrayList<Array<String>>?)
    }

    companion object {
        private val PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place"
        private val TYPE_AUTOCOMPLETE = "/autocomplete"
        private val OUT_JSON = "/json"
        private val PLACES_DETAIL_API_BASE = "https://maps.googleapis.com/maps/api/place"
    }

}
