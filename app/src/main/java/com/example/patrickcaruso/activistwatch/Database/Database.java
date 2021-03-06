package com.example.patrickcaruso.activistwatch.Database;

import android.graphics.Point;

import com.example.patrickcaruso.activistwatch.Constants.URLConstants;
import com.example.patrickcaruso.activistwatch.Event.Event;
import com.example.patrickcaruso.activistwatch.Organization.Organization;
import com.example.patrickcaruso.activistwatch.User.ThisUser;
import com.example.patrickcaruso.activistwatch.User.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Database {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH-mm-SS");
    /**
     * Creates an event in the database and returns the event's id
     * @param ownerOrganization the organization that is creating the event; -1 if not run by organization
     * @param ownerMember the member that is creating the event
     * @param pictureUrl the url of the photo that will be shown
     * @param name the name of the event
     * @param description the description of the event
     * @param blurb the blurb that is shown when swiping
     * @param location the location of the event
     * @param time the time of the event
     * @param tags the tags associated with the event
     * @param posts the posts made by the event
     * @return the id of the newly created event
     */
    public static int createEvent(int ownerOrganization,
                                  int ownerMember,
                                  String pictureUrl,
                                  String name,
                                  String description,
                                  String blurb,
                                  String location,
                                  String time,
                                  String tags,
                                  String posts) throws IOException {
        return genericUrlToId(generateCreateEventURL(ownerOrganization, ownerMember, pictureUrl, name, description, blurb,
                location, time, tags, posts));
    }

    /**
     * Queries a user from the database to get their information
     * @param id the id of the user
     * @return the user's unfiltered metadata as a JSON response
     * @throws IOException if network fails
     */
    public static String lookupEvent(int id) throws IOException {
        return query(generateEventQueryURL(id));
    }

    /**
     * Queries a user from the database to get their information
     * @param id the id of the user
     * @return the user's unfiltered metadata as a JSON response
     * @throws IOException if network fails
     */
    public static String lookupUser(int id) throws IOException {
        return query(generateUserQueryURL(id));
    }

    public static User lookupUserObject(int id) throws IOException {
        Gson gson= new Gson();
        return gson.fromJson(lookupEvent(id), User.class);
    }

    /**
     * Queries an organization from the database
     * @param id the id of the organization
     * @return the organization's unfiltered metadata as a JSON response
     * @throws IOException if network fails
     */
    public static String lookupOrganization(int id) throws IOException {
        String temp = query(generateOrganizationQueryURL(id));
        System.out.println(temp);
        return query(generateOrganizationQueryURL(id));
    }

    /**
     * Edits an organization within the database
     * @param user the user to query and update
     * @throws IOException if network fails
     */
    public static void editUser(User user) throws IOException {
        String result = query(generateEditUserURL(user));
        if (result == null || !result.trim().equals("success")) {
            //ERROR handler goes here
        }
    }

    /**
     * Edits an organization within the database
     * @param organization the organization to query and update
     * @throws IOException if network fails
     */
    public static void editOrganization(Organization organization) throws IOException {
        String result = query(generateEditOrganizationURL(organization));
        if (result == null || !result.trim().equals("success")) {
            //ERROR handler goes here
        }
    }

    /**
     * Edits an event within the database
     * @param event the event to query and update
     * @throws IOException if network fails
     */
    public static void editEvent(Event event) throws IOException {
        String result = query(generateEditEventURL(event));
        if (result == null || !result.trim().equals("success")) {
            //ERROR handler goes here
        }
    }

    /**
     * Creates an organization and returns the new organization's id
     * @param ownerId the id of the owner of the organization
     * @param organizationName the name of the organization
     * @param description the description of the organization
     * @param keywords the keywords as a list delimited by commas (ex- "animals,dogs,cats")
     * @param members the members as a list delimited by commas (ex - "123,95,519")
     * @return the id of the newly created organization
     * @throws IOException if network fails
     */
    public static int createOrganization(int ownerId,
                                         String organizationName,
                                         String description,
                                         String keywords,
                                         String members) throws IOException {
        return genericUrlToId(generateCreateOrganizationUrl(ownerId, organizationName,
                description, keywords, members));
    }

    public static List<Integer> getOrganizations(String userId) {
        return null;
    }

    public static List<Organization> getOrganizationObjects(String userId) throws IOException {
        List<Organization> org = new ArrayList<>();
//        for (Integer i: getOrganizations(userId)) {
//            org.add(new Gson().fromJson(lookupOrganization(i), Organization.class));
//        }

        org = new ArrayList<>();
        org.add(new Organization(1, ThisUser.id, "Great Org", "This is the greatest organization", "", "", ""));
        org.add(new Organization(2, ThisUser.id, "Free Tacos", "We promote free tacos everywhere", "", "", ""));
        org.add(new Organization(3, ThisUser.id, "End Taxes", "We believe taxes should be abolished", "", "", ""));

        return org;
    }

    /**
     * Attempts to login with a username and password
     *
     * @param username the user's username
     * @param password the user's password
     * @return ret >  0  : successful login; returns the user's id
     * ret = -1  : unable to login
     */
    public static int login(String username,
                            String password) throws IOException {
        return genericUrlToId(generateLoginUrl(username, password));
    }

    /**
     * Generates a request to the server to create a user
     * @param username the user's declared username
     * @param email the user's declared email
     * @param password the user's declared password
     */
    public static int register(final String username,
                              final String email,
                              final String password) throws IOException {
        return genericUrlToId(generateRegisterUserUrl(username, email, password));
    }

    /**
     * Creates a generic request to the server and returns the
     * contents as the id
     * @param url the url of the PHP query file
     * @return ret > 0  : if valid id response
     *         ret = -1 : if invalid response
     * @throws IOException if network error
     */
    private static int genericUrlToId(String url) throws IOException {
        System.out.println("Query: " + url);
        String response = query(url).trim();
        if (responseMatchesId(response)) {
            return Integer.parseInt(response);
        } else {
            return -1;
        }
    }

    public static List<Event> getAllEvents() throws IOException {
        String query = query("http://patrickcaruso.com/allEvents.php");
        String[] split = query.split("<br/>");
        ArrayList<Event> events = new ArrayList<Event>();
        for (String val: split) {
            if (val != null && val.trim().length() > 0) {
                String[] values = val.split("/<>");
                int id = Integer.parseInt(values[0].trim());
                String name = values[1].trim();
                String picture = values[2].trim();
                String blurb = values[3].trim();
                String description = values[4].trim();
                String location = values[5].trim(); //TODO fix test
                String time = values[6].trim();
                Event event = new Event(id);
                event.setBlurb(blurb);
                event.setDescription(description);
                event.setName(name);
                event.setPhoto(picture);
                event.setLocation(new Point(0, 0));
                try {
                    event.setTime(sdf.parse(time));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                events.add(event);
            }
        }
        return events;
    }

    private static String query(String url) throws IOException {
        System.out.println(url);
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    private static boolean responseMatchesId(String string){
        final String SUCCESS_PATTERN =
                "[0-9]+";
        Pattern pattern = Pattern.compile(SUCCESS_PATTERN);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    /**
     * Generates a URL to POST user information to in order
     * to add the user to the user database
     * @param username the user's declared username
     * @param password the user's declared password
     * @return an HTTPUrlConnection compatible POST URL
     */
    private static String generateLoginUrl(String username,
                                    String password) {
        StringBuilder sb = new StringBuilder();
        sb.append(URLConstants.LOGIN_URL_BASE);
        sb.append(URLConstants.POST_DELIMETER);
        sb.append(URLConstants.ADD_USER_USERNAME_ATTRIBUTE);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(username);
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_USER_PASSWORD_ATTRIBUTE);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(password);
        return sb.toString();
    }

    /**
     * Generates a URL to POST user information to in order
     * to add the user to the user database
     * @param username the user's declared username
     * @param email the user's declared email
     * @param password the user's declared password
     * @return an HTTPUrlConnection compatible POST URL
     */
    private static String generateRegisterUserUrl(String username,
                                           String email,
                                           String password) {
        StringBuilder sb = new StringBuilder();
        sb.append(URLConstants.ADD_USER_URL_BASE);
        sb.append(URLConstants.POST_DELIMETER);
        sb.append(URLConstants.ADD_USER_USERNAME_ATTRIBUTE);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(username);
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_USER_EMAIL_ATTRIBUTE);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(email);
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_USER_PASSWORD_ATTRIBUTE);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(password);
        return sb.toString();
    }

    private static String generateCreateOrganizationUrl(int ownerId,
                                                        String organizationName,
                                                        String description,
                                                        String keywords,
                                                        String members) {
        StringBuilder sb = new StringBuilder();
        sb.append(URLConstants.ADD_ORGANIZATION_URL_BASE);
        sb.append(URLConstants.POST_DELIMETER);
        sb.append(URLConstants.ADD_ORGANIZATION_OWNER);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(ownerId);
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_ORGANIZATION_NAME);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(organizationName);
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_ORGANIZATION_DESCRIPTION);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(description);
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_ORGANIZATION_KEYWORDS);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(keywords);
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_ORGANIZATION_MEMBERS);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(members);
        return sb.toString();
    }

    private static String generateOrganizationQueryURL(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append(URLConstants.LOOKUP_ORGANIZATION_URL_BASE);
        sb.append(URLConstants.POST_DELIMETER);
        sb.append(URLConstants.LOOKUP_ORGANIZATION_ID);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(id);
        return sb.toString();
    }

    private static String generateUserQueryURL(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append(URLConstants.LOOKUP_USER_URL_BASE);
        sb.append(URLConstants.POST_DELIMETER);
        sb.append(URLConstants.LOOKUP_USER_ID);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(id);
        return sb.toString();
    }

    private static String generateEventQueryURL(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append(URLConstants.LOOKUP_EVENT_URL_BASE);
        sb.append(URLConstants.POST_DELIMETER);
        sb.append(URLConstants.LOOKUP_EVENT_ID);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(id);
        return sb.toString();
    }

    private static String generateEditOrganizationURL(Organization organization) {
        StringBuilder sb = new StringBuilder();
        sb.append(URLConstants.EDIT_ORGANIZATION_URL_BASE);
        sb.append(URLConstants.POST_DELIMETER);
        sb.append(URLConstants.LOOKUP_ORGANIZATION_ID);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(organization.getOrgId());
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_ORGANIZATION_OWNER);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(organization.getOwnerId());
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_ORGANIZATION_NAME);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(organization.getName());
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_ORGANIZATION_DESCRIPTION);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(organization.getDescription());
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_ORGANIZATION_KEYWORDS);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(organization.getKeywords());
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_ORGANIZATION_MEMBERS);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(organization.getMembers());
        return sb.toString();
    }

    private static String generateEditUserURL(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append(URLConstants.EDIT_USER_URL_BASE);
        sb.append(URLConstants.POST_DELIMETER);
        sb.append(URLConstants.LOOKUP_USER_ID);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(user.getId());
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.EDIT_USER_FIRST_NAME_ATTRIBUTE);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(user.getFirstName());
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.EDIT_USER_LAST_NAME_ATTRIBUTE);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(user.getLastName());
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_USER_USERNAME_ATTRIBUTE);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(user.getUsername());
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_USER_EMAIL_ATTRIBUTE);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(user.getEmail());
        return sb.toString();
    }

    private static String generateEditEventURL(Event event) {
        StringBuilder sb = new StringBuilder();
        sb.append(URLConstants.EDIT_EVENT_URL_BASE);
        sb.append(URLConstants.POST_DELIMETER);
        sb.append(URLConstants.LOOKUP_EVENT_ID);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(event.getId());
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_EVENT_OWNER_ORGANIZATION);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(event.getOwnerOrganization());
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_EVENT_OWNER_MEMBER);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(event.getOwnerUser());
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_EVENT_PICTURE_URL);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(event.getPhoto());
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_EVENT_NAME);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(event.getName());
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_EVENT_DESCRIPTION);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(event.getDescription());
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_EVENT_BLURB);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(event.getBlurb());
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_EVENT_LOCATION);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(event.getLocation().x + "," + event.getLocation().y);
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_EVENT_TIME);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(sdf.format(event.getTime()));
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_EVENT_TAGS);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(tags(event.getTags()));
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_EVENT_POSTS);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(posts(event.getPosts()));
        return sb.toString();
    }

    private static final String posts(List<Integer> posts) {
        String result = "";
        int len = 0;
        for (Integer post: posts) {
            len++;
            result += post;
            if (len != posts.size()) {
                result += ",";
            }
        }
        return result;
    }

    private static final String tags(List<String> tags) {
        String result = "";
        int len = 0;
        for (String tag: tags) {
            len++;
            result += tag;
            if (len != tags.size()) {
                result += ",";
            }
        }
        return result;
    }

    private static String generateCreateEventURL(int ownerOrganization,
                                                 int ownerMember,
                                                 String pictureUrl,
                                                 String name,
                                                 String description,
                                                 String blurb,
                                                 String location,
                                                 String time,
                                                 String tags,
                                                 String posts) {
        StringBuilder sb = new StringBuilder();
        sb.append(URLConstants.ADD_EVENT_URL_BASE);
        sb.append(URLConstants.POST_DELIMETER);
        sb.append(URLConstants.ADD_EVENT_OWNER_ORGANIZATION);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(ownerOrganization);
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_EVENT_OWNER_MEMBER);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(ownerMember);
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_EVENT_PICTURE_URL);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(pictureUrl);
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_EVENT_NAME);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(name);
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_EVENT_DESCRIPTION);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(description);
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_EVENT_BLURB);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(blurb);
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_EVENT_LOCATION);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(location);
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_EVENT_TIME);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(time);
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_EVENT_TAGS);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(tags);
        sb.append(URLConstants.POST_AND);
        sb.append(URLConstants.ADD_EVENT_POSTS);
        sb.append(URLConstants.POST_EQUALS);
        sb.append(posts);
        return sb.toString();
    }
}
