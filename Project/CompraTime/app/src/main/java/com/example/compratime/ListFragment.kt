package com.example.compratime

import android.R
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.compratime.databinding.FragmentListBinding
import com.google.android.material.snackbar.Snackbar
import androidx.lifecycle.observe

class ListFragment : Fragment() {
    private var _binding:FragmentListBinding? = null
    private val binding:FragmentListBinding
        get() = _binding!!

    val model: GameViewModel by viewModels(
        ownerProducer = { this.requireActivity()}
    )


    private val TAG = "ProductListFragment"

    private lateinit var productList: List<Producto>
    private lateinit var selectedProduct: Producto

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        val view = binding.root

        // Simulando la lista de productos guardados
        productList = model.getProductos() ?: emptyList()

        selectedProduct = productList[0]

        // Configurar el adaptador para el Spinner
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, productList.map { it.nombre })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerProductos.adapter = adapter

        // Manejar la selección del Spinner
        binding.spinnerProductos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Aquí puedes manejar la selección del producto
                selectedProduct = productList[position]
                // Puedes usar selectedProduct para lo que necesites, como agregarlo a la lista de la compra
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Manejar la falta de selección, si es necesario
            }
        }

        binding.btnAddProducto.setOnClickListener {
            // Verificar si hay un producto seleccionado
            if (::selectedProduct.isInitialized) {
                // Agregar el producto seleccionado a la lista de la compra
                model.agregarProducto(selectedProduct)

                mostrarProductos()

                model.updateTotalPrice()

                // Mostrar un mensaje de confirmación
                val msg = "Producto agregado a la lista de compra: ${selectedProduct.nombre}"
                Snackbar.make(binding.btnAddProducto, msg, Snackbar.LENGTH_SHORT).show()
            } else {
                // Si no hay un producto seleccionado, mostrar un mensaje de error
                Snackbar.make(binding.btnAddProducto, "Por favor, selecciona un producto", Snackbar.LENGTH_SHORT).show()
            }
        }
        mostrarProductos()

        // Observar el cambio en el precio total desde el ViewModel
        model.totalPrice.observe(viewLifecycleOwner) { totalPrice ->
            // Actualizar el precio total en la interfaz de usuario
            binding.txtTotalPrice.text = String.format("%.2f €", totalPrice)
        }

        return view
    }

    private fun mostrarProductos() {
        Log.d(TAG, "mostrarProductos: Mostrando productos")
        // Limpiar el contenedor de la lista de productos
        binding.listLayout.removeAllViews()

        // Obtener la lista de la compra del ViewModel
        val productos = model.getCompra()

        // Verificar si la lista de productos no es nula ni está vacía
        if (!productos.isNullOrEmpty()) {
            // Iterar sobre la lista de productos y agregar cada uno a la vista
            for (producto in productos) {
                val productTextView = TextView(requireContext())
                productTextView.text = "${producto.nombre} - ${producto.precio} €"
                binding.listLayout.addView(productTextView)
            }
        } else{
            Log.d(TAG, "mostrarProductos: No hay productos para mostrar")
        }
    }
}