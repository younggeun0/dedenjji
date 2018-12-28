package dedenjji.client.evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import dedenjji.client.view.DedenjjiClientView;

public class DedenjjiClientEvt implements ActionListener {

	private DedenjjiClientView dcv;
	private Socket client;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	public DedenjjiClientEvt(DedenjjiClientView dcv) {
		this.dcv = dcv;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dcv.getJbConnectToServer()) {
			String nick = JOptionPane.showInputDialog(dcv, "서버에 접속할 닉네임을 입력해주세요.");
			
			if (client == null) {
				try {
					client = new Socket("localhost", 6000);
					
					
					
					
				} catch (UnknownHostException uhe) {
					uhe.printStackTrace();
				} catch (IOException ie) {
					ie.printStackTrace();
				} finally {
					if (client != null) {
						try {
							client.close();
						} catch (IOException ie) {
							ie.printStackTrace();
						}
					}
					if (dis != null) {
						try {
							dis.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					if (dos != null) {
						try {
							dos.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
			} else {
				JOptionPane.showMessageDialog(dcv, "이미 접속중입니다.");
				return;
			}
		}
		
		if (e.getSource() == dcv.getJbSend()) {
			System.out.println(dcv.getJcbTeam().getSelectedItem());
		}
		
		if (e.getSource() == dcv.getJbClose()) {
			dcv.dispose();
		}
	}
}
