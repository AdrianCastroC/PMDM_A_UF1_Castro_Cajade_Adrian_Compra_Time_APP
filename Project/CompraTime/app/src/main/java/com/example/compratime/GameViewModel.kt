package com.example.compratime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel: ViewModel() {
    val productList = MutableLiveData<MutableList<Producto>>(
        mutableListOf(
            Producto("Leche", 2.50),
            Producto("Pan", 1.20),
            Producto("Huevos", 3.50),
            Producto("Arroz", 1.80),
            Producto("Pasta", 1.50),
            Producto("Aceite de oliva", 4.50),
            Producto("Café", 3.00),
            Producto("Azúcar", 1.00),
            Producto("Sal", 0.80),
            Producto("Pimienta", 1.20),
            Producto("Vinagre", 2.00),
            Producto("Zanahorias", 1.50),
            Producto("Manzanas", 2.00),
            Producto("Plátanos", 1.80),
            Producto("Naranjas", 2.50),
            Producto("Tomates", 2.00),
            Producto("Cebollas", 1.50),
            Producto("Patatas", 1.20),
            Producto("Pollo", 5.00),
            Producto("Pescado", 7.50)
        ))

    val buyList = MutableLiveData<MutableList<Producto>>(mutableListOf())

    private val _totalPrice = MutableLiveData<Double>(0.0)
    val totalPrice: LiveData<Double> = _totalPrice

    fun getProductos(): List<Producto>? {
        return productList.value.orEmpty().toList()
    }

    fun getCompra(): List<Producto>? {
        return buyList.value.orEmpty().toList()
    }

    fun updateTotalPrice() {
        val totalPrice = buyList.value?.sumByDouble { it.precio } ?: 0.0
        _totalPrice.value = totalPrice
    }

    fun newProduct(producto: Producto){
        val listaActual = productList.value ?: mutableListOf()
        listaActual.add(producto)
        productList.value = listaActual
    }

    fun agregarProducto(producto: Producto) {
        val listaActual = buyList.value ?: mutableListOf()
        listaActual.add(producto)
        buyList.value = listaActual
    }
}