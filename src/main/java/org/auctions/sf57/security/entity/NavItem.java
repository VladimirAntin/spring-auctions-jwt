package org.auctions.sf57.security.entity;

/**
 * Created by vladimir_antin on 13.5.17..
 */
public class NavItem {
    public String href;
    public String name;
    public String icon;

    public NavItem(String href, String name, String icon) {
        this.href = href;
        this.name = name;
        this.icon = icon;
    }
}