package com.grappim.docsofmine.ui.screens.main.groups.create

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.godaddy.android.colorpicker.HsvColor
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker
import com.grappim.docsofmine.uikit.theme.DefaultHorizontalPadding
import com.grappim.docsofmine.uikit.utils.toDomString
import com.grappim.docsofmine.uikit.widget.DomButtonDefault
import com.grappim.docsofmine.uikit.widget.DomTextFieldDefault

@Composable
fun CreateGroupScreen(
    viewModel: CreateGroupViewModel = hiltViewModel()
) {
    val createGroupState by viewModel.createGroupInputState.collectAsState()

    var openDialog by remember { mutableStateOf(false) }

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
                        createGroupState.colorString = hsvColor.toColor().toDomString()
                    },
                    color = createGroupState.color
                )
                DomButtonDefault(
                    onClick = {
                        openDialog = false
                    },
                    text = "Done",
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
        DomTextFieldDefault(
            value = createGroupState.name,
            onValueChange = { createGroupState.name = it },
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
                text = "Color"
            )
            Card(
                backgroundColor = createGroupState.color,
                onClick = {
                    openDialog = true
                },
                border = BorderStroke(1.dp, Color.Gray),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(60.dp)
            ) {

            }
        }

        DomButtonDefault(
            modifier = Modifier
                .padding(top = 16.dp),
            onClick = {
                viewModel.createGroup()
            },
            text = "Create"
        )

    }
}