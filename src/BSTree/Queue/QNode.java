/*
 * Project: Redbox Inventory System
 * Author: Jimmy Nguyen
 * Contact me: Jimmy@JimmyWorks.net
 */
package BSTree.Queue;

import BSTree.*;


//==============================================================================
//                      Custom QNode (Queue Node)
//==============================================================================
//  This custom QNode class was created to support the binary search tree's
//  breath-first print routine, used to debug and illustrate the structure of
//  the binary search tree.  This class serves as nodes to the Queue class.
//============================================================================== 
public class QNode {
    public QNode next;  //next node
    public QNode prev;  //previous node
    public Node BSTNode;    //the binary search tree Node that this QNode contains
    public int depth;       //the depth of that Node in the binary search tree
//==============================================================================
//                          Constructor
//   aa(Note that the contructor requires the Node's depth when created)
//==============================================================================
    public QNode(Node BSTNode, int depth){
        next = null;
        prev = null;
        this.BSTNode = BSTNode; //the Node that will be processed
        this.depth = depth;     //found and passed in when traversing, breadth-first
    }
    
    
}
