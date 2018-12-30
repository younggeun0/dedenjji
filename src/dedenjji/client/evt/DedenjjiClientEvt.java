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
	
	public void sendChoice(String nick) throws IOException {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dcv.getJbConnectToServer()) {
			String nick = JOptionPane.showInputDialog(dcv, "������ ������ �г����� �Է����ּ���.");
			
			if (client == null) {
				try {
					client = new Socket("localhost", 6000);
					dos = new DataOutputStream(client.getOutputStream());
					dos.writeUTF(nick);
					
				} catch (IOException ie) {
					ie.printStackTrace();
				} 
			} else {
				JOptionPane.showMessageDialog(dcv, "�̹� �������Դϴ�.");
				return;
			}
		}
		
		if (e.getSource() == dcv.getJbSend()) {
			System.out.println(dcv.getJcbTeam().getSelectedItem());
			/*try {
//				sendChoice();
			} catch (IOException e1) {
				e1.printStackTrace();
			}*/
			
		}
		
		if (e.getSource() == dcv.getJbClose()) {
			dcv.dispose();
			
			if (client != null) {
				try {
					client.close();
				} catch (IOException ie) {
					ie.printStackTrace();
				}
			}
		}
	}
}
