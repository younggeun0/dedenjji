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
		// Ŭ���̾�Ʈ�κ��� �� �޽��� �б�(���� ����)
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
		// �ѹ��� �ϳ��� �����常 ȣ�Ⱑ��, �Ű������� ������ �޽����� ��� �����ڿ��� ���
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
