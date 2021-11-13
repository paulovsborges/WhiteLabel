package br.com.douglasmotta.whitelabeltutorial.domain.usecase

import android.net.Uri
import br.com.douglasmotta.whitelabeltutorial.data.ProductRepository
import br.com.douglasmotta.whitelabeltutorial.domain.model.Product
import java.util.*
import javax.inject.Inject

class CreateProductUseCaseImpl @Inject constructor(
    private val useCase: UploadProductImageUseCase,
    private val repository: ProductRepository
) : CreateProductUseCase {

    override suspend fun invoke(description: String, price: Double, imageUri: Uri): Product {
        return try {
            val imageUrl = useCase(imageUri)
            val product = Product(UUID.randomUUID().toString(), description, price, imageUrl)
            repository.createProduct(product)
        } catch (e: Exception) {
            throw  e
        }
    }
}