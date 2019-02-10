package no.asmadsen.databindingbug


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_first.*
import no.asmadsen.databindingbug.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {
    private lateinit var vm : FirstViewModel
    /**
     * Here I create a MutableLiveData that will contain the Id of the currently selected RadioButton
     * this will be passed to the View to be Bindable
     */
    private lateinit var selectedId : MutableLiveData<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = ViewModelProviders.of(this).get(FirstViewModel::class.java)
        selectedId = MutableLiveData<Int>()

        /**
         * Here I set up the synchronisation between the state saved inside the ViewModel
         * and the selectedId MutableLiveData.
         */
        vm.gameMode.observe(this, Observer { gameMode ->
            if (gameMode == null) return@Observer
            val id = when (gameMode) {
                GameMode.SinglePlayer -> R.id.radio_button__single_player
                GameMode.TwoPlayer -> R.id.radio_button__two_player
            }

            if (selectedId.value != id) {
                selectedId.postValue(id)
            }
        })
        selectedId.observe(this, Observer { id ->
            val mode = when (id) {
                R.id.radio_button__single_player -> GameMode.SinglePlayer
                R.id.radio_button__two_player -> GameMode.TwoPlayer
                else -> null
            } ?: return@Observer
            vm.setGameMode(mode)
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentFirstBinding>(inflater, R.layout.fragment_first, container, false)
        binding.lifecycleOwner = this
        binding.selectedId = selectedId
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button__next_fragment.setOnClickListener {
            findNavController().navigate(R.id.action_firstFragment_to_secondFragment)
        }
    }
}
