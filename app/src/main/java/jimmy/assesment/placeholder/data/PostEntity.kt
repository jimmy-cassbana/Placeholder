package jimmy.assesment.placeholder.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    val createdAt: Long = System.currentTimeMillis(),
    val syncType: SyncType? = null,
    val deleted: Boolean = false
)

enum class SyncType {
    CREATE, UPDATE, DELETE
}