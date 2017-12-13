package socket02;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Vector;

public class Sv {
	public static void main(String[] args) {
		ServerSocket serversocket = null;
		List<Socket> list = new Vector<>();// 각 클라이언트들의 정보를 보관하는 부분

		try {
			serversocket = new ServerSocket(9000);
			System.out.println("클라이언트를 기다립니다.");
			while (true) {
				Socket socket = serversocket.accept();
				list.add(socket);
				Th t = new Th(socket, list);
				Thread thd = new Thread(t);
				thd.start();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serversocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class Th implements Runnable {
	List<Socket> list;
	Socket socket;

	public Th(Socket socket, List<Socket> list) {
		super();
		this.socket = socket;
		this.list = list;// 모든 클라이언트의 정보를 여기에 담이 쓰레드로 뿌립니다.
	}

	@Override
	public void run() {
		System.out.println(socket.getLocalAddress() + "접속중");
		InputStream in = null;

		BufferedReader br = null;
		try {
			in = socket.getInputStream();

			br = new BufferedReader(new InputStreamReader(in));

			String msg = "";
			while ((msg = br.readLine()) != null) {
				broadcast(msg);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				list.remove(socket);
				System.out.println(socket.getInetAddress()+": 접속해제");
				System.out.println(list);
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// 런 메소드와는 별개로 여러 사람들에게 뿌리는 곳
	public void broadcast (String msg) {
		for (Socket socket : list) {
			OutputStream out = null;
			PrintWriter pw = null;
			try {
				out = socket.getOutputStream();
				pw = new PrintWriter(new OutputStreamWriter(out));
				System.out.println("클라이언트로부터 : " + msg);
				pw.println(msg);
				pw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
