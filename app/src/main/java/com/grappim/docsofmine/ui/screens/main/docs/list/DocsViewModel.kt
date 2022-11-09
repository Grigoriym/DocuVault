package com.grappim.docsofmine.ui.screens.main.docs.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grappim.docsofmine.model.document.DocumentListUI
import com.grappim.docsofmine.uikit.utils.toColor
import com.grappim.docsofmine.utils.FilePreviewHelper
import com.grappim.docsofmine.utils.WhileViewSubscribed
import com.grappim.docsofmine.utils.datetime.DateTimeUtils
import com.grappim.docsofmine.utils.files.mime.MimeTypes
import com.grappim.domain.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DocsViewModel @Inject constructor(
    documentRepository: DocumentRepository,
    private val dateTimeUtils: DateTimeUtils,
    private val filePreviewHelper: FilePreviewHelper
) : ViewModel() {

    val documents = documentRepository.getAllDocumentsFlow()
        .map { list ->
            list.map { document ->
                DocumentListUI(
                    id = document.id.toString(),
                    name = document.name,
                    createdDate = dateTimeUtils.formatToDemonstrate(document.createdDate),
                    preview = document
                        .filesUri
                        .find {
                            it.mimeType.contains(MimeTypes.Image.PREFIX)
                        }?.uriString
                        ?: document.filesUri.find {
                            it.previewUriString != null
                        }?.previewUriString ?: "",
                    groupColor = document.group.color.toColor()
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileViewSubscribed,
            initialValue = emptyList()
        )

}