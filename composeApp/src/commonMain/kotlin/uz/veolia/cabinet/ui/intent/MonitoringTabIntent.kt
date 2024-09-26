package uz.veolia.cabinet.ui.intent

sealed interface MonitoringTabIntent {
    object LogOut : MonitoringTabIntent {

    }

    object ToastHide : MonitoringTabIntent {

    }

    object Clear : MonitoringTabIntent {

    }

    object Search : MonitoringTabIntent {

    }

    object MonitoringReload : MonitoringTabIntent {

    }

    class Filter(val district : Int?,val service : String?,val startDate : Long,val endDate : Long) : MonitoringTabIntent
    class OnSearchChange(val search: String) : MonitoringTabIntent {

    }
}
