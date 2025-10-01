package gradingproject;
import java.util.concurrent.locks.ReentrantLock;
import java.util.*;
import java.time.LocalDate;
class Book
{
	String userbook[]=new String[200];
	int userbookno=0;
	String passwordbook[]=new String[200];
	int passwordbookno=0;
	String title[]=new String[200];
	int titleno=0;
	String titletaken[]=new String[200];
	int titletakenno=0;
	String author[]=new String[200];
	int authorno=0;
	int quantity[]=new int[200];
	int quantityno=0;
	String datestore[]=new String[200];
	int datestoreno=0;
	String username[]=new String[20];
	int usernameno=0;
	public ReentrantLock lock=new ReentrantLock();
	public void addtitle(String title)
	{
		this.title[titleno]=title.trim();
		titleno++;
		return;
	}
	
	public void addauthor(String author)
	{
		this.author[authorno]=author.trim();
		authorno++;
		return;
	}
	
	public void addquantity(int quantity)
	{
		this.quantity[quantityno]=quantity;
		quantityno++;
		return;
	}
	
}

class Library extends Book
{
	public synchronized int updateinfo(String titlen,int quant)
	{
		int i;
		for(i=0;i<titleno;i++)
		{
			if(titlen.equals(title[i]))
			{
				quantity[i]=quantity[i]+quant;
				return 0;
			}
			if(i==titleno-1)
			{
				System.out.println("There is no such Book with this title name ");
			}
		}
		if(i==0)
		{
			System.out.println("There is no such Book with this title name ");
		}
		return 1;
	
	}
	public void genreport()
	{
		int i;
		for(i=0;i<usernameno;i++)
		{
			System.out.println("-----------------------------------");
			System.out.println("username :"+username[i]);
			System.out.println("book taken :"+titletaken[i]);
			System.out.println("Due date was on :"+datestore);
			System.out.println("-----------------------------------");
		}
		if(i==0)
		{
			System.out.println("ALL USER ARE UP TO DATE :)");
			return;
		}
		return;
	
	}
	public synchronized void addbook(String t,String a,int q)
	{
		addtitle(t);
		addauthor(a);
		addquantity(q);
	}
	
	public synchronized boolean verifyuser(String user,String password,int num)
	{
		int i;
		for(i=0;i<userbookno;i++)
		{
			if(user.equals(userbook[i]))
			{
				if(num==0)
				{
					return true;
				}
				else if(password.equals(passwordbook[i]))
				{
					return true;
				}
				else
				{
					return false;
				}
			}
		}
		return false;
		
	}
	
		public synchronized void registerationBook(String username,String password)
		{
			if(userbookno==199) {
				System.out.println("No more space for new user!!!");
				return;
			}
			userbook[userbookno]=username;
			userbookno++;
			passwordbook[passwordbookno]=password;
			passwordbookno++;
			System.out.println("Congratulation! Now you are registered\nDon't forget your username and password!!!");
			return;
		}
	
		public synchronized  void borrowbook(String title,String username)
		{	
			lock.lock();
			int i;
			if(usernameno==199)
			{
				System.out.println("No more space to lend any book to any user!!!");
			}
			LocalDate date=LocalDate.now();
			LocalDate after=date.plusDays(5);
			for(i=0;i<title.length();i++)
			{
				if(title.equals(this.title[i]) && quantity[i]>0)
				{
					System.out.println("Book "+title+" has been successfully borrowed by "+username+" on "+date+"\nYou have to submit it on "+after);
					quantity[i]--;
					datestore[datestoreno]=after.toString();
					this.username[usernameno]=username;
					this.titletaken[titletakenno]=title;
					titletakenno++;
					datestoreno++;
					usernameno++;
					break;
				}
				else if(i!=title.length()-1)
				{
					continue;
				}
				else
				{
					System.out.println("Sorry the book \""+title+"\" is not available for borrowing");
				}
			}
				lock.unlock();
				return;
		}
	
		public synchronized int checkfine(String strdate,int yearcheck,int dayOfYear,int i,int num)
		{
			LocalDate date=LocalDate.parse(strdate);
			LocalDate due=LocalDate.parse(datestore[i]);
			int dayofyear=due.getDayOfYear();
			if(datestore[i].equals(strdate) || date.isBefore(due))
			{
				System.out.println(date);
				return 0;
			}
			else
			{
				//RS 10 fine for late return for a day
				if(num==1 || num==0 && datestore[i].substring(0,4).equals(strdate.substring(0,4)))
				{
					return((dayOfYear-dayofyear)*10);
				}
				else if(num==0 && !datestore[i].substring(0,4).equals(strdate.substring(0,4)))
				{
					return(Math.abs((dayofyear-366)+(0-dayOfYear))*10);
				}
				else if(num==1 && !datestore[i].substring(0,4).equals(strdate.substring(0,4)))
				{
					return(Math.abs((dayofyear-365)+(0-dayOfYear))*10);
				}
				else
				{  
					return(1000);
				}
			}
			
		}
		
