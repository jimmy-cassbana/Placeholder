package jimmy.assesment.placeholder.ui.posts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import jimmy.assesment.placeholder.data.PostEntity

@Composable
fun PostsScreen(viewModel: PostViewModel = hiltViewModel()) {
    val posts = viewModel.posts.collectAsState().value

    var isEditing by remember { mutableStateOf(false) }
    var currentPost by remember { mutableStateOf<PostEntity?>(null) }
    var updatedTitle by remember { mutableStateOf("") }
    var updatedBody by remember { mutableStateOf("") }
    var isCreating by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isCreating = true
                    updatedTitle = ""
                    updatedBody = ""
                },
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add New Post")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            if ((isEditing && currentPost != null) || isCreating) {
                EditPostDialog(
                    title = updatedTitle,
                    body = updatedBody,
                    onTitleChange = { updatedTitle = it },
                    onBodyChange = { updatedBody = it },
                    onSave = {
                        if (isCreating) {
                            val newPost = PostEntity(
                                id = (posts.maxOfOrNull { it.id } ?: 0) + 1,
                                userId = 1,  // assuming a default user ID for new posts
                                title = updatedTitle,
                                body = updatedBody
                            )
                            viewModel.createPost(newPost)
                        } else {
                            currentPost?.let { post ->
                                val updatedPost = post.copy(title = updatedTitle, body = updatedBody)
                                viewModel.updatePost(updatedPost)
                            }
                        }
                        isEditing = false
                        isCreating = false
                    },
                    onCancel = {
                        isEditing = false
                        isCreating = false
                    },
                    isCreating = isCreating
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(posts) { post ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = post.title, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = post.body, style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(onClick = {
                                    isEditing = true
                                    currentPost = post
                                    updatedTitle = post.title
                                    updatedBody = post.body
                                }) {
                                    Icon(Icons.Filled.Edit, contentDescription = "Edit Post")
                                }
                                IconButton(onClick = { viewModel.deletePost(post.id) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Delete Post")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditPostDialog(
    title: String,
    body: String,
    onTitleChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    isCreating: Boolean
) {
    AlertDialog(
        onDismissRequest = { onCancel() },
        title = {
            Text(text = if (isCreating) "Create Post" else "Edit Post")
        },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
                TextField(
                    value = body,
                    onValueChange = onBodyChange,
                    label = { Text("Body") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave() }) {
                Text(if (isCreating) "Create Post" else "Save Changes")
            }
        },
        dismissButton = {
            Button(onClick = { onCancel() }) {
                Text("Cancel")
            }
        }
    )
}