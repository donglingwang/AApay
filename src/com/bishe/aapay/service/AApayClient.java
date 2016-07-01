package com.bishe.aapay.service;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import android.R.integer;

import com.bishe.aapay.dao.CategoryDao;
import com.bishe.aapay.dao.ConsumptionDao;
import com.bishe.aapay.dao.ParticipantDao;
import com.bishe.aapay.dao.PaymentDao;
import com.bishe.aapay.dao.PersonalBudgetDAO;
import com.bishe.aapay.dao.TeamDao;
import com.bishe.aapay.dto.Category;
import com.bishe.aapay.dto.Consumption;
import com.bishe.aapay.dto.Participant;
import com.bishe.aapay.dto.Payment;
import com.bishe.aapay.dto.PersonalBudget;
import com.bishe.aapay.dto.Team;
import com.bishe.aapay.dto.User;

public class AApayClient {
	private static AApayClient client;
	private static final  String ipAdress = "192.168.191.1";
	private static final int port = 30002;
	private static final int BUFFER_SIZE=1024*1024; 
	private AApayClient() {
		
	}
	private AApayClient(String ipAdress, int port) {
		
	}
	public static AApayClient getInstance() {
		if(client == null) {
			client = new AApayClient(ipAdress,port);
		}
		return client;
	}
	/**
	 * 向服务器发送用户
	 * @param user
	 */
	public boolean sendUser(User user) {
		Socket socket = null;
		OutputStream os = null;
		ObjectOutputStream oos = null;
		InputStream is = null;
		BufferedReader br = null;
		try {
			socket = new Socket(ipAdress, port);
			os = socket.getOutputStream();
			os.write("sendUser\n".getBytes("UTF-8"));
			os.flush();
			oos = new ObjectOutputStream(os);
			oos.writeObject(user);
			oos.flush();	
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String result = br.readLine();
			if("success".equals(result)) {
				return true;
			}
		} catch(IOException ex) {
			
		} 
		finally {
			try {
				os.close();
				oos.close();
				is.close();
				br.close();
				socket.close();
			} catch(Exception ex) 
			{
				
			}
		}
		return false;
	}
	
	public boolean backupCategory(List<Category> categories,int userId) {
		Socket socket = null;
		OutputStream os = null;
		ObjectOutputStream oos = null;
		InputStream is = null;
		BufferedReader br = null;
		try {
			socket = new Socket(ipAdress, port);
			os = socket.getOutputStream();
			os.write("backupCategory\n".getBytes("UTF-8"));
			os.flush();
			oos = new ObjectOutputStream(os);
			oos.writeInt(userId);
			oos.writeObject(categories);
			oos.flush();	
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String result = br.readLine();
			if("success".equals(result)) {
				return true;
			}
		} catch(IOException ex) {
			
		} 
		finally {
			try {
				os.close();
				oos.close();
				is.close();
				br.close();
				socket.close();
			} catch(Exception ex) 
			{
				
			}
		}
		return false;
	}
	
	public boolean backupConsumption(List<Consumption> consumptions,int userId) {
		Socket socket = null;
		OutputStream os = null;
		ObjectOutputStream oos = null;
		InputStream is = null;
		BufferedReader br = null;
		try {
			socket = new Socket(ipAdress, port);
			os = socket.getOutputStream();
			os.write("backupConsumption\n".getBytes("UTF-8"));
			os.flush();
			oos = new ObjectOutputStream(os);
			oos.writeInt(userId);
			oos.writeObject(consumptions);
			oos.flush();	
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String result = br.readLine();
			if("success".equals(result)) {
				return true;
			}
		} catch(IOException ex) {
			
		} 
		finally {
			try {
				os.close();
				oos.close();
				is.close();
				br.close();
				socket.close();
			} catch(Exception ex) 
			{
				
			}
		}
		return false;
	}
	
	public boolean backupParticipant(List<Participant> participants,int userId) {
		Socket socket = null;
		OutputStream os = null;
		ObjectOutputStream oos = null;
		InputStream is = null;
		BufferedReader br = null;
		try {
			socket = new Socket(ipAdress, port);
			os = socket.getOutputStream();
			os.write("backupParticipant\n".getBytes("UTF-8"));
			os.flush();
			oos = new ObjectOutputStream(os);
			oos.writeInt(userId);
			oos.writeObject(participants);
			oos.flush();	
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String result = br.readLine();
			if("success".equals(result)) {
				return true;
			}
		} catch(IOException ex) {
			
		} 
		finally {
			try {
				os.close();
				oos.close();
				is.close();
				br.close();
				socket.close();
			} catch(Exception ex) 
			{
				
			}
		}
		return false;
	}
	
