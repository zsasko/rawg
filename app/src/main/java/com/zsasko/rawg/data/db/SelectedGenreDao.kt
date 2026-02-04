package com.zsasko.rawg.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow


@Dao
interface SelectedGenreDao {
    @Query("SELECT * FROM selected_genre")
    suspend fun getAll(): List<SelectedGenre>

    @Query("SELECT * FROM selected_genre")
    fun getAllFlow(): Flow<List<SelectedGenre>>

    @Query("SELECT COUNT(*) FROM selected_genre")
    fun getCount(): Int

    @Query("SELECT EXISTS(SELECT 1 FROM selected_genre WHERE genre_id = :genreId)")
    suspend fun exists(genreId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(genre: SelectedGenre)


    @Transaction
    suspend fun toggle(genre: SelectedGenre) {
        if (exists(genre.genreId)) {
            delete(genre)
        } else {
            insert(genre)
        }
    }

    @Delete
    fun delete(user: SelectedGenre)
}