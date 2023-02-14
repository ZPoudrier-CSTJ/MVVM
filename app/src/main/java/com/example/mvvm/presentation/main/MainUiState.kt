package com.example.mvvm.presentation.main

import com.example.mvvm.domain.models.Pilot

//class PilotUiState(val isSucces: Boolean, val pilot: Pilot)

sealed class MainUiState {
    class Succes(val pilot: Pilot): MainUiState()
    object Loading: MainUiState() //N'est pas toujours presente
    class Error(val message: String): MainUiState()
    object Empty: MainUiState() //N'est toujours presente
}