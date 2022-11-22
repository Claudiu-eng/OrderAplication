package mypack.bll;

import mypack.dao.OrderDAO;
import mypack.model.Orders;

import java.util.ArrayList;

public class OrderBLL {
    private OrderDAO orderDAO;
    public OrderBLL(){
        orderDAO=new OrderDAO();
    }
    public ArrayList<Orders> getOrders(){
        return orderDAO.findAll();
    }
    public void deleteOrder(Orders orders){
        orderDAO.delete(orders);
    }
    public void insert(Orders orders){
        orderDAO.insert(orders);
    }
}
