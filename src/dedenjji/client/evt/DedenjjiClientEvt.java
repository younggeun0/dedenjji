package dedenjji.client.evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

import dedenjji.client.view.DedenjjiClientView;

public class DedenjjiClientEvt extends WindowAdapter implements ActionListener, Runnable {

	private String nick;
	private DedenjjiClientView dcv;
	private Socket client;
	private DataInputStream readStream;
	private DataOutputStream writeStream;
	private boolean serverFlag;
	private boolean sendFlag;
	
	public DedenjjiClientEvt(DedenjjiClientView dcv) {
		this.dcv = dcv;
	}
	
	@Override
	public void run() {
		String serverMsg;
		try {
			while(true) {
				serverMsg = readStream.readUTF();
				dcv.getJtaLogs().append(serverMsg+"\n");
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(dcv, "서버로부터 로그를 읽어올 수 없습니다.");
			e.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dcv.getJbConnectToServer()) {
			if (client == null) {
				nick = JOptionPane.showInputDialog(dcv, "서버에 접속할 닉네임을 입력해주세요.");
				try {
					client = new Socket("localhost", 6000);
					writeStream = new DataOutputStream(client.getOutputStream());
					readStream = new DataInputStream(client.getInputStream());
					
					Thread readThread = new Thread(this);
					readThread.start();
					
					serverFlag = true;
				} catch (IOException ie) {
					ie.printStackTrace();
				} 
			} else {
				JOptionPane.showMessageDialog(dcv, "이미 접속중입니다.");
				return;
			}
		}
		
		if (e.getSource() == dcv.getJbSend()) {
			if (serverFlag) {
				if (!sendFlag) {
					String team = dcv.getJcbTeam().getSelectedItem().toString();
					sendMsg("["+nick+"]님께서 "+team+"을 선택하였습니다.");
					sendFlag = !sendFlag;
				} else {
					JOptionPane.showMessageDialog(dcv, "서버로부터 응답대기중입니다.");
				}
			}
		}
		
		if (e.getSource() == dcv.getJbClose()) {
			dcv.dispose();
			close();
		}
	}
	
	public void sendMsg(String msg) {
		try {
			writeStream.writeUTF(msg);
			writeStream.flush();
//			dcv.getJtaLogs().append("[client]: 서버에  선택("+team+")을 전송하였습니다.\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	public void close() {
		try {
			if (readStream != null) {
				readStream.close();
			}
			if (writeStream != null) {
				writeStream.close();
			}
			if (client != null) {
				client.close();
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
