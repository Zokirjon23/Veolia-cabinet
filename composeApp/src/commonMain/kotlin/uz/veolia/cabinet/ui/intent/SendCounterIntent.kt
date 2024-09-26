package uz.veolia.cabinet.ui.intent

import uz.veolia.cabinet.data.remote.request.SendCounterItem

sealed interface SendCounterIntent {
    data object OpenCameraScreen : SendCounterIntent
    data class Send(val list : List<SendCounterItem>) : SendCounterIntent
    data object ToastHide : SendCounterIntent
    data object Back : SendCounterIntent
    data object Add : SendCounterIntent
    data class Delete(val pos : Int) : SendCounterIntent
//    class ImageUpload(val imageByteArray: ByteArray?,val uri : Uri,val index : Int) : SendCounterIntent
    class OnInputChange(val data: String,val index: Int) : SendCounterIntent
    class DeleteItem(val index: Int) : SendCounterIntent
}
