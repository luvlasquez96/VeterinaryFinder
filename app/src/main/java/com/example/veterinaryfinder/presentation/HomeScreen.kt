package com.example.veterinaryfinder.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.veterinaryfinder.model.Veterinary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: VeterinaryViewModel = hiltViewModel()
    val viewState by viewModel.viewState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.onSurface,
                onClick = { navController.navigate("add_veterinaria") }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Veterinaria")
            }
        }
    ) { contentPadding ->
        when (viewState) {
            is VeterinaryViewModel.VeterinaryViewState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is VeterinaryViewModel.VeterinaryViewState.Success -> {
                val veterinaries = (viewState as VeterinaryViewModel.VeterinaryViewState.Success).veterinaries
                val sortedVeterinaries = veterinaries.sortedBy { it.name }

                if (sortedVeterinaries.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No hay veterinarias disponibles", fontSize = 16.sp)
                    }
                } else {
                    LazyColumn(
                        contentPadding = contentPadding,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        items(sortedVeterinaries, key = { it.id }) { veterinary ->
                            val dismissState = rememberDismissState(
                                confirmValueChange = {
                                    if (it == DismissValue.DismissedToEnd) {
                                        viewModel.deleteVeterinary(veterinary)
                                    }
                                    true
                                }
                            )

                            SwipeToDismiss(
                                state = dismissState,
                                directions = setOf(DismissDirection.StartToEnd),
                                background = {
                                    if (dismissState.dismissDirection != null) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.Red)
                                                .padding(horizontal = 20.dp),
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Eliminar",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                },
                                dismissContent = {
                                    VeterinaryCard(veterinary, navController, viewModel)
                                }
                            )
                        }
                    }
                }
            }

            is VeterinaryViewModel.VeterinaryViewState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = (viewState as VeterinaryViewModel.VeterinaryViewState.Error).message,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun VeterinaryCard(veterinary: Veterinary, navController: NavController, viewModel: VeterinaryViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Imagen y detalles de la veterinaria
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Display image or placeholder
                if (veterinary.image.isNullOrEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color.Gray)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Sin Imagen",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(veterinary.image)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Imagen de Veterinaria",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Gray, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Details about the veterinary
                Column(
                    modifier = Modifier.weight(1f) // This allows the column to take up remaining space
                ) {
                    Text(
                        text = veterinary.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = veterinary.address,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = veterinary.phone,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // Edit icon button
                IconButton(
                    onClick = { navController.navigate("edit_veterinaria/${veterinary.id}") },
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = MaterialTheme.colorScheme.primary 
                    )
                }
            }
        }
    }
}