		public boolean checkauthority(String username,String password)
		{
			if(username.equals("li"))
			{
				if(password.equals("li"))
				{
					return true;
				}
			}
			return false;
		}
		
		public synchronized void returnbook(String title,String username)
		{
			int i,j,checking;
		    LocalDate returndate=LocalDate.now();
		    
		    for(i=0;i<usernameno;i++)
		    {
		    	if(username.equals(this.username[i]))
		    	{
		    		if(!title.equals(titletaken[i])) 
		    		{
				    	System.out.println("No such book with title name :\""+title+"\" is taken by "+username);
				    	return;
				    }
		    		String strdate=returndate.toString();
					int dayofyear=returndate.getDayOfYear();
					int yearcheck=Integer.valueOf(strdate.substring(0,4));
					if(yearcheck%400==0) 
					{
						checking=checkfine(strdate,yearcheck,dayofyear,i,0);
					}
					else
					{
						checking=checkfine(strdate,yearcheck,dayofyear,i,1);
					}
					
		    		if(checking==0)
		    		{
						System.out.println("Successfully returned the book .\nFeel free to borrow a new book :)");
		    		}
		    		else
		    		{
		    			System.out.println("Successfully returned the book .\nFeel free to borrow a new book :) \nFINE :RS"+checking);
		    		}
		    		for(j=i;j<usernameno;j++)
	    			{
	    				datestore[i]=datestore[i+1];
	    				this.username[i]=this.username[i+1];
						titletaken[i]=titletaken[i+1];
	    			}
		    		usernameno--;
					datestoreno--;
					titletakenno--;
		    		quantity[i]=quantity[i]+1;
	                return;
		    	}
		    	else if(i==usernameno-1)
		    	{
		    		System.out.println("\nTill now you have not borrowed any book \nor\n May be you are not registered");
		    		return;
		    	}
                
		    }
		    if(i==0)
		    {
		    	System.out.println("Till now you have not borrowed any book \nOR\nMay be you are not registered");
		    }
			return;
		}
        
        public void searchByTitle(String title)
        {
            int i,cnt=0;
            for(i=0;i<titleno;i++)
            {
                if(title.toLowerCase().equals(this.title[i].toLowerCase()))
                {
                    System.out.println("---------------------------------------------------------------------");
                    System.out.println("Title Name :"+this.title[i]);
                    System.out.println("Author Name :"+author[i]);
                    System.out.println("Stock Available :"+quantity[i]);
                    System.out.println("---------------------------------------------------------------------");
                    cnt++;
                }
                if(cnt>0 && i==titleno-1)
                {
                	return;
                }
            }
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("Oops! No Exact match for \""+title+"\"");
            System.out.println("-----------------------------------------------------------------------------");
			return;
		}

        public void searchByAuthor(String author)
        {
            int i,cnt=0;
            for(i=0;i<authorno;i++)
            {
                if(author.toLowerCase().equals(this.author[i].toLowerCase()))
                {
                    System.out.println("---------------------------------------------------------------------");
                    System.out.println("Title Name :"+title[i]);
                    System.out.println("Author Name :"+this.author[i]);
                    System.out.println("Stock Available :"+quantity[i]);
                    System.out.println("---------------------------------------------------------------------");
                    cnt++;
                }
                if(cnt>0 && i==authorno-1)
                {
                	return;
                }
			}
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println("Oops! No Exact match for \""+author+"\"");
            System.out.println("-----------------------------------------------------------------------------");
			return;
		}
}



