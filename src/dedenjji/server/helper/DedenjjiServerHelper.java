package dedenjji.server.helper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DedenjjiServerHelper implements Runnable {

	private Socket client;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	@Override
	public void run() {
		// 클라이언트로부터 온 메시지 읽기(무한 루프)
		String result;
		try {
			dis = new DataInputStream(client.getInputStream());
			result = dis.readUTF();
			System.out.println(result);
//			broadcast(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void broadcast(String msg) throws IOException {
		// 한번에 하나의 쓰레드만 호출가능, 매개변수로 들어오는 메시지를 모든 접속자에게 출력
		try {
			dos = new DataOutputStream(client.getOutputStream());
			dos.writeUTF(msg);
			System.out.println(msg);
			dos.flush();
		} finally {
			if (dos != null) {
				dos.close();
			}
		}
	}

	public void setClient(Socket client) {
		this.client = client;
	}

	public Socket getClient() {
		return client;
	}
	public DataInputStream getDis() {
		return dis;
	}
	public DataOutputStream getDos() {
		return dos;
	}
}
