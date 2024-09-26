package uz.veolia.cabinet.data.remote


sealed interface ResultData<out T> {
    class TimeOut<T> : ResultData<T>
    class Success<T>(val data : T) : ResultData<T>
    class Message<T>(val message: String,val code : Int = 0) : ResultData<T>
}

