package io.astefanich.shinro.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.InstructionsListFragmentBinding
import io.astefanich.shinro.di.instructions.DaggerInstructionsComponent
import io.astefanich.shinro.di.instructions.InstructionsComponent
import io.astefanich.shinro.di.instructions.InstructionsModule
import io.astefanich.shinro.domain.Instruction
import io.astefanich.shinro.domain.InstructionType
import io.astefanich.shinro.util.InstructionRecyclerAdapter
import javax.inject.Inject

class InstructionsListFragment : Fragment() {

    lateinit var instructionType: InstructionType
    lateinit var component: InstructionsComponent

    @Inject
    lateinit var items: List<Instruction>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: InstructionsListFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.instructions_list_fragment, container, false
        )

        val instructionsListArgs by navArgs<InstructionsListFragmentArgs>()
        instructionType = instructionsListArgs.instructionType

        component = DaggerInstructionsComponent
            .builder()
            .instructionsModule(InstructionsModule(instructionType))
            .build()

        component.inject(this)

        val recyclerAdapter = InstructionRecyclerAdapter(items)
        binding.instructionsRecyclerView.apply {
            adapter = recyclerAdapter
        }
        binding.fragment = this
        binding.lifecycleOwner = this
        return binding.root
    }

}