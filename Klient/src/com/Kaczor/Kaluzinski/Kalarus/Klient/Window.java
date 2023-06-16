package com.Kaczor.Kaluzinski.Kalarus.Klient;

import java.awt.EventQueue;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JFrame;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.Kaczor.Kaluzinski.Kalarus.SerwerConnector.SerwerConnector;
import com.Kaczor.Kaluzinski.Kalarus.Communication.Aggrement;
import com.Kaczor.Kaluzinski.Kalarus.Communication.Client;
import com.Kaczor.Kaluzinski.Kalarus.Communication.Trip;
import com.Kaczor.Kaluzinski.Kalarus.Communication.WhatToDo;

import javax.swing.JTabbedPane;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import com.toedter.calendar.JDateChooser;

import java.util.Date;
import java.util.Iterator;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.Font;

import javax.swing.ListSelectionModel;

/**
 * Klasa zawieraj¹ca wygenerowany przez WindowBuildera kod tworz¹cy czêœæ graficzn¹ aplikacji klienta projektu 
 * oraz implementacjê elementów funkcjonalnych takich jak przyciski, suwaki, itd.
 */
public class Window {
	
	private static final Logger logger = Logger.getLogger(Window.class);
	private static JFrame frame;
	private JTextField login;
	private JPasswordField haslo;
	private JTextField peselusun;
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private JTextField clientname;
	private JTextField clientsurname;
	private JTextField clientpesel;
	private JTextField clientphonenumber;
	private JTextField clientcountry;
	private JTextField clientcity;
	private JTextField clientstreet;
	private JTextField clienthomenumber;
	private JTextField clientpostcode;
	private static SerwerConnector connector = null;
	private JDateChooser ofertfrom,ofertto;
	private JTextField guestcount;
	private JTextField totalcost;
	private JTable table;
	private JTable table_1;
	private JTextField ofertname;
	private static Trip[] tripbuff = null;
	private static Aggrement[]aggrements = null;
	private static Client clientbuff = null;
	private static Trip wycieczka = null;
	
	/**
	 * @wbp.nonvisual location=62,189
	 */
	private final ButtonGroup ofertstandard = new ButtonGroup();
	private JTextField ofertprice;
	private JTextField ofertphone;
	private JTextField ofertmax;
	private JTextField clientpeseltrip;
	
	/**
	 * Metoda main, s³u¿¹ca do uruchomienia aplikacji stworzenia pod³o¿a dla interfejsu graficznego.
	 * Tworzy obiekt Frame, do którego dodawane s¹ wszystkie inne graficzne elementy.
	 * @param args argumenty startowe aplikacji (w naszym przypadku brak)
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					@SuppressWarnings("unused")
					Window window = new Window();
					Window.frame.setVisible(true);
					connector = new SerwerConnector();
				} catch (Exception e) {
					logger.error("Error!", e);
				}
			}
		});
	}

	/**
	 * Konstruktor wywo³uj¹cy metodê inicjalizuj¹c¹ czêœæ graficzn¹ projektu.
	 */
	public Window() {
		initialize();
	}

