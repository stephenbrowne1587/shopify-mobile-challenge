package example.com.shopify_mobile

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text

class MainActivityKotlin : AppCompatActivity() {
    var orders: ArrayList<Order> = arrayListOf()
    val NAPOLEON_BATZ: String = "Napoleon Batz"
    val AWESOME_BRONZE_BAG: String = "Awesome Bronze Bag"
    var get_results_button: RelativeLayout? = null
    var progress_spinner: ProgressBar? = null
    var napoleon_message: TextView? = null
    var napoleon_result: TextView? = null
    var bags_message: TextView? = null
    var bags_result: TextView? = null
    var clear_button: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        get_results_button = findViewById(R.id.get_results_button) as RelativeLayout
        progress_spinner = findViewById(R.id.progree_bar) as ProgressBar
        napoleon_message = findViewById(R.id.naopleon_message) as TextView
        napoleon_result = findViewById(R.id.napoleon_result) as TextView
        bags_message = findViewById(R.id.bags_message) as TextView
        bags_result = findViewById(R.id.bags_result) as TextView
        clear_button= findViewById(R.id.clear_button) as TextView

        setFonts()

        napoleon_message?.translationX = 1500f
        napoleon_result?.translationX = -1500f
        bags_message?.translationX = 1500f
        bags_result?.translationX = -1500f


        get_results_button?.setOnClickListener {
            get_results_button?.visibility = View.GONE
            progress_spinner?.visibility = View.VISIBLE
            getOrders()
        }
        clear_button?.setOnClickListener {
            napoleon_message?.animate()!!.translationX(1500f)
            napoleon_result?.animate()!!.translationX(-1500f)
            bags_message?.animate()!!.translationX(1500f)
            bags_result?.animate()!!.translationX(-1500f)
            get_results_button?.isEnabled = true
            get_results_button?.alpha = 1f
            clear_button?.visibility = View.GONE
        }

    }

    fun setFonts(){
        val my_font: Typeface = Typeface.createFromAsset(assets, "fonts/Montserrat-ExtraBold.ttf")
        napoleon_message?.typeface = my_font
        napoleon_result?.typeface = my_font
        bags_message?.typeface = my_font
        bags_result?.typeface = my_font
        clear_button?.typeface = my_font
    }

    fun getOrders() {

        val queue = Volley.newRequestQueue(this)
        val url : String = "https://shopicruit.myshopify.com/admin/orders.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6"
        val stringRequest = object : StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
            val jsonResponse: JSONObject
            val APIKey: Int?

            try {
                jsonResponse = JSONObject(response)
                val ordersArray: JSONArray = jsonResponse.getJSONArray("orders")
                Log.i("JSON response", ordersArray.length().toString())
                parseOrders(ordersArray)

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, Response.ErrorListener {
            showDialog()
        }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
//                params.put("key", value)
                //add any paramaters here
                return params
            }
        }
        queue.add(stringRequest)
    }
    fun parseOrders(orders_json_array: JSONArray){
        orders = arrayListOf()
        for (i in 0.until(orders_json_array.length())){
            val order_json: JSONObject = orders_json_array.get(i) as JSONObject
            val new_order: Order = Order(order_json)
            orders.add(new_order)
        }
        Log.i("orders arrays", orders.size.toString())
        var total_spent: Double = 0.0
        var total_bags: Int = 0
        for (order in orders){
            total_spent += if (order.name == NAPOLEON_BATZ) order.total_price else 0.0
            for (item in order.items){
                total_bags += if (item.title == AWESOME_BRONZE_BAG) item.quantity else 0
            }
        }
        displayResults(total_spent, total_bags)
    }
    fun displayResults(total_spent: Double, total_bags: Int){
        napoleon_result?.text = "$$total_spent"
        bags_result?.text = total_bags.toString()
        napoleon_message?.animate()!!.translationX(0f)
        napoleon_result?.animate()!!.translationX(0f)
        bags_message?.animate()!!.translationX(0f)
        bags_result?.animate()!!.translationX(0f)
        get_results_button?.visibility = View.VISIBLE
        progress_spinner?.visibility = View.GONE
        get_results_button?.alpha = 0.5f
        get_results_button?.isEnabled = false
        clear_button?.visibility = View.VISIBLE
    }

    fun showDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setMessage("Please check your network connection.").setTitle("Error")
        builder.setPositiveButton("Okay") { dialog, which -> }

        val dialog = builder.create()
        dialog.show()
        get_results_button?.visibility = View.VISIBLE
        progress_spinner?.visibility = View.GONE

    }
}
