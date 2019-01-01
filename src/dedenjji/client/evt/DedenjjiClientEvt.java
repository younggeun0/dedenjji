package dedenjji.client.evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import com.sun.javafx.embed.swing.Disposer;

import dedenjji.client.view.DedenjjiClientView;

public class DedenjjiClientEvt extends WindowAdapter implements ActionListener, Runnable {

	private DedenjjiClientView dcv;
	private Socket client;
	private DataInputStream dis;
	private DataOutputStream dos;
	private boolean serverFlag;
	
	public DedenjjiClientEvt(DedenjjiClientView dcv) {
		this.dcv = dcv;
	}
	
	@Override
	public void run() {
		String serverMsg;
		try {
			while(true) {
				serverMsg = dis.readUTF();
				dcv.getJtaLogs().append(serverMsg+"\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dcv.getJbConnectToServer()) {
			String nick = JOptionPane.showInputDialog(dcv, "������ ������ �г����� �Է����ּ���.");
			
			if (client == null) {
				try {
					client = new Socket("localhost", 6000);
					dcv.getJtaLogs().append("[client]: "+nick+"������ ������ �����Ͽ����ϴ�.\n");
					dos = new DataOutputStream(client.getOutputStream());
					dis = new DataInputStream(client.getInputStream());
					dos.writeUTF(nick);
					dos.flush();
					
					Thread readThread = new Thread(this);
					readThread.start();
					
					serverFlag = true;
				} catch (IOException ie) {
					ie.printStackTrace();
				} 
			} else {
				JOptionPane.showMessageDialog(dcv, "�̹� �������Դϴ�.");
				return;
			}
		}
		
		if (e.getSource() == dcv.getJbSend()) {
			if (serverFlag) {
				sendChoice();
			}
		}
		
		if (e.getSource() == dcv.getJbClose()) {
			dcv.dispose();
			close();
		}
	}
	
	public void sendChoice() {
		try {
			String team = dcv.getJcbTeam().getSelectedItem().toString();
			dos.writeUTF(team);
			dos.flush();
			dcv.getJtaLogs().append("[client]: ������  ����("+team+")�� �����Ͽ����ϴ�.\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void close() {
		try {
			if (client != null) {
				client.close();
			}
			if (dis != null) {
				dis.close();
			}
			if (dos != null) {
				dos.close();
			}
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		dcv.dispose();
	}
	
	@Override
	public void windowClosed(WindowEvent e) {
		close();
	}
}
