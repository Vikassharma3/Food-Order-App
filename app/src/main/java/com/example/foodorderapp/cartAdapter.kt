import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapp.CartDataClass
import com.example.foodorderapp.R
import com.squareup.picasso.Picasso

class cartAdapter(
    private var cartList: MutableList<CartDataClass>,
    private val removeItemClickListener: (cartId: Int) -> Unit
) :
    RecyclerView.Adapter<cartAdapter.cartviewholder>() {

    class cartviewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val cartimg: ImageView = itemView.findViewById(R.id.cartimg)
        var carttitle: TextView = itemView.findViewById(R.id.carttitle)
        var cartprice: TextView = itemView.findViewById(R.id.cartprice)
        val removebtn: ImageView = itemView.findViewById(R.id.removeitem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartviewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return cartviewholder(view)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun onBindViewHolder(holder: cartviewholder, position: Int) {
        val cart = cartList[position]

        // Optimize image loading with Picasso
        Picasso.get().load(cart.img)
            .fit()
            .centerCrop()
            .into(holder.cartimg)

        holder.carttitle.text = cart.title
        holder.cartprice.text = "$" + cart.price.toString()

        var cartId = cart.id


        holder.removebtn.setOnClickListener {
            removeItemClickListener.invoke(position)
        }
    }
}
