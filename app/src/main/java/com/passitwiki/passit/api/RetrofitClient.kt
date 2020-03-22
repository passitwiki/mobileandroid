package com.passitwiki.passit.api

//import io.reactivex.android.plugins.RxAndroidPlugins
//import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*


/**
 * An object which builds an api interface with the base url,
 * adds a gson factory to convert a json file to e.g. a list of objects,
 * attaches a client, finally building and creating a full-fledged object to use.
 * As it is built from an interface, you create an instance and get the function you need.
 */
object RetrofitClient {
    //TODO REMEMBER THE URL
//    private const val BASE_URL = "http://192.168.122.1:8000/api/"
    private const val BASE_URL = "https://passit.beyondthe.dev/api/"

    private var client: OkHttpClient =
        OkHttpClient.Builder()
            .connectionSpecs(Arrays.asList(ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val newRequest: Request = chain.request().newBuilder()
                        .build()
                    return chain.proceed(newRequest)
                }
            }).build()
//            .sslSocketFactory(customSslSocketFactory, trustManager)



    val instance: Api by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()

        retrofit.create(Api::class.java)
    }

//    var trustManager: X509TrustManager = defaultTrustManager()
//    var sslSocketFactory: SSLSocketFactory = defaultSslSocketFactory(trustManager)
//    var customSslSocketFactory: SSLSocketFactory =
//        object : DelegatingSSLSocketFactory(sslSocketFactory) {
//            @Throws(IOException::class)
//            protected fun configureSocket(socket: SSLSocket): SSLSocket? {
//                socket.enabledCipherSuites = javaNames(spec.cipherSuites())
//                return socket
//            }
//        }
//
//    /**
//     * Returns the VM's default SSL socket factory, using `trustManager` for trusted root
//     * certificates.
//     */
//    @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
//    private fun defaultSslSocketFactory(trustManager: X509TrustManager): SSLSocketFactory? {
//        val sslContext: SSLContext = SSLContext.getInstance("TLS")
//        sslContext.init(null, arrayOf(trustManager), null)
//        return sslContext.getSocketFactory()
//    }
//
//    /** Returns a trust manager that trusts the VM's default certificate authorities.  */
//    @Throws(GeneralSecurityException::class)
//    private fun defaultTrustManager(): X509TrustManager? {
//        val trustManagerFactory: TrustManagerFactory = TrustManagerFactory.getInstance(
//            TrustManagerFactory.getDefaultAlgorithm()
//        )
//        trustManagerFactory.init(null as KeyStore?)
//        val trustManagers: Array<TrustManager> = trustManagerFactory.getTrustManagers()
//        check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
//            ("Unexpected default trust managers:"
//                    + Arrays.toString(trustManagers))
//        }
//        return trustManagers[0] as X509TrustManager
//    }
//
//    private fun javaNames(cipherSuites: List<CipherSuite>): Array<String?>? {
//        val result = arrayOfNulls<String>(cipherSuites.size)
//        for (i in result.indices) {
//            result[i] = cipherSuites[i].javaName
//        }
//        return result
//    }
//    internal open class DelegatingSSLSocketFactory(protected val delegate: SSLSocketFactory) :
//        SSLSocketFactory() {
//        override fun getDefaultCipherSuites(): Array<String> {
//            return delegate.defaultCipherSuites
//        }
//
//        override fun getSupportedCipherSuites(): Array<String> {
//            return delegate.supportedCipherSuites
//        }
//
//        @Throws(IOException::class)
//        override fun createSocket(
//            socket: Socket?, host: String?, port: Int, autoClose: Boolean
//        ): Socket? {
//            return configureSocket(
//                delegate.createSocket(
//                    socket,
//                    host,
//                    port,
//                    autoClose
//                ) as SSLSocket
//            )
//        }
//
//        @Throws(IOException::class)
//        override fun createSocket(host: String?, port: Int): Socket? {
//            return configureSocket(delegate.createSocket(host, port) as SSLSocket)
//        }
//
//        @Throws(IOException::class)
//        override fun createSocket(
//            host: String?, port: Int, localHost: InetAddress?, localPort: Int
//        ): Socket? {
//            return configureSocket(
//                delegate.createSocket(
//                    host,
//                    port,
//                    localHost,
//                    localPort
//                ) as SSLSocket
//            )
//        }
//
//        @Throws(IOException::class)
//        override fun createSocket(host: InetAddress?, port: Int): Socket? {
//            return configureSocket(delegate.createSocket(host, port) as SSLSocket?)
//        }
//
//        @Throws(IOException::class)
//        override fun createSocket(
//            address: InetAddress?, port: Int, localAddress: InetAddress?, localPort: Int
//        ): Socket? {
//            return configureSocket(
//                delegate.createSocket(
//                    address, port, localAddress, localPort
//                ) as SSLSocket?
//            )
//        }
//
//        @Throws(IOException::class)
//        protected fun configureSocket(socket: SSLSocket?): SSLSocket? {
//            return socket
//        }
//
//    }
}