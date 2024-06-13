package com.example.busappics4u.ui

import androidx.lifecycle.viewmodel.viewModelFactory

object AppViewModelProvider {
    val Factory = viewModelFactory {

//        // Initializer for ItemEditViewModel
//        initializer {
//            ItemEditViewModel(
//                this.createSavedStateHandle(),
//                inventoryApplication().container.itemsRepository
//            )
//        }
//        // Initializer for ItemEntryViewModel
//        initializer {
//            ItemEntryViewModel(inventoryApplication().container.itemsRepository)
//        }
//
//        // Initializer for ItemDetailsViewModel
//        initializer {
//            ItemDetailsViewModel(
//                this.createSavedStateHandle(),
//                inventoryApplication().container.itemsRepository
//            )
//        }
//
//        // Initializer for HomeViewModel
//        initializer {
//            HomeViewModel(inventoryApplication().container.itemsRepository)
//        }
    }
}

///**
// * Extension function to queries for [Application] object and returns an instance of
// * [InventoryApplication].
// */
//fun CreationExtras.busApplication(): BusApplication =
//    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as BusApplication)