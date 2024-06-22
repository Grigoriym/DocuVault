package com.grappim.docuvault.feature.group.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.grappim.docuvault.uikit.theme.DefaultHorizontalPadding
import com.grappim.docuvault.uikit.widget.PlatoButtonDefault
import com.grappim.docuvault.uikit.widget.PlatoGroupItem

private const val DEFAULT_GRID_SIZE = 3

@Composable
fun GroupListScreenRoute(
    viewModel: GroupListViewModel = hiltViewModel(),
    onCreateGroupClick: () -> Unit,
    onGroupClick: (Long) -> Unit
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()

    GroupListScreenContent(
        state = state,
        onCreateGroupClick = onCreateGroupClick,
        onGroupClick = onGroupClick
    )
}

@Composable
private fun GroupListScreenContent(
    state: GroupListState,
    onCreateGroupClick: () -> Unit,
    onGroupClick: (Long) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = DefaultHorizontalPadding)
            .padding(top = 16.dp)
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(DEFAULT_GRID_SIZE),
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalItemSpacing = 12.dp
        ) {
            items(state.groups) { group ->
                PlatoGroupItem(
                    id = group.id,
                    name = group.name,
                    color = group.color,
                    isSelected = true,
                    onGroupClick = {
                        onGroupClick(group.id)
                    }
                )
            }
        }
        PlatoButtonDefault(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 18.dp),
            onClick = onCreateGroupClick,
            text = stringResource(id = R.string.create_group)
        )
    }
}
