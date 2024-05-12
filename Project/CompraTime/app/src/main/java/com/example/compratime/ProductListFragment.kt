package com.example.compratime

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.compratime.databinding.FragmentProductListBinding
import com.google.android.material.snackbar.Snackbar

class ProductListFragment : Fragment() {
    private var _binding: FragmentProductListBinding? = null
    private val binding:FragmentProductListBinding
        get() = _binding!!

    private val TAG = "ProductListFragment"

    val model: GameViewModel by viewModels(
        ownerProducer = { this.requireActivity()}
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        val view = binding.root

        Log.d(TAG, "onCreateView: Fragment view created")

        binding.btnAddProducto.setOnClickListener {
            val productName = binding.productName.text.toString()
            val productPriceText = binding.productPrice.text.toString()
            var msg = ""
            if (productName.isNotEmpty() && productPriceText.isNotEmpty()) {
                val productPrice = productPriceText.toDouble() // Convertir el precio a Double

                // Crear un nuevo objeto Producto con los datos ingresados por el usuario
                val nuevoProducto = Producto(productName, productPrice)


                model.newProduct(nuevoProducto)

                mostrarProductos()

                msg = "Nuevo producto añadido: $productName - $productPrice"

                // Limpiar los EditText después de agregar el producto
                binding.productName.text = null
                binding.productPrice.text = null


            } else {
                msg = "Por favor, completa todos los campos."
            }
            Snackbar.make(binding.btnAddProducto, msg, Snackbar.LENGTH_SHORT).show()
        }

        mostrarProductos()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: Fragment view created")
        mostrarProductos()
    }


    private fun mostrarProductos() {
        Log.d(TAG, "mostrarProductos: Mostrando productos")
        // Limpiar el contenedor de la lista de productos
        binding.productListLayout.removeAllViews()

        // Obtener la lista de productos del ViewModel
        val productos = model.getProductos()

        // Verificar si la lista de productos no es nula ni está vacía
        if (!productos.isNullOrEmpty()) {
            // Iterar sobre la lista de productos y agregar cada uno a la vista
            for (producto in productos) {
                val productTextView = TextView(requireContext())
                productTextView.text = "${producto.nombre} - ${producto.precio} €"
                binding.productListLayout.addView(productTextView)
            }
        } else{
            Log.d(TAG, "mostrarProductos: No hay productos para mostrar")
        }
    }

}