import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.stanford.zoha.simpleyelp.R
import edu.stanford.zoha.simpleyelp.YelpRestaurants

private lateinit var tvName: TextView

class RestaurantsAdapter(val context: Context, val restaurants: List<YelpRestaurants>) :

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
            Glide.with(context).load(restaurant.imageUrl).into(itemView.findViewById<ImageView>(R.id.imageView))
        }

    }
}