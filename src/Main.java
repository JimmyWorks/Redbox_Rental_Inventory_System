/*
 * Project: Redbox Inventory System
 * Author: Jimmy Nguyen
 * Contact me: Jimmy@JimmyWorks.net
 */
//==============================================================================
//                              Project Scope
//
//  The purpose of this project is to simulate a Redbox DVD Rental System with
//  all movie titles along with their available and rented units stored in a
//  binary search tree with full functionality for adding additional units, 
//  removing units, renting and returning units, and deleting units, should
//  removing units yield a title without any available or rented units.  The
//  current inventory is imported from an inventory.dat file and the transactions
//  are simulated with an imported transaction.log file.  Errors in both files
//  are also handled and logged to an error.log file.  To simulate reports logged
//  after every day, when the transaction file is done processing, prior to closing
//  the program, an inventory report is generated and exported to redbox_kiosk.txt.
//==============================================================================

import java.io.*;                   //Import for accessing files and exporting to files
import java.util.*;                 //Import for Scanner Class and Wrapper Classes
import BSTree.*;                    //Import for Customized Binary Search Tree Class and Nodes
import BSTree.BSTreeExceptions.*;   //Import for Customized Exception Handling Within Binary Search Trees


public class Main {
//==============================================================================
//                              Global Constants
//==============================================================================
//  Strings for all of the filenames imported from and exported to for easy reference
//  and easy access for changes.
//==============================================================================
        static final String INVENTORY_FILENAME = "inventory.dat";
        static final String TRANSACTION_FILENAME = "transaction.log";
        static final String ERROR_FILENAME = "error.log";
        static final String REPORT_FILENAME = "redbox_kiosk.txt";
    
//==============================================================================
//                            Program Main
//      (This is the main driver function for the Redbox Inventory System)
//==============================================================================
    public static void main(String[] args) throws Exception{


//==============================================================================
//                              Loading
//==============================================================================
//  The following block of code creates all the Scanner objects needed by the 
//  program to import the library of movies in the inventory file and the 
//  transaction file which will be processed to update the system and export the
//  library to a final file.  In the event the filenames declared in the Global
//  Constants section above is invalid, the program will also prompt the user
//  for a valid filename or filepath.  The PrintWriter for any errors encountered
//  is also created below.
//==============================================================================
        System.out.println("Setting up files...");
       
        File[] inputFiles = new File[2];
        inputFiles[0] = new File(INVENTORY_FILENAME);
        inputFiles[1] = new File (TRANSACTION_FILENAME);
        
        //Scanner for user input if invalid file is found
        Scanner input = new Scanner(System.in);
        
        //Check if file exists, if it does not exist or is a directory, prompt
        //the user to input the correct filename and try again
        for(int i = 0; i < inputFiles.length; i++){
            while(!inputFiles[i].exists() && inputFiles[i].isDirectory()){
                System.out.printf("Your file name \"%s\" was not found.", inputFiles[i].getName());
                System.out.println("Please specify the name of your input file:");
                String filename = input.nextLine();
                inputFiles[i] = new File(filename);
            }
        }
        
        Scanner[] readFiles = new Scanner[inputFiles.length];
        for(int i = 0; i < readFiles.length; i++)
            readFiles[i] = new Scanner(inputFiles[i]);
        
        //PrintWriter for errors while creating the binary search tree or processing transactions
        PrintWriter errorPrinter = new PrintWriter(ERROR_FILENAME);

//==============================================================================
//              Import Inventory File and Create Binary Search Tree
//==============================================================================

        BinaryTree myTree = new BinaryTree();
        
        System.out.println("\nImporting from Inventory File...\n");
        while(readFiles[0].hasNext()){
            try{
            String[] buffer;
            buffer = readFiles[0].nextLine().split(",");
            //Debug Print to see what is being processed by buffer
            System.out.println(Arrays.toString(buffer));
            Node newNode = new Node(buffer[0], Integer.parseInt(buffer[1]), Integer.parseInt(buffer[2]));
            System.out.printf("New Node created: %s, available: %d, rented: %d\n", newNode.title, newNode.available, newNode.rented);
            myTree.insertNode(newNode);
            }catch(InvalidNodeException err){
                errorPrinter.println(err.inputLine);
                //Debug Print to see if any imported files are corrupted
                System.out.printf("Import Error: %s\n", err.inputLine);
                errorPrinter.flush();
            }
        }
        //Debug Print to see what the completed binary search tree structure
        System.out.println("\n\nBinary Search Tree Completed.\nCreated Tree is:\n");
        myTree.printTree();
        System.out.println();
        
//==============================================================================
//          Process the Transaction File Using the Newly Created Tree
//==============================================================================
//  This portion of the program processes each line of the transaction file.
//  It is assumed that a different program will check that all transactions to
//  be performed, can be performed, e.g. cannot rent when none available or
//  cannot remove more than available.  This program handles a variety of
//  exceptions and any line of the transaction file that has errors, e.g. typos
//  due to communication errors, is not processed and printed to the error logger.
//==============================================================================
        System.out.println("\nProcessing transactions...\n");
        while(readFiles[1].hasNext()){ //while not at the end of the file
            String[] bufferInstr;   //first buffer array for handling the string
            //split the string in half with space delimiter
            bufferInstr = readFiles[1].nextLine().split(" ", 2);
            //now try to utilize the switch statement checking the validity of the
            //first string in the line.  If invalid, catch statement will handle it.
            try{
            System.out.printf("Now processing: %s\n", Arrays.toString(bufferInstr));
            switch(bufferInstr[0]){ //check the first string
                case "add": //valid add case
                        Node newNode, foundNode;

                        System.out.println("===Add Transaction===");
                        //create another buffer to split the string
                        String[] buffer = bufferInstr[1].split(",");
                        System.out.printf("Now processing: %s\n", Arrays.toString(buffer));
                        //try to make a new Node with contents of buffer
                        newNode = new Node(buffer[0], Integer.parseInt(buffer[1]));
                        //search for that node
                        foundNode = myTree.searchTree(newNode);
                        //if not found, insert the newly created Node into the tree
                        if(foundNode == null){
                            myTree.insertNode(newNode);
                        }
                        else //otherwise, add the correct number of copies to the Node
                            foundNode.addAvailable(Integer.parseInt(buffer[1]));
                    break;
                case "remove":  //valid remove case
                    System.out.println("===Remove Transaction===");
                    //create another buffer to split the string
                    buffer = bufferInstr[1].split(",");
                    System.out.printf("Now processing: %s\n", Arrays.toString(buffer));
                    //try to make a new Node with contents of buffer
                    newNode = new Node(buffer[0], Integer.parseInt(buffer[1]));
                    //search for that node
                    foundNode = myTree.searchTree(newNode);
                    //if not found, throw an exception
                    if(foundNode == null)
                        throw new InvalidNodeException();
                    else{
                        //otherwise, remove the correct number of copies
                        foundNode.removeAvailable(Integer.parseInt(buffer[1]));
                        //if no copies available or rented, delete the Node
                        if(foundNode.available == 0 && foundNode.rented == 0){
                            System.out.println("Deleting node...");
                            myTree.deleteNode(foundNode);
                        }
                    }
                    break;
                case "rent":    //valid rent case
                    System.out.println("===Rent Transaction===");
                    newNode = new Node(bufferInstr[1]);
                    System.out.printf("Now processing title: %s", bufferInstr[1]);
                    foundNode = myTree.searchTree(newNode);

                    //if not found, throw an exception
                    if(foundNode == null)
                        throw new InvalidNodeException();
                    else  //otherwise, rent one copy
                        foundNode.rentDVD(1);

                    break;
                case "return":  //valid return case
                    System.out.println("===Return Transaction===");
                    newNode = new Node(bufferInstr[1]);
                    
                    foundNode = myTree.searchTree(newNode);
                    
                    //if not found, throw an exception
                    if(foundNode == null)
                        throw new InvalidNodeException();
                    else  //otherwise, return one copy
                        foundNode.returnDVD(1);
                    break;
                default:    //otherwise, this line has an error
                    System.out.printf("Processing Error: %s\n", String.format("%s %s", bufferInstr[0], bufferInstr[1]));
                    //have the error printer export the line to the error log
                    errorPrinter.println(String.format("%s %s", bufferInstr[0], bufferInstr[1]));
                    errorPrinter.flush();
            }
        }catch(Exception ite){
            //any exception encountered will require a print exported to the error log
            errorPrinter.println(String.format("%s %s", bufferInstr[0], bufferInstr[1]));
            System.out.println("Encountered an exception...");
                }
        //Debug Routine for Checking Tree Contents After Each Transaction    
        System.out.println();
        myTree.printTree();
        System.out.println("=============================================\n\n");
        
        }
//==============================================================================
//                      Print to Final Report
//==============================================================================
//  Once completed with all transactions, print the final report.
//==============================================================================
        System.out.println("\nExporting final report...\n");
        PrintWriter reportPrinter = new PrintWriter(REPORT_FILENAME);
        myTree.exportReport(reportPrinter);
//==============================================================================
//                      End Program
//==============================================================================
       
        System.out.println("\n\n====================================================\n\n");
        System.out.println("Thank you for visiting!\n\n");
        System.out.println("For all questions, please contact me at:");
        System.out.println("Jimmy@JimmyWorks.net\n\n");
        System.out.println("====================================================");
        
        System.exit(0);
    }
}
