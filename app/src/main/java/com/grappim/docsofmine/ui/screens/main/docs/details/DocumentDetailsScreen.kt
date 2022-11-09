package com.grappim.docsofmine.ui.screens.main.docs.details

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grappim.docsofmine.uikit.R
import com.grappim.docsofmine.utils.DomFileItem
import com.grappim.docsofmine.utils.files.FileData
import com.grappim.domain.model.document.Document
import timber.log.Timber

@Composable
fun DocumentDetailsScreen(
    viewModel: DocumentDetailsViewModel = hiltViewModel()
) {
    val document by viewModel.document.collectAsState()
    val files by viewModel.filesData.collectAsState(emptyList())
    if (document != null) {
        DocumentDetailsScreenContent(
            document = document!!,
            files = files
        )
    }
}

@Composable
private fun DocumentDetailsScreenContent(
    document: Document,
    files: List<FileData>
) {
    val context = LocalContext.current

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
        items(files) { item: FileData ->
            DomFileItem(
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
                    } catch (e: SecurityException) {
                        Timber.e(e)
                        Toast.makeText(context, R.string.error_content_not_found, Toast.LENGTH_LONG)
                            .show()
                    } catch (e: Exception) {
                        Timber.e(e)
                        Toast.makeText(context, R.string.error_unknown, Toast.LENGTH_LONG).show()
                    }
                }
            )
        }
    }
}