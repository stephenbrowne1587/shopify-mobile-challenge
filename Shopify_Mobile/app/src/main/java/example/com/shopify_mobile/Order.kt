package example.com.shopify_mobile

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.util.Spliterators.iterator
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus

/**
 * Created by steph on 8/14/2017.
 */
class Order{
    val name: String
    val total_price: Double
    val items: ArrayList<Item>

    constructor(name: String, total_price: Double, items: ArrayList<Item>){
        this.name = name
        this.total_price = total_price
        this.items = items
    }
    constructor(order_data: JSONObject){
        val billing_address: JSONObject?
        billing_address = order_data.optJSONObject("billing_address")
        val name_string: String
        if (billing_address != null){
            name_string = billing_address.optString("name")
        }else{
            name_string = ""
        }
        this.name = name_string

        val total_price_double = order_data.optDouble("total_price")
        this.total_price = total_price_double

        val items_json_array: JSONArray = order_data.optJSONArray("line_items")
        val items_arraylist: ArrayList<Item> = arrayListOf()
        for (i in 0.until(items_json_array.length())){
            val item_json: JSONObject = items_json_array.opt(i) as JSONObject
            val new_item: Item = Item(item_json)
            items_arraylist.add(new_item)
        }
        this.items = items_arraylist


    }

    }