public class LibraryManagementSystem extends Thread {
	public static void main(String[] args) {
		Scanner in=new Scanner(System.in);
		System.out.print("Enter number of users :");
		int nouser=in.nextInt();
		in.nextLine();
		String name;
		LibraryManagementSystem[] a=new LibraryManagementSystem[nouser];
		for(int i=0;i<nouser;i++)
		{
			a[i]=new LibraryManagementSystem();
			System.out.print("Enter the name of user :");
			name=in.nextLine();
			a[i].setName(name);
		}
		System.out.println("\t\t\tWELCOME TO LIBRARY MANAGEMENT SYSTEM\n");
		for(LibraryManagementSystem thread:a)
		{
			thread.start();
		}
	}
        public synchronized void run()
        {
        	Scanner in=new Scanner(System.in);
    		Library lib=new Library();
    		int choice;
        do
        {
            System.out.print("\nLIST OF OPERATIONS :\n1.New User\n2.Search Book\n3.Borrow Book\n4.Return Book\n5.Library Authority\n6.Exit\n");
            System.out.print("Enter your choice :");
            choice=in.nextInt();
			in.nextLine();
            switch(choice)
            { 
			    case 1:
				{           locking.lock();
							System.out.print("Enter Username :");
							String telluser=in.nextLine();
							System.out.print("Enter password :");
							String tellpwd=in.next();
							boolean verify=lib.verifyuser(telluser,tellpwd,0);
							if(verify==false)
							{
								lib.registerationBook(telluser,tellpwd);
							}
							else
							{
								System.out.println("Already exist with this username");
							}
							break;
				}
					
                case 2:
                {
                	int ch;
					do{
						System.out.print("\nDo you want to search by :\n1.Title Name\n2.Author Name\n3.Back\nEnter your choice :");
						ch=in.nextInt();
						in.nextLine();
						switch(ch)
						{
							case 1:
							{
								System.out.print("Enter title of the Book :");
								String titlename=in.nextLine();
								lib.searchByTitle(titlename.trim());
								break;
							}
							case 2:
							{
								System.out.print("Enter author name of the Book :");
								String authorname=in.nextLine();
								lib.searchByAuthor(authorname.trim());
								break;
							}
							case 3:
							{
								System.out.println("EXITING....");
								break;
							}
							
							default:
							{
								System.out.println("Please choose from the given option");
								break;
							}
						}	
					}while(ch!=3);
					break;
				}
				
				case 3:
				{
					System.out.println("---------------------------------------------------------------------");
					System.out.print("Enter your username :");
					String user=in.nextLine();
					System.out.print("Enter your password :");
					String pwd=in.next();
					boolean verify=lib.verifyuser(user,pwd,1);
					if(verify==true)
					{
						System.out.print("Enter title name of the book which you want to borrow :");
						in.nextLine();
						String tit=in.nextLine();
						lib.borrowbook(tit.trim(),user);
						System.out.println("---------------------------------------------------------------------");
					}
					else
					{
						System.out.println("\nOops!! ,But you are not registered .Please register yourself :)");
						System.out.println("---------------------------------------------------------------------");
					}
					break;
				}
				
				case 4:
				{
					System.out.print("Enter Title :");
					String title=in.nextLine();
					System.out.print("Enter Username :");
					String user=in.next();
					lib.returnbook(title.trim(),user);
					break;
				}
				
				case 5:
				{
					int ch;
					System.out.print("\nEnter library authority username :");
					String username=in.next();
					System.out.print("Enter the authority password :");
					String password=in.next();
					boolean authoritycheck=lib.checkauthority(username,password);
					if(authoritycheck==true)
					{
						System.out.println("logged in successfully");
						do
						{
							System.out.print("\nLIST OF OPERATIONS :\n1.Add books\n2.Update information\n3.Generate Reports\n4.Back\nEnter your choice :");
							ch=in.nextInt();
							in.nextLine();
							switch(ch)
							{
								case 1:
								
								{
									System.out.print("Enter Title name :");
									String title=in.nextLine();
									System.out.print("Enter Author name :");
									String author=in.nextLine();
									System.out.print("Enter Quantity :");
									int quantity=in.nextInt();
									lib.addbook(title,author,quantity);
									System.out.println("Successfully added ");
									break;
								}
								case 2:
								{
									System.out.print("Enter the book title :");
									String title=in.nextLine();
									System.out.print("Enter the quantity that you want to add :");
									int quant=in.nextInt();
									int keycheck=lib.updateinfo(title.trim(),quant);
									if(keycheck==0)
									{
										System.out.println("Updated successfully");
									}
									else
									{
										System.out.println("No Updated has been done");
									}
									
									break;
								}
								case 3:
								{
									lib.genreport();
									break;
								}
							
								case 4:
								{
									System.out.println("Exited...");
									break;
								}
							
								default:
								{
									System.out.println("You entered incorrect option");
									break;
								}
							}
						}while(ch!=4);
					}
				
					else
					{
						System.out.println("Incorrect Password OR Username ");
					}
					break;
				}
				
				case 6:
				{
					break;
				}
				
				default:
				{
					System.out.println("Please select the appropriate option!!! ");
				}
			}
		}while(choice!=6);
        System.out.println("Exited....\n\n\n");
        System.out.println("\t\t\tTHANKS FOR VISITING :)");
}

}
