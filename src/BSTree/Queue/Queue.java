/*
 * Project: Redbox Inventory System
 * Author: Jimmy Nguyen
 * Contact me: Jimmy@JimmyWorks.net
 */
package BSTree.Queue;

import BSTree.*;

//==============================================================================
//                      Custom Queue Class
//==============================================================================
//  This custom Queue class was created to support the binary search tree's
//  breath-first print routine, used to debug and illustrate the structure of
//  the binary search tree.  The Queue utilizes a custom QNode class for
//  implementation.
//==============================================================================  

public class Queue {
    
    public QNode front; //QNode at the front of the Queue
    public QNode back;  //QNode at the back of the Queue
//==============================================================================
//                          Constructor
//==============================================================================    
    public Queue(){
        front = null;   //initialized to null
        back = null;    //initialized to null
    }
//==============================================================================
//                              Push
//==============================================================================
//  Push method to push new QNodes onto the Queue.  Simply makes the next pointer
//  on the new QNode to the current back and back to the current QNode.  Then,
//  if QNode's next is null, front is now also the QNode.  If next is not null,
//  next.prev is the current QNode.
//==============================================================================     
    public void push(Node newNode, int depth){
        if(newNode != null){
            QNode newQNode = new QNode(newNode, depth);

            newQNode.next = back;
            if(newQNode.next != null)
                newQNode.next.prev = newQNode;       
            back = newQNode;

            if(front == null)
                front = newQNode;
        }
    }
//==============================================================================
//                               Pop
//==============================================================================
//  Pops the front of the Queue, returning the QNode that is there, reassigning
//  the front pointer and back pointer, if necessary.
//==============================================================================     
    public QNode pop(){
        if(front == null){
            return null;
        }
        else{
            QNode thisNode = front;
            
            front = front.prev;
            if(front == null){
                back = null;
            }
            else
                front.next = null;
            
            return thisNode;
        }
    }
//==============================================================================
//                      Is Empty (Boolean) Method
//==============================================================================
//  This boolean is equivalent to checking if the front of the line is null.
//==============================================================================     
    public Boolean isEmpty(){
        return front == null;
    }
    
}
