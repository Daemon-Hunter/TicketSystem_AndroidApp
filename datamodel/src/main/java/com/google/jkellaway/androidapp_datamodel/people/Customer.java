/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.people;

import com.google.jkellaway.androidapp_datamodel.bookings.IOrder;
import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.reviews.IHaveReviews;
import com.google.jkellaway.androidapp_datamodel.reviews.IReview;
import com.google.jkellaway.androidapp_datamodel.utilities.Validator;

import java.util.LinkedList;
import java.util.List;

import static com.google.jkellaway.androidapp_datamodel.utilities.Validator.idValidator;

/**
 *
 * @author 10512691
 */
public class Customer extends User implements IHaveReviews, ICustomer {

    private List<IReview> reviews;
    private List<IOrder> orders;
    
    
    
    /**
     * Use this when creating a customer object from the database.
     * @param ID is known.
     * @param firstName
     * @param lastName
     * @param email
     * @param address
     * @param postcode 
     */
    public Customer(Integer ID, String firstName, String lastName,
                    String email, String address, String postcode){

        super(ID, firstName, lastName, email, address, postcode);
        this.table = DatabaseTable.CUSTOMER;
        this.orders = new LinkedList<>();
    }
    
    /**
     * Use this when creating a new customer object.
     * ID is unknown.
     * @param firstName
     * @param lastName
     * @param email
     * @param address
     * @param postcode 
     */
    public Customer(String firstName, String lastName,
            String email, String address, String postcode) 
    {
        super(firstName, lastName, email, address, postcode);
        table = DatabaseTable.CUSTOMER;
        this.ID = 0;
    }

    @Override
    public DatabaseTable getTable() {
        return table;
    }

    @Override
    public List<IReview> getReviews() {
        if (reviews == null) {
            throw new NullPointerException();
        } else {
            return reviews;
        }
    }

    @Override
    public IReview getReview(Integer customerID) {
        if (customerID == null) {
            throw new NullPointerException();
        } else {
            Boolean valid = idValidator(customerID);

            if (valid) {
                for (IReview r : reviews) {
                    if (r.getCustomerID().equals(customerID)) {
                        return r;
                    }
                }
                throw new IllegalArgumentException("No customers with that ID have "
                        + "written a review for this venue.");

            } else {
                throw new IllegalArgumentException("Invalid ID");
            }
        }
    }

    @Override
    public Boolean deleteReview(IReview review) {
        if (review == null) {
            throw new NullPointerException("Review to be deleted was null");
        } else if (!reviews.contains(review)) {
            throw new IllegalArgumentException("Review to be deleted wasn't in list");
        } else {
            reviews.remove(review);
            notifyObservers();
            return true;
        }
    }

    @Override
    public Boolean setFirstName(String name) {
        if (name == null) {
            throw new NullPointerException("Cannot set name to null");
        } else {
            Boolean valid = Validator.nameValidator(name);
            if (valid) {
                firstName = name;
                notifyObservers();
            }
            return valid;
        }
    }

    @Override
    public Boolean setLastName(String name) {
        if (name == null) {
            throw new NullPointerException("Cannot set name to null");
        } else {
            Boolean valid = Validator.nameValidator(name);
            if (valid) {
                lastName = name;
                notifyObservers();
            }
            return valid;
        }
    }

    @Override
    public List<IOrder> getOrderList() {
        if (orders == null){
            throw new NullPointerException("No orders in list");
        } else {
            return new LinkedList(orders);
        }
    }

    @Override
    public IOrder getOrder(int orderID) {
        return orders.get(orderID);
    }

    @Override
    public Boolean addOrder(IOrder order) {
        if (order == null){
            throw new IllegalArgumentException("Order cannot be Null.");
        } else {
        return orders.add(order);
        }
    }

    @Override
    public Boolean addOrderList(List<IOrder> orderList) {
        if (orderList == null){
            throw new IllegalArgumentException("Cannot add null order list.");
        } else {
        return this.orders.addAll(orderList);
        }
    }

    @Override
    public Boolean removeOrder(IOrder order) {
        if (order == null){
            throw new IllegalArgumentException("Cannot remove null order");
        } else {
        return orders.remove(order);
        }
    }
}
