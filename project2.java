import java.sql.*;
import oracle.jdbc.*;
import java.math.*;
import java.io.*;
import java.awt.*;
import oracle.jdbc.pool.OracleDataSource;
import java.util.Scanner;

public class project2 {
    public static void main(String args[]) {
        Scanner scannerObj = new Scanner(System.in);

        try{            
            // takes username .
            System.out.print("Enter your username: ");
            String username = scannerObj.nextLine();
            // takes password to connect to Oracle Database
            System.out.print("Enter your password: ");
            String password = scannerObj.nextLine();

            // creating connection object  
            OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
            ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:ACAD111");
            Connection connObj = ds.getConnection(username, password);
            CallableStatement cs = null;
            ResultSet rs;
            BufferedReader  readKeyBoard;
            String employee_id,e_id,e_name,e_telephone,e_email,p_id,c_id;
            int outer_operation,inner_operation,pur_qty;
            float pur_unit_price;
            boolean outerloop = true;
            boolean innerloop = true;
            //while loop takes care of main menu options
            while(outerloop) {
                innerloop = true;
                System.out.println("");
                System.out.println("Select an option.");
                System.out.println(" 1. Display ");
                System.out.println(" 2. Report monthly sales ");
                System.out.println(" 3. Add employee ");
                System.out.println(" 4. Add purchase ");
                System.out.println(" 0. Exit ");
                outer_operation = scannerObj.nextInt();
                //switch case to enter that particular case
                switch(outer_operation) {
                    case 1:
                        //one more while loop to provide tables data and return to main menu 
                        while(innerloop) {
                            System.out.println("Select a table to display the records.");
                            System.out.println(" 1. Employees ");
                            System.out.println(" 2. Customers ");
                            System.out.println(" 3. Products ");
                            System.out.println(" 4. Prod_Discnt ");
                            System.out.println(" 5. Purchases ");
                            System.out.println(" 6. Logs ");
                            System.out.println(" 0. Return to main menu ");
                            //reading input to switch to that particular case
                            inner_operation = scannerObj.nextInt();
                            switch(inner_operation) {
                                case 1:
                                    //prepare to call stored procedure
                                    cs = connObj.prepareCall("begin project2.show_employees(:1); end; ");
                                    // set return type
                                    cs.registerOutParameter(1, OracleTypes.CURSOR);
                                    // execute procedure
                                    cs.execute();
                                    // reading cursor
                                    rs = (ResultSet)cs.getObject(1);
                                    // printing data in table
                                    System.out.println("");
                                    System.out.println(String.format("%-6s|%-10s|%-15s|%-30s",
                                     "EID", "NAME", "TELEPHONE#", "EMAIL"));

                                    while (rs.next()) {
                                        System.out.println(String.format("%-6s|%-10s|%-15s|%-30s",
                                                                        rs.getString(1), rs.getString(2).trim(), 
                                                                        rs.getString(3).trim(), rs.getString(4).trim()));
                                    }
                                break;
                                
                                case 2:
                                    cs = connObj.prepareCall("begin project2.show_customers(:1); end;");
                                    cs.registerOutParameter(1, OracleTypes.CURSOR);
                                    cs.execute();
                                    rs = (ResultSet)cs.getObject(1);
                                    System.out.println("");
                                    System.out.println(String.format("%-6s|%-13s|%-13s|%-15s|%-13s|%-20s",
                                     "CID", "FIRST_NAME", "LAST_NAME", "PHONE#", "VISITS_MADE", "LAST_VISIT_DATE"));

                                    while (rs.next()) {
                                        System.out.println(String.format("%-6s|%-13s|%-13s|%-15s|%-13s|%-20s", 
                                                                        rs.getString(1), rs.getString(2).trim(), 
                                                                        rs.getString(3).trim(), rs.getString(4).trim(), 
                                                                        rs.getString(5), rs.getString(6)));
                                    }
                                break;

                                case 3:
                                    cs = connObj.prepareCall("begin project2.show_products(:1); end;");
                                    cs.registerOutParameter(1, OracleTypes.CURSOR);
                                    cs.execute();
                                    rs = (ResultSet)cs.getObject(1);
                                    System.out.println("");
                                    System.out.println(String.format("%-6s|%-15s|%-8s|%-13s|%-12s|%-15s", 
                                     "PID", "NAME", "QOH", "QOH_THRESHOLD", "ORIG_PRICE", "DISCNT_CATEGORY"));

                                    while (rs.next()) {
                                        System.out.println(String.format("%-6s|%-15s|%-8s|%-13s|%-12s|%-15s", 
                                                                        rs.getString(1), rs.getString(2).trim(), 
                                                                        rs.getString(3), rs.getString(4), 
                                                                        rs.getDouble(5), rs.getString(6)));
                                    }
                                break;

                                case 4:
                                    cs = connObj.prepareCall("begin project2.show_prod_discnt(:1); end;");
                                    cs.registerOutParameter(1, OracleTypes.CURSOR);
                                    cs.execute();
                                    rs = (ResultSet)cs.getObject(1);
                                    System.out.println("");
                                    System.out.println(String.format("%-20s|%-15s", "DISCNT_CATEGORY", "DISCNT_RATE"));

                                    while (rs.next()) {
                                        System.out.println(String.format("%-20s|%-15s", rs.getString(1), rs.getDouble(2)));
                                    }
                                break;

                                case 5:
                                    cs = connObj.prepareCall("begin project2.show_purchases(:1); end;");
                                    cs.registerOutParameter(1, OracleTypes.CURSOR);
                                    cs.execute();
                                    rs = (ResultSet)cs.getObject(1);
                                    System.out.println("");
                                    System.out.println(String.format("%-8s|%-8s|%-8s|%-8s|%-20s|%-10s|%-12s|%-12s|%-10s",
                                    "PUR#", "EID", "PID", "CID", "PUR_TIME", "QUANTITY", "UNIT_PRICE", "PAYMENT", "SAVING"));

                                    while (rs.next()) {
                                        System.out.println(String.format("%-8s|%-8s|%-8s|%-8s|%-20s|%-10s|%-12s|%-12s|%-10s",
                                                                        rs.getString(1), rs.getString(2), rs.getString(3), 
                                                                        rs.getString(4), rs.getString(5), rs.getString(6), 
                                                                        rs.getDouble(7), rs.getDouble(8), rs.getDouble(9)));
                                    }
                                break;

                                case 6:
                                    cs = connObj.prepareCall("begin project2.show_logs(:1); end;");
                                    cs.registerOutParameter(1, OracleTypes.CURSOR);
                                    cs.execute();
                                    rs = (ResultSet)cs.getObject(1);
                                    System.out.println("");
                                    System.out.println(String.format("%-6s|%-15s|%-10s|%-20s|%-12s|%-15s", 
                                     "LOG# ", "USER_NAME", "OPERATION", "OP_TIME", "TABLE_NAME", "TUPLE_PKEY"));

                                    while (rs.next()) {
                                        System.out.println(String.format("%-6s|%-15s|%-10s|%-20s|%-12s|%-15s", 
                                                                        rs.getString(1), rs.getString(2).trim(), 
                                                                        rs.getString(3), rs.getString(4), 
                                                                        rs.getString(5), rs.getString(6)));
                                    }
                                break;

                                case 0:
                                    innerloop = false;
                                break;

                                default:
                                    System.out.println("Invalid option.");
                                break;
                            }
                        }
                    break;

                    case 2:
                        // Read user input data and pass as input paramters to procedure
                        readKeyBoard = new BufferedReader(new InputStreamReader(System.in));
                        //asking the parameter
                        System.out.print("Enter employee_id(e##):");
                        employee_id = readKeyBoard.readLine();
                        //checks the data type length as per the DDL
                        if (employee_id.length() != 3) {
                            throw new RuntimeException("e_id not valid");
                        }
                        //Prepare to call stored procedure
                        cs = connObj.prepareCall("begin project2.monthly_sale_activities(:1,:2); end;");
                        // Set in and out parameters
                        cs.setString(1, employee_id);
                        //set out parameters
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        // Execute procedure
                        cs.execute();
                        // Read cursor data
                        rs = ((OracleCallableStatement)cs).getCursor(2);
                        
                        // Print contents of table
                        System.out.println("");
                        System.out.println(String.format("%-6s|%-10s|%-8s|%-10s|%-12s|%-15s|%-10s", 
                                     "EID", "NAME", "MONTH", "YEAR", "SALES_COUNT", "QTY_SOLD","TOTAL"));

                        while (rs.next()) {
                            System.out.println(String.format("%-6s|%-10s|%-8s|%-10s|%-12s|%-15s|%-10s", 
                                                            rs.getString(1), rs.getString(2).trim(), 
                                                            rs.getString(3), rs.getString(4), 
                                                            rs.getString(5), rs.getString(6),rs.getDouble(7)));
                        }
                    break;

                    case 3:
                        readKeyBoard = new BufferedReader(new InputStreamReader(System.in)); 
                        
                        System.out.print("Enter e_id(e##):");
                        e_id = readKeyBoard.readLine();
                        if (e_id.length() != 3) {
                            throw new RuntimeException("e_id not valid");
                        }
                        System.out.print("Enter e_name:");
                        e_name = readKeyBoard.readLine();
                        if (e_name.length() > 15) {
                            throw new RuntimeException("e_name < 16 characters");
                        }
                        System.out.print("Enter e_telephone#(###-###-####):");
                        e_telephone = readKeyBoard.readLine();
                        if (e_telephone.length() != 12) {
                            throw new RuntimeException("telephone# needs to be above way");
                        }
                        System.out.print("Enter e_email(...@..com):");
                        e_email = readKeyBoard.readLine();
                        if (e_email.length() > 20) {
                            throw new RuntimeException(" ");
                        }

                        cs = connObj.prepareCall("begin project2.add_employee(:1,:2,:3,:4); end;");
                        // Set in parameters.
                        cs.setString(1, e_id);
                        cs.setString(2, e_name);
                        cs.setString(3, e_telephone);
                        cs.setString(4, e_email);
                        cs.execute();
                        System.out.println("Successfully Inserted");
                    break;

                    case 4:
                        readKeyBoard = new BufferedReader(new InputStreamReader(System.in)); 
                        
                        System.out.print("Enter e_id(e##):");
                        e_id = readKeyBoard.readLine();
                        if (e_id.length() != 3) {
                            throw new RuntimeException("e_id not valid");
                        }
                        System.out.print("Enter p_id(p###):");
                        p_id = readKeyBoard.readLine();
                        if (p_id.length() != 4) {
                            throw new RuntimeException("p_id not valid");
                        }
                        System.out.print("Enter c_id(c###): ");
                        c_id = readKeyBoard.readLine();
                        if (c_id.length() != 4) {
                            throw new RuntimeException("c_id not valid");
                        }
                        System.out.print("Enter pur_qty(less than 100000): ");
                        pur_qty = Integer.parseInt(readKeyBoard.readLine());
                        if (pur_qty > 99999) {
                            throw new RuntimeException("pur_qty not vlaid");
                        }
                        System.out.print("Enter unit_price(less than 1000000): ");
                        pur_unit_price = Float.parseFloat(readKeyBoard.readLine());
                        if (pur_unit_price > 99999.99) {
                            throw new RuntimeException("pur_unit_price not vlaid");
                        }

                        cs = connObj.prepareCall("begin project2.add_purchase(:1,:2,:3,:4,:5); end;");
                        // set in parameters to procedure.
                        cs.setString(1, e_id);
                        cs.setString(2, p_id);
                        cs.setString(3, c_id);
                        cs.setInt(4, pur_qty);
                        cs.setFloat(5, pur_unit_price);
                        cs.execute();

                        // Retrieve the messages from the buffer
                        CallableStatement stmt = connObj.prepareCall("{CALL DBMS_OUTPUT.GET_LINES(?, ?)}");
                        stmt.registerOutParameter(1, Types.ARRAY, "DBMSOUTPUT_LINESARRAY");
                        stmt.registerOutParameter(2, Types.INTEGER);
                        stmt.execute();
                        Array array = stmt.getArray(1);
                        String[] lines = (String[])array.getArray();

                        // Log the messages
                        for (String line : lines) {
                            if (line != null){
                                System.out.println(line);
                            }
                        }

                        System.out.println("Successfully Inserted");
                    break;

                    case 0:
                        outerloop = false;
                    break;

                    default:
                        System.out.println("Invalid option.");
                    break;
                }

            }

            cs.close();
            connObj.close();
        }
        //this part gives the error message when ever execption takes place
        catch (SQLException ex) { System.out.println ("\n*** SQLException caught ***\n" + ex.getMessage());}
        catch (Exception e) {System.out.println ("\n*** other Exception caught ***\n" + e.getMessage());}
    }
}

// for one case and different syntaxs the inline comments are given