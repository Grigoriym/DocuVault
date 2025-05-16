package com.grappim.docuvault.feature.docgroup.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.grappim.docuvault.feature.docgroup.db.model.GroupEntity
import com.grappim.docuvault.feature.docgroup.db.model.GroupFieldEntity
import com.grappim.docuvault.feature.docgroup.db.model.GroupWithFieldsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupsDao {

    @[Transaction Query("SELECT * FROM group_table")]
    fun getGroupsFlow(): Flow<List<GroupWithFieldsEntity>>

    @[Transaction Query("SELECT * FROM group_table WHERE groupId=:groupId")]
    suspend fun getFullGroupById(groupId: Long): GroupWithFieldsEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(groupEntity: GroupEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFields(list: List<GroupFieldEntity>)

    @[Transaction Query("SELECT * FROM group_table ORDER BY groupId ASC LIMIT 1")]
    fun getFirstGroup(): GroupWithFieldsEntity

    @Transaction
    suspend fun insertGroupAndFields(groupEntity: GroupEntity, list: List<GroupFieldEntity>) {
        insert(groupEntity)
        insertFields(list)
    }
}
