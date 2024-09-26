package uz.veolia.cabinet.data.model


enum class Role {
    REGION,
    CONSUMER,
    CONSUMER_JURIDICAL,
    CONSUMER_JURIDICAL_DETAIL,
    CONSUMER_JURIDICAL_DETAIL_EDIT,
    CONSUMER_JURIDICAL_CREATE,
    CONSUMER_PHYSICAL,
    CONSUMER_PHYSICAL_DETAIL,
    CONSUMER_PHYSICAL_DETAIL_EDIT,
    CONSUMER_PHYSICAL_CREATE,
    CONSUMER_PHYSICAL_DETAIL_ACCRUALS_TAB,
    CONSUMER_PHYSICAL_DETAIL_ACCRUALS_ADD,
    MONITORINGPAY,
    ADMINISTRATOR,
    SERVICE,
    METER,
    EMPLOYEE,
    UNKNOWN
}

fun String?.toRole(): Role {
    val role = Role.entries.find { it.name == this }
    return role ?: Role.UNKNOWN
}


lateinit var role: List<Role>

class RoleManager {
    companion object {
        fun canJuridical() : Boolean {
            return role.contains(Role.CONSUMER_JURIDICAL)
        }
        fun canPhysical() : Boolean {
            return role.contains(Role.CONSUMER_PHYSICAL)
        }
        fun canConsumerAdd(): Boolean {
            return role.contains(Role.CONSUMER_PHYSICAL_CREATE) || role.contains(Role.CONSUMER_JURIDICAL_CREATE)
        }

        fun canAddPhysical(): Boolean {
            return role.contains(Role.CONSUMER_PHYSICAL_CREATE)
        }

        fun canAddJuridical(): Boolean {
            return role.contains(Role.CONSUMER_JURIDICAL_CREATE)
        }

        fun canAccrualView(): Boolean {
            return role.contains(Role.CONSUMER_PHYSICAL_DETAIL_ACCRUALS_TAB)
        }

        fun canAccrualAdd(): Boolean {
            return role.contains(Role.CONSUMER_PHYSICAL_DETAIL_ACCRUALS_ADD)
        }

        fun canViewDetailsJuridical(): Boolean {
            return role.contains(Role.CONSUMER_JURIDICAL_DETAIL)
        }

        fun canViewDetailsPhysical(): Boolean {
            return role.contains(Role.CONSUMER_PHYSICAL_DETAIL)
        }

        fun hasConsumerAccess(): Boolean {
            return role.contains(Role.CONSUMER)
        }

        fun hasMonitoringPay(): Boolean {
            return role.contains(Role.MONITORINGPAY)
        }

        fun canEdit(type: String?): Boolean {
            return type == "1" && role.contains(Role.CONSUMER_PHYSICAL_DETAIL_EDIT) || type == "3" && role.contains(
                Role.CONSUMER_JURIDICAL_DETAIL_EDIT
            )
        }
    }
}
