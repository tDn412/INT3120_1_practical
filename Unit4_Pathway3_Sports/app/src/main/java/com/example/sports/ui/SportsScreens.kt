package com.example.sports.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sports.R
import com.example.sports.model.Sport
import com.example.sports.utils.SportsContentType

/**
 * Main composable
 */
@Composable
fun SportsApp(
    windowSize: WindowWidthSizeClass,
    onBackPressed: () -> Unit,
) {
    val viewModel: SportsViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val selectedSports by viewModel.selectedSports.collectAsState()
    var showResult by remember { mutableStateOf(false) }

    if (showResult) {
        val totalCalories = viewModel.getTotalCalories() //tong calo
        SportsResultScreen(totalCalories = totalCalories)
    } else {
        Scaffold(
            topBar = {
                SportsAppBar(
                    isShowingListPage = uiState.isShowingListPage,
                    onBackButtonClick = { viewModel.navigateToListPage() },
                    windowSize = windowSize
                )
            }
        ) { innerPadding ->
            if (windowSize == WindowWidthSizeClass.Expanded) {
                SportsListAndDetail(
                    sports = uiState.sportsList,
                    selectedSport = uiState.currentSport,
                    onClick = {
                        viewModel.updateCurrentSport(it)
                        viewModel.navigateToDetailPage()
                    },
                    onBackPressed = {
                        viewModel.navigateToListPage()
                    },
                    contentPadding = innerPadding,
                )
            } else {
                if (uiState.isShowingListPage) {
                    SportsList(
                        sports = uiState.sportsList,
                        selectedIds = selectedSports,
                        onCheckedChange = { sport, checked ->
                            viewModel.toggleSportSelection(sport.id)
                        },
                        onNextClick = { showResult = true },
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
                        contentPadding = innerPadding
                    )
                } else {
                    SportsDetail(
                        selectedSport = uiState.currentSport,
                        onBackPressed = { viewModel.navigateToListPage() },
                        contentPadding = innerPadding
                    )
                }
            }
        }
    }
}


/**
 * Top app bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SportsAppBar(
    onBackButtonClick: () -> Unit,
    isShowingListPage: Boolean,
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val isShowingDetailPage = windowSize != WindowWidthSizeClass.Expanded && !isShowingListPage
    TopAppBar(
        title = {
            Text(
                text = if (isShowingDetailPage) {
                    stringResource(R.string.detail_fragment_label)
                } else {
                    stringResource(R.string.list_fragment_label)
                },
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        },
        navigationIcon = if (isShowingDetailPage) {
            {
                IconButton(onClick = onBackButtonClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        } else {
            { Box {} }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier,
    )
}

/**
 * List item with checkbox
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SportsListItem(
    sport: Sport,
    isSelected: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(),
        modifier = modifier,
        shape = RoundedCornerShape(dimensionResource(R.dimen.card_corner_radius))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.card_image_height)), // Thay size bằng height để flexbox hoạt động tốt hơn
            verticalAlignment = Alignment.CenterVertically
        ) {
            SportsListImageItem(
                sport = sport,
                modifier = Modifier.size(dimensionResource(R.dimen.card_image_height))
            )
            Column(
                modifier = Modifier
                    .padding(
                        vertical = dimensionResource(R.dimen.padding_small),
                        horizontal = dimensionResource(R.dimen.padding_medium)
                    )
                    .weight(1f)
            ) {
                Text(text = stringResource(sport.titleResourceId), style = MaterialTheme.typography.titleMedium) // Thêm style
                Text(text = "Calories/week: ${sport.caloriesPerWeek}", style = MaterialTheme.typography.bodySmall) // Thêm style
            }
            Checkbox(
                checked = isSelected,
                onCheckedChange = onCheckedChange
            )
        }
    }
}



/**
 * Result screen
 */
@Composable
fun SportsResultScreen(totalCalories: Int) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Tổng calo tiêu thụ/tuần: $totalCalories",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

/**
 * Image item
 */
@Composable
private fun SportsListImageItem(sport: Sport, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(sport.imageResourceId),
            contentDescription = null,
            alignment = Alignment.Center,
            contentScale = ContentScale.FillWidth
        )
    }
}

/**
 * List screen
 */
@Composable
private fun SportsList(
    sports: List<Sport>,
    selectedIds: Set<Int>,
    onCheckedChange: (Sport, Boolean) -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = contentPadding,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            modifier = Modifier
                .weight(1f)
                .padding(top = dimensionResource(R.dimen.padding_medium)),
        ) {
            items(sports, key = { sport -> sport.id }) { sport ->
                SportsListItem(
                    sport = sport,
                    isSelected = selectedIds.contains(sport.id),
                    onCheckedChange = { checked -> onCheckedChange(sport, checked) }
                )
            }
        }

        // nút Next
        Button(
            onClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            enabled = selectedIds.isNotEmpty()
        ) {
            Text("Next")
        }
    }
}



/**
 * Detail screen
 */
@Composable
private fun SportsDetail(
    selectedSport: Sport,
    onBackPressed: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    BackHandler { onBackPressed() }
    val scrollState = rememberScrollState()
    val layoutDirection = LocalLayoutDirection.current
    Box(
        modifier = modifier
            .verticalScroll(state = scrollState)
            .padding(top = contentPadding.calculateTopPadding())
    ) {
        Column(
            modifier = Modifier
                .padding(
                    bottom = contentPadding.calculateBottomPadding(),
                    start = contentPadding.calculateStartPadding(layoutDirection),
                    end = contentPadding.calculateEndPadding(layoutDirection)
                )
        ) {
            Box {
                Image(
                    painter = painterResource(selectedSport.sportsImageBanner),
                    contentDescription = null,
                    alignment = Alignment.TopCenter,
                    contentScale = ContentScale.FillWidth,
                )
                Column(
                    Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, MaterialTheme.colorScheme.scrim),
                                0f,
                                400f
                            )
                        )
                ) {
                    Text(
                        text = stringResource(selectedSport.titleResourceId),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        modifier = Modifier
                            .padding(horizontal = dimensionResource(R.dimen.padding_small))
                    )
                    Row(
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                    ) {
                        Text(
                            text = pluralStringResource(
                                R.plurals.player_count_caption,
                                selectedSport.playerCount,
                                selectedSport.playerCount
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = stringResource(R.string.olympic_caption),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                        )
                    }
                }
            }
            Text(
                text = stringResource(selectedSport.sportDetails),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(
                    vertical = dimensionResource(R.dimen.padding_detail_content_vertical),
                    horizontal = dimensionResource(R.dimen.padding_detail_content_horizontal)
                )
            )
        }
    }
}

/**
 * Master-detail (tablet)
 */
@Composable
private fun SportsListAndDetail(
    sports: List<Sport>,
    selectedSport: Sport,
    onClick: (Sport) -> Unit,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Row(modifier = modifier) {
        SportsList(
            sports = sports,
            selectedIds = emptySet(),
            onCheckedChange = { _, _ -> },
            contentPadding = PaddingValues(top = contentPadding.calculateTopPadding()),
            onNextClick = { },
            modifier = Modifier.weight(1f)
        )
        SportsDetail(
            selectedSport = selectedSport,
            modifier = Modifier.weight(3f),
            contentPadding = PaddingValues(top = contentPadding.calculateTopPadding()),
            onBackPressed = onBackPressed,
        )
    }
}