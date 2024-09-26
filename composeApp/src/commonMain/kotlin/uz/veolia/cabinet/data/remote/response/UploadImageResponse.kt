package uz.veolia.cabinet.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import uz.veolia.cabinet.data.remote.request.SendCounterItem

@Serializable
data class UploadImageResponse(
    @SerialName("detail")
    val error: List<FileUploadErrorItem?>? = null,
    @SerialName("file_details")
    val fileDetails: List<FileDetail?>? = null
)

@Serializable
data class FileDetail(
    @SerialName("contentType")
    val contentType: String? = null,
    @SerialName("createdAt")
    val createdAt: String? = null,
    @SerialName("createdBy")
    val createdBy: String? = null,
    @SerialName("extension")
    val extension: String? = null,
    @SerialName("fileName")
    val fileName: String? = null,
    @SerialName("fileSize")
    val fileSize: Int? = null,
    @SerialName("id")
    val id: String? = null,
    @SerialName("pageCount")
    val pageCount: Int? = null,
    @SerialName("relativePath")
    val relativePath: String? = null,
    @SerialName("savedFileName")
    val savedFileName: String? = null,
    @SerialName("updatedAt")
    val updatedAt: String? = null,
    @SerialName("updatedBy")
    val updatedBy: String? = null,
    @SerialName("url")
    val url: String? = null
)

fun FileDetail.toRequest() = SendCounterItem(id, url, fileName, "")

@Serializable
data class FileUploadErrorItem(
    @SerialName("loc")
    val loc: List<String?>? = null,
    @SerialName("msg")
    val msg: String? = null,
    @SerialName("type")
    val type: String? = null
)