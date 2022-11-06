package com.grappim.docsofmine.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.grappim.docsofmine.data.db.model.GroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupsDao {

    @Query("SELECT * FROM group_table")
    fun getAll(): Flow<List<GroupEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groupEntity: GroupEntity)
}