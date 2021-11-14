package br.com.douglasmotta.whitelabeltutorial.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import br.com.douglasmotta.whitelabeltutorial.databinding.FragmentProductsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private val viewModel: ProductsViewModel by viewModels()
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val adapterList = ProductsListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProducts()
        observeVm()
        setRecyclerView()
    }

    private fun observeVm() {

        viewModel.productsList.observe(viewLifecycleOwner) {
//            binding.mainRecycler.adapter = ProductsAdapter(it)

            adapterList.submitList(it)
        }
    }

    private fun setRecyclerView() {
        binding.mainRecycler.apply {
            setHasFixedSize(true)
            adapter = adapterList
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}