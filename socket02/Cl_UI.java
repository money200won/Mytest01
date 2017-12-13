package socket02;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;

public class Cl_UI {
	JFrame f =new JFrame("채팅 클라이언트");
	Button b1,b2;
	TextField ip,port,id,msg;
	TextArea ta;
	
	Cl_UI(){
		Panel p1 = new Panel();
		p1.add(ip = new TextField("70.12.109.67"));
		p1.add(port = new TextField("9000"));
		p1.add(b1 = new Button("연결"));
		p1.add(b2 = new Button("종료"));
		f.add(p1, BorderLayout.NORTH);
		f.add(ta = new TextArea(25,30));
		
		Panel p2 = new Panel();
		p2.add(id=new TextField(100));
		p2.add(msg=new TextField(100));
		f.add(p2, BorderLayout.SOUTH);
		f.setSize(400, 350);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
		b1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
			}
		});
	}
	
	

	public static void main(String[] args) {

		Socket socket = null;
		InputStream in = null;
		BufferedReader br = null;
		OutputStream out = null;
		PrintWriter pw = null;
		try {
			socket = new Socket("70.12.109.67", 9000);
			System.out.println("접속 성공!");

			BufferedReader incoming = new BufferedReader(new InputStreamReader(System.in));

			in = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(in));
			out = socket.getOutputStream();
			pw = new PrintWriter(new OutputStreamWriter(out));

			//ClientThread(socket, br).start();

			String msg = "";
			while ((msg = incoming.readLine()) != null) {
				if (msg.equals("quit"))
					break;

				pw.println(msg);
				pw.flush();
				String echoMessage = br.readLine();
				System.out.println("서버로부터 : " + echoMessage);
			}

		} catch (IOException e) {

			e.printStackTrace();
		} finally {

			try {
				br.close();
				pw.close();
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	class ClientThread extends Thread {
		Socket socket;
		BufferedReader br;

		public ClientThread() {
		}

		public ClientThread(Socket socket, BufferedReader br) {
			super();
			this.socket = socket;
			this.br = br;
		}

		public void run() {
			try {
				String msg = "";
				while ((msg = br.readLine()) != null) {
					System.out.println(msg);
				}
			} catch (IOException e) {
				e.getMessage();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}