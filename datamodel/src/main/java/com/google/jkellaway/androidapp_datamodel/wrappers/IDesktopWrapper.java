/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.jkellaway.androidapp_datamodel.wrappers;

import java.util.List;
import com.google.jkellaway.androidapp_datamodel.people.IAdmin;
import com.google.jkellaway.androidapp_datamodel.people.IUser;

/**
 *
 * @author 10467841
 */
public interface IDesktopWrapper extends IWrapper {
    public Boolean      addUser(IUser user);
    public List<IUser>  getUsers();
    public IUser        getUser(Integer index);
    public Boolean      removeUser(IUser user);
    
    public Boolean      addAdmin(IAdmin admin);
    public IAdmin       getAdmin(Integer index);
    public List<IAdmin> getAdmins();
    public Boolean      removeAdmin(IAdmin admin);
}
