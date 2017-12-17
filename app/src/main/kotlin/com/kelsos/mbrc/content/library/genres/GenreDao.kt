package com.kelsos.mbrc.content.library.genres

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface GenreDao {

  @Query("delete from genre")
  fun deleteAll()

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun saveAll(list: List<GenreEntity>)

  @Query("select * from genre")
  fun getAll(): DataSource.Factory<Int, GenreEntity>

  @Query("select * from genre where genre like '%' || :term || '%'")
  fun search(term: String): DataSource.Factory<Int, GenreEntity>

  @Query("select count(*) from genre")
  fun count(): Long
}
