package br.com.douglasmotta.whitelabeltutorial.domain.model

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    var id: String = "",
    var description: String = "null",
    var price: Double = 0.0,

    @get:PropertyName ("image_url")
    var imageUrl: String = ""
): Parcelable
