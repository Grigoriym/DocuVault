package com.grappim.docuvault.feature.docs.details

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grappim.docuvault.uikit.widget.PlatoFileItem
import timber.log.Timber

@Composable
fun DocumentDetailsScreen(
    viewModel: DocumentDetailsViewModel = hiltViewModel(),
    onEditClicked: (Long) -> Unit,
    isFromEdit: Boolean
) {
    val state by viewModel.viewState.collectAsState()
    if (state.document != null) {
        DocumentDetailsScreenContent(
            state = state,
            onEditClicked = onEditClicked,
            isFromEdit = isFromEdit
        )
    }
}

@Composable
private fun DocumentDetailsScreenContent(
    state: DocumentDetailsState,
    onEditClicked: (Long) -> Unit,
    isFromEdit: Boolean
) {
    val context = LocalContext.current
    val document = requireNotNull(state.document)

    LaunchedEffect(isFromEdit) {
        if (isFromEdit) {
            state.updateProduct()
        }
    }

    Text(text = document.name)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            top = 8.dp,
            bottom = 8.dp
        )
    ) {
        item {
            Button(onClick = { onEditClicked(document.documentId) }) {
                Text(text = stringResource(id = R.string.edit))
            }
        }
        items(state.files) { item ->
            PlatoFileItem(
                fileData = item,
                onFileClicked = {
                    try {
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW)
                                .setDataAndType(it.uri, it.mimeType)
                                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        )
                    } catch (e: ActivityNotFoundException) {
                        Timber.e(e)
                        Toast.makeText(
                            context,
                            R.string.error_no_activity_to_view,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            )
        }
    }
}
