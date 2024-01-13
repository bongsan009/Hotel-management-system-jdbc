import com.mysql.cj.xdevapi.Table;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class HotelReservationSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String username = "root";
    private static final String password = "012966630";
    private static final Scanner in = new Scanner(System.in);



    public static void main(String[] args)
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);

            boolean programOn = true;
            while (programOn)
            {
               int userOption = menu(in); in.nextLine();

                switch (userOption)
                {
                    case 1 :
                    {
                        reserveRoom(connection, in);

                        break;
                    }

                    case 2 :
                    {
                        viewReservations(connection);
                        break;
                    }

                    case 3 :
                    {
                        getRoomNumber(connection);
                        break;
                    }

                    case 4:
                    {
                        updateReservation(connection);
                        break;
                    }

                    case 5:
                    {
                        deleteReservations(connection);
                        break;
                    }

                    case 0:
                    {

                        exit();
                        return;
                    }
                }
            }




        }catch (Exception e)
        {
            System.out.println("Error!");
        }
    }

    public static int menu(Scanner in)
    {
        System.out.println(">>>>> HOTEL MANAGEMENT SYSTEM <<<<<<");
        System.out.println("1). Reserve a room.");
        System.out.println("2). View Reservations.");
        System.out.println("3). Get Room Number.");
        System.out.println("4). Update Reservations.");
        System.out.println("5). Delete Reservations.");
        System.out.println("0). Exit.");
        System.out.println("Choose an option :: "); int userOption = in.nextInt();
        return userOption;
    }

    public static void reserveRoom(Connection con, Scanner in)
    {
        try
        {
            System.out.println("Enter guest name     :: "); String name = in.nextLine();
            System.out.println("Enter room number    :: "); int roomNumber = in.nextInt(); in.nextLine();
            System.out.println("Enter contact number :: "); String contact = in.nextLine();

            String query = "Insert into reservations (guest_name, room_number, contact_number) " +
                    "Values ('" + name + "' , " + roomNumber +  ", '" + contact + " ')";

            Statement statement = con.createStatement();
            int affectRows = statement.executeUpdate(query);

            if(affectRows > 0)
            {
                System.out.println("Reservation successfully.");
            }
            else
            {
                System.out.println("Reservation failed.");
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void viewReservations(Connection con)
    {
        try
        {
            String query = "select reservation_id, guest_name, room_number, contact_number, reservation_date From reservations";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("+-------------------------------------------------------------------------------------------------------------+");
            System.out.println("| Reservation ID           | Guest          | Room                 | Contact         | Reservation Date       |");
            System.out.println("+-------------------------------------------------------------------------------------------------------------+");

            while (resultSet.next())
            {
                int reservation_id = resultSet.getInt("reservation_id");
                String name = resultSet.getString("guest_name");
                int room = resultSet.getInt("room_number");
                String contact = resultSet.getString("contact_number");
                String date = resultSet.getTimestamp("reservation_date").toString();

                System.out.println(reservation_id + ",  " + name + ", " + room + ", " + contact + ", " + date);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("EEEE");
        }
    }

    public static void getRoomNumber(Connection con)
    {
        try {

            System.out.println("Enter reservation ID   :: "); int reservationId = in.nextInt(); in.nextLine();
            System.out.println("Enter guest name       :: "); String guestName = in.nextLine();

            String query = "Select room_number From reservations where reservation_id = " + reservationId + " and guest_name = '" + guestName + "'";

            System.out.println(query);

            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next())
            {
                int roomNumnber = resultSet.getInt("room_number");

                System.out.println("Reservation ID :: " + reservationId + ", Guest Name :: " + guestName + ", and Room Number :: " + roomNumnber);
            }
            else {
                System.out.println("----> Reservation not found!");
            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void exit()
    {
        try {
            System.out.print("Exiting System");
            int i = 5;

            while (i != 0)
            {
                System.out.print(".");
                Thread.sleep(1000);
                i--;
            }
            System.out.println("");
            System.out.println("Thank you for using our application!!!");
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void updateReservation(Connection con)
    {
        try {
            System.out.println("Enter Reservation Id to update  :: ");
            int reservationId = in.nextInt(); in.nextLine();
            boolean condition = reservationExist(con, reservationId);
           if(condition)
           {
               System.out.println("Enter new guest name      :: "); String guestName = in.nextLine();
               System.out.println("Enter new room number     :: "); int room = in.nextInt(); in.nextLine();
               System.out.println("Enter new contact number  :: "); String contact = in.nextLine();

               String query = "Update reservations Set guest_name = '" + guestName + "', room_number = " + room + ", contact_number = '" + contact + "' where reservation_id = " + reservationId;
               System.out.println(query);
               Statement statement = con.createStatement();
               int affectedRows = statement.executeUpdate(query);

               if(affectedRows > 0)
               {
                   System.out.println("Update successfully.");
               }
               else
               {
                   System.out.println("Update failed.");
               }
           }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static  boolean reservationExist(Connection con, int reservationId)
    {

        try {
            String query = "Select reservation_id from reservations where reservation_id = " + reservationId;
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            return  resultSet.next();
        }catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static void deleteReservations(Connection con)
    {
        try {
            System.out.println("Enter reservation Id to delete :: "); int reservationId = in.nextInt(); in.nextLine();

            String query = "Delete From reservations Where reservation_id = " + reservationId;

            System.out.println(query);



            if(reservationExist(con, reservationId))
            {
                Statement statement = con.createStatement();
                int affectedRows = statement.executeUpdate(query);

                if(affectedRows > 0)
                {
                    System.out.println("Delete successfully.");
                }
                else {
                    System.out.println("Delete failed.");
                }
            }else
            {
                System.out.println("Delete failed.");
                return;
            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
