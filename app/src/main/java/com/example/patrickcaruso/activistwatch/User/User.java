package com.example.patrickcaruso.activistwatch.User;

import com.example.patrickcaruso.activistwatch.Event.Event;
import com.example.patrickcaruso.activistwatch.Organization.Organization;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {

    private int id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private List<Organization> organizations;
    private List<Event> attendedEvents;
    private List<Event> interestedEvents;

    public User(int id) {
        this.id = id;
    }

    public User(int id,
                String username,
                String email,
                String firstName,
                String lastName,
                Date dateOfBirth,
                List<Organization> organizations,
                List<Event> attendedEvents,
                List<Event> interestedEvents) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.organizations = organizations;
        this.attendedEvents = attendedEvents;
        this.interestedEvents = interestedEvents;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void AddOrganization(Organization org) {
        if(organizations == null) {
            organizations = new ArrayList<Organization>();
        }
        organizations.add(org);
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public List<Event> getAttendedEvents() {
        return attendedEvents;
    }

    public void setAttendedEvents(ArrayList<Event> attendedEvents) {
        this.attendedEvents = attendedEvents;
    }

    public List<Event> getInterestedEvents() {
        return interestedEvents;
    }

    public void setInterestedEvents(List<Event> interestedEvents) {
        this.interestedEvents = interestedEvents;
    }
}
