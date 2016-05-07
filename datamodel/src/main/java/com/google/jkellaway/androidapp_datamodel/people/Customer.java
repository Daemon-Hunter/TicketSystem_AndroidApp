/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.people;

import com.google.jkellaway.androidapp_datamodel.bookings.IBooking;
import com.google.jkellaway.androidapp_datamodel.bookings.IOrder;
import com.google.jkellaway.androidapp_datamodel.database.APIHandle;
import com.google.jkellaway.androidapp_datamodel.database.DatabaseTable;
import com.google.jkellaway.androidapp_datamodel.reviews.IReview;
import com.google.jkellaway.androidapp_datamodel.utilities.Validator;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static com.google.jkellaway.androidapp_datamodel.utilities.HashString.Encrypt;

/**
 * @author 10512691
 */
public class Customer implements ICustomer {

    private String firstName, lastName, email, address, postcode, password;
    private DatabaseTable table;
    private Integer ID;
    private List<IReview> reviews;
    private List<IOrder> orders;


    /**
     * Use this when creating a customer object from the database.
     *
     * @param ID        is known.
     * @param firstName
     * @param lastName
     * @param email
     * @param address
     * @param postcode
     */
    public Customer(Integer ID, String firstName, String lastName, String email, String address, String postcode) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.postcode = postcode;
        this.table = DatabaseTable.CUSTOMER;
    }

    /**
     * Use this when creating a new customer object.
     * ID is unknown.
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param address
     * @param postcode
     */
    public Customer(String firstName, String lastName, String email, String address, String postcode, String password) throws IllegalArgumentException {
        // Check if names are null or valid. All users must have a valid name.
        if (firstName == null)
            throw new NullPointerException("First name must have a value");
        if (firstName == null)
            throw new NullPointerException("Last name must have a value");

        Validator.nameValidator(firstName);
        Validator.nameValidator(lastName);
        this.firstName = firstName;
        this.lastName = lastName;

        Validator.emailValidator(email);
        this.email = email;

        Validator.addressValidator(address);
        this.address = address;

        Validator.postcodeValidator(postcode);
        this.postcode = postcode;

        this.password = Encrypt(password);
        table = DatabaseTable.CUSTOMER;
        this.ID = 0;
    }

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
        if (customerID == null)
            throw new NullPointerException("CustomerID cannot be null");
        for (IReview r : reviews) {
            if (r.getCustomerID().equals(customerID)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No customers with that ID have " + "written a review for this venue.");
    }


    @Override
    public Boolean deleteReview(IReview review) throws IOException {
        if (review == null) {
            throw new NullPointerException("Review to be deleted was null");
        } else if (!reviews.contains(review)) {
            throw new IllegalArgumentException("Review to be deleted wasn't in list");
        } else {
            this.reviews.remove(review);
            return true;
        }
    }

    @Override
    public Boolean setFirstName(String name) throws IllegalArgumentException {
        if (name == null)
            throw new NullPointerException("Cannot set name to null");
        Validator.nameValidator(name);
        this.firstName = name;
        return this.firstName.equals(firstName);
    }

    @Override
    public Boolean setLastName(String name) throws IllegalArgumentException {
        if (name == null)
            throw new NullPointerException("Cannot set name to null");
        Validator.nameValidator(name);
        this.lastName = name;
        return this.lastName.equals(name);
    }

    @Override
    public Boolean setPassword(String password) {
        this.password = password;
        return this.password == password;
    }

    @Override
    public List<IOrder> getOrderList() throws IOException {
        if (orders == null) {
            orders = (List<IOrder>) (Object) APIHandle.getObjectsFromObject(this.ID, DatabaseTable.ORDER, DatabaseTable.CUSTOMER);
        }
        return new LinkedList(orders);
    }

    @Override
    public IOrder getOrder(int orderID) throws IOException {
        if (orders != null) {
            for (IOrder order : orders) {
                if (order.getOrderID().equals(orderID)) return order;
            }
        }
        return (IOrder) APIHandle.getSingle(orderID, DatabaseTable.ORDER);
    }

    @Override
    public Boolean addOrder(IOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be Null.");
        } else {
            return orders.add(order);
        }
    }

    @Override
    public Boolean addOrderList(List<IOrder> orderList) {
        if (orderList == null) {
            throw new IllegalArgumentException("Cannot add null order list.");
        } else {
            return this.orders.addAll(orderList);
        }
    }

    @Override
    public Boolean removeOrder(IOrder order) {
        if (order == null) {
            throw new IllegalArgumentException("Cannot remove null order");
        } else {
            return orders.remove(order);
        }
    }

    @Override
    public List<IBooking> getBookings() throws IOException {
        List<IBooking> bookings = new LinkedList<>();
        getOrderList();
        for (IOrder order : orders) {
            bookings.addAll(order.getBookingList());
        }
        return bookings;
    }

    @Override
    public String getEmail() {
        if (email == null) {
            throw new NullPointerException("Null email");
        } else {
            return email;
        }
    }

    @Override
    public Boolean setEmail(String email) throws IllegalArgumentException {
        if (email == null)
            throw new NullPointerException("Cannot set email to null");
        Validator.emailValidator(email);
        this.email = email;
        return this.email.equals(email);
    }

    @Override
    public Integer getID() {
        if (ID == null) {
            throw new NullPointerException("ID is null");
        }
        return ID;
    }

    @Override
    public String getFirstName() {
        if (firstName == null) {
            throw new NullPointerException("Null first name");
        } else {
            return firstName;
        }
    }

    @Override
    public String getLastName() {
        if (lastName == null) {
            throw new NullPointerException("Null last name");
        } else {
            return lastName;
        }
    }

    @Override
    public String getAddress() {
        if (address == null) {
            throw new NullPointerException("Null address");
        } else {
            return address;
        }
    }

    @Override
    public Boolean setAddress(String address) throws IllegalArgumentException {
        if (address == null)
            throw new NullPointerException("Cannot set address to null");
        Validator.addressValidator(address);
        this.address = address;
        return this.address.equals(address);
    }

    @Override
    public String getPostcode() {
        if (postcode == null) {
            throw new NullPointerException("Null postcode");
        } else {
            return postcode;
        }
    }

    @Override
    public Boolean setPostcode(String postcode) throws IllegalArgumentException {
        if (postcode == null)
            throw new NullPointerException("Cannot set postcode to null");
        Validator.postcodeValidator(postcode);
        this.postcode = postcode;
        return this.postcode.equals(postcode);
    }

    @Override
    public String getPassword() {
        return password;
    }
}
