import com.example.airneis_sdv_app.model.Category
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductsResponse(
    @SerialName("success")
    val success: Boolean,

    @SerialName("products")
    val products: List<Product>,

    @SerialName("limit")
    val limit: Int,

    @SerialName("page")
    val page: Int,

    @SerialName("total")
    val total: Int
)

@Serializable
data class Product(
    @SerialName("priority")
    val priority: Int,

    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("description")
    val description: String,

    @SerialName("slug")
    val slug: String,

    @SerialName("price")
    val price: String,

    @SerialName("stock")
    val stock: Int? = null,

    @SerialName("createdAt")
    val createdAt: String,

    @SerialName("updatedAt")
    val updatedAt: String,

    @SerialName("category")
    val category: Category? = null,

    @SerialName("backgroundImage")
    val backgroundImage: BackgroundImage? = null,

    @SerialName("images")
    val images: List<Image>? = null,

    @SerialName("materials")
    val materials: List<Material>? = null
)

@Serializable
data class Image(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("filename")
    val filename: String,

    @SerialName("type")
    val type: String,

    @SerialName("size")
    val size: Int,

    @SerialName("createdAt")
    val createdAt: String,

    @SerialName("updatedAt")
    val updatedAt: String
)

@Serializable
data class BackgroundImage(
    @SerialName("id")
    val id: Int,

    @SerialName("name")
    val name: String,

    @SerialName("filename")
    val filename: String,

    @SerialName("type")
    val type: String,

    @SerialName("size")
    val size: Int,

    @SerialName("createdAt")
    val createdAt: String,

    @SerialName("updatedAt")
    val updatedAt: String
)


