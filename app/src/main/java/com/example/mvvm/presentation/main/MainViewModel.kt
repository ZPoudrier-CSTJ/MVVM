package com.example.mvvm.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.core.Constants
import com.example.mvvm.domain.models.Pilot
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val pilot = Pilot( "BigPotato", 18)

    private val _mainUiState = MutableStateFlow<MainUiState>(MainUiState.Empty)
    val mainUiState = _mainUiState.asStateFlow()

    init {
        _mainUiState.update {
            //MainUiState.Succes(pilot)
           MainUiState.Succes(pilot)
        }
    }

    fun fly(revolution: Int, isTraining: Boolean){
        if(pilot.canFly()){
            viewModelScope.launch {

                //1. Changement d'état pour Loading (animating)
                _mainUiState.update {
                    MainUiState.Loading
                }

                //2. Attendre x millisecondes
                delay(Constants.REVOLUTION_DURATION * revolution)

                //3. Voler
                pilot.fly(revolution, isTraining)

                //4. Changement d'état pour Success

                _mainUiState.update {
                    MainUiState.Succes(pilot)
                }

            }



        } else {
            _mainUiState.update {
                MainUiState.Error("Can't Fly")
            }

        }
    }

    fun recharge() {
        pilot.recharge()
        _mainUiState.value = MainUiState.Succes(pilot)
    }
}