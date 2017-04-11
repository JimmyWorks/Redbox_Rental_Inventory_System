/*
 * Project: Redbox Inventory System
 * Author: Jimmy Nguyen
 * Contact me: Jimmy@JimmyWorks.net
 */
package BSTree;

import BSTree.BSTreeExceptions.*;
import java.io.*;

//==============================================================================
//                      Custom Binary Search Tree Node
//==============================================================================

public class Node {
    public String title;    //title of DVD excluding the title quotations
    public int available;   //number of available copies
    public int rented;  //number of rented copies in circulation
    Node left;  //left subtree
    Node right; //right subtree
//==============================================================================
//                      Overloaded Constructors
//==============================================================================
//  These overloaded constructors all create Nodes while checking for valid
//  titles.  Titles must contain a set of quotations and are checked with the
//  method call for setTitle.  If invalid, an exception is thrown.
//==============================================================================     
    //Primary Constructor
    public Node(String title, int available, int rented) throws InvalidNodeException{
        try{
        setTitle(title);
        }catch(InvalidTitleException TE){
            throw new InvalidNodeException(title, available, rented);
        }
        this.available = available;
        this.rented = rented;
    }
    //Overloaded Constructor, typically to just check title
    public Node(String title)throws InvalidTitleException{
            setTitle(title);
            available = 0;
            rented = 0;
    }
    //Overloaded Constructor for inserting new releases
    public Node(String title, int available)throws InvalidTitleException{
            setTitle(title);
            this.available = available;
            rented = 0;
    }
    
//==============================================================================
//                      Title Get/Set Methods
//==============================================================================
//  Getters and setters for the title.  The setter method checks for a valid
//  title with quotations.  If valid, quotations are removed and saved, otherwise
//  an exception is thrown.  
//==============================================================================     
    public String getTitle(){
        return title;
    }
    public void setTitle(String title) throws InvalidTitleException{
	if(title.charAt(0) == '\"' && title.charAt(title.length()-1) == '\"'){
            title = title.replaceAll("\"", "");
            
            this.title = title;
        }
        else
            throw new InvalidTitleException(String.format("Invalid title: %s", title));
        }
    
//==============================================================================
//                      Add/Remove Available
//==============================================================================
//  Add/remove method for updating available copies.  Due to the scope of this
//  project, it is assumed that another program manages that all transactions are
//  valid.  For overdraft, available is simply set to zero (assumes this never happens)
//============================================================================== 
    public void addAvailable(int Num){
	available += Num;
    }
    public void removeAvailable(int Num){
        if(available >= Num)
            available -= Num;
	else 
            available = 0;
    }
//==============================================================================
//                      Rent and Return DVDs
//==============================================================================
//   Rent/return method for updating rented copies.  Due to the scope of this
//  project, it is assumed that another program manages that all transactions are
//  valid.  For overdraft, available is simply set to zero (assumes this never happens)  
//============================================================================== 	
    public void rentDVD(int Num){
	if (available >= Num){
            available -= Num;
            rented += Num;
        }
        else{
	available = 0;
	rented += Num;
        }
    }
    public void returnDVD( int Num){
	if(rented >= Num){
            available += Num;
            rented -= Num;
        }
        else{
            available += Num;
            rented -= Num;
        }
    }
//==============================================================================
//                      Overloaded Formatted Print Node Details
//==============================================================================
//  Print method to print contents of the Node in a formatted form.  The signature
//  with no arguments simply prints to the console, whereas the signature with a
//  PrintWriter exports to the file specified.
//============================================================================== 
    public void printDetails(PrintWriter printer){
	printer.printf("%-50s%4d%10d", title, available, rented);
        printer.println();
        printer.flush();
    } 
    
        public void printDetails(){
	System.out.printf("%-50s%4d%10d", title, available, rented);
        System.out.println();
        System.out.flush();
    } 
//==============================================================================
}