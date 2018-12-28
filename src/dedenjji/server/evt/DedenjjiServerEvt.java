package dedenjji.server.evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import dedenjji.server.view.DedenjjiServerView;

public class DedenjjiServerEvt implements ActionListener {
	
	private DedenjjiServerView dsv;
	private boolean serverFlag;
	private ServerSocket server;
	private List<Socket> listClient;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	public DedenjjiServerEvt(DedenjjiServerView dsv) {
		this.dsv = dsv;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dsv.getJbServerOnOff()) {
			serverFlag = !serverFlag;
			if (serverFlag) {
				// server on
				dsv.getJtaLogs().append("���� ���� ����...\n");
				dsv.getJbServerOnOff().setText("Close");
				
				/*try {
					// ���ϻ���
					server = new ServerSocket(6000);
					// client�� �����Ͽ� ���� socket�� ����Ʈ�� �߰�
					listClient.add(server.accept());
					
				} catch (IOException ie) {
					ie.printStackTrace();
				} finally {
					if (server != null) { 
						try {
							server.close();
						} catch (IOException ie) {
							ie.printStackTrace();
						}
					}
				}*/
			} else {
				// server off
				dsv.getJbServerOnOff().setText("Open");
				dsv.getJtaLogs().append("������ �����Ͽ����ϴ�...\n");
				/*try {
					close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}*/
			}
		}
		if (e.getSource() == dsv.getJbShowResult()) {
			// ��� Ŭ���̾�Ʈ���� ��û�� �޾��� �� ���� �� �ִ� ��ư
			// Ŭ���̾�Ʈ���� send���� �а� ���� send������ ���� ������ ���� �ȸ����� ���� ��� ��ȯ
			// ���� ������ ���� ��� ��ȯ
		}
		if (e.getSource() == dsv.getJbSaveLog()) {
			// system log�� .dat���Ϸ� ����ϴ� ��ư
			// ��üȭ�Ͽ� ���� �� ���� �������� ���
		}
		if (e.getSource() == dsv.getJbExit()) {
			dsv.dispose();
		}
	}
	
	public void close() throws IOException {
		if (server != null) {
			server.close();
		}
		for (int i=0; i<listClient.size(); i++) {
			if (listClient.get(i) != null) {
				listClient.get(i).close();
			}
		}
		if (dis != null) {
			dis.close();
		}
		if (dos != null) {
			dos.close();
		}
	}
}
