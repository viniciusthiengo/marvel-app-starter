package vini.lop.io.marvelappstarter.ui.state

sealed class ResourceState<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : ResourceState<T>(data)

    open class Error<T>(
        message: String,
        data: T? = null
    ) : ResourceState<T>(data, message)

    class InternetError<T> : Error<T>(message = "Erro de conexão com a Internet.")

    class ParseError<T> : Error<T>(message = "Falha na conversão de dados.")

    class Loading<T> : ResourceState<T>()

    class Empty<T> : ResourceState<T>()
}
