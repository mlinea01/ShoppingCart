import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;

public class Cart 
{
	Scanner input = new Scanner(System.in);
	
	private JFrame frame;
	private JTextField item_name;
	private JLabel item_list;
	private JLabel alert;
	private List<String> shopping_cart = new ArrayList<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					Cart window = new Cart();
					window.initialize();
					window.frame.setVisible(true);
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() throws IOException
	{
		//Creates frame.
		frame = new JFrame();
		frame.setBounds(100, 100, 513, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		
		create_labels();
		
		create_textfields();
		
		create_buttons();
		
		add_image();
	}
	
	private void create_labels()
	{ 
		
		//Label used to set a title prompting the user to enter an item.  
		JLabel enter_item_title = new JLabel("Enter an Item");
		enter_item_title.setFont(new Font("Times New Roman", Font.BOLD, 20));
		enter_item_title.setBounds(17, 36, 130, 14);
		frame.getContentPane().add(enter_item_title);
		
		//Label that will be used to set the title of the shopping list where all items will appear. 
		JLabel shopping_list = new JLabel("Shopping List");
		shopping_list.setHorizontalAlignment(SwingConstants.CENTER);
		shopping_list.setFont(new Font("Times New Roman", Font.BOLD, 17));
		shopping_list.setBounds(317, 3, 116, 30);
		frame.getContentPane().add(shopping_list);
		
		//Label that is used to print out the shopping list to the screen.  
		item_list = new JLabel("");
		item_list.setOpaque(true);
		item_list.setBackground(Color.WHITE);
		item_list.setVerticalAlignment(SwingConstants.TOP);
		item_list.setFont(new Font("Times New Roman", Font.BOLD, 15));
		item_list.setBounds(259, 29, 228, 401);
		JScrollPane item_list_scrollbar = new JScrollPane(item_list);
		item_list_scrollbar.setBounds(259, 29, 228, 401);
		frame.getContentPane().add(item_list_scrollbar);
		item_list.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.black),
		BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		/*
		 * Label that will be used to print out alerts such as "You already added that item" if the item 
		 * you want to add is already in the list.  
		 */
		alert = new JLabel("");
		alert.setForeground(Color.RED);
		alert.setHorizontalAlignment(SwingConstants.CENTER);
		alert.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		alert.setBounds(10, 199, 239, 36);
		frame.getContentPane().add(alert);
	}
	
	private void create_textfields()
	{
		//Text field used to enter an item.  
		item_name = new JTextField();
		item_name.setBounds(9, 75, 216, 20);
		frame.getContentPane().add(item_name);
		item_name.setColumns(10);
	}
	
	private void create_buttons()
	{
		//Button that will be used to add an item into the list. 
		JButton add_item = new JButton("Add Item");
		add_item.setBounds(17, 106, 99, 23);
		frame.getContentPane().add(add_item);
		
		//Button that will be used to delete an item from the list
		JButton delete_item = new JButton("Delete Item");
		delete_item.setBounds(126, 106, 99, 23);
		frame.getContentPane().add(delete_item);
		
		//Button used to quit out of the application and close the window.  
		JButton quit = new JButton("Quit");
		quit.setBounds(68, 153, 99, 23);
		frame.getContentPane().add(quit);
		
		/*
		 * Action listener that is called when the "add item" button is clicked to add an item to the shopping list. 
		 * If the text field is empty and you click the "add item" button, it will alert the user to enter an item name. 
		 * If you enter an item that is already in the list, it will alert the user that the item is already in the list. 
		 */
		add_item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if(item_name.getText().isEmpty())
				{
					alert.setText("Please enter an item to add!");
					item_name.requestFocus();
				}
				else if(!shopping_cart.contains(item_name.getText().trim().replaceAll("( )+", " ").toLowerCase()))
				{	
					shopping_cart.add(item_name.getText().trim().replaceAll("( )+", " ").toLowerCase());
					
					build_grocery_list();
					
					reset_text();
				}
				else
				{
					alert.setText("You already added that item!");
					item_name.setText("");
					item_name.requestFocus();
				}
				
			}
			
		});
		
		/*
		 * Action listener that is called when the "delete item" button is clicked to delete an item from the shopping list. 
		 * If the text field is empty and you click the "delete item" button, it will alert the user to enter an item name. 
		 * If you enter an item that is not in the list, it will alert the user that the item is not in the list. 
		 */
		delete_item.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				
				if(item_name.getText().isEmpty())
				{
					alert.setText("Please enter an item to delete!");
					item_name.requestFocus();
				}
				else
				{
					if(!shopping_cart.contains(item_name.getText().trim().replaceAll("( )+", " ").toLowerCase()))
					{
						alert.setText("That item is not in your list.");
						item_name.setText("");
						item_name.requestFocus();
					}
					else
					{
						shopping_cart.remove(item_name.getText().trim().replaceAll("( )+", " ").toLowerCase());
						
						build_grocery_list();
					
						reset_text();
					}
				}
			}
			
		});
		
		//Action listener that is called when the "quit button" is clicked which will dispose of the frame, closing the window. 
		quit.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				
				frame.dispose();
				
			}
			
		});
	}
	
	//Method used to reset text fields after an item is added or deleted from the list.
	private void reset_text()
	{
		item_name.setText("");
		alert.setText("");
		item_name.requestFocus();
	}
	
	//Method used to build the StringBuilder that will be printed onto the screen after an item is added or deleted from the list.
	private void build_grocery_list()
	{
		StringBuilder print_list = new StringBuilder();
		
		for(int i = 0; i < shopping_cart.size(); i++)
		{
			print_list.append("<HTML>" + (i + 1) + ". "+ shopping_cart.get(i) + "<BR>");
		}
		
		item_list.setText(print_list.toString());
	}
	
	private void add_image() throws IOException
	{
		/*
		 * Grabs the image from a specified file and stores it into a BufferedImage variable. 
		 * This variable will then be used to add the image to a label to print the image out the screen.  
		 */
		String path = "groceries.png";
	    File file = new File(path);
	    BufferedImage image = ImageIO.read(file);
		
		//Label that is used to set an image in the frame.  
	    JLabel grocery_image = new JLabel(new ImageIcon(image));
		grocery_image.setBackground(Color.WHITE);
		grocery_image.setBounds(10, 276, 239, 165);
		frame.getContentPane().add(grocery_image);
	}
}
