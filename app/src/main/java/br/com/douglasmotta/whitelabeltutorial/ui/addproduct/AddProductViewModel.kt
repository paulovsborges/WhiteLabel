package br.com.douglasmotta.whitelabeltutorial.ui.addproduct

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.douglasmotta.whitelabeltutorial.R
import br.com.douglasmotta.whitelabeltutorial.domain.model.Product
import br.com.douglasmotta.whitelabeltutorial.domain.usecase.CreateProductUseCase
import br.com.douglasmotta.whitelabeltutorial.util.fromCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val createProductUseCase: CreateProductUseCase
) : ViewModel() {

    private var isFormValid = false

    private val _imageUriErrorResId = MutableLiveData<Int>()
    val imageUriErrorResId: LiveData<Int> = _imageUriErrorResId

    private val _descriptionFieldErrorResId = MutableLiveData<Int?>()
    val descriptionFieldErrorResId: LiveData<Int?> = _descriptionFieldErrorResId

    private val _priceErrorFieldResId = MutableLiveData<Int?>()
    val priceErrorFieldResId: LiveData<Int?> = _priceErrorFieldResId

    private val _productCreated = MutableLiveData<Product>()
    val productCreated: LiveData<Product> = _productCreated

    fun createProduct(description: String, price: String, imageUri: Uri?) =
        viewModelScope.launch {
            isFormValid = true

            _imageUriErrorResId.postValue(getDrawableResIdIfNull(imageUri))
            _descriptionFieldErrorResId.postValue(getErrorStringResIdIfEmpty(description))
            _priceErrorFieldResId.postValue(getErrorStringResIdIfEmpty(price))

            if (isFormValid) {
                try {
                    imageUri?.let {
                        val product = createProductUseCase(description, price.fromCurrency(), it)
                        _productCreated.value = product
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "createProduct: $e ")

                }
            }
        }

    private fun getErrorStringResIdIfEmpty(value: String?): Int? {

        return if (value.isNullOrEmpty()) {
            isFormValid = false
            R.string.add_product_field_error
        } else null
    }

    private fun getDrawableResIdIfNull(value: Uri?): Int {

        return if (value == null) {
            isFormValid = false
            R.drawable.background_product_image_error
        } else R.drawable.background_product_image
    }

    companion object {
        const val TAG = "add product view model"
    }

}