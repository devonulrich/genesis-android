package com.devonulrich.genesisclient.network;

import com.devonulrich.genesisclient.data.ClassAssignment;
import com.devonulrich.genesisclient.data.SchoolClass;
import com.devonulrich.genesisclient.data.LoginInfo;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GenesisHTTP {

    private static final String USER_AGENT = "Mozilla/5.0";

    private static final String LOGIN_PAGE_URL =
            "https://parents.cresskillboe.k12.nj.us/genesis/parents?gohome=true";
    private static final String LOGIN_POST_URL =
            "https://parents.cresskillboe.k12.nj.us/genesis/j_security_check";
    private static final String OVERVIEW_PAGE_URL =
            "https://parents.cresskillboe.k12.nj.us/genesis/parents?tab1=" +
                    "studentdata&tab2=studentsummary&action=form&studentid=";
    private static final String GRADEBOOK_PAGE_URL_1 =
            "https://parents.cresskillboe.k12.nj.us/genesis/parents?tab1=studentdata" +
                    "&tab2=gradebook&tab3=weeklysummary&action=form&studentid=";
    private static final String GRADEBOOK_PAGE_URL_2 = "&mpToView=";
    private static final String CLASS_PAGE_URL_1 =
            "https://parents.cresskillboe.k12.nj.us/genesis/parents?tab1=studentdata&tab2=gradebook&tab3=coursesummary&studentid=";
    private static final String CLASS_PAGE_URL_2 = "&action=form&courseCode=";
    private static final String CLASS_PAGE_URL_3 = "&courseSection=";
    private static final String CLASS_PAGE_URL_4 = "&mp=";

    //returns the session ID, or null if it failed
    public static String login(LoginInfo li) {
        try {
            //load the login page, and get the cookie
            Connection loginPage = Jsoup.connect(LOGIN_PAGE_URL);
            Connection.Response pageResponse = loginPage.execute();
            Map<String, String> cookies = pageResponse.cookies();

            //send a POST with the login info
            Connection loginPost = Jsoup.connect(LOGIN_POST_URL);
            loginPost.userAgent(USER_AGENT);
            loginPost.data("j_username", li.email);
            loginPost.data("j_password", li.pass);
            loginPost.cookies(cookies);
            loginPost.followRedirects(true);
            loginPost.method(Connection.Method.POST);
            Connection.Response postResponse = loginPost.execute();

            //Genesis redirects the user back to the login page if the info is incorrect,
            //so if the response url is different to the login url, then the login was successful
            if (!postResponse.url().toString().equals(LOGIN_PAGE_URL)) {
                return postResponse.cookie("JSESSIONID");
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<SchoolClass> overview(String session, String id) {
        try {
            //get the page with the given student ID and session ID
            Connection page = Jsoup.connect(OVERVIEW_PAGE_URL + id);
            page.userAgent(USER_AGENT);
            page.followRedirects(true);
            page.cookie("JSESSIONID", session);
            Connection.Response response = page.execute();

            //parse the document
            Document html = response.parse();
            //get the "note card" items - the divs which show the student overviews
            Elements students = html.getElementsByClass("notecard");
            //get the first one (the one for the current student ID)
            Element studentInfo = students.get(0);

            //create an ArrayList for storing all info about all classes
            ArrayList<SchoolClass> classesTable = new ArrayList<>();
            //cycle through all rows in the schedule table
            for (Element classRow : studentInfo.select("table.list").get(2)
                    .select("tr.listrowodd, tr.listroweven")) {
                SchoolClass sc = new SchoolClass();
                //get all columns of the current row
                Elements columns = classRow.getElementsByTag("td");
                //if it doesn't have all the information we need, then skip it
                // (so we don't get errors)
                if (columns.size() <= 5) continue;
                sc.period = columns.get(0).text();
                sc.name = columns.get(1).text();
                sc.room = columns.get(4).text();
                sc.teacher = columns.get(5).text();
                classesTable.add(sc);
            }

            //return the combined info about all of the classes
            return classesTable;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, String> gradebook(String session, String id, String mp) {
        try {
            //get the page with the given student ID and session ID
            String fullURL = GRADEBOOK_PAGE_URL_1 + id + GRADEBOOK_PAGE_URL_2 + mp;
            Connection page = Jsoup.connect(fullURL);
            page.userAgent(USER_AGENT);
            page.followRedirects(true);
            page.cookie("JSESSIONID", session);
            Connection.Response response = page.execute();

            //parse the document
            Document html = response.parse();

            //stores the names of classes as the key, and the grade and class ID as a value
            HashMap<String, String> classGrades = new HashMap<>();
            for (Element classRow : html.select("tr.listrowodd, tr.listroweven")) {
                //cycle through all table rows (which contain class information and grades)
                //get the class name by getting the list of table data, selecting the first one,
                //getting the text, and getting the part after the ' - '
                //the last part is necessary so the class name matches the class name from the
                //overview page, otherwise it would have the class ID number in front of it
                //ex: "1000/2 - English I Honors"
                String className = classRow.getElementsByTag("td").get(0).text().split(" - ")[1];
                //save the class id for later use
                String classID = classRow.getElementsByTag("td").get(0).text().split(" - ")[0];
                //get the class grade, which is the 3rd column in
                String classGrade = classRow.getElementsByTag("td").get(2).text();
                //add the class name and grade to the hashmap
                classGrades.put(className, classGrade + "---" + classID);
            }

            return classGrades;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<ClassAssignment> classPage(String session, String id, String classID, String mp) {
        String[] parts = classID.split(":");
        String fullURL = CLASS_PAGE_URL_1 + id + CLASS_PAGE_URL_2 + parts[0] + CLASS_PAGE_URL_3 + parts[1] +
                CLASS_PAGE_URL_4 + mp;
        try {
            //get the page
            Connection page = Jsoup.connect(fullURL);
            page.userAgent(USER_AGENT);
            page.followRedirects(true);
            page.cookie("JSESSIONID", session);
            Connection.Response response = page.execute();

            //parse the document
            Document html = response.parse();

            //create an ArrayList for storing all info about all assignments
            ArrayList<ClassAssignment> data = new ArrayList<>();

            //cycle through each row - a row is for one assignment
            for (Element assignmentRow : html.select("table.list").get(0)
                    .select("tr.listrowodd, tr.listroweven")) {
                ClassAssignment assignment = new ClassAssignment();
                // get all columns of the current row
                Elements columns = assignmentRow.getElementsByTag("td");
                // if it doesn't have all of the information we need, then skip it
                // (to avoid errors)
                if (columns.size() <= 17) continue;
                assignment.date = columns.get(1).text();
                String[] categoryStrs = columns.get(5).text().split("\\s+");
                assignment.category = categoryStrs[categoryStrs.length - 1];
                assignment.name = columns.get(7).text();
                assignment.points = columns.get(14).text() + " / " + columns.get(16).text();
                assignment.grade = columns.get(17).text();

                /*for(int x = 0; x < columns.size(); x++) {
                    Log.i(GenesisHTTP.class.getSimpleName(), x + ": " + columns.get(x).text());
                }*/

                //add the assignment info to the arraylist
                data.add(assignment);
            }

            //return all collected info about all assignments for the class
            return data;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String markingPeriod(String session, String id) {
        try {
            //get the page with the given student ID and session ID
            Connection page = Jsoup.connect(GRADEBOOK_PAGE_URL_1 + id);
            page.userAgent(USER_AGENT);
            page.followRedirects(true);
            page.cookie("JSESSIONID", session);
            Connection.Response response = page.execute();

            //parse the document
            Document html = response.parse();

            Element e = html.select("option[selected]").get(1);
            return e.text();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
