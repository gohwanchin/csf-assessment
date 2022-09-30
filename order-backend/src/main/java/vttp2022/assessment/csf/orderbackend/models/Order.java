package vttp2022.assessment.csf.orderbackend.models;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.*;

// IMPORTANT: You can add to this class, but you cannot delete its original content

public class Order {

	private Integer orderId;
	private String name;
	private String email;
	private Integer size;
	private String sauce;
	private Boolean thickCrust;
	private List<String> toppings = new LinkedList<>();
	private String comments;

	public void setOrderId(Integer orderId) { this.orderId = orderId; }
	public Integer getOrderId() { return this.orderId; }

	public void setName(String name) { this.name = name; }
	public String getName() { return this.name; }

	public void setEmail(String email) { this.email = email; }
	public String getEmail() { return this.email; }

	public void setSize(Integer size) { this.size = size; }
	public Integer getSize() { return this.size; }

	public void setSauce(String sauce) { this.sauce = sauce; }
	public String getSauce() { return this.sauce; }

	public void setThickCrust(Boolean thickCrust) { this.thickCrust = thickCrust; }
	public Boolean isThickCrust() { return this.thickCrust; }

	public void setToppings(List<String> toppings) { this.toppings = toppings; }
	public List<String> getToppings() { return this.toppings; }
	public void addTopping(String topping) { this.toppings.add(topping); }

	public void setComments(String comments) { this.comments = comments; }
	public String getComments() { return this.comments; }

	public static Order create(String json) {
		Order o = new Order();
		JsonObject obj = Json.createReader(new StringReader(json)).readObject();
		o.setName(obj.getString("name"));
		o.setEmail(obj.getString("email"));
		o.setSize(obj.getInt("size"));
		if (obj.getString("base").equals("thick")) 
			o.setThickCrust(true);
		else
			o.setThickCrust(false);
		o.setSauce(obj.getString("sauce"));
		JsonArray toppings = obj.getJsonArray("toppings");
		List<String> l = toppings.getValuesAs(JsonString::getString);
		o.setToppings(l);
		o.setComments(obj.getString("comments"));
		return o;
	}

	public static Order create(SqlRowSet rs) {
		Order o = new Order();
		o.setOrderId(rs.getInt("order_id"));
		o.setName(rs.getString("name"));
		o.setEmail(rs.getString("email"));
		o.setSize(rs.getInt("pizza_size"));
		o.setThickCrust(rs.getBoolean("thick_crust"));
		o.setSauce(rs.getString("sauce"));
		List<String> list = Stream.of(rs.getString("toppings").split(",",-1)).toList();
		o.setToppings(list);
		o.setComments(rs.getString("comments"));
		return o;
	}
	public JsonObject toJson() {
		JsonArrayBuilder ab = Json.createArrayBuilder();
		for (String topping : toppings)
			ab.add(topping);
		JsonArray arr = ab.build();
		return Json.createObjectBuilder()
				.add("name", name)
				.add("email", email)
				.add("size", size)
				.add("thickCrust", thickCrust)
				.add("sauce", sauce)
				.add("toppings", arr)
				.add("comments", comments)
				.build();
	}
}
