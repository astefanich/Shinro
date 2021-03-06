package io.astefanich.shinro.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.astefanich.shinro.R
import io.astefanich.shinro.databinding.FragmentStatisticsBinding
import io.astefanich.shinro.viewmodels.StatisticsViewModel
import io.astefanich.shinro.viewmodels.ViewModelFactory
import javax.inject.Inject

class StatisticsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: StatisticsViewModel

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity)
            .mainActivityComponent
            .getStatisticsComponent()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StatisticsViewModel::class.java)
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_statistics, container, false)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        viewModel.show.observe(viewLifecycleOwner, { calculated ->
            if (calculated) {
                binding.progressBar.visibility = View.GONE
                binding.progressText.visibility = View.GONE
                binding.statisticsLayout.visibility = View.VISIBLE
            }
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}