package com.example.mvvm.presentation.main

import com.example.mvvm.domain.models.Pilot

//class PilotUiState(val isSucces: Boolean, val pilot: Pilot)

sealed class PilotUiState {
    class Succes(val pilot: Pilot): PilotUiState()
    object Loading: PilotUiState()
    class Error(val message: String): PilotUiState()
    object Empty: PilotUiState()
}