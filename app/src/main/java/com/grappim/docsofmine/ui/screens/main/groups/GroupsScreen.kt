package com.grappim.docsofmine.ui.screens.main.groups

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.grappim.docsofmine.uikit.theme.DefaultHorizontalPadding
import com.grappim.docsofmine.uikit.widget.DomButtonDefault
import com.grappim.docsofmine.uikit.widget.DomGroupItem

@Composable
fun GroupsScreen(
    viewModel: GroupsViewModel = hiltViewModel(),
    onCreateGroupClick: () -> Unit
) {
    val groups by viewModel.groups.collectAsState()

    Column(
        modifier = Modifier
            .padding(horizontal = DefaultHorizontalPadding)
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(3),
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalItemSpacing = 12.dp,
        ) {
            items(groups) { group ->
                DomGroupItem(
                    group = group,
                    onGroupClick = {

                    },
                    isSelected = true
                )
            }
        }
        DomButtonDefault(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 18.dp),
            onClick = onCreateGroupClick,
            text = "Create Group"
        )
    }
}