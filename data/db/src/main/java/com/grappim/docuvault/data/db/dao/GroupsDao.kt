package com.grappim.docuvault.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.grappim.docuvault.data.db.model.group.GroupEntity
import com.grappim.docuvault.data.db.model.group.GroupWithFieldsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupsDao {

    @[Transaction Query("SELECT * FROM group_table")]
    fun getAll(): Flow<List<GroupWithFieldsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groupEntity: GroupEntity)

    @[Transaction Query("SELECT * FROM group_table ORDER BY groupId ASC LIMIT 1")]
    fun getFirstGroup(): GroupWithFieldsEntity
}
