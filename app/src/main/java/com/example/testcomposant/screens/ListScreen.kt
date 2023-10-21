package com.example.testcomposant.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.testcomposant.R
import com.example.testcomposant.composants.ElementList
import com.example.testcomposant.models.CardContent

@Composable
fun ListScreen(modifier: Modifier = Modifier, selectedItem: CardContent? = null, onItemSelected: (CardContent) -> Unit = {}) {
    val myData = listOf(
        CardContent("Card 1", "Contenu de la card 1", R.drawable.logo),
        CardContent("Card 2", "Contenu de la card 2", R.drawable.logo),
        CardContent("Card 3", "Voilà du contenu", R.drawable.logo),
        CardContent("Card 4", "YOLO", R.drawable.logo),
        CardContent("Card 5", "Contenu de la card", R.drawable.logo),
        CardContent("Card 6", "Pif Paf Pouf", R.drawable.logo),
        CardContent("Card 7", "Ceci est une démo pour L'ESEO", R.drawable.logo),
        CardContent("Card 8", "Contenu de la card", R.drawable.logo),
        CardContent("Card 9", "Playmoweb", R.drawable.logo),
        CardContent("Card 10", "Contenu de la card", R.drawable.ic_android_black_24dp),
    )

    Column(modifier) {
        if (selectedItem == null) {
            LazyColumn {
                items(myData) { item ->
                    ElementList(
                        title = item.title,
                        content = item.content,
                        image = item.image
                    ) {
                        // Code appelé lors du clique sur un élément de la liste.
                        onItemSelected(item)
                    }
                }
            }
        } else {
            ElementList(
                title = selectedItem.title,
                content = selectedItem.content,
                image = selectedItem.image
            )
        }
    }
}