	public boolean backupPayment(List<Payment> payments,int userId) {
		Socket socket = null;
		OutputStream os = null;
		ObjectOutputStream oos = null;
		InputStream is = null;
		BufferedReader br = null;
		try {
			socket = new Socket(ipAdress, port);
			os = socket.getOutputStream();
			os.write("backupPayment\n".getBytes("UTF-8"));
			os.flush();
			oos = new ObjectOutputStream(os);
			oos.writeInt(userId);
			oos.writeObject(payments);
			oos.flush();	
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String result = br.readLine();
			if("success".equals(result)) {
				return true;
			}
		} catch(IOException ex) {
			
		} 
		finally {
			try {
				os.close();
				oos.close();
				is.close();
				br.close();
				socket.close();
			} catch(Exception ex) 
			{
				
			}
		}
		return false;
	}
	
	public boolean backupBudget(List<PersonalBudget> budgets,int userId) {
		Socket socket = null;
		OutputStream os = null;
		ObjectOutputStream oos = null;
		InputStream is = null;
		BufferedReader br = null;
		try {
			socket = new Socket(ipAdress, port);
			os = socket.getOutputStream();
			os.write("backupBudget\n".getBytes("UTF-8"));
			os.flush();
			oos = new ObjectOutputStream(os);
			oos.writeInt(userId);
			oos.writeObject(budgets);
			oos.flush();	
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String result = br.readLine();
			if("success".equals(result)) {
				return true;
			}
		} catch(IOException ex) {
			
		} 
		finally {
			try {
				os.close();
				oos.close();
				is.close();
				br.close();
				socket.close();
			} catch(Exception ex) 
			{
				
			}
		}
		return false;
	}
	
	public boolean backupTeam(List<Team> teams,int userId) {
		Socket socket = null;
		OutputStream os = null;
		ObjectOutputStream oos = null;
		InputStream is = null;
		BufferedReader br = null;
		try {
			socket = new Socket(ipAdress, port);
			os = socket.getOutputStream();
			os.write("backupTeam\n".getBytes("UTF-8"));
			os.flush();
			oos = new ObjectOutputStream(os);
			oos.writeInt(userId);
			oos.writeObject(teams);
			oos.flush();	
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is,"UTF-8"));
			String result = br.readLine();
			if("success".equals(result)) {
				return true;
			}
		} catch(IOException ex) {
			
		} 
		finally {
			try {
				os.close();
				oos.close();
				is.close();
				br.close();
				socket.close();
			} catch(Exception ex) 
			{
				
			}
		}
		return false;
	}
	/**
	 * 从服务器读取用户数据
	 * @param email
	 * @return
	 */
	public User readUser(String email)  {
		Socket socket = null;
		ObjectInputStream is = null;
		OutputStream os = null;
		User user = null;
		try {
			socket = new Socket(ipAdress, port);	
			os = socket.getOutputStream();
			os.write("readUser\n".getBytes("UTF-8"));
			os.write((email+"\n").getBytes("UTF-8"));
			os.flush();
			is = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			Object obj = is.readObject();
			if (obj != null) {
				user = (User)obj;
			}
		} catch(IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		finally {
			try {
				is.close();
				socket.close();
			} catch(Exception ex) 
			{
				
			}
		}
		return user;
	}
	
	public void dataRecovery(CategoryDao categoryDao,ConsumptionDao consumptionDao,ParticipantDao participantDao,
			PaymentDao paymentDao,PersonalBudgetDAO personalBudgetDAO,TeamDao teamDao,int userId) {
		Socket socket = null;
		InputStream is = null;
		ObjectInputStream ois = null;
		OutputStream os = null;
		ObjectOutputStream oos = null;
		
		try {
			socket = new Socket(ipAdress, port);	
			os = socket.getOutputStream();
			os.write("dataRecovery\n".getBytes("UTF-8"));
			oos = new ObjectOutputStream(os);
			os.flush();
			oos.writeInt(userId);
			oos.flush();
			is = socket.getInputStream();
			byte [] buffer = new  byte[BUFFER_SIZE];
			//is.read(buffer);
			ois = new ObjectInputStream(is);
			//ois = new ObjectInputStream(new ByteArrayInputStream(buffer) );
			Object category = ois.readObject();
			List<Category> categories = (List<Category>)category;
			System.out.println(categories.size());
			
			Object consumption = ois.readObject();
			List<Consumption> consumptions = (List<Consumption>) consumption;
			Object participant = ois.readObject();
			List<Participant> participants = (List<Participant>) participant;
			Object payment = ois.readObject();
			List<Payment> payments = (List<Payment>) payment;
			Object budget = ois.readObject();
			List<PersonalBudget> budgets = (List<PersonalBudget>) budget;
			Object team = ois.readObject();
			List<Team> teams = (List<Team>) team;
			
			categoryDao.batchInsert(categories);
			consumptionDao.batchInsert(consumptions);
			participantDao.batchInsert(participants);
			paymentDao.batchInsert(payments);
			personalBudgetDAO.batchInsert(budgets);
			teamDao.batchInsert(teams);
		} catch(IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		finally {
			try {
				ois.close();
				socket.close();
			} catch(Exception ex) 
			{
				
			}
		}
	}
}
