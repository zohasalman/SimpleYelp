package edu.stanford.zoha.simpleyelp

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.app.Activity as Activity1
import android.content.Context as Context1
import android.widget.Toast.makeText as makeText1

private const val TAG = "MainActivity"
private const val Base_URL = "https://api.yelp.com/v3/"
private const val API_KEY = "THrIQTeAHc3gzTUWq3EVDbqH5TVcWr7Rgf3HtekLo_PFBlLu4Y6DDFA4EN7faLPzgxrLPn6NtBvEO0860GTPPrXPqykj87l017P_Z1RjUOkmFDifXZGoiEoIRPSFYXYx"

private lateinit var rvRestaurants: RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvRestaurants = findViewById(R.id.rvRestaurants)

        val restaurants = mutableListOf<YelpRestaurants>()
        val adapter = RestaurantsAdapter(this, restaurants)
        rvRestaurants.adapter = adapter
        rvRestaurants.layoutManager = LinearLayoutManager(this)

        NetworkConnectionInterceptor(context = this).isConnectionOn()

        val retrofit =
            Retrofit.Builder()
                .baseUrl(Base_URL).addConverterFactory(GsonConverterFactory.create())
                .build()


        val yelpService = retrofit.create(YelpService::class.java)
        yelpService.searchRestaurants("Bearer $API_KEY", "Avocado Toast", "New York")
            .enqueue(object : Callback<YelpSearchResult> {

                override fun onResponse(
                    call: Call<YelpSearchResult>,
                    response: Response<YelpSearchResult>
                ) {
                    Log.i(TAG, "onResponse $response")
                    val body = response.body()
                    if (body == null) {
                        Log.w(TAG, "Did not receive valid response body from Yelp API... exiting")
                        return
                    }
                    restaurants.addAll(body.restaurants)
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<YelpSearchResult>, t: Throwable) {
                    Log.i(TAG, "onFailure $t")
                }


            })

    }



}

    private lateinit var tvName: TextView

    class RestaurantsAdapter(val context: Context1, val restaurants: List<YelpRestaurants>) :

        RecyclerView.Adapter<RestaurantsAdapter.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false))
        }
        override fun getItemCount() = restaurants.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val restaurant = restaurants[position]
            holder.bind(restaurant)
        }

        inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            fun bind(restaurant: YelpRestaurants) {
                itemView.findViewById<TextView>(R.id.tvName).text= restaurant.name
                itemView.findViewById<RatingBar>(R.id.ratingBar).rating= restaurant.rating.toFloat()
                itemView.findViewById<TextView>(R.id.tvNumReviews).text= "${restaurant.numReviews} Reviews"
                itemView.findViewById<TextView>(R.id.tvAddress).text= restaurant.location.address
                itemView.findViewById<TextView>(R.id.tvCategory).text= restaurant.categories[0].title
                itemView.findViewById<TextView>(R.id.tvDistance).text= restaurant.displayDistance()
                itemView.findViewById<TextView>(R.id.tvPrice).text= restaurant.price
                Glide.with(context).load(restaurant.imageUrl).apply(RequestOptions().transforms(
                    CenterCrop(), RoundedCorners(20)
                )).into(itemView.findViewById<ImageView>(R.id.imageView))
            }

        }
    }

