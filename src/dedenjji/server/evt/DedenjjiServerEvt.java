package dedenjji.server.evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import dedenjji.server.helper.DedenjjiServerHelper;
import dedenjji.server.view.DedenjjiServerView;

public class DedenjjiServerEvt implements ActionListener, Runnable {
	
	private DedenjjiServerView dsv;
	private boolean serverFlag;
	private ServerSocket server;
	private List<DedenjjiServerHelper> listClient;
	private Thread operThread;
	
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
				
				operThread = new Thread(this);
				operThread.start();
				
			} else {
				// server off
				dsv.getJbServerOnOff().setText("Open");
				dsv.getJtaLogs().append("������ �����Ͽ����ϴ�...\n");
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
	
	@Override
	public void run() {

		// ���� ���� ����, ������ �ޱ� Threadó��
		DedenjjiServerHelper dsh;
		try {
			// ���ϻ���
			server = new ServerSocket(6000);
			dsh = new DedenjjiServerHelper();
			dsh.setClient(server.accept());
			// client�� �����Ͽ� ���� socket�� ����Ʈ�� �߰�
			listClient.add(dsh);
			
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
		}
	}
}