	/**
	 * Metoda, w której generowany jest ca³y kod odpowiedzialny za tworzenie czêœci graficznej projektu.
	 * Zawiera te¿ implementacjê elementów funkcjonalnych.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1091, 717);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		CardLayout lay = new CardLayout();
		panel.setLayout(lay);
		
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			logger.error("Error!", e);
		}
		
		JPanel panellogowania = new JPanel();

		panellogowania.setBackground(new Color(28,28,26));
				
		panel.add(panellogowania, "panellogowania");
		
		JLabel lblNewLabel = new JLabel("Panel Logowania", SwingConstants.CENTER);
		lblNewLabel.setBackground(new Color(255, 99, 71));
		lblNewLabel.setFont(lblNewLabel.getFont().deriveFont(25.0f));
		lblNewLabel.setForeground(new Color(255, 99, 71));
		
		login = new JTextField();
		login.setText("");
		login.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Has\u0142o");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_1.setForeground(new Color(255, 99, 71));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblNewLabel_2 = new JLabel("Login");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_2.setForeground(new Color(255, 99, 71));
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton loginbutton = new JButton("Zaloguj");
		loginbutton.setFont(new Font("Tahoma", Font.BOLD, 14));
		loginbutton.setBackground(new Color(255, 99, 71));
		loginbutton.setForeground(Color.BLACK);
	
		haslo = new JPasswordField();
		GroupLayout gl_panellogowania = new GroupLayout(panellogowania);
		gl_panellogowania.setHorizontalGroup(
			gl_panellogowania.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panellogowania.createSequentialGroup()
					.addGroup(gl_panellogowania.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panellogowania.createSequentialGroup()
							.addGap(226)
							.addGroup(gl_panellogowania.createParallelGroup(Alignment.TRAILING)
								.addGroup(Alignment.LEADING, gl_panellogowania.createSequentialGroup()
									.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
									.addGap(8))
								.addComponent(login, Alignment.LEADING)
								.addComponent(haslo, Alignment.LEADING)
								.addComponent(loginbutton, GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)))
						.addGroup(gl_panellogowania.createSequentialGroup()
							.addGap(313)
							.addComponent(lblNewLabel_2, GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
							.addGap(89))
						.addGroup(gl_panellogowania.createSequentialGroup()
							.addGap(315)
							.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
							.addGap(87)))
					.addGap(236))
		);
		gl_panellogowania.setVerticalGroup(
			gl_panellogowania.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panellogowania.createSequentialGroup()
					.addGap(27)
					.addComponent(lblNewLabel)
					.addGap(18)
					.addComponent(lblNewLabel_2)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(login, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(haslo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(loginbutton, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(137, Short.MAX_VALUE))
		);
		panellogowania.setLayout(gl_panellogowania);
		
		JPanel paneloperatora = new JPanel();
		paneloperatora.setBackground(new Color(28, 28, 26));
		panel.add(paneloperatora, "paneloperatora");
		
		JLabel zal = new JLabel("Zalogowano jako: ");
		zal.setFont(new Font("Tahoma", Font.BOLD, 14));
		zal.setForeground(new Color(255, 99, 71));
		
		JButton paneladdofert = new JButton("Dodaj Oferte");
		paneladdofert.setForeground(Color.BLACK);
		paneladdofert.setFont(new Font("Tahoma", Font.BOLD, 14));
		paneladdofert.setBackground(new Color(255, 99, 71));
		
		
		JButton panelclient = new JButton("Klient");
		panelclient.setForeground(Color.BLACK);
		panelclient.setFont(new Font("Tahoma", Font.BOLD, 14));
		panelclient.setBackground(new Color(255, 99, 71));
		
		JButton panellogout = new JButton("Wyloguj");
		panellogout.setForeground(Color.BLACK);
		
		panellogout.setFont(new Font("Tahoma", Font.BOLD, 14));
		panellogout.setBackground(new Color(255, 99, 71));
		GroupLayout gl_paneloperatora = new GroupLayout(paneloperatora);
		gl_paneloperatora.setHorizontalGroup(
			gl_paneloperatora.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_paneloperatora.createSequentialGroup()
					.addGap(258)
					.addGroup(gl_paneloperatora.createParallelGroup(Alignment.TRAILING)
						.addComponent(panellogout, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
						.addComponent(panelclient, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
						.addComponent(paneladdofert, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE)
						.addComponent(zal, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 434, Short.MAX_VALUE))
					.addGap(267))
		);
		gl_paneloperatora.setVerticalGroup(
			gl_paneloperatora.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_paneloperatora.createSequentialGroup()
					.addGap(39)
					.addComponent(zal)
					.addGap(41)
					.addComponent(paneladdofert, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(panelclient, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(panellogout, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addGap(429))
		);
		paneloperatora.setLayout(gl_paneloperatora);
		
		JPanel Klient = new JPanel();
		panel.add(Klient, "Klient");
		Klient.setBackground(new Color(28,28,26));
		
		JButton dodajklienta = new JButton("Dodaj Modyfikuj Klienta");
		dodajklienta.setForeground(Color.BLACK);
		dodajklienta.setFont(new Font("Tahoma", Font.BOLD, 14));
		dodajklienta.setBackground(new Color(255, 99, 71));
		
		
		JButton clientmenudelete = new JButton("Usu\u0144 Klienta");
		clientmenudelete.setForeground(Color.BLACK);
		clientmenudelete.setBackground(new Color(255, 99, 71));
		clientmenudelete.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		
		JButton clientmenuback = new JButton("Powr\u00F3t");
		clientmenuback.setForeground(Color.BLACK);
		clientmenuback.setFont(new Font("Tahoma", Font.BOLD, 14));
		clientmenuback.setBackground(new Color(255, 99, 71));

		JLabel lblNewLabel_4 = new JLabel("Menu Wyboru");
		lblNewLabel_4.setForeground(new Color(255, 99, 71));
		lblNewLabel_4.setBackground(new Color(255, 99, 71));
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton clientmenuofert = new JButton("Wycieczki");
		clientmenuofert.setForeground(Color.BLACK);
		clientmenuofert.setBackground(new Color(255, 99, 71));
		clientmenuofert.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		GroupLayout gl_Klient = new GroupLayout(Klient);
		gl_Klient.setHorizontalGroup(
			gl_Klient.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_Klient.createSequentialGroup()
					.addGap(249)
					.addGroup(gl_Klient.createParallelGroup(Alignment.TRAILING)
						.addComponent(clientmenuback, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
						.addComponent(clientmenuofert, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
						.addComponent(clientmenudelete, GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
						.addComponent(lblNewLabel_4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
						.addComponent(dodajklienta, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE))
					.addGap(271))
		);
		gl_Klient.setVerticalGroup(
			gl_Klient.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Klient.createSequentialGroup()
					.addGap(11)
					.addComponent(lblNewLabel_4, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(dodajklienta, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(clientmenudelete, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(clientmenuofert, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(clientmenuback, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(371, Short.MAX_VALUE))
		);
		Klient.setLayout(gl_Klient);
		DefaultListModel<String> nod = new DefaultListModel<String>();
		nod.addElement("Jestem sobie czlowiekem!");
		
		JPanel Oferty = new JPanel();
		Oferty.setBackground(new Color(28,28,26));
		panel.add(Oferty, "Oferty");
		
		JLabel lblNewLabel_24 = new JLabel("Nazwa");
		lblNewLabel_24.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_24.setForeground(new Color(255, 99, 71));
		lblNewLabel_24.setHorizontalAlignment(SwingConstants.CENTER);
		
		ofertname = new JTextField();
		ofertname.setColumns(10);
		
		JLabel lblNewLabel_25 = new JLabel("Standard");
		lblNewLabel_25.setForeground(new Color(255, 99, 71));
		lblNewLabel_25.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_25.setHorizontalAlignment(SwingConstants.CENTER);
		
		JRadioButton std1 = new JRadioButton("1");
		std1.setFont(new Font("SansSerif", Font.BOLD, 14));
		std1.setForeground(new Color(255, 99, 71));
		ofertstandard.add(std1);
		
		JRadioButton std2 = new JRadioButton("2");
		std2.setFont(new Font("SansSerif", Font.BOLD, 14));
		std2.setForeground(new Color(255, 99, 71));
		ofertstandard.add(std2);
		
		JRadioButton std3 = new JRadioButton("3");
		std3.setFont(new Font("SansSerif", Font.BOLD, 14));
		std3.setForeground(new Color(255, 99, 71));
		ofertstandard.add(std3);
		
		JRadioButton std4 = new JRadioButton("4");
		std4.setFont(new Font("SansSerif", Font.BOLD, 14));
		std4.setForeground(new Color(255, 99, 71));
		ofertstandard.add(std4);
	
		JRadioButton std5 = new JRadioButton("5");
		std5.setFont(new Font("SansSerif", Font.BOLD, 14));
		std5.setForeground(new Color(255, 99, 71));
		ofertstandard.add(std5);
		
		JLabel lblNewLabel_26 = new JLabel("Od:");
		lblNewLabel_26.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_26.setForeground(new Color(255, 99, 71));
		lblNewLabel_26.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblNewLabel_27 = new JLabel("Do:");
		lblNewLabel_27.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_27.setForeground(new Color(255, 99, 71));
		lblNewLabel_27.setHorizontalAlignment(SwingConstants.CENTER);
		
		ofertfrom = new JDateChooser();
		
		ofertto = new JDateChooser();
		
		JLabel lblNewLabel_28 = new JLabel("Cena");
		lblNewLabel_28.setForeground(new Color(255, 99, 71));
		lblNewLabel_28.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_28.setHorizontalAlignment(SwingConstants.CENTER);
		
		ofertprice = new JTextField();
		ofertprice.setColumns(10);
		
		JLabel lblNewLabel_29 = new JLabel("Telefon");
		lblNewLabel_29.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_29.setForeground(new Color(255, 99, 71));
		lblNewLabel_29.setHorizontalAlignment(SwingConstants.CENTER);
		
		ofertphone = new JTextField();
		ofertphone.setColumns(10);
		
		JButton ofertadd = new JButton("Dodaj");
		ofertadd.setForeground(Color.BLACK);
		ofertadd.setFont(new Font("Tahoma", Font.BOLD, 14));
		ofertadd.setBackground(new Color(255, 99, 71));
		
		JButton ofertback = new JButton("Powr\u00F3t");
		ofertback.setForeground(Color.BLACK);
		ofertback.setFont(new Font("Tahoma", Font.BOLD, 14));
		ofertback.setBackground(new Color(255, 99, 71));
		
		JLabel lblNewLabel_30 = new JLabel("Maksymalna liczba uczestnik\u00F3w");
		lblNewLabel_30.setForeground(new Color(255, 99, 71));
		lblNewLabel_30.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_30.setHorizontalAlignment(SwingConstants.CENTER);
		
		ofertmax = new JTextField();
		ofertmax.setText("0");
		ofertmax.setColumns(10);
		GroupLayout gl_Oferty = new GroupLayout(Oferty);
		gl_Oferty.setHorizontalGroup(
			gl_Oferty.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_Oferty.createSequentialGroup()
					.addGap(453)
					.addComponent(lblNewLabel_29, GroupLayout.PREFERRED_SIZE, 76, Short.MAX_VALUE)
					.addGap(460))
				.addGroup(gl_Oferty.createSequentialGroup()
					.addGroup(gl_Oferty.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_Oferty.createSequentialGroup()
							.addGap(444)
							.addComponent(lblNewLabel_26, GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
							.addGap(75))
						.addGroup(Alignment.LEADING, gl_Oferty.createSequentialGroup()
							.addGap(458)
							.addComponent(lblNewLabel_27, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
							.addGap(82))
						.addGroup(Alignment.LEADING, gl_Oferty.createSequentialGroup()
							.addGap(442)
							.addComponent(lblNewLabel_24, GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
							.addGap(98))
						.addGroup(Alignment.LEADING, gl_Oferty.createSequentialGroup()
							.addGap(172)
							.addGroup(gl_Oferty.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_Oferty.createSequentialGroup()
									.addGap(284)
									.addComponent(lblNewLabel_28, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
									.addGap(84))
								.addGroup(gl_Oferty.createSequentialGroup()
									.addGap(189)
									.addGroup(gl_Oferty.createParallelGroup(Alignment.LEADING)
										.addComponent(ofertto, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
										.addGroup(gl_Oferty.createSequentialGroup()
											.addComponent(std1, GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(std2, GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(std3, GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(std4, GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(std5, GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
										.addComponent(ofertfrom, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
										.addComponent(lblNewLabel_25, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
										.addComponent(ofertname, Alignment.TRAILING, 255, 255, Short.MAX_VALUE)
										.addComponent(ofertprice, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
										.addComponent(ofertphone, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
										.addComponent(ofertmax, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
										.addComponent(ofertadd, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
										.addComponent(lblNewLabel_30, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
										.addComponent(ofertback, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))))))
					.addGap(373))
		);
		gl_Oferty.setVerticalGroup(
			gl_Oferty.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_Oferty.createSequentialGroup()
					.addGap(16)
					.addComponent(lblNewLabel_24)
					.addGap(13)
					.addComponent(ofertname, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel_25, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
					.addGap(12)
					.addGroup(gl_Oferty.createParallelGroup(Alignment.BASELINE)
						.addComponent(std1)
						.addComponent(std2)
						.addComponent(std3)
						.addComponent(std4)
						.addComponent(std5))
					.addGap(11)
					.addComponent(lblNewLabel_26)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(ofertfrom, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel_27)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(ofertto, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNewLabel_28)
					.addGap(4)
					.addComponent(ofertprice, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_29)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(ofertphone, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_30, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(ofertmax, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(ofertadd, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(ofertback, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(90, Short.MAX_VALUE))
		);
		Oferty.setLayout(gl_Oferty);
		
		
		JPanel UsunKlienta = new JPanel();
		UsunKlienta.setBackground(new Color(28,28,26));
		panel.add(UsunKlienta, "UsunKlienta");
		
		JLabel lblNewLabel_6 = new JLabel("Usuwanie Klienta");
		lblNewLabel_6.setForeground(new Color(255, 99, 71));
		lblNewLabel_6.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton clientdelete = new JButton("Usu\u0144 Klienta");
		clientdelete.setForeground(Color.BLACK);
		clientdelete.setBorderPainted(false);
		clientdelete.setBackground(new Color(255, 99, 71));
		clientdelete.setFont(new Font("Tahoma", Font.BOLD, 14));

		
		
		peselusun = new JTextField();
		peselusun.setColumns(10);
		
		JLabel lblNewLabel_7 = new JLabel("PESEL");
		lblNewLabel_7.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_7.setForeground(new Color(255, 99, 71));
		lblNewLabel_7.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton clientdelback = new JButton("Powr\u00F3t");
		clientdelback.setForeground(Color.BLACK);
		clientdelback.setFont(new Font("Tahoma", Font.BOLD, 14));
		clientdelback.setBackground(new Color(255, 99, 71));
		clientdelback.setBorderPainted(false);
		
		GroupLayout gl_UsunKlienta = new GroupLayout(UsunKlienta);
		gl_UsunKlienta.setHorizontalGroup(
			gl_UsunKlienta.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_UsunKlienta.createSequentialGroup()
					.addGap(301)
					.addComponent(lblNewLabel_7, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
					.addGap(324))
				.addGroup(gl_UsunKlienta.createSequentialGroup()
					.addGroup(gl_UsunKlienta.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_UsunKlienta.createSequentialGroup()
							.addGap(288)
							.addComponent(lblNewLabel_6, GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
							.addGap(49))
						.addGroup(gl_UsunKlienta.createSequentialGroup()
							.addGap(258)
							.addGroup(gl_UsunKlienta.createParallelGroup(Alignment.LEADING)
								.addComponent(clientdelete, GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
								.addComponent(clientdelback, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE)
								.addComponent(peselusun, GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE))))
					.addGap(266))
		);
		gl_UsunKlienta.setVerticalGroup(
			gl_UsunKlienta.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_UsunKlienta.createSequentialGroup()
					.addGap(55)
					.addComponent(lblNewLabel_6, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
					.addGap(19)
					.addComponent(lblNewLabel_7, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(peselusun, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(clientdelete, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(clientdelback, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(386, Short.MAX_VALUE))
		);
		UsunKlienta.setLayout(gl_UsunKlienta);
		
		JPanel KlientDodajModyfikuj = new JPanel();
		KlientDodajModyfikuj.setBackground(new Color(28,28,26));
		panel.add(KlientDodajModyfikuj, "KlientDodajModyfikuj");
		tabbedPane.setBackground(Color.LIGHT_GRAY);
		tabbedPane.setBorder(null);
		tabbedPane.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setOpaque(true);
		layeredPane.setBorder(null);
		layeredPane.setBackground(Color.DARK_GRAY);
		layeredPane.setFont(new Font("Dialog", Font.BOLD, 14));
		tabbedPane.addTab("Dane Klienta", null, layeredPane, null);
		
		JLabel lblNewLabel_3 = new JLabel("Imie");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setForeground(new Color(255, 99, 71));
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		clientname = new JTextField();
		clientname.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("Nazwisko");
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5.setForeground(new Color(255, 99, 71));
		lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		clientsurname = new JTextField();
		clientsurname.setColumns(10);
		
		clientpesel = new JTextField();
		clientpesel.setColumns(10);
		
		JLabel lblNewLabel_8 = new JLabel("PESEL");
		lblNewLabel_8.setForeground(new Color(255, 99, 71));
		lblNewLabel_8.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_8.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblNewLabel_9 = new JLabel("Numer Telefonu");
		lblNewLabel_9.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_9.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_9.setForeground(new Color(255, 99, 71));
		
		clientphonenumber = new JTextField();
		clientphonenumber.setColumns(10);
		GroupLayout gl_layeredPane = new GroupLayout(layeredPane);
		gl_layeredPane.setHorizontalGroup(
			gl_layeredPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane.createSequentialGroup()
					.addGap(314)
					.addGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_9, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
						.addComponent(lblNewLabel_8, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
						.addComponent(lblNewLabel_5, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
						.addComponent(lblNewLabel_3, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
						.addComponent(clientsurname, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
						.addComponent(clientname, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
						.addComponent(clientpesel, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
						.addComponent(clientphonenumber, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE))
					.addGap(423))
		);
		gl_layeredPane.setVerticalGroup(
			gl_layeredPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_3)
					.addGap(5)
					.addComponent(clientname, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
					.addGap(3)
					.addComponent(lblNewLabel_5)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(clientsurname, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
					.addGap(4)
					.addComponent(lblNewLabel_8)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(clientpesel, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_9)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(clientphonenumber, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
					.addGap(105))
		);
		layeredPane.setLayout(gl_layeredPane);
		
		JLayeredPane layeredPane_2 = new JLayeredPane();
		layeredPane_2.setBackground(Color.DARK_GRAY);
		layeredPane_2.setOpaque(true);
		tabbedPane.addTab("Adres Klienta", null, layeredPane_2, null);
		
		JLabel lblNewLabel_10 = new JLabel("Kraj");
		lblNewLabel_10.setForeground(new Color(255, 99, 71));
		lblNewLabel_10.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_10.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		clientcountry = new JTextField();
		clientcountry.setColumns(10);
		
		JLabel lblNewLabel_11 = new JLabel("Miejscowo\u015B\u0107");
		lblNewLabel_11.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_11.setForeground(new Color(255, 99, 71));
		lblNewLabel_11.setHorizontalAlignment(SwingConstants.CENTER);
		
		clientcity = new JTextField();
		clientcity.setColumns(10);
		
		JLabel lblNewLabel_12 = new JLabel("Ulica");
		lblNewLabel_12.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_12.setForeground(new Color(255, 99, 71));
		lblNewLabel_12.setHorizontalAlignment(SwingConstants.CENTER);
		
		clientstreet = new JTextField();
		clientstreet.setColumns(10);
		
		JLabel lblNewLabel_13 = new JLabel("Numer Domu");
		lblNewLabel_13.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_13.setForeground(new Color(255, 99, 71));
		lblNewLabel_13.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		clienthomenumber = new JTextField();
		clienthomenumber.setColumns(10);
		
		JLabel lblNewLabel_14 = new JLabel("Kod Pocztowy");
		lblNewLabel_14.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_14.setForeground(new Color(255, 99, 71));
		lblNewLabel_14.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		clientpostcode = new JTextField();
		clientpostcode.setColumns(10);
		GroupLayout gl_layeredPane_2 = new GroupLayout(layeredPane_2);
		gl_layeredPane_2.setHorizontalGroup(
			gl_layeredPane_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane_2.createSequentialGroup()
					.addGap(317)
					.addComponent(lblNewLabel_11, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
					.addGap(439))
				.addGroup(gl_layeredPane_2.createSequentialGroup()
					.addGap(317)
					.addComponent(clientcity, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
					.addGap(439))
				.addGroup(gl_layeredPane_2.createSequentialGroup()
					.addGap(317)
					.addComponent(lblNewLabel_13, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
					.addGap(439))
				.addGroup(gl_layeredPane_2.createSequentialGroup()
					.addGap(317)
					.addComponent(clienthomenumber, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
					.addGap(439))
				.addGroup(gl_layeredPane_2.createSequentialGroup()
					.addGap(317)
					.addComponent(lblNewLabel_12, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
					.addGap(439))
				.addGroup(gl_layeredPane_2.createSequentialGroup()
					.addGap(317)
					.addComponent(clientstreet, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
					.addGap(439))
				.addGroup(gl_layeredPane_2.createSequentialGroup()
					.addGap(317)
					.addComponent(lblNewLabel_14, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
					.addGap(439))
				.addGroup(gl_layeredPane_2.createSequentialGroup()
					.addGap(317)
					.addComponent(clientpostcode, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
					.addGap(439))
				.addGroup(Alignment.TRAILING, gl_layeredPane_2.createSequentialGroup()
					.addGap(317)
					.addGroup(gl_layeredPane_2.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblNewLabel_10, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
						.addComponent(clientcountry, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE))
					.addGap(439))
		);
		gl_layeredPane_2.setVerticalGroup(
			gl_layeredPane_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel_10, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(clientcountry, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(lblNewLabel_11)
					.addGap(11)
					.addComponent(clientcity, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(lblNewLabel_13)
					.addGap(11)
					.addComponent(clienthomenumber, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(lblNewLabel_12)
					.addGap(11)
					.addComponent(clientstreet, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(lblNewLabel_14)
					.addGap(11)
					.addComponent(clientpostcode, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
		);
		layeredPane_2.setLayout(gl_layeredPane_2);
		
		JButton clientmodadd = new JButton("Dodaj");
		clientmodadd.setForeground(Color.BLACK);
		clientmodadd.setFont(new Font("Tahoma", Font.BOLD, 14));
		clientmodadd.setBackground(new Color(255, 99, 71));
		
		JButton clientmodinfo = new JButton("Pobierz informacje");
		clientmodinfo.setBackground(new Color(255, 99, 71));
		clientmodinfo.setFont(new Font("Tahoma", Font.BOLD, 14));
		clientmodinfo.setForeground(Color.BLACK);
		
		JButton clientmodmod = new JButton("Modyfikuj");
		clientmodmod.setForeground(Color.BLACK);
		clientmodmod.setFont(new Font("Tahoma", Font.BOLD, 14));
		clientmodmod.setBackground(new Color(255, 99, 71));
		
		JButton clientmodback = new JButton("Powr\u00F3t");
		clientmodback.setForeground(Color.BLACK);
		clientmodback.setBackground(new Color(255, 99, 71));
		clientmodback.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JButton btnNewButton = new JButton("Wyczy\u015B\u0107 Pola");
		
		btnNewButton.setBackground(new Color(255, 99, 71));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
		GroupLayout gl_KlientDodajModyfikuj = new GroupLayout(KlientDodajModyfikuj);
		gl_KlientDodajModyfikuj.setHorizontalGroup(
			gl_KlientDodajModyfikuj.createParallelGroup(Alignment.LEADING)
				.addComponent(clientmodadd, GroupLayout.DEFAULT_SIZE, 1075, Short.MAX_VALUE)
				.addComponent(clientmodinfo, GroupLayout.DEFAULT_SIZE, 1075, Short.MAX_VALUE)
				.addComponent(clientmodmod, GroupLayout.DEFAULT_SIZE, 1075, Short.MAX_VALUE)
				.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 1075, Short.MAX_VALUE)
				.addComponent(btnNewButton, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 1075, Short.MAX_VALUE)
				.addComponent(clientmodback, GroupLayout.DEFAULT_SIZE, 1075, Short.MAX_VALUE)
		);
		gl_KlientDodajModyfikuj.setVerticalGroup(
			gl_KlientDodajModyfikuj.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_KlientDodajModyfikuj.createSequentialGroup()
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 431, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(clientmodadd, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(clientmodinfo, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(clientmodmod, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(clientmodback, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(12, Short.MAX_VALUE))
		);
		KlientDodajModyfikuj.setLayout(gl_KlientDodajModyfikuj);
		
		JPanel wycieczki = new JPanel();
		wycieczki.setBackground(new Color(28,28,26));
		panel.add(wycieczki, "wycieczki");
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JLayeredPane layeredPane_4 = new JLayeredPane();
		layeredPane_4.setBackground(Color.DARK_GRAY);
		layeredPane_4.setOpaque(true);
		tabbedPane_1.addTab("Wy\u015Bwietl wycieczki", null, layeredPane_4, null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBackground(Color.LIGHT_GRAY);
		scrollPane_1.setBorder(null);
		
		table_1 = new JTable() {
			@Override
			 public boolean editCellAt(int row, int column, java.util.EventObject e) {
		            return false;
			 }
		};
		table_1.setShowVerticalLines(true);
		table_1.setShowHorizontalLines(true);
		table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_1.setFillsViewportHeight(true);
		table_1.setGridColor(Color.BLACK);
		table_1.setBorder(null);
		table_1.setBackground(Color.LIGHT_GRAY);
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Nazwa", "Cena","Data Umowy", "Stan p\u0142atno\u015Bci"
			}
		) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, String.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		
		scrollPane_1.setViewportView(table_1);
		
		JDateChooser datefrom = new JDateChooser();
		
		JDateChooser dateto = new JDateChooser();
		
		JLabel lblNewLabel_22 = new JLabel("Od:");
		lblNewLabel_22.setForeground(new Color(255, 99, 71));
		lblNewLabel_22.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_22.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblNewLabel_23 = new JLabel("Do:");
		lblNewLabel_23.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_23.setForeground(new Color(255, 99, 71));
		lblNewLabel_23.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton searchTrip = new JButton("Szukaj");
		searchTrip.setBackground(new Color(255, 99, 71));
		searchTrip.setFont(new Font("Tahoma", Font.BOLD, 14));
		searchTrip.setForeground(Color.BLACK);
		
		JButton statuschange = new JButton("Zmie\u0144 Stan P\u0142atno\u015Bci");
		statuschange.setForeground(Color.BLACK);
	
		statuschange.setFont(new Font("Tahoma", Font.BOLD, 14));
		statuschange.setBackground(new Color(255, 99, 71));
		GroupLayout gl_layeredPane_4 = new GroupLayout(layeredPane_4);
		gl_layeredPane_4.setHorizontalGroup(
			gl_layeredPane_4.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_layeredPane_4.createSequentialGroup()
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 676, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_layeredPane_4.createParallelGroup(Alignment.TRAILING)
						.addComponent(searchTrip, GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
						.addComponent(dateto, GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
						.addComponent(lblNewLabel_22, GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
						.addComponent(datefrom, GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
						.addComponent(lblNewLabel_23, GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
						.addComponent(statuschange, GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_layeredPane_4.setVerticalGroup(
			gl_layeredPane_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane_4.createSequentialGroup()
					.addGroup(gl_layeredPane_4.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_layeredPane_4.createSequentialGroup()
							.addGap(15)
							.addComponent(lblNewLabel_22)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(datefrom, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblNewLabel_23)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(dateto, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
							.addGap(29)
							.addComponent(searchTrip)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(statuschange))
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE))
					.addGap(0))
		);
		layeredPane_4.setLayout(gl_layeredPane_4);
		
		//ButtonGroup zapobiega wybieraniu wiêcej ni¿ jednej opcji wyboru
		ButtonGroup group = new ButtonGroup();
		
		JLayeredPane layeredPane_3 = new JLayeredPane();
		layeredPane_3.setBackground(Color.DARK_GRAY);
		layeredPane_3.setOpaque(true);
		tabbedPane_1.addTab("Wystaw umow\u0119", null, layeredPane_3, null);
		
		JButton btnNewButton_11 = new JButton("Dodaj");
		btnNewButton_11.setForeground(Color.BLACK);
		btnNewButton_11.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_11.setBackground(new Color(255, 99, 71));
			
			guestcount = new JTextField();
			guestcount.setColumns(10);
			
			totalcost = new JTextField();
			totalcost.setColumns(10);
			
			JRadioButton rdbtnNewRadioButton = new JRadioButton("Got\u00F3wka");
			rdbtnNewRadioButton.setForeground(new Color(255, 99, 71));
			rdbtnNewRadioButton.setFont(new Font("SansSerif", Font.BOLD, 14));
			
			JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Przelew");
			rdbtnNewRadioButton_1.setForeground(new Color(255, 99, 71));
			rdbtnNewRadioButton_1.setFont(new Font("SansSerif", Font.BOLD, 14));
			
			JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("Karta p\u0142atnicza");
			rdbtnNewRadioButton_2.setFont(new Font("SansSerif", Font.BOLD, 14));
			rdbtnNewRadioButton_2.setForeground(new Color(255, 99, 71));
			group.add(rdbtnNewRadioButton);
			group.add(rdbtnNewRadioButton_1);
			group.add(rdbtnNewRadioButton_2);
			
			JLabel aggrementdate = new JLabel("Data wystawienia umowy:");
			aggrementdate.setFont(new Font("Tahoma", Font.BOLD, 14));
			aggrementdate.setForeground(new Color(255, 99, 71));
			aggrementdate.setHorizontalAlignment(SwingConstants.LEFT);
			
			JLabel lblNewLabel_16 = new JLabel("Liczba uczestnik\u00F3w wycieczki");
			lblNewLabel_16.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel_16.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblNewLabel_16.setForeground(new Color(255, 99, 71));
			
			JLabel lblNewLabel_17 = new JLabel("Koszt ca\u0142kowity wycieczki");
			lblNewLabel_17.setForeground(new Color(255, 99, 71));
			lblNewLabel_17.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblNewLabel_17.setHorizontalAlignment(SwingConstants.CENTER);
			
			JLabel lblNewLabel_18 = new JLabel("Spos\u00F3b p\u0142atno\u015Bci");
			lblNewLabel_18.setForeground(new Color(255, 99, 71));
			lblNewLabel_18.setFont(new Font("Tahoma", Font.BOLD, 14));
			lblNewLabel_18.setHorizontalAlignment(SwingConstants.CENTER);
			
			JLabel choosenTrip = new JLabel("Wybrano wycieczk\u0119:");
			choosenTrip.setForeground(new Color(255, 99, 71));
			choosenTrip.setFont(new Font("Tahoma", Font.BOLD, 14));
			choosenTrip.setHorizontalAlignment(SwingConstants.CENTER);
			GroupLayout gl_layeredPane_3 = new GroupLayout(layeredPane_3);
			gl_layeredPane_3.setHorizontalGroup(
				gl_layeredPane_3.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_layeredPane_3.createSequentialGroup()
						.addGap(170)
						.addComponent(choosenTrip, GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
						.addGap(154))
					.addGroup(gl_layeredPane_3.createSequentialGroup()
						.addGap(170)
						.addComponent(lblNewLabel_16, GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
						.addGap(154))
					.addGroup(gl_layeredPane_3.createSequentialGroup()
						.addGap(10)
						.addComponent(aggrementdate, GroupLayout.PREFERRED_SIZE, 400, GroupLayout.PREFERRED_SIZE))
					.addGroup(Alignment.TRAILING, gl_layeredPane_3.createSequentialGroup()
						.addGap(246)
						.addGroup(gl_layeredPane_3.createParallelGroup(Alignment.TRAILING)
							.addComponent(rdbtnNewRadioButton_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
							.addComponent(rdbtnNewRadioButton_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
							.addComponent(guestcount, Alignment.LEADING)
							.addComponent(lblNewLabel_17, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(totalcost, Alignment.LEADING)
							.addComponent(lblNewLabel_18, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE))
						.addGap(242))
					.addGroup(gl_layeredPane_3.createSequentialGroup()
						.addGap(246)
						.addGroup(gl_layeredPane_3.createParallelGroup(Alignment.TRAILING)
							.addComponent(rdbtnNewRadioButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnNewButton_11, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE))
						.addGap(242))
			);
			gl_layeredPane_3.setVerticalGroup(
				gl_layeredPane_3.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_layeredPane_3.createSequentialGroup()
						.addGap(8)
						.addComponent(choosenTrip, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addGap(11)
						.addComponent(lblNewLabel_16, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
						.addGap(11)
						.addComponent(guestcount, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
						.addGap(11)
						.addComponent(lblNewLabel_17, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
						.addGap(11)
						.addComponent(totalcost, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
						.addGap(11)
						.addComponent(lblNewLabel_18, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(rdbtnNewRadioButton_2, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addGap(12)
						.addComponent(rdbtnNewRadioButton_1, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addGap(12)
						.addComponent(rdbtnNewRadioButton, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addGap(4)
						.addComponent(btnNewButton_11, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
						.addGap(12)
						.addComponent(aggrementdate, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
			);
			layeredPane_3.setLayout(gl_layeredPane_3);
			
			//przycisk odpowiedzialny za dodawanie nowej umowy p³atniczej klienta biura
			btnNewButton_11.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(clientbuff==null)
					{
						showErrorMessage("Nie wybrano klienta!");
						return;
					}
					if(wycieczka == null)
					{
						showErrorMessage("Nie wybrano wycieczki!");
						return;
					}
					
					Iterator<AbstractButton> ab = group.getElements().asIterator();
					String choose = null;
					AbstractButton absta;
					while(ab.hasNext())
					{
						absta = ab.next();
						if(absta.isSelected())
						{
							choose = absta.getText();
							break;
						}	
					}
					if(choose == null)
					{
						showErrorMessage("Nie wybrano rodzaju p³atnoœci!");
						return;
					}
					
					try {
						connector.addAggrement(new Aggrement(-1,wycieczka.getIdTrip(), 0, 0, 0, Integer.parseInt(guestcount.getText()), clientbuff.getIdClient(), null, null, new java.sql.Date(new Date().getTime()), null, null, Float.parseFloat(totalcost.getText()), choose, 
								choose.equalsIgnoreCase("Gotówka") || choose.equalsIgnoreCase("Karta p³atnicza")?"Zap³acono":"Niezap³acono"));
					}
					catch (NumberFormatException e1) {
						showErrorMessage("Z³y format !");
					}
				}
			});
		
		JLayeredPane layeredPane_5 = new JLayeredPane();
		layeredPane_5.setBackground(Color.DARK_GRAY);
		layeredPane_5.setOpaque(true);
		layeredPane_5.setBorder(null);
		tabbedPane_1.addTab("Wybierz wycieczk\u0119", null, layeredPane_5, null);
		
		JScrollPane scrollPane = new JScrollPane();
		
		table = new JTable() {
			 @Override
			 public boolean editCellAt(int row, int column, java.util.EventObject e) {
		            return false;
		         }
		};
		table.setShowVerticalLines(true);
		table.setShowHorizontalLines(true);
		table.setGridColor(Color.BLACK);
		table.setBackground(Color.LIGHT_GRAY);
		table.setFillsViewportHeight(true);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Nazwa", "Standard", "Cena", "Telefon", "Data wyjazdu", "Data powrotu", "Wolne miejsca"
			}
		) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, String.class, String.class, String.class
			};
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		scrollPane.setViewportView(table);
		
		JDateChooser datefroms = new JDateChooser();
		
		JLabel lblNewLabel_19 = new JLabel("Od:");
		lblNewLabel_19.setForeground(new Color(255, 99, 71));
		lblNewLabel_19.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_19.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblNewLabel_20 = new JLabel("Do:");
		lblNewLabel_20.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel_20.setForeground(new Color(255, 99, 71));
		lblNewLabel_20.setHorizontalAlignment(SwingConstants.CENTER);
		
		JDateChooser datetos = new JDateChooser();

		JButton btnNewButton_12 = new JButton("Szukaj");
		btnNewButton_12.setForeground(Color.BLACK);
		btnNewButton_12.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_12.setBackground(new Color(255, 99, 71));
		
		JButton btnNewButton_13 = new JButton("Wybierz");
		btnNewButton_13.setForeground(Color.BLACK);
		btnNewButton_13.setBackground(new Color(255, 99, 71));
		btnNewButton_13.setFont(new Font("Tahoma", Font.BOLD, 14));
		GroupLayout gl_layeredPane_5 = new GroupLayout(layeredPane_5);
		gl_layeredPane_5.setHorizontalGroup(
			gl_layeredPane_5.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane_5.createSequentialGroup()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_layeredPane_5.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNewButton_13, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
						.addGroup(gl_layeredPane_5.createSequentialGroup()
							.addGap(56)
							.addComponent(lblNewLabel_19, GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
							.addGap(47))
						.addComponent(datefroms, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
						.addGroup(gl_layeredPane_5.createSequentialGroup()
							.addGap(56)
							.addComponent(lblNewLabel_20, GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE)
							.addGap(47))
						.addComponent(datetos, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE)
						.addComponent(btnNewButton_12, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 242, Short.MAX_VALUE))
					.addGap(3))
		);
		gl_layeredPane_5.setVerticalGroup(
			gl_layeredPane_5.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane_5.createSequentialGroup()
					.addGroup(gl_layeredPane_5.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_layeredPane_5.createSequentialGroup()
							.addGap(11)
							.addComponent(lblNewLabel_19)
							.addGap(11)
							.addComponent(datefroms, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
							.addGap(12)
							.addComponent(lblNewLabel_20)
							.addGap(11)
							.addComponent(datetos, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
							.addGap(32)
							.addComponent(btnNewButton_12)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_13))
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE))
					.addGap(0))
		);
		layeredPane_5.setLayout(gl_layeredPane_5);
		
		JLayeredPane layeredPane_6 = new JLayeredPane();
		layeredPane_6.setOpaque(true);
		layeredPane_6.setBackground(Color.DARK_GRAY);
		tabbedPane_1.addTab("Znajdz Klienta", null, layeredPane_6, null);
		
		clientpeseltrip = new JTextField();
		clientpeseltrip.setColumns(10);
		
		JLabel lblNewLabel_31 = new JLabel("Pesel Klienta");
		lblNewLabel_31.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_31.setForeground(new Color(255, 99, 71));
		lblNewLabel_31.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JButton searchclient = new JButton("Szukaj Klienta");
		searchclient.setForeground(Color.BLACK);
		searchclient.setFont(new Font("Tahoma", Font.BOLD, 14));
		searchclient.setBackground(new Color(255, 99, 71));
		GroupLayout gl_layeredPane_6 = new GroupLayout(layeredPane_6);
		gl_layeredPane_6.setHorizontalGroup(
			gl_layeredPane_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane_6.createSequentialGroup()
					.addGap(299)
					.addComponent(lblNewLabel_31, GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
					.addGap(334))
				.addGroup(gl_layeredPane_6.createSequentialGroup()
					.addGap(212)
					.addComponent(clientpeseltrip, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
					.addGap(277))
				.addGroup(gl_layeredPane_6.createSequentialGroup()
					.addGap(212)
					.addComponent(searchclient, GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
					.addGap(277))
		);
		gl_layeredPane_6.setVerticalGroup(
			gl_layeredPane_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane_6.createSequentialGroup()
					.addGap(40)
					.addComponent(lblNewLabel_31)
					.addGap(11)
					.addComponent(clientpeseltrip, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addGap(61)
					.addComponent(searchclient, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE))
		);
		layeredPane_6.setLayout(gl_layeredPane_6);
		
		JButton tripback = new JButton("Powr\u00F3t");
		tripback.setForeground(Color.BLACK);
		tripback.setFont(new Font("Tahoma", Font.BOLD, 14));
		tripback.setBackground(new Color(255, 99, 71));
		
		JLabel choosenclient = new JLabel("Pesel klienta: ");
		choosenclient.setHorizontalAlignment(SwingConstants.LEFT);
		choosenclient.setFont(new Font("Tahoma", Font.BOLD, 14));
		choosenclient.setForeground(new Color(255, 99, 71));
		GroupLayout gl_wycieczki = new GroupLayout(wycieczki);
		gl_wycieczki.setHorizontalGroup(
			gl_wycieczki.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_wycieczki.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_wycieczki.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_wycieczki.createSequentialGroup()
							.addComponent(tabbedPane_1, GroupLayout.DEFAULT_SIZE, 894, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tripback, GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE))
						.addComponent(choosenclient, GroupLayout.PREFERRED_SIZE, 445, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_wycieczki.setVerticalGroup(
			gl_wycieczki.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_wycieczki.createSequentialGroup()
					.addGroup(gl_wycieczki.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_wycieczki.createSequentialGroup()
							.addContainerGap()
							.addComponent(tripback, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.LEADING, gl_wycieczki.createSequentialGroup()
							.addComponent(choosenclient, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tabbedPane_1, GroupLayout.PREFERRED_SIZE, 631, Short.MAX_VALUE)))
					.addGap(17))
		);
		wycieczki.setLayout(gl_wycieczki);
		datefrom.setBorder(null);
		datefroms.setBorder(null);
		dateto.setBorder(null);
		datetos.setBorder(null);

		//######################## IMPLEMENTACJA LISTENERÓW ###############################
		//przycisk logowania na pierwszym panelu autoryzacyjnym pracownika
		loginbutton.addActionListener((e)->{
			if(connector.login(login.getText(), String.valueOf(haslo.getPassword())))
			{
				lay.show(panel, "paneloperatora");
				zal.setText("Zalogowano jako: " + login.getText());
			}
		});
		
		//przycisk przechodz¹cy do panelu dodawania nowej oferty wycieczkowej
		paneladdofert.addActionListener((e)->{
			lay.show(panel, "Oferty");
		});
		
		//przycisk przechodz¹cy do panelu dodawania nowego klienta biura
		panelclient.addActionListener((e)->{
			lay.show(panel, "Klient");
			
		});
		
		//przycisk wracaj¹cy z panelu dodawania nowej oferty do panelu g³ównego pracownika 
		ofertback.addActionListener((e)->{
			clearOfertFields();
			lay.show(panel, "paneloperatora");
		});
		
		//przycisk dodaj¹cy now¹ ofertê wycieczkow¹. Wywo³uje funkcjê za to odpowiedzialn¹
		ofertadd.addActionListener((e)->{
			addOfert();
		});
		
		//przycisk usuwaj¹cy klienta o podanym peselu
		clientdelete.addActionListener((e)->{
			connector.removeClient(peselusun.getText());
		});
		
		//przycisk wracaj¹cy z panelu modyfikacji klienta do panelu zarz¹dzania klientami
		clientmodback.addActionListener((e)->{
			lay.show(panel, "Klient");
		});
		
		//przycisk wracaj¹cy z panelu zarz¹dzania klientami do panelu g³ównego pracownika
		clientmenuback.addActionListener((e)->{
			lay.show(panel, "paneloperatora");
		});
		
		//przycisk wracaj¹cy z panelu usuwania klienta do panelu zarz¹dzania klientami
		clientdelback.addActionListener((e)->{
			lay.show(panel, "Klient");
		});
		
		//przycisk wracaj¹cy z panelu zarz¹dzania wycieczkami klienta do panelu zarz¹dzania klientami
		//czyœci wszystkie wprowadzone dane
		tripback.addActionListener((e)->{
			lay.show(panel, "Klient");
			tripbuff = null;
			wycieczka = null;
			clientbuff = null;
			choosenclient.setText("Pesel klienta: ");
			choosenTrip.setText("Wybrano wycieczkê: ");
		});
		
		//przycisk przechodz¹cy z panelu zarz¹dzania klientami do panelu usuwania klienta
		clientmenudelete.addActionListener((e)->{
			lay.show(panel, "UsunKlienta");
		});
		
		//przycisk przechodz¹cy z panelu zarz¹dzania klientami do panelu modyfikacji lub dodawania klienta
		dodajklienta.addActionListener((e)->{
			lay.show(panel,"KlientDodajModyfikuj");
		});
		
		//przycisk przechodz¹cy z panelu zarz¹dzania klientami do panelu wycieczek klienta
		clientmenuofert.addActionListener((e)->{
			lay.show(panel, "wycieczki");
			SimpleDateFormat fo = new SimpleDateFormat("dd/MM/yyyy");
			
			aggrementdate.setText("Data wystawienia umowy: " + fo.format(new Date().getTime()));
		});
		
		//przycisk obs³uguj¹cy wybranie wycieczki z listy dostêpnych wycieczek
		btnNewButton_13.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow();
				if(tripbuff==null || index == -1 && tripbuff.length<index)
				{
					showInfoMessage("Wybierz wycieczkê klikaj¹c na wiersz!");
					return;
				}
				wycieczka = tripbuff[index];
				choosenTrip.setText("Wybrano wycieczkê: " + wycieczka.getName());
				showInfoMessage("Wybrano wycieczke!");
			}
		});
		
		//przycisk wyszukiwania wycieczki spelniaj¹cej podane kryteria
		btnNewButton_12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(datefroms.getDate()==null || datetos.getDate()==null)
				{
					showErrorMessage("Wybierz date!");
					return;
				}
				Trip tr = new Trip(0,0, 0, new java.sql.Date(datefroms.getDate().getTime()), new java.sql.Date(datetos.getDate().getTime()), null, null, 0);
				Trip []trips = connector.getTrips(tr);
				tripbuff = trips;
				DefaultTableModel tab = (DefaultTableModel)table.getModel();
				for(int i = tab.getRowCount() - 1; i >= 0; --i)
					tab.removeRow(i);
				
				int i = 0;
				DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
				for(Trip trip : trips)
				{
					if(trip.getWhatToDo()==WhatToDo.TRIPS_ERROR)
					{
						showErrorMessage("Nie uda³o siê za³adowaæ wszystkich lub czêœci wycieczek!");
						return;
					}
					tab.insertRow(i++, new Object[] {trip.getName(), Integer.toString(trip.getStandard()), trip.getPhonenumber() ,
							Float.toString(trip.getPrice()), format.format(trip.getDate_out()), format.format(trip.getDate_back()), trip.getMaxguest()});
				}
			}
		});
		
		//przycisk wyszukiwania klienta
		searchclient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client cl = connector.getClient(clientpeseltrip.getText());
				if(cl==null)
					return;
				
				clientbuff = cl;
				showInfoMessage("Znaleziono klienta o peselu: " + cl.getPesel());
				choosenclient.setText("Pesel klienta: " + cl.getPesel());
			}
		});
		
		//przycisk wylogowania siê z aplikacji pracownika (klienta aplikacji)
		panellogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(connector.logout())
					lay.show(panel, "panellogowania");
			}
		});
		
		//przycisk wyszukiwania wycieczki
		searchTrip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(datefrom.getDate()==null || dateto.getDate()==null)
				{
					showErrorMessage("Wybierz zakres poszukiwañ!");
					return;
				}
				if(clientbuff == null)
				{
					showErrorMessage("Wybierz klienta!!!");
					return;
				}
				Aggrement []ag = connector.getAggrements( new Aggrement(-1,0, 0, 0, 0, 0, clientbuff.getIdClient(), new java.sql.Date(datefrom.getDate().getTime()), new java.sql.Date(dateto.getDate().getTime()), null, null, null, 0, null, null));
			
				
				if(ag == null)
				{
					showErrorMessage("Coœ posz³o nie tak!!!");
					return;
				}
				aggrements = ag;
				DefaultTableModel tab = (DefaultTableModel)table_1.getModel();
				
				for(int i = tab.getRowCount()- 1; i >= 0; --i)
					tab.removeRow(i);
				
				int i = 0;
				SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
				for(Aggrement buff : ag)
				{
					if(buff.getWhatToDo()==WhatToDo.TRIPS_ERROR)
					{
						showErrorMessage("Nie uda³o siê pobraæ wszystkich rekordów!!!");
						return;
					}
					tab.insertRow(i++,new Object[] {buff.getName(), Float.toString(buff.getPrice()), form.format(buff.getAggrementdate()), buff.getPayment_status()});
				}
				
			}
		});
		
		//przycisk dodawania nowego klienta biura z panelu dodawania i modyfikacji klientów
		clientmodadd.addActionListener((e)->{
			
			Client client = new Client(-1,clientname.getText(),clientsurname.getText(), clientphonenumber.getText(), 
					clientpesel.getText(), clientcountry.getText(), clientcity.getText(),
					clientstreet.getText(), clienthomenumber.getText(), clientpostcode.getText());
			
			if(connector.addClient(client))
				clearClientFields();
		});
		
		//przycisk modyfikacji danych istniej¹cego klienta biura z panelu dodawania i modyfikacji klientów
		clientmodmod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client client = new Client(-1,clientname.getText(),clientsurname.getText(), clientphonenumber.getText(), 
						clientpesel.getText(), clientcountry.getText(), clientcity.getText(),
						clientstreet.getText(), clienthomenumber.getText(), clientpostcode.getText());
				connector.updateClient(client);
				clearClientFields();
			}
		});
		
		//przycisk pobrania informacji istniej¹cego klienta biura z panelu dodawania i modyfikacji klientów
		clientmodinfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client cl = connector.getClient(clientpesel.getText());
				if(cl != null)
				{
					clientcity.setText(cl.getCity());
					clientcountry.setText(cl.getCountry());
					clientstreet.setText(cl.getStreet());
					clientpostcode.setText(cl.getPostcode());
					clienthomenumber.setText(cl.getHomenumber());
					clientname.setText(cl.getName());
					clientsurname.setText(cl.getSurname());
					clientphonenumber.setText(cl.getPhoneNumber());
				}
			}
		});
		
		//przycisk zmiany stanu p³atnoœci wybranej z listy wycieczki
		statuschange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = table_1.getSelectedRow();
				if(index == -1)
				{
					showErrorMessage("Wybierz odpowiednij wiersz który chcesz zmieniæ!!!");
					return;
				}
				if(aggrements == null || index >= aggrements.length)
				{
					showErrorMessage("Wyst¹pi³ b³¹d");
					return;
				}
				Aggrement aggrement = aggrements[index];
				String status = "Zap³acono";
				if(aggrement.getPayment_status().equalsIgnoreCase("Zap³acono"))
					status = "Niezap³acono";
				aggrement = new Aggrement(aggrement.getIdaggrement(), index, index, index, index, index, index, null, null, null, null, null, index, aggrement.getPayment_type(), status);
				if(!connector.updatePayment(aggrement))
					return;
				
				table_1.setValueAt(status, index, 3);
				aggrements[index] = aggrement;
			}
		});
		
		//przycisk czyszcz¹cy pola w panelu dodawania nowego klienta
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clientcity.setText("");
				clientcountry.setText("");
				clientname.setText("");
				clientphonenumber.setText("");
				clientsurname.setText("");
				clientpesel.setText("");
				clientpostcode.setText("");
				clientstreet.setText("");
				clienthomenumber.setText("");
			}
		});
		
        
        /**
         * Funkcja uniemo¿liwiaj¹ca wpisanie liter i znaków w pole ceny przy dodawaniu wycieczki.
         * Dozwolone s¹ tylko cyfry. Jeœli wpisany znak nie jest cyfr¹ to "zjada go".
         */
        ofertprice.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();

