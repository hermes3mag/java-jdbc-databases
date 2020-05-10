package com.pluralsight.order.dao;

import com.pluralsight.order.dto.OrderDto;
import com.pluralsight.order.dto.ParamsDto;
import com.pluralsight.order.util.Database;
import com.pluralsight.order.util.ExceptionHandler;

import java.sql.*;

/**
 * DAO to get an order
 */
public class GetOrderDao {
    private String query = "SELECT * FROM orders o WHERE o.order_id = ?";
    private Database database;

    /**
     * Constructor
     * @param database Database object
     */
    public GetOrderDao(Database database) {
        this.database = database;
    }

    /**
     * Gets an order by its ID
     * @param paramsDto Object with the parameters for the operation
     * @return Object with the main information of an order
     */
    public OrderDto getOrderById(ParamsDto paramsDto) {
        OrderDto orderDto = null;

        try (Connection con = database.getConnection()) {

            PreparedStatement ps = createPreparedStatement(con, paramsDto.getOrderId());
            ResultSet rs = createResultSet(ps);

            while(rs.next()){
                Long orderId = rs.getLong("order_id");
                Long customerId = rs.getLong("order_customer_id");
                Date orderDate = rs.getDate("order_date");
                String orderStatus = rs.getString("order_status");

                OrderDto orderDto1 = new OrderDto();
                orderDto1.setOrderId(orderId);
                orderDto1.setCustomerId(customerId);
                orderDto1.setDate(orderDate);
                orderDto1.setStatus(orderStatus);

                orderDto = orderDto1;
            }

        } catch (SQLException ex) {
            ExceptionHandler.handleException(ex);
        }

        return orderDto;
    }

    /**
     * Creates a PreparedStatement object to get an order
     * @param con Connnection object
     * @param orderId Order ID to set on the PreparedStatement
     * @return A PreparedStatement object
     * @throws SQLException In case of an error
     */
    private PreparedStatement createPreparedStatement(Connection con, long orderId) throws SQLException {
        PreparedStatement statement = con.prepareStatement(query);
        statement.setLong(1, orderId);
        return statement;
    }

    /**
     * Creates a ResultSet object to get the results of the query
     * @param ps PreparedStatement object to create the query
     * @return A ResultSet object
     * @throws SQLException In case of an error
     */
    private ResultSet createResultSet(PreparedStatement ps) throws SQLException {

        ResultSet rs = ps.executeQuery();
        return rs;
    }
}
