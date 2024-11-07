package jimmy.assesment.placeholder.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY createdAt DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<PostEntity>)

    @Query("DELETE FROM posts WHERE id = :postId")
    suspend fun deletePost(postId: Int)

    @Query("UPDATE posts SET syncType = :syncType WHERE id = :postId")
    suspend fun markPendingSync(postId: Int, syncType: SyncType)

    @Query("SELECT * FROM posts WHERE syncType IS NOT NULL")
    suspend fun getPendingSyncItems(): List<PostEntity>

    @Query("UPDATE posts SET syncType = NULL WHERE id = :postId")
    suspend fun removePendingSync(postId: Int)

    @Query("UPDATE posts SET deleted = 1 WHERE id = :postId")
    suspend fun markDeleted(postId: Int)
}