package dedenjji.server.helper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import dedenjji.server.view.DedenjjiServerView;

public class DedenjjiServerHelper implements Runnable {

	private String nick;
	private String team;
	private Socket client;
	private DataInputStream dis;
	private DataOutputStream dos;
	private DedenjjiServerView dsv;
	
	@Override
	public void run() {
		// Ŭ���̾�Ʈ�κ��� �� �޽��� �б�(���� ����)
		try {
			dis = new DataInputStream(client.getInputStream());
			dos = new DataOutputStream(client.getOutputStream());
			while(true) {
				team = dis.readUTF();
				dsv.getJtaLogs().append("[server]:"+nick+"���� "+team+"�� �����Ͽ����ϴ�.\n");
				broadcast(nick, team);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// �ϴ� ���� ���
	public synchronized void broadcast(String nick, String team) throws IOException {
		// �ѹ��� �ϳ��� �����常 ȣ�Ⱑ��, �Ű������� ������ �޽����� ��� �����ڿ��� ���
		try {
			dos.writeUTF("[server]:"+nick+"���� "+team+"�� �����Ͽ����ϴ�.\n");
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
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public void setDis(InputStream is) {
		this.dis = new DataInputStream(is);
	}
	public void setDos(OutputStream os) {
		this.dos = new DataOutputStream(os);
	}
	public DedenjjiServerView getDsv() {
		return dsv;
	}
	public void setDsv(DedenjjiServerView dsv) {
		this.dsv = dsv;
	}
}
