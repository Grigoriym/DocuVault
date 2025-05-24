package com.grappim.docuvault.feature.docgroup.manager

import androidx.compose.ui.graphics.Color
import com.grappim.docuvault.utils.ui.NativeText

/**
 * @param isNewGroup indicates if we creating a new product, the value being true, or editing the
 * product, the value being false
 */
data class GroupManagerState(
    val name: String = "",
    val color: Color,

    val setName: (String) -> Unit,
    val setColor: (Color) -> Unit,

    val onGroupDone: () -> Unit,

    val groupSaved: Boolean = false,
    val isNewGroup: Boolean = true,
    val doneButtonText: NativeText = NativeText.Empty
)
