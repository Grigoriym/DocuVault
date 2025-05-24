package com.grappim.docuvault.feature.docgroup.manager

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.godaddy.android.colorpicker.HsvColor
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker
import com.grappim.docuvault.uikit.theme.DefaultHorizontalPadding
import com.grappim.docuvault.uikit.theme.DocuVaultTheme
import com.grappim.docuvault.uikit.utils.ThemePreviews
import com.grappim.docuvault.uikit.widget.PlatoButtonDefault
import com.grappim.docuvault.uikit.widget.PlatoTextFieldDefault
import com.grappim.docuvault.utils.ui.NativeText
import com.grappim.docuvault.utils.ui.asString
import com.grappim.docuvault.utils.ui.collectSnackbarMessage
import com.grappim.feature.docgroup.manager.R

@Composable
fun GroupManagerScreenRoute(
    viewModel: GroupManagerViewModel = hiltViewModel(),
    onBack: (isNewProduct: Boolean) -> Unit,
    onGroupSaved: (isNewProduct: Boolean) -> Unit,
    onShowSnackbar: suspend (message: String, String?) -> Boolean
) {
    val context = LocalContext.current
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackBarMessage.collectSnackbarMessage()

    LaunchedEffect(state.groupSaved) {
        if (state.groupSaved) {
            onGroupSaved(state.isNewGroup)
        }
    }

    BackHandler(enabled = true) {
        onBack(state.isNewGroup)
    }

    LaunchedEffect(snackbarMessage) {
        if (snackbarMessage !is NativeText.Empty) {
            onShowSnackbar(snackbarMessage.asString(context), null)
        }
    }

    GroupManagerScreenContent(state = state)
}

@Composable
private fun GroupManagerScreenContent(state: GroupManagerState) {
    val context = LocalContext.current
    var openDialog by remember { mutableStateOf(false) }
    val updatedHsvColor by remember(state.color) {
        mutableStateOf(HsvColor.from(state.color))
    }

    if (openDialog) {
        Dialog(
            onDismissRequest = {
                openDialog = false
            },
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HarmonyColorPicker(
                    modifier = Modifier
                        .size(400.dp),
                    harmonyMode = ColorHarmonyMode.NONE,
                    onColorChanged = { hsvColor: HsvColor ->
                        state.setColor(hsvColor.toColor())
                    },
                    color = updatedHsvColor
                )
                PlatoButtonDefault(
                    onClick = {
                        openDialog = false
                    },
                    text = stringResource(id = R.string.done),
                    modifier = Modifier
                        .padding(top = 12.dp)
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = DefaultHorizontalPadding)
    ) {
        PlatoTextFieldDefault(
            value = state.name,
            onValueChange = state.setName,
            label = "Group name"
        )
        Row(
            modifier = Modifier
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 8.dp),
                text = stringResource(id = R.string.color)
            )
            Card(
                border = BorderStroke(1.dp, Color.Gray),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(60.dp),
                content = {},
                onClick = {
                    openDialog = true
                },
                colors = CardDefaults.cardColors().copy(
                    containerColor = state.color
                )
            )
        }

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            PlatoButtonDefault(
                modifier = Modifier
                    .padding(top = 16.dp),
                onClick = state.onGroupDone,
                text = state.doneButtonText.asString(context)
            )
        }
    }
}

@Composable
@ThemePreviews
private fun GroupManagerScreenContentPreview() {
    DocuVaultTheme {
        GroupManagerScreenContent(
            state = GroupManagerState(
                name = "name",
                color = Color.Blue,
                setName = { },
                setColor = {},
                onGroupDone = {},
                doneButtonText = NativeText.Resource(R.string.update)
            )
        )
    }
}