                if(ofertprice.getText().length()+1 > 6)
                	e.consume();
                else if(!Character.isDigit(c))
                    e.consume();
            }
        });
        
        /**
         * Funkcja uniemo¿liwiaj¹ca wpisanie liter i znaków w pole numeru telefonu przy dodawaniu wycieczki.
         * Dozwolone s¹ tylko cyfry. Jeœli wpisany znak nie jest cyfr¹ to "zjada go".
         */
        ofertphone.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();

                if(ofertphone.getText().length()+1 > 9)
                	e.consume();
                else if(!Character.isDigit(c))
                    e.consume();
            }
        });
        
        /**
         * Funkcja uniemo¿liwiaj¹ca wpisanie liter i znaków w pole iloœci uczestników przy dodawaniu wycieczki.
         * Dozwolone s¹ tylko cyfry. Jeœli wpisany znak nie jest cyfr¹ to "zjada go".
         */
        ofertmax.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();

                if(ofertmax.getText().length()+1 > 4)
                	e.consume();
                else if(!Character.isDigit(c))
                    e.consume();
            }
        });
        
        /**
         * Funkcja uniemo¿liwiaj¹ca wpisanie liter i znaków w pole peselu usuwanego klienta.
         * Dozwolone s¹ tylko cyfry. Jeœli wpisany znak nie jest cyfr¹ to "zjada go".
         */
        peselusun.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();

                if(peselusun.getText().length()+1 > 11)
                	e.consume();
                else if(!Character.isDigit(c))
                    e.consume();
            }
        });
        
        /**
         * Funkcja uniemo¿liwiaj¹ca wpisanie liter i znaków w pole peselu dodawanego lub modyfikowanego klienta.
         * Dozwolone s¹ tylko cyfry. Jeœli wpisany znak nie jest cyfr¹ to "zjada go".
         */
        clientpesel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();

                if(clientpesel.getText().length()+1 > 11)
                	e.consume();
                else if(!Character.isDigit(c))
                    e.consume();
            }
        });
        
        /**
         * Funkcja uniemo¿liwiaj¹ca wpisanie liter i znaków w pole numeru domu dodawanego lub modyfikowanego klienta.
         * Dozwolone s¹ tylko cyfry. Jeœli wpisany znak nie jest cyfr¹ to "zjada go".
         */
        clienthomenumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();

                if(clienthomenumber.getText().length()+1 > 4)
                	e.consume();
                else if(!Character.isDigit(c))
                    e.consume();
            }
        });
        
        /**
         * Funkcja uniemo¿liwiaj¹ca wpisanie liter i znaków w pole numeru telefonu dodawanego lub modyfikowanego klienta.
         * Dozwolone s¹ tylko cyfry. Jeœli wpisany znak nie jest cyfr¹ to "zjada go".
         */
        clientphonenumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();

                if(clientphonenumber.getText().length()+1 > 9)
                	e.consume();
                else if(!Character.isDigit(c))
                    e.consume();
            }
        });
        
        /**
         * Funkcja uniemo¿liwiaj¹ca wpisanie liter i znaków innych ni¿ - w pole kodu pocztowego dodawanego lub modyfikowanego klienta.
         * Dozwolone s¹ tylko cyfry i znak -. Jeœli wpisany znak nie jest cyfr¹ to "zjada go".
         */
        clientpostcode.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();

                if(clientpostcode.getText().length()+1 > 6)
                	e.consume();
                else if(!Character.isDigit(c) && !Character.toString(c).equals("-"))
                    e.consume();
            }
        });
		
        /**
         * Funkcja uniemo¿liwiaj¹ca wpisanie liter i znaków w pole iloœci uczestników wybranej wycieczki.
         * Dozwolone s¹ tylko cyfry i znak -. Jeœli wpisany znak nie jest cyfr¹ to "zjada go".
         */
        guestcount.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();

                if(guestcount.getText().length()+1 > 4)
                	e.consume();
                else if(!Character.isDigit(c))
                    e.consume();
            }
        });
        
        /**
         * Funkcja uniemo¿liwiaj¹ca wpisanie liter i znaków w pole ca³kowitego kosztu wybranej wycieczki.
         * Dozwolone s¹ tylko cyfry i znak -. Jeœli wpisany znak nie jest cyfr¹ to "zjada go".
         */
        totalcost.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();

                if(totalcost.getText().length()+1 > 6)
                	e.consume();
                else if(!Character.isDigit(c))
                    e.consume();
            }
        });
        
        /**
         * Funkcja uniemo¿liwiaj¹ca wpisanie liter i znaków w pole peselu wybieranego klienta.
         * Dozwolone s¹ tylko cyfry. Jeœli wpisany znak nie jest cyfr¹ to "zjada go".
         */
        clientpeseltrip.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();

                if(clientpeseltrip.getText().length()+1 > 11)
                	e.consume();
                else if(!Character.isDigit(c))
                    e.consume();
            }
        });
        
        /**
         * Funkcja uniemo¿liwiaj¹ca wpisanie cyfr w pole imienia dodawanego klienta.
         * Dozwolone s¹ tylko litery. Jeœli wpisany znak nie jest liter¹ to "zjada go".
         */
        clientname.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();

                if(Character.isDigit(c) || !Character.isAlphabetic(c))
                    e.consume();
            }
        });
        
        /**
         * Funkcja uniemo¿liwiaj¹ca wpisanie cyfr w pole imienia dodawanego klienta.
         * Dozwolone s¹ tylko litery. Jeœli wpisany znak nie jest liter¹ to "zjada go".
         */
        clientsurname.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();

                if(Character.isDigit(c) || !Character.isAlphabetic(c))
                    e.consume();
            }
        });
        
        /**
         * Funkcja uniemo¿liwiaj¹ca wpisanie cyfr w pole kraju dodawanego klienta.
         * Dozwolone s¹ tylko litery. Jeœli wpisany znak nie jest liter¹ to "zjada go".
         */
        clientcountry.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();

                if(Character.isDigit(c) || !Character.isAlphabetic(c))
                    e.consume();
            }
        });
        
        /**
         * Funkcja uniemo¿liwiaj¹ca wpisanie cyfr w pole miasta dodawanego klienta.
         * Dozwolone s¹ tylko litery. Jeœli wpisany znak nie jest liter¹ to "zjada go".
         */
        clientcity.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();

                if(Character.isDigit(c) || !Character.isAlphabetic(c))
                    e.consume();
            }
        });
        
        
	}//##################### KONIEC KONSTRUKTORA ######################
	
	/**
	 * Metoda czyszcz¹ca pola wpisywania w panelu dodawania oferty wycieczkowej
	 */
	private void clearOfertFields()
	{
		ofertmax.setText("");
		ofertprice.setText("");
		ofertphone.setText("");
		ofertname.setText("");
	}
	/**
	 * Metoda pobieraj¹ca wartoœci pól do wpisywania z panelu dodawania oferty wycieczkowej i przesy³aj¹ca
	 * ¿¹danie do serwera w celu dodania nowej oferty do bazy danych
	 */
	private void addOfert()
	{
		Iterator<AbstractButton> ab = ofertstandard.getElements().asIterator();
		int standard = -1;
		AbstractButton absta;
		while(ab.hasNext())
		{
			absta = ab.next();
			if(absta.isSelected())
			{
				try {
					standard = Integer.parseInt(absta.getText());
				} 
				catch(NumberFormatException exc)
				{
					exc.printStackTrace();
				}
				
				break;
			}	
		}
		Trip ofert = null;
		try {
			if(ofertfrom.getDate()==null && ofertto.getDate()==null)
				{
					showErrorMessage("Wybierz date!");
				}
			ofert = new Trip(-1,Integer.parseInt(ofertmax.getText()), standard, new java.sql.Date(ofertfrom.getDate().getTime()), new java.sql.Date(ofertto.getDate().getTime())
					, ofertname.getText(), ofertphone.getText(), Float.parseFloat(ofertprice.getText()));
		}
		catch (NumberFormatException exc) {
			showErrorMessage("Z³y format!");
			return;
		}
		
		if(ofert.getName().length() < 4 || ofert.getName().length() > 30)
		{
			showErrorMessage("Nazwa oferty musi mieæ d³ugoœæ od 4 do 30 znaków");
			return;
		}
		if(ofert.getMaxguest() < 0)
		{
			showErrorMessage("Iloœæ goœci nie mo¿e byæ mniejsza od 0!");
			return;
		}
		if(ofert.getDate_out().getTime() < new Date().getTime())
		{
			showErrorMessage("Data nie mo¿e byæ mniejsza od obecnej daty!");
			return;
		}
		if(ofert.getStandard()==-1)
		{
			showErrorMessage("Wybierz standard wycieczki!");
			return;
		}
		if(connector.addTrip(ofert))
			clearOfertFields();
	}
	
	/**
	 * Metoda czyszcz¹ca pola w panelu dodawania nowego klienta
	 */
	private void clearClientFields()
	{
		clientcity.setText("");
		clientcountry.setText("");
		clienthomenumber.setText("");
		clientname.setText("");
		clientpesel.setText("");
		clientphonenumber.setText("");
		clientpostcode.setText("");
		clientstreet.setText("");
		clientsurname.setText("");
	}
	
	/**
	 * Metoda wyœwietlaj¹ca w oknie dialogowym b³¹d jaki wyst¹pi³
	 * @param text wiadomoœæ b³êdu
	 */
	public static void showErrorMessage(@NotNull String text)
	{
		JOptionPane.showMessageDialog(frame, text,"B³¹d", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Metoda wyœwietlaj¹ca okno dialogowe z jakimœ komunikatem.
	 * @param text wiadomoœæ komunikatu
	 */
	public static void showInfoMessage(@NotNull String text)
	{
		JOptionPane.showMessageDialog(frame, text,"Komunikat", JOptionPane.INFORMATION_MESSAGE);
	}
}