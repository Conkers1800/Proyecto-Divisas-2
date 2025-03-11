package com.example.marsphotos.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.marsphotos.network.DatabaseProvider

private const val AUTHORITY = "com.example.marsphotos.provider"
private const val CURRENCIES_CODE = 1
private const val TARGET_CURRENCIES_CODE = 2
private const val EXCHANGE_RATE_CODE = 3

private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
    addURI(AUTHORITY, "currencies", CURRENCIES_CODE)
    addURI(AUTHORITY, "target_currencies", TARGET_CURRENCIES_CODE)
    addURI(AUTHORITY, "exchange_rate", EXCHANGE_RATE_CODE)
}

class rateContentProvider : ContentProvider() {
    private lateinit var database: DaoApiRate.ExchangeRateDatabase

    override fun onCreate(): Boolean {
        database = DatabaseProvider.getDatabase(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            CURRENCIES_CODE -> database.exchangeRateDao().getDistinctCurrencies()
            TARGET_CURRENCIES_CODE -> database.exchangeRateDao().getDistinctTargetCurrencies()
            EXCHANGE_RATE_CODE -> {
                val baseCode = uri.getQueryParameter("baseCode")
                val targetCode = uri.getQueryParameter("targetCode")
                val startDate = uri.getQueryParameter("startDate")?.toLongOrNull()
                val endDate = uri.getQueryParameter("endDate")?.toLongOrNull()

                if (baseCode == null || targetCode == null || startDate == null || endDate == null) {
                    return null
                }

                database.exchangeRateDao().getRates(baseCode, targetCode, startDate, endDate)
            }
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return when (sUriMatcher.match(uri)) {
            CURRENCIES_CODE -> "vnd.android.cursor.dir/vnd.$AUTHORITY.currencies"
            TARGET_CURRENCIES_CODE -> "vnd.android.cursor.dir/vnd.$AUTHORITY.target_currencies"
            EXCHANGE_RATE_CODE -> "vnd.android.cursor.dir/vnd.$AUTHORITY.exchange_rate"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}
