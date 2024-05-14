import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airneis_sdv_app.model.Product
import com.example.airneis_sdv_app.model.ProductsResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ProductViewModel : ViewModel() {
    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    init {
    }

    fun getProductsByCategory(categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val url = URL("https://c1bb0d8a5f1d.airneis.net/api/products?categories=$categoryId")
            val connection = url.openConnection() as HttpURLConnection
            try {
                connection.connect()
                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = reader.readText()
                    reader.close()

                    val productsResponse = Gson().fromJson(response, ProductsResponse::class.java)
                    _products.value = productsResponse.products
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                connection.disconnect()
            }
        }
    }

}