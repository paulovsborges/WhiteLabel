package br.com.douglasmotta.whitelabeltutorial.ui.products

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.douglasmotta.whitelabeltutorial.config.Config
import br.com.douglasmotta.whitelabeltutorial.domain.model.Product
import br.com.douglasmotta.whitelabeltutorial.domain.usecase.GetProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val useCase: GetProductsUseCase,
    config: Config
) : ViewModel() {

    private val _productsList = MutableLiveData<List<Product>>()
    val productsList: LiveData<List<Product>> = _productsList

    private val _buttonVisibility = MutableLiveData(config.addButtonVisibility)
    val buttonVisibility: LiveData<Int> = _buttonVisibility

    fun getProducts() {

        viewModelScope.launch {
            try {
                val products = useCase()
                _productsList.value = products
            } catch (e: Exception) {
                Log.d(TAG, "getProducts: $e")
            }
        }
    }

    companion object {
        const val TAG = "getProductsViewModel"
    }
}