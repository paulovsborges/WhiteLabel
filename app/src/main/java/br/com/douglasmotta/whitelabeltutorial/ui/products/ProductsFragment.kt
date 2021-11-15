package br.com.douglasmotta.whitelabeltutorial.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import br.com.douglasmotta.whitelabeltutorial.R
import br.com.douglasmotta.whitelabeltutorial.databinding.FragmentProductsBinding
import br.com.douglasmotta.whitelabeltutorial.domain.model.Product
import br.com.douglasmotta.whitelabeltutorial.util.PRODUCT_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment() {

    private val viewModel: ProductsViewModel by viewModels()
    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private val adapterList = ProductsAdapter()
    private val newAdapter = ProductsListAdapter()

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
        getProducts()
        observeVm()
        setRecyclerView()
        setListeners()
        observeNavBackStack()


        val fakeList = listOf(
            Product("", "", 0.0, ""),
            Product("", "", 0.0, ""),
            Product("", "", 0.0, ""),
            Product("", "", 0.0, ""),
            Product("", "", 0.0, ""),
        )

        newAdapter.submitList(fakeList)
    }

    private fun observeVm() {

        viewModel.productsList.observe(viewLifecycleOwner) {
//            adapterList.setList(it)

            newAdapter.submitList(it)
            binding.swipeProduct.isRefreshing = false
        }

        viewModel.buttonVisibility.observe(viewLifecycleOwner) { visibility ->
            binding.fabAddProduct.visibility = visibility
        }
    }

    private fun setRecyclerView() {
        binding.mainRecycler.apply {
            setHasFixedSize(true)
            adapter = adapterList
        }
    }

    private fun setListeners() {

        binding.run {
            fabAddProduct.setOnClickListener {
                findNavController().navigate(R.id.action_productsFragment_to_addProductFragment)
            }

            swipeProduct.setOnRefreshListener {
                getProducts()
            }
        }
    }

    private fun getProducts() {
        viewModel.getProducts()
    }

    private fun observeNavBackStack() {
        findNavController().run {
            val navBackStackEntry = getBackStackEntry(R.id.productsFragment)
            val savedStateHandle = navBackStackEntry.savedStateHandle
            val observer = LifecycleEventObserver { _, Event ->
                if (Event == Lifecycle.Event.ON_RESUME && savedStateHandle.contains(PRODUCT_KEY)) {
                    val product = savedStateHandle.get<Product>(PRODUCT_KEY)
                    val oldList = adapterList.currentList()
                    val newList = oldList.toMutableList().apply {
                        product?.let {
                            add(it)
                        }
                    }
                    adapterList.setList(newList)
                    binding.mainRecycler.smoothScrollToPosition(newList.size - 1)
                    savedStateHandle.remove<Product>(PRODUCT_KEY)
                }
            }

            navBackStackEntry.lifecycle.addObserver(observer)

            viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    navBackStackEntry.lifecycle.removeObserver(observer)
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}