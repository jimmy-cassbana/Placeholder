package jimmy.assesment.placeholder.ui.posts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jimmy.assesment.placeholder.data.PostEntity
import jimmy.assesment.placeholder.data.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    private val _posts = MutableStateFlow<List<PostEntity>>(emptyList())
    val posts: StateFlow<List<PostEntity>> = _posts.asStateFlow()

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            repository.getPosts().collect { postList ->
                _posts.value = postList.filterNot { it.deleted }
            }
        }
    }

    fun createPost(post: PostEntity) {
        viewModelScope.launch {
            repository.createPost(post)
        }
    }

    fun updatePost(post: PostEntity) {
        viewModelScope.launch {
            repository.updatePost(post)
        }
    }

    fun deletePost(postId: Int) {
        viewModelScope.launch {
            repository.deletePost(postId)
        }
    }
}