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
	private JTextField itemName;
	private JLabel itemList;
	private JLabel alert;
	private List<String> shoppingCart = new ArrayList<>();

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
		
		createLabels();
		
		createTextfields();
		
		createButtons();
		
		addImage();
	}
	
	private void createLabels()
	{ 
		
		//Label used to set a title prompting the user to enter an item.  
		JLabel enterItemTitle = new JLabel("Enter an Item");
		enterItemTitle.setFont(new Font("Times New Roman", Font.BOLD, 20));
		enterItemTitle.setBounds(17, 36, 130, 14);
		frame.getContentPane().add(enterItemTitle);
		
		//Label that will be used to set the title of the shopping list where all items will appear. 
		JLabel shoppingList = new JLabel("Shopping List");
		shoppingList.setHorizontalAlignment(SwingConstants.CENTER);
		shoppingList.setFont(new Font("Times New Roman", Font.BOLD, 17));
		shoppingList.setBounds(317, 3, 116, 30);
		frame.getContentPane().add(shoppingList);
		
		//Label that is used to print out the shopping list to the screen.  
		itemList = new JLabel("");
		itemList.setOpaque(true);
		itemList.setBackground(Color.WHITE);
		itemList.setVerticalAlignment(SwingConstants.TOP);
		itemList.setFont(new Font("Times New Roman", Font.BOLD, 15));
		itemList.setBounds(259, 29, 228, 401);
		JScrollPane itemListScrollbar = new JScrollPane(itemList);
		itemListScrollbar.setBounds(259, 29, 228, 401);
		frame.getContentPane().add(itemListScrollbar);
		itemList.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.black),
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
	
	private void createTextfields()
	{
		//Text field used to enter an item.  
		itemName = new JTextField();
		itemName.setBounds(9, 75, 216, 20);
		frame.getContentPane().add(itemName);
		itemName.setColumns(10);
	}
	
	private void createButtons()
	{
		//Button that will be used to add an item into the list. 
		JButton addItem = new JButton("Add Item");
		addItem.setBounds(17, 106, 99, 23);
		frame.getContentPane().add(addItem);
		
		//Button that will be used to delete an item from the list
		JButton deleteItem = new JButton("Delete Item");
		deleteItem.setBounds(126, 106, 99, 23);
		frame.getContentPane().add(deleteItem);
		
		//Button used to quit out of the application and close the window.  
		JButton quit = new JButton("Quit");
		quit.setBounds(68, 153, 99, 23);
		frame.getContentPane().add(quit);
		
		/*
		 * Action listener that is called when the "add item" button is clicked to add an item to the shopping list. 
		 * If the text field is empty and you click the "add item" button, it will alert the user to enter an item name. 
		 * If you enter an item that is already in the list, it will alert the user that the item is already in the list. 
		 */
		addItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				if(itemName.getText().isEmpty())
				{
					alert.setText("Please enter an item to add!");
					itemName.requestFocus();
				}
				else if(!shoppingCart.contains(itemName.getText().trim().replaceAll("( )+", " ").toLowerCase()))
				{	
					shoppingCart.add(itemName.getText().trim().replaceAll("( )+", " ").toLowerCase());
					
					buildGroceryList();
					
					resetText();
				}
				else
				{
					alert.setText("You already added that item!");
					itemName.setText("");
					itemName.requestFocus();
				}
				
			}
			
		});
		
		/*
		 * Action listener that is called when the "delete item" button is clicked to delete an item from the shopping list. 
		 * If the text field is empty and you click the "delete item" button, it will alert the user to enter an item name. 
		 * If you enter an item that is not in the list, it will alert the user that the item is not in the list. 
		 */
		deleteItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				
				if(itemName.getText().isEmpty())
				{
					alert.setText("Please enter an item to delete!");
					itemName.requestFocus();
				}
				else
				{
					if(!shoppingCart.contains(itemName.getText().trim().replaceAll("( )+", " ").toLowerCase()))
					{
						alert.setText("That item is not in your list.");
						itemName.setText("");
						itemName.requestFocus();
					}
					else
					{
						shoppingCart.remove(itemName.getText().trim().replaceAll("( )+", " ").toLowerCase());
						
						buildGroceryList();
					
						resetText();
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
	private void resetText()
	{
		itemName.setText("");
		alert.setText("");
		itemName.requestFocus();
	}
	
	//Method used to build the StringBuilder that will be printed onto the screen after an item is added or deleted from the list.
	private void buildGroceryList()
	{
		StringBuilder printList = new StringBuilder();
		
		for(int i = 0; i < shoppingCart.size(); i++)
		{
			printList.append("<HTML>" + (i + 1) + ". "+ shoppingCart.get(i) + "<BR>");
		}
		
		itemList.setText(printList.toString());
	}
	
	private void addImage() throws IOException
	{
		/*
		 * Grabs the image from a specified file and stores it into a BufferedImage variable. 
		 * This variable will then be used to add the image to a label to print the image out the screen.  
		 */
		String path = "groceries.png";
	    File file = new File(path);
	    BufferedImage image = ImageIO.read(file);
		
		//Label that is used to set an image in the frame.  
	    JLabel groceryImage = new JLabel(new ImageIcon(image));
		groceryImage.setBackground(Color.WHITE);
		groceryImage.setBounds(10, 276, 239, 165);
		frame.getContentPane().add(groceryImage);
	}
}
