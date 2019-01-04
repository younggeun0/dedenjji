package dedenjji.server.evt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import dedenjji.server.helper.DedenjjiServerHelper;
import dedenjji.server.view.DedenjjiServerView;

public class DedenjjiServerEvt extends WindowAdapter implements ActionListener, Runnable {
	
	private DedenjjiServerView dsv;
	private boolean serverFlag;
	private boolean divideTeamFlag;
	private ServerSocket server;
	private List<DedenjjiServerHelper> listClient;
	private Thread serverThread;
	
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
				dsv.getJtaLogs().append("���� ���� ����...\n");
				dsv.getJbServerOnOff().setText("Close");
				
				try {
					server = new ServerSocket(6000);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(dsv, "���� ���� ����");
					e1.printStackTrace();
				}
				
				serverThread = new Thread(this);
				serverThread.start();
			} else {
				// server off
				dsv.getJbServerOnOff().setText("Open");
				JOptionPane.showMessageDialog(dsv, "������ �����մϴ�..");
				close();
			}
		}
		if (e.getSource() == dsv.getJbShowResult()) {
			// Ŭ���̾�Ʈ���� send���� �а� ���� send������ ���� ������ ���� �ȸ����� ���� ��� ��ȯ
			// ���� ������ ���� ��� ��ȯ
			if (divideTeamFlag) {
				divideTeam();
			} else {
				JOptionPane.showMessageDialog(dsv, "���� ���� �غ� �ȵǾ����ϴ�.");
			}
		}
		if (e.getSource() == dsv.getJbSaveLog()) {
			// system log�� .dat���Ϸ� ����ϴ� ��ư
			// ��üȭ�Ͽ� ���� �� ���� �������� ���
			if (!dsv.getJtaLogs().getText().isEmpty()) {
				
			} else {
				JOptionPane.showMessageDialog(dsv, "������ �αװ� �����ϴ�.");
			}
		}
		if (e.getSource() == dsv.getJbExit()) {
			dsv.dispose();
			close();
		}
	}

	public void divideTeam() {
		List<String> listTeamFront = new ArrayList<String>(); 
		List<String> listTeamBack = new ArrayList<String>();
		DedenjjiServerHelper tempSh = null;
		for (int i = 0; i < listClient.size(); i++) {
			tempSh = listClient.get(i);
			// ���ο��� ¦�����װ� ����� size/2 ���� ���̷� ����Ʈ�� ���ٸ�
			if (tempSh.getTeam().equals("��")) {
				listTeamFront.add(tempSh.getNick());
			} else {
				listTeamBack.add(tempSh.getNick());
			}
		}
		
		if (listTeamFront.size() == listTeamBack.size()) {
			// �� �й� ����
			for (int i=0; i<listClient.size(); i++) {
				tempSh = listClient.get(i);
				tempSh.broadcast("�� �й迡 �����߽��ϴ�.. �ٽ� �������ּ���.");
			}
		} else {
			// �� �й� ����
			for (int i=0; i<listClient.size(); i++) {
				tempSh = listClient.get(i);
				// ���⼭ ���� ���� �й�///////////////////////////////////////////////////
				if (tempSh.getTeam().equals("��")) {
					tempSh.broadcast("�� �й迡 �����߽��ϴ�. ���� ������\n");
					for(int j=0; j<listTeamFront.size(); j++) {
						tempSh.broadcast(listTeamFront.get(j).getN);
					}
				}
				
			}
		}
	}

	@Override
	public void run() {
		try {
			Socket someClient = null;
			DedenjjiServerHelper dsh = null;
			DefaultListModel<String> dlmTemp = dsv.getDlm();
			JTextArea jtaTemp = dsv.getJtaLogs();
			for(int cnt=1;;cnt++) {
				someClient = server.accept();
				dsh = new DedenjjiServerHelper(someClient, dlmTemp, jtaTemp, cnt, dsv, listClient, divideTeamFlag);
				listClient.add(dsh);
				dsh.start();
			}
		} catch (IOException ie) {
			JOptionPane.showMessageDialog(dsv, "���� ���� ����");
			ie.printStackTrace();
		}
	}
	
	public void close() {
		try {
			if (server != null) {
				server.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
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
