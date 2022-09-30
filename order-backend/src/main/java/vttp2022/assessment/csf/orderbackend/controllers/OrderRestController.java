package vttp2022.assessment.csf.orderbackend.controllers;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.json.*;
import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;
import vttp2022.assessment.csf.orderbackend.services.OrderService;

@RestController
@RequestMapping("/api")
public class OrderRestController {

    @Autowired
    OrderService orderService;

    private Logger logger = Logger.getLogger(OrderRestController.class.getName());

    @PostMapping("/order")
    public ResponseEntity<String> createOrder(@RequestBody String payload) {
        Order o;

        logger.info("Payload: %s".formatted(payload));

        try {
            o = Order.create(payload);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        orderService.createOrder(o);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("/order/{email}/all")
    public ResponseEntity<String> getOrders(@PathVariable String email) {
        logger.info(email);
        List<OrderSummary> list = orderService.getOrdersByEmail(email);
        JsonArrayBuilder ab = Json.createArrayBuilder();
        for (OrderSummary orderSummary : list) 
            ab.add(orderSummary.toJson());
        JsonArray array = ab.build();
        return ResponseEntity.status(HttpStatus.OK).body(array.toString());
    }
}
