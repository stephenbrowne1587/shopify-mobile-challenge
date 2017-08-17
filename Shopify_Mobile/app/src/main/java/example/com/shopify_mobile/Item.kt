package example.com.shopify_mobile

import org.json.JSONObject

/**
 * Created by steph on 8/14/2017.
 */
class Item{
    val title: String
    val quantity: Int


    constructor(title: String, quantity: Int){
        this.title = title
        this.quantity = quantity

    }
    constructor(item_data: JSONObject){
        val title_string = item_data.optString("title")
        this.title = title_string
        val quantity_int = item_data.optInt("quantity")
        this.quantity = quantity_int
    }
}
