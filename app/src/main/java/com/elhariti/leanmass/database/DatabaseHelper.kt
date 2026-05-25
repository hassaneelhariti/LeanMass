package com.elhariti.leanmass.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.elhariti.leanmass.models.Calcul
import com.elhariti.leanmass.models.Sexe
import com.elhariti.leanmass.models.User
import java.security.MessageDigest

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "leanmass.db"
        const val DATABASE_VERSION = 1

        // Table Users
        const val TABLE_USERS = "users"
        const val COL_USER_ID = "id"
        const val COL_USER_NAME = "name"
        const val COL_USER_EMAIL = "email"
        const val COL_USER_PASSWORD = "password_hash"

        // Table Calculs
        const val TABLE_CALCULS = "calculs"
        const val COL_CALCUL_ID = "id"
        const val COL_CALCUL_USER_ID = "user_id"
        const val COL_CALCUL_POIDS = "poids"
        const val COL_CALCUL_TAILLE = "taille"
        const val COL_CALCUL_SEXE = "sexe"
        const val COL_CALCUL_LBM = "lbm"
        const val COL_CALCUL_MASSE_GRASSE = "masse_grasse"
        const val COL_CALCUL_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_USERS (
                $COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USER_NAME TEXT NOT NULL,
                $COL_USER_EMAIL TEXT NOT NULL UNIQUE,
                $COL_USER_PASSWORD TEXT NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE $TABLE_CALCULS (
                $COL_CALCUL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CALCUL_USER_ID INTEGER NOT NULL,
                $COL_CALCUL_POIDS REAL NOT NULL,
                $COL_CALCUL_TAILLE INTEGER NOT NULL,
                $COL_CALCUL_SEXE TEXT NOT NULL,
                $COL_CALCUL_LBM REAL NOT NULL,
                $COL_CALCUL_MASSE_GRASSE REAL NOT NULL,
                $COL_CALCUL_DATE TEXT NOT NULL,
                FOREIGN KEY($COL_CALCUL_USER_ID) REFERENCES $TABLE_USERS($COL_USER_ID)
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CALCULS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun insertUser(name: String, email: String, password: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_USER_NAME, name)
            put(COL_USER_EMAIL, email.lowercase().trim())
            put(COL_USER_PASSWORD, hashPassword(password))
        }
        return db.insert(TABLE_USERS, null, values)
    }

    fun loginUser(email: String, password: String): User? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            null,
            "$COL_USER_EMAIL = ? AND $COL_USER_PASSWORD = ?",
            arrayOf(email.lowercase().trim(), hashPassword(password)),
            null, null, null
        )
        return if (cursor.moveToFirst()) {
            User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_EMAIL)),
                passwordHash = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PASSWORD))
            ).also { cursor.close() }
        } else {
            cursor.close()
            null
        }
    }

    fun emailExiste(email: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS, arrayOf(COL_USER_ID),
            "$COL_USER_EMAIL = ?", arrayOf(email.lowercase().trim()),
            null, null, null
        )
        val existe = cursor.moveToFirst()
        cursor.close()
        return existe
    }

    fun insertCalcul(calcul: Calcul): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_CALCUL_USER_ID, calcul.userId)
            put(COL_CALCUL_POIDS, calcul.poids)
            put(COL_CALCUL_TAILLE, calcul.taille)
            put(COL_CALCUL_SEXE, calcul.sexe.name)
            put(COL_CALCUL_LBM, calcul.lbm)
            put(COL_CALCUL_MASSE_GRASSE, calcul.massGrasse)
            put(COL_CALCUL_DATE, calcul.date)
        }
        return db.insert(TABLE_CALCULS, null, values)
    }

    fun getHistorique(userId: Int): List<Calcul> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CALCULS, null,
            "$COL_CALCUL_USER_ID = ?", arrayOf(userId.toString()),
            null, null, "$COL_CALCUL_DATE DESC"
        )
        val liste = mutableListOf<Calcul>()
        while (cursor.moveToNext()) {
            liste.add(
                Calcul(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CALCUL_ID)),
                    userId = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CALCUL_USER_ID)),
                    poids = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_CALCUL_POIDS)),
                    taille = cursor.getInt(cursor.getColumnIndexOrThrow(COL_CALCUL_TAILLE)),
                    sexe = Sexe.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(COL_CALCUL_SEXE))),
                    lbm = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_CALCUL_LBM)),
                    massGrasse = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_CALCUL_MASSE_GRASSE)),
                    date = cursor.getString(cursor.getColumnIndexOrThrow(COL_CALCUL_DATE))
                )
            )
        }
        cursor.close()
        return liste
    }

    fun deleteCalcul(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_CALCULS, "$COL_CALCUL_ID = ?", arrayOf(id.toString()))
    }

    fun getMoyenneLBM(userId: Int): Double {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT AVG($COL_CALCUL_LBM) FROM $TABLE_CALCULS WHERE $COL_CALCUL_USER_ID = ?",
            arrayOf(userId.toString())
        )
        val moyenne = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()
        return moyenne
    }

    fun getNombreEntrees(userId: Int): Int {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT COUNT(*) FROM $TABLE_CALCULS WHERE $COL_CALCUL_USER_ID = ?",
            arrayOf(userId.toString())
        )
        val count = if (cursor.moveToFirst()) cursor.getInt(0) else 0
        cursor.close()
        return count
    }
}