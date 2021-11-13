package br.com.douglasmotta.whitelabeltutorial.data

import android.net.Uri
import br.com.douglasmotta.whitelabeltutorial.BuildConfig
import br.com.douglasmotta.whitelabeltutorial.domain.model.Product
import br.com.douglasmotta.whitelabeltutorial.util.COLLECTION_PRODUCTS
import br.com.douglasmotta.whitelabeltutorial.util.COLLECTION_ROOT
import br.com.douglasmotta.whitelabeltutorial.util.STORAGE_IMAGES
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class FirebaseProductDataSource @Inject constructor(
    firebaseFireStore: FirebaseFirestore,
    firebaseStorage: FirebaseStorage
) : ProductDataSource {

    private val documentReference =
        firebaseFireStore.document("$COLLECTION_ROOT/${BuildConfig.FIREBASE_FLAVOR_COLLECTION}/")

    private val storageReference = firebaseStorage.reference

    //return uur added products from firebase
    override suspend fun getProducts(): List<Product> {

        return suspendCoroutine { continuation ->
            val productReference = documentReference.collection(COLLECTION_PRODUCTS)
            productReference.get().addOnSuccessListener { documents ->

                val productsList = mutableListOf<Product>()
                for (document in documents) {
                    document.toObject(Product::class.java).run {
                        productsList.add(this)
                    }
                }

                continuation.resumeWith(Result.success(productsList))
            }
            productReference.get().addOnFailureListener {
                continuation.resumeWith(Result.failure(it))
            }
        }
    }

    //upload the image product to firebase storage
    override suspend fun uploadProductImage(imageUri: Uri): String {
        return suspendCoroutine { continuation ->
            val randomKey = UUID.randomUUID()
            val childReference = storageReference.child(
                "$STORAGE_IMAGES/${BuildConfig.FIREBASE_FLAVOR_COLLECTION}/$randomKey"
            )

            childReference.putFile(imageUri)
                .addOnSuccessListener { taskSnapShot ->
                    taskSnapShot.storage.downloadUrl.addOnSuccessListener { uri ->
                        continuation.resumeWith(Result.success(uri.toString()))
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }

    //creates the product in the firebase
    override suspend fun createProduct(product: Product): Product {
        return suspendCoroutine { continuation ->

            documentReference
                .collection(COLLECTION_PRODUCTS)
                .document(System.currentTimeMillis().toString())
                .set(product)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(product))
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }
}