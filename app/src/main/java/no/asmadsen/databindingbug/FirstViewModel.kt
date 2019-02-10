package no.asmadsen.databindingbug

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FirstViewModel : ViewModel() {
    private val _gameMode : MutableLiveData<GameMode> by lazy {
        MutableLiveData<GameMode>().also {
            it.value = GameMode.SinglePlayer
        }
    }

    val gameMode : LiveData<GameMode> = _gameMode

    fun setGameMode(value: GameMode) {
        if (_gameMode.value != value) {
            _gameMode.postValue(value)
        }
    }
}