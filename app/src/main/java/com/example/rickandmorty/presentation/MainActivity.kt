package com.example.rickandmorty.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.rickandmorty.domain.models.Character
import com.example.rickandmorty.ui.theme.RickAndMortyTheme
import com.example.rickandmorty.util.clickableWithoutRipple
import com.skydoves.orbital.Orbital
import com.skydoves.orbital.animateBounds
import com.skydoves.orbital.animateSharedElementTransition
import com.skydoves.orbital.rememberContentWithOrbitalScope
import com.skydoves.orbital.rememberMovableContentOf
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {
                CharactersScreen()
            }
        }
    }
}

@Composable
fun CharactersScreen(viewModel: CharactersViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    when (state.screenData) {
        ScreenData.Initial -> {}
        ScreenData.Loading -> CircularProgressIndicator()
        is ScreenData.Data -> ListScreen(
            data = (state.screenData as ScreenData.Data).characters
        )
    }
}

@Composable
fun ListScreen(
    data: List<Character>,
) {
    Orbital {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            items(
                items = data,
                key = { it.name }
            ) { character ->
                CharacterItem(character = character)
            }
        }
    }
}


@Composable
fun SingleCharacterContent(
    data: List<Character>,
) {
    val character by remember { mutableStateOf(data[0]) }
    var isTransformed by rememberSaveable { mutableStateOf(false) }

    val sharedImage = rememberContentWithOrbitalScope {
        AsyncImage(
            modifier = if (isTransformed) {
                Modifier
                    .fillMaxWidth()
                    .height(500.dp)
            } else {
                Modifier
                    .size(130.dp, 220.dp)
                    .clip(RoundedCornerShape(size = 10.dp))
            }.animateSharedElementTransition(
                orbitalScope = this,
                movementSpec = SpringSpec(stiffness = 500f),
                transformSpec = SpringSpec(stiffness = 500f)
            ),
            model = ImageRequest.Builder(LocalContext.current)
                .data(character.image)
                .crossfade(true)
                .build(),
            contentDescription = character.name,
            contentScale = ContentScale.Crop
        )
    }

    Orbital(
        modifier = Modifier.clickable { isTransformed = !isTransformed }
    ) {
        if (isTransformed) {
            DetailsScreen(
                character = character,
                sharedElement = { sharedImage() }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                sharedImage()
            }
        }
    }
}

@Composable
fun CharacterItem(
    modifier: Modifier = Modifier,
    character: Character,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    AnimatedVisibility(
        visibleState = remember { MutableTransitionState(false) }
            .apply { targetState = true },
        enter = fadeIn(tween(durationMillis = 300)),
        exit = fadeOut(tween(durationMillis = 300))
    ) {
        Orbital(
            modifier = Modifier
                .fillMaxWidth()
                .clickableWithoutRipple(
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { expanded = !expanded }
                )
        ) {
            val text = rememberMovableContentOf {
                Column(
                    modifier = Modifier
                        .padding(
                            vertical = 10.dp,
                            horizontal = if (expanded) 20.dp else 10.dp
                        )
                        .animateBounds(
                            sizeAnimationSpec = tween(durationMillis = 300),
                            positionAnimationSpec = tween(durationMillis = 300)
                        )
                ) {
                    Text(
                        text = character.name,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            val image = rememberContentWithOrbitalScope {
                AsyncImage(
                    modifier = Modifier
                        .padding(10.dp)
                        .animateBounds(
                            modifier = if (expanded) {
                                Modifier.fillMaxSize()
                            } else {
                                Modifier.size(100.dp)
                            },
                            sizeAnimationSpec = tween(durationMillis = 300),
                            positionAnimationSpec = tween(durationMillis = 300)
                        )
                        .clip(RoundedCornerShape(10.dp)),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(character.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = character.name,
                    contentScale = ContentScale.Crop,
                )
            }

            if (expanded) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp))
                ) {
                    image()
                    text()
                }
            } else {
                Row {
                    image()
                    text()
                }
            }
        }
    }
}

    @Composable
    fun DetailsScreen(
        character: Character,
        sharedElement: @Composable () -> Unit,
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = scrollState)
        ) {
            sharedElement()
            AnimatedVisibility(
                visibleState = remember { MutableTransitionState(false) }
                    .apply { targetState = true },
                enter = fadeIn(tween(durationMillis = 600)),
                exit = fadeOut(tween(durationMillis = 600))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = character.name,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        RickAndMortyTheme {
            CharactersScreen()
        }
    }