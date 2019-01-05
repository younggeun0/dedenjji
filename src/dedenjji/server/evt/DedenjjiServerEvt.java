package dedenjji.server.evt;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
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
				
				FileDialog fd = new FileDialog(dsv, "�α� ����", FileDialog.SAVE);
				fd.setVisible(true);
				
				String fileDir = fd.getDirectory();
				String fileName = fd.getFile();
				
				Date date = new Date();
				
				File file = new File(fd.getDirectory()+"\\"+fd.getFile()+"_"+date.getTime()+".dat");
				
				try {
					BufferedWriter br = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
					br.write(dsv.getJtaLogs().getText());
					br.flush();
					JOptionPane.showMessageDialog(dsv, "�α� ���� ����");
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(dsv, "�α� ���� ����");
					e1.printStackTrace();
				}
				
			} else {
				JOptionPane.showMessageDialog(dsv, "������ �αװ� �����ϴ�.");
			}
		}
		if (e.getSource() == dsv.getJbExit()) {
			close();
		}
	}

	public void divideTeam() {
		List<String> listTeamFront = new ArrayList<String>(); 
		List<String> listTeamBack = new ArrayList<String>();
		DedenjjiServerHelper tempSh = null;
		
		System.out.println("listClient ������ : "+listClient.size());
		try {
			for (int i = 0; i < listClient.size(); i++) {
				tempSh = listClient.get(i);
					if (tempSh.getTeam().equals("��")) {
						listTeamFront.add(tempSh.getNick());
					} else {
						listTeamBack.add(tempSh.getNick());
					}
			}
		} catch (NullPointerException e) {
			JOptionPane.showMessageDialog(dsv, "�������� ���� ���� �������� �ʾҽ��ϴ�.");
		}
		
		if (!(listTeamFront.size() == listTeamBack.size())) {
			// �� �й� ����
			tempSh = listClient.get(0);
			tempSh.broadcast("�� �й迡 �����߽��ϴ�.. �ٽ� �������ּ���.");
			dsv.getJtaLogs().append("�� �й迡 �����߽��ϴ�.\n");
			dsv.getJspLogs().getVerticalScrollBar().setValue(dsv.getJspLogs().getVerticalScrollBar().getMaximum());
		} else {
			// �� �й� ����
			boolean sendFrontTeamFlag = false;
			boolean sendBackTeamFlag = false;
			
			for (int i=0; i<listClient.size(); i++) {
				tempSh = listClient.get(i);
				
				if (tempSh.getTeam().equals("��") && !sendFrontTeamFlag) {
					StringBuilder msg = new StringBuilder();
					msg.append("�� �й迡 �����߽��ϴ�. ");
					msg.append("���� ������ ������ ");
					for(int j=0; j<listTeamFront.size(); j++) {
						if (j == listTeamFront.size()-1) {
							msg.append(listTeamFront.get(j));
						} else {
							msg.append(listTeamFront.get(j)).append(", ");
						}
					}
					msg.append(" �Դϴ�.");
					tempSh.broadcast(msg.toString());
					sendFrontTeamFlag = true;
					dsv.getJtaLogs().append(msg.toString()+"\n");
					dsv.getJspLogs().getVerticalScrollBar().setValue(dsv.getJspLogs().getVerticalScrollBar().getMaximum());
				}
				
				if (tempSh.getTeam().equals("��") && !sendBackTeamFlag) {
					StringBuilder msg = new StringBuilder();
					msg.append("�� �й迡 �����߽��ϴ�. ");
					msg.append("�ڸ� ������ ������ ");
					for(int j=0; j<listTeamBack.size(); j++) {
						if (j == listTeamBack.size()-1) {
							msg.append(listTeamBack.get(j));
						} else {
							msg.append(listTeamBack.get(j)).append(", ");
						}
					}
					msg.append(" �Դϴ�.");
					tempSh.broadcast(msg.toString());
					sendBackTeamFlag = true;
					dsv.getJtaLogs().append(msg.toString()+"\n");
					dsv.getJspLogs().getVerticalScrollBar().setValue(dsv.getJspLogs().getVerticalScrollBar().getMaximum());
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
				dsh = new DedenjjiServerHelper(someClient, dlmTemp, jtaTemp, cnt, dsv, listClient, this, dsv.getJspLogs());
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

	public void setDivideTeamFlag(boolean divideTeamFlag) {
		this.divideTeamFlag = divideTeamFlag;
	}
}
