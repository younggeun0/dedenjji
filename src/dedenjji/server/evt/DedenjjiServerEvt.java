package dedenjji.server.evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import dedenjji.server.helper.DedenjjiServerHelper;
import dedenjji.server.view.DedenjjiServerView;
import sun.security.util.DisabledAlgorithmConstraints;

public class DedenjjiServerEvt extends WindowAdapter implements ActionListener, Runnable {
	
	private DedenjjiServerView dsv;
	private boolean serverFlag;
	private ServerSocket server;
	private List<DedenjjiServerHelper> listClient;
	private Thread operThread;
	
	public DedenjjiServerEvt(DedenjjiServerView dsv) {
		this.dsv = dsv;
		listClient = new ArrayList<DedenjjiServerHelper>();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == dsv.getJbServerOnOff()) {
			serverFlag = !serverFlag;
			if (serverFlag) {
				// server on
				dsv.getJtaLogs().append("[server]: ���� ���� ����...\n");
				dsv.getJbServerOnOff().setText("Close");
				
				operThread = new Thread(this);
				operThread.start();
				
			} else {
				// server off
				dsv.getJbServerOnOff().setText("Open");
				dsv.getJtaLogs().append("[server]: ������ �����Ͽ����ϴ�...\n");
				close();
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
		String nick;
		Thread thClient;
		try {
			server = new ServerSocket(6000);
			while(true) {
				// ���ϻ���
				dsh = new DedenjjiServerHelper();
				dsh.setClient(server.accept());
				dsh.setDsv(dsv);
				dsh.setDis(dsh.getClient().getInputStream());
				dsh.setDos(dsh.getClient().getOutputStream());
				nick = dsh.getDis().readUTF();
				dsh.setNick(nick);
				dsv.getJtaLogs().append("[server]: ������ "+nick+"���� �����߽��ϴ�.\n");
				dsv.getDlm().addElement(nick);

				// client�� �����Ͽ� ���� socket�� ����Ʈ�� �߰�
				listClient.add(dsh);
				thClient = new Thread(dsh);
				thClient.start();
			}
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
	
	public void close() {
		if (server != null) {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		dsv.dispose();
	}
	@Override
	public void windowClosed(WindowEvent e) {
		close();
	}
}
