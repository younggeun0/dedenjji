package dedenjji.server.helper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import dedenjji.server.evt.DedenjjiServerEvt;
import dedenjji.server.view.DedenjjiServerView;

public class DedenjjiServerHelper extends Thread {

	private String nick;
	private String team;
	private Socket client;
	private DataInputStream readStream;
	private DataOutputStream writeStream;
	private DefaultListModel<String> dlmClients;
	private List<DedenjjiServerHelper> listClient;
	private JTextArea jtaLogs;
	private int cnt;
	private JFrame jf;
	
	public DedenjjiServerHelper(Socket client, DefaultListModel<String> dlmClients, JTextArea jtaLogs, int cnt,
			JFrame jf, List<DedenjjiServerHelper> listClient) {
		this.client = client;
		this.jtaLogs = jtaLogs;
		this.dlmClients = dlmClients;
		this.cnt = cnt;
		this.listClient = listClient;
		this.jf = jf;
		
		try {
			readStream = new DataInputStream(client.getInputStream());
			writeStream = new DataOutputStream(client.getOutputStream());
			broadcast("["+cnt+"] ��° �����ڰ� �����߽��ϴ�.");
		} catch (IOException ie) {
			JOptionPane.showMessageDialog(jf, "������ ���� �� ���� �߻�...");
			ie.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		if(readStream != null) {
			String revMsg = "";
			
			try {
				while (true) {
					revMsg = readStream.readUTF();
					broadcast(revMsg);
				}
			} catch (IOException ie) {
				
				ie.printStackTrace();
			}
		}
//				dsv.getJtaLogs().append("[server]:"+nick+"���� "+team+"�� �����Ͽ����ϴ�.\n");
	}
	
	// �ѹ��� �ϳ��� �����常 ȣ�Ⱑ��, �Ű������� ������ �޽����� ��� �����ڿ��� ���
	public synchronized void broadcast(String msg) {
		
		DedenjjiServerHelper dsh = null;
		try {
			for(int i=0; i<listClient.size(); i++) {
				dsh = listClient.get(i);
				dsh.writeStream.writeUTF(msg);
				dsh.writeStream.flush();
			}
		} catch (IOException ie) {
			JOptionPane.showMessageDialog(jf, "["+cnt+"]��° �����ڿ��� �޼����� ���� �� �����ϴ�.");
			ie.printStackTrace();
		}
		
		
		/*for(int i=0; i<dse.getListClient().size(); i++) {
			// �������ڸ��� �����鿡�� �ѷ�����..
			dse.getListClient().get(i).dos.writeUTF("[server]: ���� ���� �ο��� "+dse.getListClient().size()+"�� �Դϴ�.\n");
//			dse.getListClient().get(i).dos.writeUTF("[server]:"+nick+"���� ���� �����Ͽ����ϴ�.\n");
			dse.getListClient().get(i).dos.flush();
		}*/
	}
	
	public void setClient(Socket client) {
		this.client = client;
	}
	public Socket getClient() {
		return client;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
}
