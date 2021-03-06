package dedenjji.server.helper;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import dedenjji.server.evt.DedenjjiServerEvt;

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
	private DedenjjiServerEvt dce;
	private JScrollPane jspLogs;
	
	public DedenjjiServerHelper(Socket client, DefaultListModel<String> dlmClients, 
			JTextArea jtaLogs, int cnt,
			JFrame jf, List<DedenjjiServerHelper> listClient, DedenjjiServerEvt dce,
			JScrollPane jspLogs) {
		this.client = client;
		this.jtaLogs = jtaLogs;
		this.dlmClients = dlmClients;
		this.cnt = cnt;
		this.listClient = listClient;
		this.jf = jf;
		this.dce = dce;
		this.jspLogs = jspLogs;
		
		
		try {
			readStream = new DataInputStream(client.getInputStream());
			writeStream = new DataOutputStream(client.getOutputStream());
			
			nick = readStream.readUTF();
			dlmClients.addElement(nick);
			broadcast("["+cnt+"]번째 접속자 ["+nick+"]님이 접속했습니다.");
			jtaLogs.append("["+nick+"]님이 접속하였습니다.\n");
			
			if (cnt % 2 == 0) {
				System.out.println(cnt);
				dce.setDivideTeamFlag(true);
				broadcast("짝수 인원이 접속했습니다. 팀을 나눌 준비가 되었습니다...\n팀을 선택해주세요...");
				jtaLogs.append("짝수 인원이 접속했습니다. 팀 선택 대기중...\n");
				jspLogs.getVerticalScrollBar().setValue(jspLogs.getVerticalScrollBar().getMaximum());
//				jtaLogs.append("모든 유저 팀선택 완료...\nShow Result로 팀을 나눌 수 있습니다.\n");
			} else {
				dce.setDivideTeamFlag(false);
				broadcast("짝수 인원이 아닙니다. 팀을 나눌 준비가 안되었습니다. 대기해주세요...");
				jtaLogs.append("짝수 인원이 아닙니다. 짝수인원 접속 대기중...\n");
				jspLogs.getVerticalScrollBar().setValue(jspLogs.getVerticalScrollBar().getMaximum());
			}
			
		} catch (IOException ie) {
			JOptionPane.showMessageDialog(jf, "접속자 연결 중 문제 발생...");
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
					jspLogs.getVerticalScrollBar().setValue(jspLogs.getVerticalScrollBar().getMaximum());
					nick = revMsg.substring(revMsg.indexOf("[")+1, revMsg.indexOf("]"));
					team = revMsg.substring(revMsg.indexOf(" ")+1, revMsg.indexOf("을"));
					System.out.println(team);
					broadcast("["+nick+"]님이 팀을 선택하셨습니다.");
					
				}
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}
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
			JOptionPane.showMessageDialog(jf, "["+nick+"]님에게 메세지를 보낼 수 없습니다.");
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
