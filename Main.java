import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Main {
	private JButton ConnectButton, ClearQuery, ExecuteButton, ClearWindow;
	private JLabel QueryLabel, dbInfoLabel, jdbcLabel, UrlLabel, UserLabel, PasswordLabel;
	private JTextArea TextQuery;
	private JComboBox DriverCombo;
	private JComboBox UrlCombo;
	private JTextField UserText;
	private JPasswordField PasswordText;
	private JLabel StatusLabel, WindowLabel;
	private ResultTable tableModel;
	private Connection connect;
	private TableModel Empty;
	private JTable resultTable;
	private JFrame frame;
	private JPanel panel;

	public Main() {
		// Window header and panel
		frame = new JFrame("Project 3 - SQL Client App - (MJL - CNT 4714 - Fall 2021)");
		panel = new JPanel();

		// drop down menue options
		String[] DriverItems = { "com.mysql.cj.jdbc.Driver", "" };
		String[] UrlItems = { "jdbc:mysql://localhost:3306/project3", "" };

		// database header label
		dbInfoLabel = new JLabel();
		dbInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
		dbInfoLabel.setText("Enter Database Information");
		dbInfoLabel.setBounds(10, 0, 300, 25);
		dbInfoLabel.setForeground(Color.blue);

		// driver header
		jdbcLabel = new JLabel(" JDBC Driver");
		jdbcLabel.setBackground(Color.lightGray);
		jdbcLabel.setForeground(Color.black);
		jdbcLabel.setBounds(10, 21, 125, 25);
		jdbcLabel.setOpaque(true);

		// database url header
		UrlLabel = new JLabel(" Database URL");
		UrlLabel.setBackground(Color.lightGray);
		UrlLabel.setForeground(Color.black);
		UrlLabel.setBounds(10, 49, 125, 25);
		UrlLabel.setOpaque(true);

		// username header
		UserLabel = new JLabel(" Username");
		UserLabel.setBackground(Color.lightGray);
		UserLabel.setForeground(Color.black);
		UserLabel.setBounds(10, 78, 125, 24);
		UserLabel.setOpaque(true);

		// password header
		PasswordLabel = new JLabel(" Password");
		PasswordLabel.setBackground(Color.lightGray);
		PasswordLabel.setForeground(Color.black);
		PasswordLabel.setBounds(10, 107, 125, 24);
		PasswordLabel.setOpaque(true);

		// driver dropdown box
		DriverCombo = new JComboBox(DriverItems);
		DriverCombo.setBounds(135, 21, 290, 25);

		// url dropdown box
		UrlCombo = new JComboBox(UrlItems);
		UrlCombo.setBounds(135, 49, 290, 25);

		// username text box
		UserText = new JTextField("", 10);
		UserText.setBounds(135, 78, 290, 25);

		// password text box
		PasswordText = new JPasswordField("", 10);
		PasswordText.setBounds(135, 106, 290, 25);

		// command label
		QueryLabel = new JLabel("Enter an SQL Command");
		QueryLabel.setFont(new Font("Arial", Font.BOLD, 14));
		QueryLabel.setBounds(450, 0, 215, 25);
		QueryLabel.setForeground(Color.blue);

		// SQL command window
		TextQuery = new JTextArea(5, 5);

		// clear command button
		ClearQuery = new JButton("Clear SQL Command");
		ClearQuery.setFont(new Font("Arial", Font.BOLD, 12));
		ClearQuery.setBounds(480, 150, 175, 25);
		ClearQuery.setForeground(Color.red);
		ClearQuery.setBackground(Color.white);
		ClearQuery.setBorderPainted(false);
		ClearQuery.setOpaque(true);

		// execute command button
		ExecuteButton = new JButton("Execute SQL Command");
		ExecuteButton.setFont(new Font("Arial", Font.BOLD, 12));
		ExecuteButton.setBounds(695, 150, 175, 25);
		ExecuteButton.setBackground(Color.green);
		ExecuteButton.setBorderPainted(false);
		ExecuteButton.setOpaque(true);

		// connect to database button
		ConnectButton = new JButton("Connect to Database");
		ConnectButton.setFont(new Font("Arial", Font.BOLD, 12));
		ConnectButton.setBounds(20, 187, 165, 25);
		ConnectButton.setBackground(Color.blue);
		ConnectButton.setForeground(Color.yellow);
		ConnectButton.setBorderPainted(false);
		ConnectButton.setOpaque(true);

		// connection status
		StatusLabel = new JLabel("    No Connection Now");
		StatusLabel.setFont(new Font("Arial", Font.BOLD, 14));
		StatusLabel.setBounds(200, 187, 690, 25);
		StatusLabel.setBackground(Color.black);
		StatusLabel.setForeground(Color.red);
		StatusLabel.setOpaque(true);

		// result window header
		WindowLabel = new JLabel("SQL Execution Result Window");
		WindowLabel.setFont(new Font("Arial", Font.BOLD, 14));
		WindowLabel.setBounds(45, 231, 220, 25);
		WindowLabel.setForeground(Color.blue);

		// table
		resultTable = new JTable();
		Empty = new DefaultTableModel();

		// window settings
		panel.setPreferredSize(new Dimension(1000, 520));
		panel.setLayout(null);
		final Box square = Box.createHorizontalBox();
		square.add(new JScrollPane(resultTable));
		square.setBounds(45, 254, 841, 220);
		Box sqlSquare = Box.createHorizontalBox();
		sqlSquare.add(new JScrollPane(TextQuery));
		sqlSquare.setBounds(450, 22, 438, 125);
		resultTable.setEnabled(false);
		resultTable.setGridColor(Color.black);

		// clear window button
		ClearWindow = new JButton("Clear Result Window");
		ClearWindow.setFont(new Font("Arial", Font.BOLD, 12));
		ClearWindow.setBounds(25, 480, 168, 25);
		ClearWindow.setBackground(Color.yellow);
		ClearWindow.setBorderPainted(false);
		ClearWindow.setOpaque(true);

		// adds all the creations to the window
		panel.add(ConnectButton);
		panel.add(ClearQuery);
		panel.add(ExecuteButton);
		panel.add(ClearWindow);
		panel.add(QueryLabel);
		panel.add(sqlSquare);
		panel.add(dbInfoLabel);
		panel.add(jdbcLabel);
		panel.add(UrlLabel);
		panel.add(UserLabel);
		panel.add(PasswordLabel);
		panel.add(DriverCombo);
		panel.add(UrlCombo);
		panel.add(UserText);
		panel.add(PasswordText);
		panel.add(StatusLabel);
		panel.add(WindowLabel);
		panel.add(square);

		// resets the result window
		ClearWindow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				resultTable.setModel(Empty);
			}
		});

		// resets the command window
		ClearQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				TextQuery.setText("");
			}
		});

		// connect to database button
		ConnectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					Class.forName(DriverCombo.getSelectedItem().toString());

					// gets password from array of characters
					String password = String.valueOf(PasswordText.getPassword());

					// uses the username, password, and url to connect to the database
					connect = DriverManager.getConnection(UrlCombo.getSelectedItem().toString(), UserText.getText(),
							password);

					// successful connection label
					StatusLabel.setText("   Connected to " + UrlCombo.getSelectedItem().toString());
				}

				catch (Exception e) {
					StatusLabel.setText(e.getMessage());
				}
			}
		});

		// execute command button
		ExecuteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				try {
					resultTable.setEnabled(true);
					resultTable.setAutoscrolls(true);
					tableModel = new ResultTable(connect, TextQuery.getText());

					if (TextQuery.getText().toUpperCase().contains("SELECT")) {
						tableModel.setQuery(TextQuery.getText());
						resultTable.setModel(tableModel);
					} else {
						tableModel.setUpdate(TextQuery.getText());
					}
				} catch (SQLException e) { // database error message
					JOptionPane.showMessageDialog(null, e.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE);
				} catch (ClassNotFoundException NotFound) { // error message
					JOptionPane.showMessageDialog(null, "MySQL driver not found", "Driver not found",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new Main();
	}
}