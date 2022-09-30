package vttp2022.assessment.csf.orderbackend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;
import vttp2022.assessment.csf.orderbackend.repositories.OrderRepository;

@Service
public class OrderService {

	@Autowired
	private PricingService priceSvc;

	@Autowired
	OrderRepository orderRepo;

	// POST /api/order
	// Create a new order by inserting into orders table in pizzafactory database
	// IMPORTANT: Do not change the method's signature
	public void createOrder(Order order) {
		orderRepo.addOrder(order);
	}

	// GET /api/order/<email>/all
	// Get a list of orders for email from orders table in pizzafactory database
	// IMPORTANT: Do not change the method's signature
	public List<OrderSummary> getOrdersByEmail(String email) {
		// Use priceSvc to calculate the total cost of an order
		List<Order> orders = orderRepo.getOrders(email);
		List<OrderSummary> summaries = orders.stream().map(order -> createOrderSummary(order)).toList();
		return summaries;
	}

	private OrderSummary createOrderSummary(Order o){
		OrderSummary os = new OrderSummary();
		os.setOrderId(o.getOrderId());
		os.setName(o.getName());
		os.setEmail(o.getEmail());
		Float total = 0f;
		total += priceSvc.size(o.getSize());
		total += priceSvc.sauce(o.getSauce());
		if (o.isThickCrust())
			total += priceSvc.thickCrust();
		else
			total += priceSvc.thinCrust();
		for (String topping : o.getToppings()) {
			total += priceSvc.topping(topping);
		}
		os.setAmount(total);
		return os;
	}
}
