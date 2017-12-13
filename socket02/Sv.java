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
		List<Socket> list = new Vector<>();// �� Ŭ���̾�Ʈ���� ������ �����ϴ� �κ�

		try {
			serversocket = new ServerSocket(9000);
			System.out.println("Ŭ���̾�Ʈ�� ��ٸ��ϴ�.");
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
		this.list = list;// ��� Ŭ���̾�Ʈ�� ������ ���⿡ ���� ������� �Ѹ��ϴ�.
	}

	@Override
	public void run() {
		System.out.println(socket.getLocalAddress() + "������");
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
				System.out.println(socket.getInetAddress()+": ��������");
				System.out.println(list);
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// �� �޼ҵ�ʹ� ������ ���� ����鿡�� �Ѹ��� ��
	public void broadcast (String msg) {
		for (Socket socket : list) {
			OutputStream out = null;
			PrintWriter pw = null;
			try {
				out = socket.getOutputStream();
				pw = new PrintWriter(new OutputStreamWriter(out));
				System.out.println("Ŭ���̾�Ʈ�κ��� : " + msg);
				pw.println(msg);
				pw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
