package vttp2022.assessment.csf.orderbackend.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.assessment.csf.orderbackend.models.Order;

@Repository
public class OrderRepository {
    private static final String SQL_POST_ORDER 
            = "insert into orders (name, email, pizza_size, thick_crust, sauce, toppings, comments) values(?,?,?,?,?,?,?)";
    private static final String SQL_GET_ORDER_BY_EMAIL 
            = "select * from orders where email=?";

    @Autowired
    JdbcTemplate template;

    public Boolean addOrder(Order o) {
        int added = template.update(SQL_POST_ORDER, o.getName(), o.getEmail(), o.getSize(), o.isThickCrust(), o.getSauce(), String.join(",", o.getToppings()), o.getComments());
        return added > 0;
    }

    public List<Order> getOrders(String email){
        List<Order> list = new ArrayList<>();
        SqlRowSet rs = template.queryForRowSet(SQL_GET_ORDER_BY_EMAIL, email);
        while(rs.next()){
            Order o = Order.create(rs);
            list.add(o);
        }
        return list;
    }
}
