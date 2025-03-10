package com.example.marsphotos.data

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.example.marsphotos.network.DatabaseProvider

class rateContentProvaider : ContentProvider(){
    companion object {
        const val AUTHORITY = "com.example.marsphotos.provider"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/exchange_rate")
    }

    private lateinit var exchangeRateDao: DaoApiRate

    override fun onCreate(): Boolean {
        val database = DatabaseProvider.getDatabase(context!!)
        exchangeRateDao = database.exchangeRateDao()
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val baseCode = uri.getQueryParameter("baseCode") ?: return null
        val startDate = uri.getQueryParameter("startDate")?.toLongOrNull() ?: return null
        val endDate = uri.getQueryParameter("endDate")?.toLongOrNull() ?: return null

        val cursor = exchangeRateDao.getRates(baseCode, "MXN", startDate, endDate)
        cursor.setNotificationUri(context?.contentResolver, uri)
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.dir/$AUTHORITY.exchange_rate"
    }
}