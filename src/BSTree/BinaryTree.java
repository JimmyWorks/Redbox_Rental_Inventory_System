/*
 * Project: Redbox Inventory System
 * Author: Jimmy Nguyen
 * Contact me: Jimmy@JimmyWorks.net
 */
package BSTree;


import BSTree.Queue.*;
import java.io.*;

//==============================================================================
//                         Custom Binary Search Tree
//==============================================================================
public class BinaryTree {

    Node root;  //the only member of a binary search tree, the root
    
    //Note: the Node, itself, contains the left and right pointers to subtrees
    
//==============================================================================
//                              Constructors
//============================================================================== 
    public BinaryTree(){
        root = null;
    }
    public BinaryTree(Node root){
        this.root = root;
    }
//==============================================================================
//                              Get Height
//==============================================================================
//  This method uses recursion to get the height of the tree.  The max of the
//  two subtrees are compared and the larger one is returned, with +1 height, for
//  each level going up.
//============================================================================== 
    public int getHeight (){
        if(root != null){ //if not null
            if(root.left == null && root.right == null)
                return 0; // the tree composed of only a root has height of zero
            else{   //otherwise, recursively find height of left and right subtree
                BinaryTree leftTree = new BinaryTree(root.left);
                BinaryTree rightTree = new BinaryTree(root.right);
                int leftTreeHeight = leftTree.getHeight(); //recursive call
                int rightTreeHeight = rightTree.getHeight(); //recursive call
                //then return larger height + 1
                if(leftTreeHeight > rightTreeHeight)
                    return 1 + leftTreeHeight;
                else
                    return 1 + rightTreeHeight;
                
            }
        }
        else //null tree has height of zero
            return 0;
    }
//==============================================================================
//                      Overloaded Search Tree Method
//==============================================================================
//  This method searches the tree for a specific title and returns the pointer
//  to that Node.  This is performed recursively.  
//============================================================================== 
    //Overloaded Method Call
    public Node searchTree(Node findMe){
        return searchTree(findMe.title);
    }
    //Primary Method Call
    public Node searchTree (String title){
        if(root != null){   //if the titles match, return the Node
            if(title.compareTo(root.title) == 0){
                return root; 
            }   //else, if the title is earlier in the alphabet, check left subtree
            else if(title.compareTo(root.title) < 0){
                BinaryTree leftTree = new BinaryTree(root.left); //recursive call
                return leftTree.searchTree(title);   
            }
            else{ //else, if the title is further in the alphabet, check right subtree
                BinaryTree rightTree = new BinaryTree(root.right); //recursive call
                return rightTree.searchTree(title);      
            } 
        }
        else
            return null;
    }
//==============================================================================
//                      Return Right Most Node
//==============================================================================
//   This method takes the parent node and finds the right-most leaf.  When found,
//  the leaf is detached from its parent and returned.
//==============================================================================     
    private Node returnRightMostNode(Node parent){
        if(parent != null && parent.right != null){
            if(parent.right.right != null){ //if left tree exists
                return returnRightMostNode(parent.right); //call recursively
            }
            else{   //else, found terminal right-most node
                Node rightMostNode = parent.right;  //save the node
                parent.right = null;    //detach the node
                return rightMostNode;   //return the node
            }
            
        }
	else
            return null;
    }
//==============================================================================
//                      Delete Node Function
//==============================================================================
//  By far, the largest and most complex method call in this class because of
//  all the possible cases that it entails.  If the root is what needs to be 
//  deleted, the same checks would need to be performed as any other node.  Here,
//  the deleteNodeHelper is used as a helper function for all other cases.  In 
//  the case of the root, all the easy cases are checked first (no children, one
//  child), before calling the replaceRootWithSuccessor method to handle the two
//  children case which returns the correct tree with the deletion and simply
//  requires the root to be assigned to it.
//============================================================================== 
    public void deleteNode(Node findMe){
        if(root != null && findMe != null){ //first check if both the root and Node exist
            // if the Node is the root
            if(findMe.title.compareTo(root.title) == 0){
                //if it has no children, simply delete it
                if(root.left == null && root.right == null)
                    root = null;
                //if it has only a right child, simply make that the root
                else if(root.left == null)
                    root = root.right;
                //if it has only a left child, simply make that the root
                else if(root.right == null)
                    root  = root.left;
                //else, both offspring exist
                else{
                    //System.out.println("Root has 2 sub-trees.  Need to delete root...");
                    if(root.left.right == null){ //if the left child has no right children, it is the successor
                        root.left.right = root.right;
                        root = root.left;
                    }
                    else{ //we need to find the successor
                        //System.out.println("Calling replaceRootWithSuccessor");
                        root = replaceRootWithSuccessor(root);
                        //System.out.println("Root is now " + root.title);
                    }
                }
            }
            //if it is not the root, if it's less than the root title and left subtree exist...
            else if(findMe.title.compareTo(root.title) < 0){
                //System.out.printf("Recursive delete call using left sub-tree with %s as the root", root.left.title);
                if(root.left != null){
                    deleteNodeHelper(root, findMe); //call delete node helper function
                }
                //else the node is not in this tree
            }
            else{ //if it is not the root and greater than the root title and right subtree exist...
                //System.out.printf("Recursive delete call using right sub-tree with %s as the root", root.left.title);
                if(root.right != null){
                    deleteNodeHelper(root, findMe); //call delete node helper function
                }
                //else the node is not in this tree
            }
                
        }
    }
//==============================================================================
//                      Delete Node Helper Function
//==============================================================================
//  This method is the helper function for the deleteNode method, the most 
//  complex method call in this class.  This helper function is only called only
//  after the root of the tree is determined to not be the node to be deleted.
//  This method will recursively traverse the tree, Node by Node, till one of the
//  offspring Nodes are the Node of interest.  Then, it will check for easy cases
//  first: no offspring or one offspring.  Either of those cases are then handled.
//  In the case there are two offspring, the replaceRootWithSuccessor method is 
//  called to handle it, simply assigning the parent's pointer to the returned 
//  Node.  The replaceRootWithSuccessor method properly manages everything needed.
//============================================================================== 
    private void deleteNodeHelper(Node cur, Node findMe){
        if(findMe.title.compareTo(cur.title)<0){
            if(cur.left != null){
                //if the left Node is the Node to be deleted
                if(findMe.title.compareTo(cur.left.title)==0){
                    //if it has no children, simply delete it
                    if(cur.left.left == null && cur.left.right == null){
                        cur.left = null;
                    }
                    //if it has only a right child, assign that as the new left Node
                    else if(cur.left.left == null){
                        cur.left = cur.left.right;
                    }
                    //if it has only a left child, assign that as the new left Node
                    else if(cur.left.right == null){
                        cur.left = cur.left.left;
                    }
                    else{ // Node to be deleted has two children
                        if(cur.left.left.right == null){ //if the left child has no right children, it is the successor
                            cur.left.left.right = cur.left.right;
                            cur.left = cur.left.left;
                        }
                        else{   //call the following method to handle it and assign to the returned node
                            cur.left = replaceRootWithSuccessor(cur.left);
                        }
                    }
                       
                }
                else{   //call helper function recursively
                    deleteNodeHelper(cur.left, findMe);
                }
            }
            //else the node is not in this tree
        }
        else{ //findMe.title > cur.title
            //see notes in top if-statement for similar comments
            if(cur.right != null){
                if(findMe.title.compareTo(cur.right.title)==0){
                    if(cur.right.left == null && cur.right.right == null){
                        cur.right = null;
                    }
                    else if(cur.right.left == null){
                        cur.right = cur.right.right;
                    }
                    else if(cur.right.right == null){
                        cur.right = cur.right.left;
                    }
                    else{
                        if(cur.right.left.right == null){ //if the left child has no right children, it is the successor
                        cur.right.left.right = cur.right.right;
                        cur.right = cur.right.left;
                        }
                        else{
                        cur.right = replaceRootWithSuccessor(cur.right);
                        }
                    }
                }
                else{   //call helper function recursively
                    deleteNodeHelper(cur.right, findMe);
                }
            }
            //else the node is not in this tree
        }
    }
//==============================================================================
//                      Replace Root with Successor
//==============================================================================
//  This method is for the worst case scenario that the Node to be deleted has
//  two offspring and requires a successor.  While this method does safety checks,
//  this method should only be called after the appropriate checks are made
//  ensuring the root sent in has two offspring.  If the left child has no
//  right children, it is the successor, and is simply promoted.  If it does
//  have right children, the returnRightMostNode is called to find the right-most
//  child of root.left.  That Node is the successor and it's left sub-tree is also
//  saved.  The root's left and right subtrees are assigned to the successor's
//  left and right subtree.  Then, viewing the newly created successor's subtree
//  as a binary search tree, the successor's previously removed left subtree, if
//  it exists, must be reinserted and attached.
//==============================================================================    
    private Node replaceRootWithSuccessor(Node root){
        if(root != null){   // If root exists (which it should)
            if(root.left != null){  // If the left subtree exists (which it should)
                if(root.left.right == null){    // If the left, right tree is empty, then left is the successor
                    root.left.right = root.right;   // Set right to left's right
                    return root.left;   // return left tree to replace root
                }
                else{   // If left tree exists and it has a right subtree...
                    //The successor is the right-most node of the left subtree
                    //The subroutine, returnRightMostNode, returns the detached Node
                    Node successor = returnRightMostNode(root.left);
                    //Save the successor's left subtree, if it exists
                    Node successorLeft = successor.left;
                    //Set the successor's left and right subtrees to those of the root's
                    successor.left = root.left;
                    successor.right = root.right;
                    //Create a temp subTree for the successor
                    BinaryTree subTree = new BinaryTree(successor);
                    //Reinsert the successor's previous left subtree
                    subTree.insertNode(successorLeft);
                    return successor;   // return the successor to replace root
                }
            }
            else
                return root.right; //In the event this call is used improperly, 
        }  //the right offspring is the successor if there is no left offspring
        else
            return null;    //Return null if called on an empty tree
    }
//==============================================================================
//                      Insert New Node into Tree
//==============================================================================
//  This method is the initial function for exporting Nodes to a text file.
//  It prints the formatted header then sends the root into the helper function
//  where everything else is handled recursively.
//==============================================================================
    public void insertNode(Node newNode){
        //System.out.println("== Inserting Node ==");
	if(root != null){
            if(newNode != null){
                if(newNode.title.compareTo(root.title) <= 0){
                    if(root.left == null){
                        System.out.flush();
                        root.left = newNode;
                    }
                    else{
                        System.out.flush();
                        new BinaryTree(root.left).insertNode(newNode);
                    }
                }
                else{    // title > root.title 
                    if(root.right == null)
                        root.right = newNode;
                    else{
                        new BinaryTree(root.right).insertNode(newNode);
                    }
                }
            }
        }
	else
            root = newNode;
    }
//==============================================================================
//                      Recursive Export Tree to File
//            (Prints Header and Calls Helper Function for Recursion)
//==============================================================================
//  This method is the initial function for exporting Nodes to a text file.
//  It prints the formatted header then sends the root into the helper function
//  where everything else is handled recursively.
//==============================================================================	
    public void exportReport(PrintWriter printer){
        //Format and print header
        printer.printf("%-45s%9s%10s", "Title", "Available", "Rented");
        printer.println();
        printer.println("================================================================");
        //Call helper function to do the rest
        exportTree(root, printer);

    }

//==============================================================================
//                      Recursive Export Tree to File
//                 (Helper Routine for exportReport Method)
//==============================================================================
//  This method is the helper function for the exportReport method.  It takes
//  a Node and a PrintWriter with the very first Node being the root of the tree
//  you want to export to the specified text file.  The method then performs
//  depth-first, in-order recursion, calling the left tree first until a null 
//  pointer is encountered.  Then, the current Node is printed, then the right
//  sub-tree called again, recursively.  This prints all the contents of the tree
//  in alphabetical order.
//==============================================================================
    private void exportTree(Node cur, PrintWriter printer){
        if(cur != null){                    // If the Node exists,
            exportTree(cur.left, printer);  // Call the left sub-tree recursively
            
            cur.printDetails(printer);      // Then, print parent Node
            
            exportTree(cur.right, printer); // Then, call right sub-tree recursively
            
        }
    }
    
//==============================================================================
//                      Print Tree to Console
//==============================================================================
//  This method prints a visual dipection of the current binary search tree
//  showing the tree's root and nodes at each depth in the tree, essentially,
//  like a tree's blueprint, it can be used to draw the current tree to see
//  the tree's current structure.  This is a more condensed print which assumes
//  all nodes are placed properly and no nodes are out of place.  This method 
//  also uses the Queue package for the customized Queue and QNode objects, 
//  since this print method requires breadth-first processing to print properly.
//==============================================================================
    public void printTree(){
        
        if(root != null){
            System.out.println("Printing tree...");
            Queue myQ = new Queue();
            
            QNode cur = null;
            int depth = 0;
            myQ.push(root, depth);
            
            while(!myQ.isEmpty() || cur != null){
                
                if(cur == null){
                    cur = myQ.pop();
                }
                else{
                    
                    if(depth != cur.depth){
                        depth = cur.depth;
                        System.out.printf("\nDepth %d: %s(a: %d, r: %d)", depth, cur.BSTNode.title, cur.BSTNode.available, cur.BSTNode.rented);
                    }
                    else if(depth == 0){
                        System.out.printf("Root: %s(a: %d, r: %d)", cur.BSTNode.title, cur.BSTNode.available, cur.BSTNode.rented);
                        //System.out.printf("Cur Depth is %d", cur.depth);
                    }
                    else{
                        System.out.printf(", %s(a: %d, r: %d)", cur.BSTNode.title, cur.BSTNode.available, cur.BSTNode.rented);
                    }
                    myQ.push(cur.BSTNode.left, (depth+1));
                    myQ.push(cur.BSTNode.right, (depth+1));
                    cur = null;
                }
            }
            System.out.println();   
        }
        else
            System.out.println("This tree is an empty tree.");                
        
        System.out.flush();
    }       
}
