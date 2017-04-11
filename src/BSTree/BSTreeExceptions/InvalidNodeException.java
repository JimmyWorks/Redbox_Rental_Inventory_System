/*
 * Project: Redbox Inventory System
 * Author: Jimmy Nguyen
 * Contact me: Jimmy@JimmyWorks.net
 */
package BSTree.BSTreeExceptions;

//==============================================================================
//                      InvalidNodeException
//==============================================================================
//  Extends the BSTreeException, the primary purpose of this exception is to be  
//  thrown carrying the details of the invalid Node that the program was trying
//  to create.
//==============================================================================  
public class InvalidNodeException extends BSTreeException{
    
    public String inputLine;
    
    public InvalidNodeException(){
        
    }
    
    public InvalidNodeException(String title, int available, int rented){
        inputLine = String.format("%s,%d,%d", title, available, rented);
    }
    
}
