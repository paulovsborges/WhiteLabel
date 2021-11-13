package br.com.douglasmotta.whitelabeltutorial.ui.addproduct

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import br.com.douglasmotta.whitelabeltutorial.databinding.AddProductFragmentBinding
import br.com.douglasmotta.whitelabeltutorial.util.CurrencyTextWatcher
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProductFragment : BottomSheetDialogFragment() {

    private val viewModel : AddProductViewModel by viewModels()
    private var _binding: AddProductFragmentBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imageUri = uri
            binding.imageProduct.setImageURI(uri)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddProductFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        observeVMEvents()
    }

    private fun observeVMEvents() {
        viewModel.imageUriErrorResId.observe(viewLifecycleOwner) { drawable ->

            binding.imageProduct.setBackgroundResource(drawable)
        }

        viewModel.descriptionFieldErrorResId.observe(viewLifecycleOwner) { stringResId ->

            binding.container1.setError(stringResId)
        }

        viewModel.priceErrorFieldResId.observe(viewLifecycleOwner) { stringResId ->
            binding.container2.setError(stringResId)
        }
    }

    private fun setListeners() {

        binding.imageProduct.setOnClickListener {
            chooseImage()
        }

        binding.btnAddProduct.setOnClickListener {
            val description = binding.editDescription.text.toString()
            val price = binding.editPrice.text.toString()

            viewModel.createProduct(description, price, imageUri)
        }

        binding.editPrice.run {
            addTextChangedListener(CurrencyTextWatcher(this))
        }
    }

    private fun chooseImage() {
        getContent.launch("image/*")
    }

    private fun TextInputLayout.setError(stringResId: Int?) {
        error = if (stringResId != null) {
            getString(stringResId)
        } else null
    }
}