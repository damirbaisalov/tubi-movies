package com.example.groupproject.api

import android.util.Log
import com.example.groupproject.model.Account
import com.example.groupproject.model.GetMoviesResponse
import com.example.groupproject.model.Credits
import com.example.groupproject.model.Movie
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.Interceptor
import retrofit2.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.*

import java.util.concurrent.TimeUnit

object RetrofitMoviesService {
    const val BASE_URL = "https://api.themoviedb.org/3/" //base url of tmdb

    fun getMovieApi(): MovieApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttp())
            .build()
        return retrofit.create(MovieApi::class.java)
    }

    private fun getOkHttp(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
        return okHttpClient.build()
    }

    private fun getLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(logger = object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d("OkHttp", message)
            }
        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}

interface MovieApi {

    //POPULAR MOVIES
//    @GET("movie/popular")
//    fun getPopularMovies(
//        @Query("api_key") apiKey: String
//    ): Call<GetMoviesResponse>

    @GET("movie/popular")
    suspend fun getPopularMoviesCoroutine(
        @Query("api_key") apiKey: String
    ): Response<GetMoviesResponse>

    //MOVIE BY ID
    @GET("movie/{movie_id}")
    fun getMovieById(
        @Path("movie_id")  id: Int,
        @Query("api_key") apiKey: String
    ): Call<Movie>

    @GET("movie/{movie_id}")
    suspend fun getMovieByIdCoroutine(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String
    ): Response<Movie>

    //CREDITS FOR MOVIE ID
    @GET("movie/{movie_id}/credits")
    fun getCredits(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String
    ): Call<Credits>

    @GET("movie/{movie_id}/credits")
    suspend fun getCreditsCoroutine(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String
    ): Response<Credits>

    //TOPRATED MOVIES
//    @GET("movie/top_rated")
//    fun getTopRatedMovies(
//        @Query("api_key") apiKey: String
//    ): Call<GetMoviesResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMoviesCoroutine(
        @Query("api_key") apiKey: String
    ): Response<GetMoviesResponse>

    //UPCOMING MOVIES
//    @GET("movie/upcoming")
//    fun getUpcomingMovies(
//        @Query("api_key") apiKey: String
//    ) : Call<GetMoviesResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMoviesCoroutine(
        @Query("api_key") apiKey: String
    ): Response<GetMoviesResponse>
////////////////////////////////////////////////////////////////////
    //REQUEST TOKEN WHILE REGISTRATION
    @GET("authentication/token/new")
    fun getToken(
        @Query("api_key") apiKey: String
    ) : Call<RequestToken>

    //VALIDATION WITH ACCOUNT == request token
    @POST("authentication/token/validate_with_login")
    fun validation(
        @Query("api_key") apiKey: String,
        @Body validation: Validation
    ) : Call<RequestToken>

    //CREATE NEW SESSION
    @POST("authentication/session/new")
    fun createSession(
        @Query("api_key") apiKey: String,
        @Body  token: RequestToken
    ) : Call<Session>
    ////////////////////////////////////
    @GET("account") ///////account
    fun getAccount(
        @Query("api_key")apiKey:String,
        @Query("session_id") sessionId: String
    ): Call<Account>
    /////////////////////////////////
    //ADD MOVIE TO FAVORITE
    @POST("account/{account_id}/favorite")
    fun addFavorite(
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId : String,
        @Body favoriteRequest: FavoriteRequest
    ) : Call<FavoriteResponse>

    //GET LIST OF FAVORITE MOVIES
    @GET("account/{account_id}/favorite/movies")
    fun getFavorite(
        @Query("api_key") apiKey: String,
        @Query("session_id") sessionId: String
    ) : Call<GetMoviesResponse>
}