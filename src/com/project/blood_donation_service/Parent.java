package com.project.blood_donation_service;

import java.util.ArrayList;

public class Parent
{
    private String text1;
    private String text2;
    private String checkedtype;  
    // ArrayList to store child objects
    private ArrayList<Child> children;
     
    
    public String getText1()
    {
        return text1;
    }
     
    public void setText1(String text1)
    {
        this.text1 = text1;
    }
     
    public String getText2()
    {
        return text2;
    }
     
    public void setText2(String text2)
    {
        this.text2 = text2;
    }
    
   // ArrayList to store child objects
    public ArrayList<Child> getChildren()
    {
        return children;
    }
     
    public void setChildren(ArrayList<Child> children)
    {
        this.children = children;
    }
}