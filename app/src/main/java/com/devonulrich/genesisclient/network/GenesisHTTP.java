package com.devonulrich.genesisclient.network;

import com.devonulrich.genesisclient.login.LoginInfo;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.Map;

public class GenesisHTTP {

    public static final String USER_AGENT = "Mozilla/5.0";

    public static final String LOGIN_PAGE_URL =
            "https://parents.cresskillboe.k12.nj.us/genesis/parents?gohome=true";
    public static final String LOGIN_POST_URL =
            "https://parents.cresskillboe.k12.nj.us/genesis/j_security_check";
    public static final String OVERVIEW_PAGE_URL =
            "https://parents.cresskillboe.k12.nj.us/genesis/parents?tab1=" +
                    "studentdata&tab2=studentsummary&action=form&studentid=";

    //returns the cookie, which contains the session ID
    public static Connection.Response login(LoginInfo li) {
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
            if(!postResponse.url().toString().equals(LOGIN_PAGE_URL)) {
                return postResponse;
            } else {
                return null;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Connection.Response overview(String session, String id) {
        try {
            Connection page = Jsoup.connect(OVERVIEW_PAGE_URL + id);
            page.userAgent(USER_AGENT);
            page.followRedirects(true);
            page.cookie("JSESSIONID", session);
            return page.execute();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
