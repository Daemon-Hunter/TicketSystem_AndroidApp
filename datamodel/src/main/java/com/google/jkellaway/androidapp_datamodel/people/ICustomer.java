/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.people;

import com.google.jkellaway.androidapp_datamodel.bookings.IOrder;
import java.util.List;

/**
 *
 * @author 10467841
 */
public interface ICustomer {
    public List<IOrder> getOrderList();
    public IOrder getOrder(int orderID);
    public Boolean addOrder(IOrder order);
    public Boolean addOrderList(List<IOrder> orderList);
    public Boolean removeOrder(IOrder order);
    
}
