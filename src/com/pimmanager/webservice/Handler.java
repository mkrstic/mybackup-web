/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pimmanager.webservice;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.ws.commons.util.Base64;
import org.apache.ws.commons.util.Base64.DecodingException;

import com.pimmanager.beans.Contact;
import com.pimmanager.beans.Telephone;
import com.pimmanager.beans.User;
import com.pimmanager.configuration.AppConfig;
import com.pimmanager.logic.RemoteUserManager;
import com.pimmanager.util.AppUtils;
import com.pimmanager.util.MessageManager;

// TODO: Auto-generated Javadoc
/**
 * The Class Handler.
 *
 * @author mladen
 */
public class Handler {

    /**
     * Instantiates a new handler.
     */
    public Handler() {
    }

    /**
     * Login.
     *
     * @param phone the phone
     * @param password the password
     * @return the hash map
     */
    public HashMap<String, String> login(String phone, String password) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            phone = new String(Base64.decode(phone));
            password = new String(Base64.decode(password));
        } catch (DecodingException ex) {
            ex.printStackTrace();
            map.put("loginSuccess", "false");
            return map;
        }

        if (!AppUtils.isPhoneValid(phone) || !AppUtils.isPasswordValid(password)) {
            map.put("loginSuccess", "false");
            return map;
        }

        User user = new User(phone, password);
        RemoteUserManager remoteUser = new RemoteUserManager(user);
        int loginResult = remoteUser.login();
        if (loginResult == MessageManager.MESSAGE_SUCCESS) { // logged in
            map.put("loginSuccess", "true");
            map.put("id", String.valueOf(remoteUser.getUser().getId()));
            String firstName = remoteUser.getUser().getFirstName();
            if (firstName == null || firstName.trim().equals("")) {
                firstName = "user";
            }
            map.put("firstName", firstName);
            String strPaid = remoteUser.getUser().isPaid() ? ("1") : ("0");
            map.put("paid", strPaid);
            String paymentURL = AppConfig.getSiteUrl() + "/buy";
            map.put("paymentURL", paymentURL);
            String strLastSyncedLong = "0";
            Date lastSynced = remoteUser.getUser().getLastSynced();
            if (lastSynced != null) {
                strLastSyncedLong = String.valueOf(lastSynced.getTime());
            }
            map.put("lastSyncedLong", strLastSyncedLong);
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return map;
        } else {
            map.put("loginSuccess", "false");
            return map;
        }
    }

    /**
     * Recover password.
     *
     * @param phone the phone
     * @return the int
     */
    public int recoverPassword(String phone) {
        try {
            phone = new String(Base64.decode(phone));
        } catch (DecodingException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
            return MessageManager.MESSAGE_ERROR;
        }
        User user = new User(phone, null);
        RemoteUserManager remoteUser = new RemoteUserManager(user);
        int result = remoteUser.recoverPassword();
        return result;
    }

    /**
     * Register.
     *
     * @param phone the phone
     * @param password the password
     * @param firstName the first name
     * @param lastName the last name
     * @param email the email
     * @param town the town
     * @return the int
     */
    public int register(String phone, String password, String firstName, String lastName, String email, String town) {
        try {
            phone = new String(Base64.decode(phone));
            password = new String(Base64.decode(password));
        } catch (DecodingException ex) {
            return MessageManager.MESSAGE_ERROR;
        }
        if (!AppUtils.isPhoneValid(phone) || !AppUtils.isPasswordValid(password)) {
            return MessageManager.MESSAGE_ERROR;
        }
        User user = new User();
        user.setPhone(phone);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setTown(town);
        RemoteUserManager remoteUser = new RemoteUserManager(user);
        int result = remoteUser.register();
        return result;
    }

    /**
     * Count contacts.
     *
     * @param userId the user id
     * @return the integer
     */
    public Integer countContacts(Integer userId) {
        User user = new User(userId.intValue());
        RemoteUserManager remoteUser = new RemoteUserManager(user);
        int result = remoteUser.countContacts();
        return result;
    }

    /**
     * Sync contact.
     *
     * @param userId the user id
     * @param name the name
     * @param email the email
     * @param url the url
     * @param note the note
     * @param address the address
     * @param strRevision the str revision
     * @param uid the uid
     * @param strNumbers the str numbers
     * @return the int
     */
    public int syncContact(Integer userId, String name, String email, String url, String note,
            String address, String strRevision, String uid, String strNumbers) {
        try {
            name = new String(Base64.decode(name));
            strNumbers = new String(Base64.decode(strNumbers));
        } catch (DecodingException ex) {
            return MessageManager.MESSAGE_SUCCESS;
        }

        User user = new User(userId);
        Contact contact = new Contact();
        contact.setName(name);
        contact.setEmail(email);
        contact.setUrl(url);
        contact.setNote(note);
        contact.setAddress(address);
        contact.setUid(uid);


        Date rev = null;
        if (strRevision != null && !strRevision.equals("")) {
            rev = new Date(Long.valueOf(strRevision));
        }

        contact.setRevision(rev);

        if (contact.getRevision() != null
                && !isContactModified(user.getId(), strRevision, contact.getUid())) {
            return MessageManager.MESSAGE_SUCCESS; // contacts are same
        }

        Set<Telephone> tels = new HashSet<Telephone>();
        String[] numbers = strNumbers.split(":");
        for (String number : numbers) {
            String[] telParts = number.split("~");
            boolean isPreferred = false;
            try {
                isPreferred = (telParts[2].equals("1"));
                Telephone t = new Telephone(telParts[0], telParts[1], isPreferred);
                tels.add(t);
            } catch (ArrayIndexOutOfBoundsException ex) {
            }


        }
        contact.setTelephones(tels);
        int result = MessageManager.MESSAGE_FAILURE;
        // check if exists contact with this uid
        Integer contactId = RemoteUserManager.findContactId(user.getId(), contact.getUid());
        if (contactId != null && contact.getUid() != null && !contact.getUid().trim().equals("")) {
            contact.setId(contactId);
            Contact contactFromBase = RemoteUserManager.getContact(user.getId(), contact.getId());
            if (!contact.equals(contactFromBase)) {
                result = RemoteUserManager.updateContact(user.getId(), contact);
            }
        } else {
            result = RemoteUserManager.storeContact(user.getId(), contact);
        }

        return result;
    }

    /**
     * Checks if is contact modified.
     *
     * @param userId the user id
     * @param strRevision the str revision
     * @param uid the uid
     * @return the boolean
     */
    public Boolean isContactModified(Integer userId, String strRevision, String uid) {

        Integer contactId = RemoteUserManager.findContactId(userId.intValue(), uid);
        if (contactId == null) {
            return true;
        }

        Date rev = null;
        if (strRevision != null && !strRevision.equals("")) {
            rev = new Date(Long.valueOf(strRevision));
        }
        Date revFromBase = RemoteUserManager.getContactRevision(userId, contactId);
        if (rev.equals(revFromBase)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Gets the contact ids.
     *
     * @param userId the user id
     * @return the contact ids
     */
    public Integer[] getContactIds(Integer userId) {
        User user = new User(userId.intValue());
        RemoteUserManager remoteUser = new RemoteUserManager(user);
        Object[] contactIdsObj = remoteUser.getContactIds();
        Integer[] contactIds = new Integer[contactIdsObj.length];
        for (int i = 0; i < contactIdsObj.length; i++) {
        	contactIds[i] = (Integer) contactIdsObj[i];
        }
        return contactIds;
    }

    /**
     * Gets the contact uids.
     *
     * @param userId the user id
     * @return the contact uids
     */
    public Vector<String> getContactUids(Integer userId) {
        User user = new User(userId.intValue());
        RemoteUserManager remoteUser = new RemoteUserManager(user);
        Vector<String> contactUids = remoteUser.getContactUids();
        return contactUids;
    }

    /**
     * Gets the contact by id.
     *
     * @param userId the user id
     * @param contactId the contact id
     * @return the contact by id
     */
    public HashMap<String, String> getContactById(Integer userId, Integer contactId) {
        HashMap<String, String> contactMap = new HashMap<String, String>();
        Contact c = RemoteUserManager.getContact(userId, contactId);
        if (c.getName().trim().equals("")) {
            c.setName("~");
        }
        if (c.getEmail().trim().equals("")) {
            c.setEmail("~");
        }
        if (c.getUrl().trim().equals("")) {
            c.setUrl("~");
        }
        if (c.getAddress().trim().equals("")) {
            c.setAddress("~");
        }
        if (c.getNote().trim().equals("")) {
            c.setNote("~");
        }
        if (c.getUid().trim().equals("")) {
            c.setUid("~");
        }
        String name = Base64.encode(c.getName().getBytes());
        contactMap.put("name", name);
        contactMap.put("email", c.getEmail());
        contactMap.put("url", c.getUrl());
        contactMap.put("address", c.getAddress());
        contactMap.put("note", c.getNote());
        contactMap.put("uid", c.getUid());
        return contactMap;
    }

    /**
     * Gets the tels by id.
     *
     * @param contactId the contact id
     * @return the tels by id
     */
    public String[] getTelsById(Integer contactId) {
        List<Telephone> tels = RemoteUserManager.getTels(contactId);
        if (tels == null) {
            return null;
        }
        String[] formattedTels = new String[tels.size()];
        for (int i = 0; i < tels.size(); i++) {
            StringBuilder str = new StringBuilder("");
            str.append(tels.get(i).getNumber());
            str.append("~");
            str.append(tels.get(i).getType());
            str.append("~");
            if (tels.get(i).isPreferred()) {
                str.append("1");
            } else {
                str.append("0");
            }
            formattedTels[i] = Base64.encode(str.toString().getBytes());
        }
        return formattedTels;
    }

    /**
     * Delete all contacts.
     *
     * @param userId the user id
     * @return the int
     */
    public int deleteAllContacts(Integer userId) {
        User user = new User(userId.intValue());
        RemoteUserManager remoteUser = new RemoteUserManager(user);
        Boolean isDeleted = remoteUser.deleteAllContacts();
        if (isDeleted) {
            return MessageManager.MESSAGE_SUCCESS;
        } else {
            return MessageManager.MESSAGE_FAILURE;
        }
    }

    /**
     * Delete contacts by uid.
     *
     * @param userId the user id
     * @param contactUidsVector the contact uids vector
     * @return the integer
     */
    public Integer deleteContactsByUid(Integer userId, Vector<String> contactUidsVector) {
        String[] contactUids = new String[contactUidsVector.size()];
        for (int i = 0; i < contactUids.length; i++) {
            contactUids[i] = (String) contactUidsVector.get(i);
        }
        User user = new User(userId.intValue());
        RemoteUserManager remoteUser = new RemoteUserManager(user);
        Integer deletedNum = remoteUser.deleteContactsByUid(contactUids);
        return deletedNum;
    }

    /**
     * Activate account.
     *
     * @param userId the user id
     * @param strKey the str key
     * @return the integer
     */
    public Integer activateAccount(Integer userId, String strKey) {
        long key;
        try {
            key = Long.parseLong(strKey);
        } catch (NumberFormatException nfe) {
            return MessageManager.MESSAGE_ERROR;
        }
        User user = new User(userId);
        RemoteUserManager remoteUser = new RemoteUserManager(user);
        return remoteUser.activateAccount(key);
    }

    /**
     * Update last synced.
     *
     * @param userId the user id
     */
    public void updateLastSynced(Integer userId) {
        User user = new User(userId);
        RemoteUserManager remoteUser = new RemoteUserManager(user);
        remoteUser.updateLastSynced();
    }
}
