package com.example.aneurinc.prcs_app.People;

import com.example.aneurinc.prcs_app.Database.DatabaseTable;
import com.example.aneurinc.prcs_app.Reviews.IHaveReviews;
import com.example.aneurinc.prcs_app.Reviews.IReview;
import com.example.aneurinc.prcs_app.bookings.IOrder;

import java.util.LinkedList;
import java.util.List;

import static com.example.aneurinc.prcs_app.Utility.Validator.idValidator;
import static com.example.aneurinc.prcs_app.Utility.Validator.nameValidator;

/**
 * Created by Dominic on 14/04/2016.
 */
public class Customer extends User implements IHaveReviews {

    private LinkedList<IReview> reviews;
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
                    String email, String address, String postcode)
    {
        super(ID, firstName, lastName, email, address, postcode);
        table = DatabaseTable.CUSTOMER;
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
    public LinkedList<IReview> getReviews() {
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
            Boolean valid = nameValidator(name);
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
            Boolean valid = nameValidator(name);
            if (valid) {
                lastName = name;
                notifyObservers();
            }
            return valid;
        }
    }


    public LinkedList<IOrder> getOrders() {
        if (orders == null) {
            throw new NullPointerException("Null orders list");
        } else return new LinkedList(orders);
    }


    public Boolean addOrder(IOrder o){
        if (orders == null){
            orders = new LinkedList();
            return orders.add(o);
        } else {
            return orders.add(o);
        }
    }

    public Boolean addOrderList(List<IOrder> o){
        if (orders == null){
            orders = new LinkedList();
            return orders.addAll(o);
        } else {
            return orders.addAll(o);
        }
    }

    public IOrder getOrder(Integer orderID){
        if (orderID == null){
            throw new IllegalArgumentException("orderID is null.");
        } else {
            return orders.get(orderID);
        }
    }
}
