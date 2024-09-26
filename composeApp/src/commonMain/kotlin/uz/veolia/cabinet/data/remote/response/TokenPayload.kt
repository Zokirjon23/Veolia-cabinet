package uz.veolia.cabinet.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenPayload(
    @SerialName("exp")
    val exp: Int? = null,
    @SerialName("iat")
    val iat: Int? = null,
    @SerialName("jti")
    val jti: String? = null,
    @SerialName("iss")
    val iss: String? = null,
//    @SerialName("aud")
//    val aud: String? = null,
    @SerialName("sub")
    val sub: String? = null,
    @SerialName("typ")
    val typ: String? = null,
    @SerialName("azp")
    val azp: String? = null,
    @SerialName("session_state")
    val sessionState: String? = null,
    @SerialName("acr")
    val acr: String? = null,
    @SerialName("allowed-origins")
    val allowedOrigins: List<String?>? = null,
    @SerialName("realm_access")
    val realmAccess: RealmAccess? = null,
    @SerialName("resource_access")
    val resourceAccess: ResourceAccess? = null,
    @SerialName("scope")
    val scope: String? = null,
    @SerialName("sid")
    val sid: String? = null,
    @SerialName("email_verified")
    val emailVerified: Boolean? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("preferred_username")
    val preferredUsername: String? = null,
    @SerialName("given_name")
    val givenName: String? = null,
    @SerialName("region")
    val region: List<Int>? = null,
    @SerialName("family_name")
    val familyName: String? = null,
    @SerialName("email")
    val email: String? = null
)

@Serializable
data class RealmAccess(
    @SerialName("roles")
    val roles: List<String?>? = null
)

@Serializable
data class ResourceAccess(
    @SerialName("cabinet-client")
    val cabinetClient: CabinetClient? = null,
    @SerialName("account")
    val account: Account? = null
)

@Serializable
data class CabinetClient(
    @SerialName("roles")
    val roles: List<String?>? = null
)

@Serializable
data class Account(
    @SerialName("roles")
    val roles: List<String?>? = null
)

