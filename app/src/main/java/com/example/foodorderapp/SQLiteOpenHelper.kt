package com.example.foodorderapp


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class CartDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "cart_database.db"

        private const val TABLE_CART = "cart"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_IMG = "img"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_CART (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_TITLE TEXT," +
                "$COLUMN_PRICE INTEGER," +
                "$COLUMN_IMG TEXT)"

        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Upgrade logic goes here if needed
    }

    fun getCartTotal(): Double {
        val db = readableDatabase
        var total = 0.0

        val query = "SELECT SUM($COLUMN_PRICE) FROM $TABLE_CART"

        try {
            val cursor = db.rawQuery(query, null)

            if (cursor.moveToFirst()) {
                total = cursor.getDouble(0)
            }

            cursor.close()
        } catch (e: Exception) {
            Log.e("DatabaseError", "Error calculating cart total: ${e.message}")
        }

        return total
    }

    fun addToCart(title: String, price: Int, img: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, title)
            put(COLUMN_PRICE, price)
            put(COLUMN_IMG, img)
        }
        db.insert(TABLE_CART, null, values)
    }

    @SuppressLint("Range")
    fun getAllItems(): List<CartDataClass> {
        val cartList = mutableListOf<CartDataClass>()

        readableDatabase.use { db ->
            try {
                val cursor = db.rawQuery("SELECT * FROM $TABLE_CART", null)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                    val title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE))
                    val price = cursor.getInt(cursor.getColumnIndex(COLUMN_PRICE))
                    val img = cursor.getString(cursor.getColumnIndex(COLUMN_IMG))
                    val cartData = CartDataClass(id.toInt(), title, price, img)
                    cartList.add(cartData)
                }

                cursor.close()
            } catch (e: Exception) {
                Log.e("DatabaseError", "Error retrieving data from the database: ${e.message}")
            }
        }

        return cartList
    }

    fun getCartItemCount(): Int {
        var count = 0

        readableDatabase.use { db ->
            try {
                val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_CART", null)

                if (cursor.moveToFirst()) {
                    count = cursor.getInt(0)
                }

                cursor.close()
            } catch (e: Exception) {
                Log.e("DatabaseError", "Error retrieving cart item count: ${e.message}")
            }
        }

        return count
    }

    fun removeItemFromCart(adjustedPosition: Int): Int {

        val db = writableDatabase
        return db.delete(TABLE_CART, "$COLUMN_ID = ?", arrayOf(adjustedPosition.toString()))
    }

}
