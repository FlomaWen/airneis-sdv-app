import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MaterialsResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("materials")
    val materials: List<Material>
)

@Serializable
data class Material(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("updatedAt")
    val updatedAt: String
)
