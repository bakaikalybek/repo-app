package kg.bakai.repotestapp.presentation.search

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kg.bakai.repotestapp.presentation.destinations.RepositoryDetailsScreenDestination

@Composable
@Destination
fun SearchScreen(
    token: String,
    navigator: DestinationsNavigator,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state = viewModel.state
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = { query ->
                viewModel.onEvent(SearchScreenEvents.OnSearchQuery(query))
            },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(text = "Search...")
            },
            maxLines = 1,
            singleLine = true
        )
        
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.repositories.size) { index ->
                val repository = state.repositories[index]
                if (index >= state.repositories.size - 1 && !state.isLoading) {
                    viewModel.loadNextItems()
                }
                RepositoryItem(repository = repository, modifier = Modifier.clickable {
                    navigator.navigate(RepositoryDetailsScreenDestination(url = repository.url ?: "https://github.com/"))
                })
            }
            item {
                if (state.isLoading) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        when {
            state.error != null -> {
                Toast.makeText(context, state.error, Toast.LENGTH_LONG).show()
            }
        }
    }
}