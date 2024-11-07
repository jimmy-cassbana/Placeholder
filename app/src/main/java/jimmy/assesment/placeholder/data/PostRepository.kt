package jimmy.assesment.placeholder.data

import jimmy.assesment.placeholder.network.PostService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class PostRepository @Inject constructor(
    private val postDao: PostDao,
    private val postService: PostService
) {

    fun getPosts(): Flow<List<PostEntity>> = flow {
        try {
            val postsFromApi = postService.getPosts()
            postDao.insertPosts(postsFromApi)
        } catch (e: Exception) {
            //TODO Handle no internet exception
        }
        emitAll(postDao.getAllPosts())
    }

    suspend fun createPost(post: PostEntity) {
        postDao.insertPosts(listOf(post))
        if (!createRemotePost(post)) {
            postDao.markPendingSync(post.id, SyncType.CREATE)
        }
    }

    suspend fun updatePost(post: PostEntity) {
        postDao.insertPosts(listOf(post))
        if (!updateRemotePost(post)) {
            postDao.markPendingSync(post.id, SyncType.UPDATE)
        }
    }

    suspend fun deletePost(postId: Int) {
        postDao.markDeleted(postId)
        if (!deleteRemotePost(postId)) {
            postDao.markPendingSync(postId, SyncType.DELETE)
        }
    }

    suspend fun syncPendingChanges() {
        val pendingItems = postDao.getPendingSyncItems()
        pendingItems.forEach { item ->
            when (item.syncType) {
                SyncType.CREATE -> {
                    if (createRemotePost(item)) {
                        postDao.removePendingSync(item.id)
                    }
                }
                SyncType.UPDATE -> {
                    if (updateRemotePost(item)) {
                        postDao.removePendingSync(item.id)
                    }
                }
                SyncType.DELETE -> {
                    if (deleteRemotePost(item.id)) {
                        postDao.removePendingSync(item.id)
                    }
                }

                null -> {}
            }
        }
    }

    private suspend fun createRemotePost(post: PostEntity): Boolean {
        return try {
            postService.createPost(post)
            true
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun updateRemotePost(post: PostEntity): Boolean {
        return try {
            postService.updatePost(post.id, post)
            true
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun deleteRemotePost(postId: Int): Boolean {
        return try {
            postService.deletePost(postId)
            true
        } catch (e: Exception) {
            false
        }
    }
}