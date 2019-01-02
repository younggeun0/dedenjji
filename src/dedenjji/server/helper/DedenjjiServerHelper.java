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
			broadcast("["+cnt+"] 번째 접속자가 접속했습니다.");
		} catch (IOException ie) {
			JOptionPane.showMessageDialog(jf, "접속자 연결 중 문제 발생...");
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
//				dsv.getJtaLogs().append("[server]:"+nick+"님이 "+team+"을 선택하였습니다.\n");
	}
	
	// 한번에 하나의 쓰레드만 호출가능, 매개변수로 들어오는 메시지를 모든 접속자에게 출력
	public synchronized void broadcast(String msg) {
		
		DedenjjiServerHelper dsh = null;
		try {
			for(int i=0; i<listClient.size(); i++) {
				dsh = listClient.get(i);
				dsh.writeStream.writeUTF(msg);
				dsh.writeStream.flush();
			}
		} catch (IOException ie) {
			JOptionPane.showMessageDialog(jf, "["+cnt+"]번째 접속자에게 메세지를 보낼 수 없습니다.");
			ie.printStackTrace();
		}
		
		
		/*for(int i=0; i<dse.getListClient().size(); i++) {
			// 접속하자마자 유저들에게 뿌려야함..
			dse.getListClient().get(i).dos.writeUTF("[server]: 현재 접속 인원은 "+dse.getListClient().size()+"명 입니다.\n");
//			dse.getListClient().get(i).dos.writeUTF("[server]:"+nick+"님이 팀을 선택하였습니다.\n");
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
