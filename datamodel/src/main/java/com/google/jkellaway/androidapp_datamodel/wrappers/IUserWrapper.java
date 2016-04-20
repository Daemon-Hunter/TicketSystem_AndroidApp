/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.wrappers;

import com.google.jkellaway.androidapp_datamodel.people.IUser;

/**
 *
 * @author 10467841
 */
public interface IUserWrapper extends IWrapper {
    public Boolean setUser(IUser user);
    public IUser   getUser();
}
