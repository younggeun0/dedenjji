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
	private boolean divideTeamFlag;
	private DataInputStream readStream;
	private DataOutputStream writeStream;
	private DefaultListModel<String> dlmClients;
	private List<DedenjjiServerHelper> listClient;
	private JTextArea jtaLogs;
	private int cnt;
	private JFrame jf;
	
	public DedenjjiServerHelper(Socket client, DefaultListModel<String> dlmClients, 
			JTextArea jtaLogs, int cnt,
			JFrame jf, List<DedenjjiServerHelper> listClient, boolean divideTeamFlag) {
		this.client = client;
		this.jtaLogs = jtaLogs;
		this.dlmClients = dlmClients;
		this.cnt = cnt;
		this.listClient = listClient;
		this.jf = jf;
		this.divideTeamFlag = divideTeamFlag;
		
		try {
			readStream = new DataInputStream(client.getInputStream());
			writeStream = new DataOutputStream(client.getOutputStream());
			
			nick = readStream.readUTF();
			dlmClients.addElement(nick);
			broadcast("["+cnt+"]��° ������ ["+nick+"]���� �����߽��ϴ�.");
			jtaLogs.append("["+nick+"]���� �����Ͽ����ϴ�.\n");
			
			if (cnt %2 == 0) {
				broadcast("¦�� �ο��� �����߽��ϴ�.\nShow Result�۵� ����...");
				jtaLogs.append("¦�� �ο��� �����߽��ϴ�.\n���� ���� �غ� �Ǿ����ϴ�...\n");
				divideTeamFlag = true;
			} else {
				broadcast("¦�� �ο��� �ƴմϴ�.\n���� ���� �غ� �ȵǾ����ϴ�. ������ּ���...");
				jtaLogs.append("¦�� �ο��� �ƴմϴ�.\nShow Result�۵� �Ұ�...\n");
				divideTeamFlag = false;
			}
		} catch (IOException ie) {
			JOptionPane.showMessageDialog(jf, "������ ���� �� ���� �߻�...");
			ie.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		if(readStream != null) {
			String revMsg = "";
			String nick = "";
			try {
				while (true) {
					revMsg = readStream.readUTF();
					jtaLogs.append(revMsg+"\n");
					nick = revMsg.substring(revMsg.indexOf("[")+1, revMsg.indexOf("]"));
					System.out.println(nick);
					broadcast("["+nick+"]���� �����ϼ̽��ϴ�.");
				}
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}
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
			JOptionPane.showMessageDialog(jf, "["+nick+"]�Կ��� �޼����� ���� �� �����ϴ�.");
			ie.printStackTrace();
		}
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
	public String getTeam() {
		return team;
	}
}
