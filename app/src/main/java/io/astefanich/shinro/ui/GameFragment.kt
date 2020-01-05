package io.astefanich.shinro.ui


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.GameFragmentBinding
import io.astefanich.shinro.di.DaggerAppComponent
import io.astefanich.shinro.di.game.GameComponent
import io.astefanich.shinro.di.game.GameModule
import io.astefanich.shinro.viewmodels.GameViewModel
import io.astefanich.shinro.viewmodels.ViewModelFactory
import javax.inject.Inject

class GameFragment : Fragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: GameViewModel
    private lateinit var binding: GameFragmentBinding
    private lateinit var gameComponent: GameComponent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)
        val gameFragmentArgs by navArgs<GameFragmentArgs>()
        var boardId = gameFragmentArgs.boardId

        if (boardId > 5)
            findNavController().navigate(GameFragmentDirections.actionGameToCheckBack())

        gameComponent = DaggerAppComponent
            .builder()
            .application(activity!!.application)
            .build()
            .getGameComponentBuilder()
            .gameModule(GameModule(boardId))
            .build()

        gameComponent.inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GameViewModel::class.java)

        binding.nextArrow.setOnClickListener { view ->
            view.findNavController()
                .navigate(
                    GameFragmentDirections.actionGameDestinationSelf(
                        viewModel.boardId + 1
                    )
                )
        }
        binding.backArrow.setOnClickListener { view ->
            view.findNavController()
                .navigate(
                    GameFragmentDirections.actionGameDestinationSelf(
                        viewModel.boardId - 1
                    )
                )
        }

        binding.resetBoard.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(activity)
            dialogBuilder
                .setTitle("Reset Board")
                .setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("YES", DialogInterface.OnClickListener { dialog, id ->
                    viewModel.onReset()
                })
                .setNegativeButton("NO", DialogInterface.OnClickListener { dialog, id ->

                })
                .create()
                .show()
        }

        if (viewModel.boardId == 1)
            binding.backArrow.visibility = View.GONE

        binding.vm = viewModel
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.overflow_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!, view!!.findNavController())
                || super.onOptionsItemSelected(item)
    }
}
